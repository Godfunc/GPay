package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.Merchant;
import com.godfunc.exception.GException;
import com.godfunc.modules.merchant.dto.MerchantDTO;
import com.godfunc.modules.merchant.dto.MerchantKeysDTO;
import com.godfunc.modules.merchant.dto.MerchantSimpleDTO;
import com.godfunc.enums.MerchantTypeEnum;
import com.godfunc.modules.merchant.enums.RoleNameEnum;
import com.godfunc.modules.merchant.mapper.MerchantMapper;
import com.godfunc.modules.merchant.param.MerchantAddParam;
import com.godfunc.modules.merchant.param.MerchantEditParam;
import com.godfunc.modules.merchant.service.MerchantService;
import com.godfunc.modules.security.util.SecurityUser;
import com.godfunc.modules.sys.entity.User;
import com.godfunc.modules.sys.enums.SuperManagerEnum;
import com.godfunc.modules.sys.service.UserService;
import com.godfunc.result.ApiCodeMsg;
import com.godfunc.util.Assert;
import com.godfunc.util.ConvertUtils;
import com.godfunc.util.RSAUtils;
import com.godfunc.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    private final UserService userService;

    @Override
    public PageDTO<MerchantDTO> getPage(Integer page, Integer limit, Integer type, Integer status, String code, String name) {
        Long agentId = null;
        Long id = null;
        Merchant merchant = getByUserId(SecurityUser.getUserId());
        if (SecurityUser.getUser().getSuperManager() == SuperManagerEnum.SUPER_MANAGER.getValue() || SecurityUser.checkRole(RoleNameEnum.MANAGE.getValue())) {

        } else if (SecurityUser.checkRole(RoleNameEnum.AGENT.getValue())) {
            agentId = merchant.getId();
        } else if (SecurityUser.checkRole(RoleNameEnum.MERCHANT.getValue())) {
            id = merchant.getId();
        } else {
            throw new GException(ApiCodeMsg.NOPERMISSION);
        }
        IPage<MerchantDTO> resultPage = new Page<>(page, limit);
        List<Merchant> list = this.baseMapper.selectCustomPage(resultPage, type, status, code, name, id, agentId);
        resultPage.setRecords(ConvertUtils.source2Target(list, MerchantDTO.class));
        return new PageDTO<MerchantDTO>(resultPage);
    }

    @Override
    public Long edit(MerchantEditParam param) {
        ValidatorUtils.validate(param);
        Merchant merchant = getById(param.getId());
        Assert.isNull(merchant, "商户不存在");
        merchant.setName(param.getName());
        merchant.setPublicKey(param.getPublicKey());
        merchant.setStatus(param.getStatus());
        updateById(merchant);
        return merchant.getId();
    }

    @Override
    public Long add(MerchantAddParam param) {
        ValidatorUtils.validate(param);
        Assert.isTrue(checkExistByUserId(param.getUserId()), "当前用户已创建过商户信息");
        User user = userService.getById(param.getUserId());
        Assert.isNull(user, "选择的用户不存在或已被删除");
        if (param.getAgentId() != null) {
            Assert.isNull(getById(param.getAgentId()), "选择的代理商户不存在或已被删除");
        }
        Merchant merchant = ConvertUtils.source2Target(param, Merchant.class);
        RSAUtils.KeyPairModel keyPairModel = null;
        try {
            keyPairModel = RSAUtils.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("生成密钥对失败", e);
            throw new GException("生成密钥对失败");
        }
        if (MerchantTypeEnum.MERCHANT.getValue() == merchant.getType()) {
            merchant.setCode("M" + IdWorker.getIdStr());
            merchant.setPlatPrivateKey(keyPairModel.getPrivateKey());
            merchant.setPlatPublicKey(keyPairModel.getPublicKey());
        } else if (MerchantTypeEnum.AGENT.getValue() == merchant.getType()) {
            merchant.setCode("A" + IdWorker.getIdStr());
        } else {
            throw new GException("未知的商户类型");
        }
        save(merchant);
        return merchant.getId();
    }

    @Override
    public boolean removeData(Long id) {
        if (SecurityUser.getUser().getSuperManager() == SuperManagerEnum.SUPER_MANAGER.getValue() || SecurityUser.checkRole(RoleNameEnum.MANAGE.getValue())) {

        } else if (SecurityUser.checkRole(RoleNameEnum.AGENT.getValue())) {
            Merchant merchant = getById(id);
            Assert.isNull(merchant, "删除的商户不存在或已被删除");
            Merchant agent = getByUserId(SecurityUser.getUserId());
            if (Objects.isNull(agent) || !agent.getId().equals(merchant.getAgentId())) {
                throw new GException(ApiCodeMsg.NOPERMISSION);
            }
        } else if (SecurityUser.checkRole(RoleNameEnum.MERCHANT.getValue())) {
            throw new GException(ApiCodeMsg.NOPERMISSION);
        } else {
            throw new GException(ApiCodeMsg.NOPERMISSION);
        }
        return removeById(id);
    }

    @Override
    public MerchantKeysDTO getKeys(Long id) {
        Merchant merchant = getById(id);
        Assert.isNull(merchant, "商户不存在或已被删除");
        return new MerchantKeysDTO(merchant.getPlatPublicKey(), merchant.getPlatPrivateKey());
    }

    @Override
    public boolean refreshKeys(Long id) {
        Merchant merchant = getById(id);
        Assert.isNull(merchant, "商户不存在或已被删除");
        Assert.isTrue(MerchantTypeEnum.MERCHANT.getValue() != merchant.getType(), "当前商户类型不支持设置密钥对");
        RSAUtils.KeyPairModel keyPairModel = null;
        try {
            keyPairModel = RSAUtils.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("生成密钥对失败", e);
            throw new GException("生成密钥对失败");
        }
        return lambdaUpdate().set(Merchant::getPlatPrivateKey, keyPairModel.getPrivateKey())
                .set(Merchant::getPlatPublicKey, keyPairModel.getPublicKey())
                .eq(Merchant::getId, merchant.getId())
                .eq(Merchant::getType, MerchantTypeEnum.MERCHANT.getValue())
                .update();
    }

    @Override
    public List<MerchantSimpleDTO> getList(Integer type) {
        Merchant merchant = getByUserId(SecurityUser.getUserId());
        Long id = null;
        if (merchant != null && merchant.getType() == MerchantTypeEnum.AGENT.getValue()) {
            id = merchant.getId();
        }
        return this.baseMapper.selectListByType(type, id);
    }

    @Override
    public Merchant getByCode(String code) {
        return getOne(Wrappers.<Merchant>lambdaQuery().eq(Merchant::getCode, code));
    }

    private Merchant getByUserId(Long userId) {
        return getOne(Wrappers.<Merchant>lambdaQuery().eq(Merchant::getUserId, userId));
    }

    private boolean checkExistByUserId(Long userId) {
        return count(Wrappers.<Merchant>lambdaQuery().eq(Merchant::getUserId, userId)) > 0;
    }
}

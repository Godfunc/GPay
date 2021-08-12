package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.Merchant;
import com.godfunc.entity.MerchantRisk;
import com.godfunc.modules.merchant.dto.MerchantRiskDTO;
import com.godfunc.modules.merchant.mapper.MerchantRiskMapper;
import com.godfunc.modules.merchant.param.MerchantRiskAddParam;
import com.godfunc.modules.merchant.param.MerchantRiskEditParam;
import com.godfunc.modules.merchant.service.MerchantRiskService;
import com.godfunc.modules.merchant.service.MerchantService;
import com.godfunc.util.AmountUtil;
import com.godfunc.util.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantRiskServiceImpl extends ServiceImpl<MerchantRiskMapper, MerchantRisk> implements MerchantRiskService {

    private final MerchantService merchantService;

    @Override
    public List<MerchantRiskDTO> getList(String merchantCode) {
        return this.baseMapper.selectMerchantList(merchantCode);
    }

    @Override
    public Long add(MerchantRiskAddParam param) {
        Merchant merchant = merchantService.getByCode(param.getMerchantCode());
        Assert.isNull(merchant, "商户不存在或已被删除");
        MerchantRisk merchantRisk = new MerchantRisk();
        merchantRisk.setMerchantId(merchant.getId());
        merchantRisk.setMerchantCode(param.getMerchantCode());
        merchantRisk.setOneAmountMax(AmountUtil.convertDollar2Cent(param.getOneAmountMax()));
        merchantRisk.setOneAmountMin(AmountUtil.convertDollar2Cent(param.getOneAmountMin()));
        merchantRisk.setDayEndTime(param.getDayEndTime());
        merchantRisk.setDayStartTime(param.getDayStartTime());
        merchantRisk.setStatus(param.getStatus());
        save(merchantRisk);
        return merchantRisk.getId();
    }

    @Override
    public Long edit(MerchantRiskEditParam param) {
        MerchantRisk merchantRisk = getById(param.getId());
        Assert.isNull(merchantRisk, "修改的数据不存在或已被删除");
        merchantRisk.setOneAmountMax(AmountUtil.convertDollar2Cent(param.getOneAmountMax()));
        merchantRisk.setOneAmountMin(AmountUtil.convertDollar2Cent(param.getOneAmountMin()));
        merchantRisk.setDayEndTime(param.getDayEndTime());
        merchantRisk.setDayStartTime(param.getDayStartTime());
        merchantRisk.setStatus(param.getStatus());
        updateById(merchantRisk);
        return merchantRisk.getId();
    }

    @Override
    public boolean removeData(Long id) {
        return removeById(id);
    }

}

package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.MerchantChannelRate;
import com.godfunc.exception.GException;
import com.godfunc.modules.merchant.dto.MerchantChannelRateDTO;
import com.godfunc.modules.merchant.mapper.MerchantChannelRateMapper;
import com.godfunc.modules.merchant.param.MerchantChannelRateAddParam;
import com.godfunc.modules.merchant.param.MerchantChannelRateEditParam;
import com.godfunc.modules.merchant.service.*;
import com.godfunc.util.Assert;
import com.godfunc.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantChannelRateServiceImpl extends ServiceImpl<MerchantChannelRateMapper, MerchantChannelRate> implements MerchantChannelRateService {

    private final PayCategoryChannelService payCategoryChannelService;
    private final MerchantService merchantService;

    @Override
    public PageDTO<MerchantChannelRateDTO> getPage(Integer page, Integer limit, String merchantCode, String channelCode, String categoryCode) {
        IPage<MerchantChannelRateDTO> resultPage = new Page<>(page, limit);
        List<MerchantChannelRateDTO> list = this.baseMapper.selectCustomPage(resultPage, merchantCode, channelCode, categoryCode);
        resultPage.setRecords(list);
        return new PageDTO<MerchantChannelRateDTO>(resultPage);
    }

    @Override
    public Long add(MerchantChannelRateAddParam param) {
        ValidatorUtils.validate(param);
        Assert.isTrue(!merchantService.checkExistsById(param.getMerchantCode()), "选择的商户不存在或已被删除");
        Assert.isTrue(!payCategoryChannelService.checkExistsById(param.getCategoryChannelId()), "选择的渠道关联数据不存在或已被删除");
        MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
        merchantChannelRate.setMerchantCode(param.getMerchantCode());
        merchantChannelRate.setCategoryChannelId(param.getCategoryChannelId());
        try {
            merchantChannelRate.setRate(Float.parseFloat(param.getRate()));
        } catch (Exception e) {
            throw new GException("费率数据不正确，请检查");
        }
        save(merchantChannelRate);
        return merchantChannelRate.getId();
    }

    @Override
    public Long edit(MerchantChannelRateEditParam param) {
        ValidatorUtils.validate(param);
        Assert.isTrue(!payCategoryChannelService.checkExistsById(param.getCategoryChannelId()), "选择的渠道关联数据不存在或已被删除");
        MerchantChannelRate merchantChannelRate = getById(param.getId());
        Assert.isNull(merchantChannelRate, "修改的数据不存在或已被删除");
        merchantChannelRate.setMerchantCode(param.getMerchantCode());
        merchantChannelRate.setCategoryChannelId(param.getCategoryChannelId());
        try {
            merchantChannelRate.setRate(Float.parseFloat(param.getRate()));
        } catch (Exception e) {
            throw new GException("费率数据不正确，请检查");
        }
        updateById(merchantChannelRate);
        return merchantChannelRate.getId();
    }

    @Override
    public boolean removeData(Long id) {
        return removeById(id);
    }
}

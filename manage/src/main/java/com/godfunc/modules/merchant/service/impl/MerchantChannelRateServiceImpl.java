package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantChannelRate;
import com.godfunc.modules.merchant.dto.MerchantChannelSimpleRateDTO;
import com.godfunc.modules.merchant.mapper.MerchantChannelRateMapper;
import com.godfunc.modules.merchant.param.MerchantChannelRateSaveParam;
import com.godfunc.modules.merchant.service.*;
import com.godfunc.util.Assert;
import com.godfunc.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MerchantChannelRateServiceImpl extends ServiceImpl<MerchantChannelRateMapper, MerchantChannelRate> implements MerchantChannelRateService {

    @Override
    public boolean removeData(Long id) {
        return removeById(id);
    }

    @Override
    public List<MerchantChannelSimpleRateDTO> listByMerchant(String merchantCode) {
        return this.baseMapper.selectListByMerchantCode(merchantCode);
    }

    @Override
    public boolean saveData(MerchantChannelRateSaveParam param) {
        ValidatorUtils.validate(param);
        if (Objects.isNull(param.getId())) {
            MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
            merchantChannelRate.setMerchantCode(param.getMerchantCode());
            merchantChannelRate.setCategoryChannelId(param.getCategoryChannelId());
            merchantChannelRate.setRate(Float.parseFloat(param.getRate()));
            return save(merchantChannelRate);
        } else {
            MerchantChannelRate merchantChannelRate = getById(param.getId());
            Assert.isNull(merchantChannelRate, "修改的数据不存在或已被删除");
            merchantChannelRate.setRate(Float.parseFloat(param.getRate()));
            return updateById(merchantChannelRate);
        }
    }
}

package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantChannelRate;
import com.godfunc.entity.PayCategoryChannel;
import com.godfunc.modules.merchant.dto.PayCategoryChannelDTO;
import com.godfunc.modules.merchant.mapper.PayCategoryChannelMapper;
import com.godfunc.modules.merchant.service.MerchantChannelRateService;
import com.godfunc.modules.merchant.service.PayCategoryChannelService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PayCategoryChannelServiceImpl extends ServiceImpl<PayCategoryChannelMapper, PayCategoryChannel> implements PayCategoryChannelService {
    @Override
    public boolean removeByCategory(Long categoryId) {
        return remove(Wrappers.<PayCategoryChannel>lambdaQuery().eq(PayCategoryChannel::getCategoryId, categoryId));
    }

    @Override
    public boolean removeByChannel(Long channelId) {

        return remove(Wrappers.<PayCategoryChannel>lambdaQuery().eq(PayCategoryChannel::getChannelId, channelId));
    }

    @Override
    public Set<Long> getByChannel(Long channelId) {
        List<PayCategoryChannel> list = list(Wrappers.<PayCategoryChannel>lambdaQuery().eq(PayCategoryChannel::getChannelId, channelId));
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(PayCategoryChannel::getCategoryId).collect(Collectors.toSet());
        } else {
            return null;
        }
    }

    @Override
    public boolean checkExistsById(Long id) {
        return count(Wrappers.<PayCategoryChannel>lambdaQuery().eq(PayCategoryChannel::getId, id)) > 0;
    }

    @Override
    public List<PayCategoryChannelDTO> getList() {
        return this.baseMapper.selectListInfo();
    }
}

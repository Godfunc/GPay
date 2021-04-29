package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.PayCategoryChannel;
import com.godfunc.modules.merchant.mapper.PayCategoryChannelMapper;
import com.godfunc.modules.merchant.service.PayCategoryChannelService;
import org.springframework.stereotype.Service;

@Service
public class PayCategoryChannelServiceImpl extends ServiceImpl<PayCategoryChannelMapper, PayCategoryChannel> implements PayCategoryChannelService {
    @Override
    public boolean removeByCategory(Long categoryId) {
        return remove(Wrappers.<PayCategoryChannel>lambdaQuery().eq(PayCategoryChannel::getCategoryId, categoryId));
    }
}

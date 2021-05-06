package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.PayCategoryChannel;

import java.util.Set;

public interface PayCategoryChannelService extends IService<PayCategoryChannel> {
    boolean removeByCategory(Long categoryId);

    boolean removeByChannel(Long channelId);

    Set<Long> getByChannel(Long channelId);
}

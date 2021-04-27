package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.PayCategoryChannel;

public interface PayCategoryChannelService extends IService<PayCategoryChannel> {
    boolean removeByCategory(String code);
}

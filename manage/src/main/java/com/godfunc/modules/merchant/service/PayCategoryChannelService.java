package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.PayCategoryChannel;
import com.godfunc.modules.merchant.dto.PayCategoryChannelDTO;
import com.godfunc.modules.merchant.param.PayCategoryChannelWeightParam;

import java.util.List;
import java.util.Set;

public interface PayCategoryChannelService extends IService<PayCategoryChannel> {
    boolean removeByCategory(Long categoryId);

    boolean removeByChannel(Long channelId);

    Set<Long> getByChannel(Long channelId);

    boolean checkExistsById(Long id);

    List<PayCategoryChannelDTO> getList(Long payCategoryId);

    boolean weight(PayCategoryChannelWeightParam param);
}

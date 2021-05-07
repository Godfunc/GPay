package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.PayChannelAccount;
import com.godfunc.modules.merchant.dto.PayChannelAccountDTO;
import com.godfunc.modules.merchant.param.PayChannelAccountAddParam;
import com.godfunc.modules.merchant.param.PayChannelAccountEditParam;

public interface PayChannelAccountService extends IService<PayChannelAccount> {
    PageDTO<PayChannelAccountDTO> getPage(Integer page, Integer limit, Integer status, String channelCode, String name, String accountCode);

    Long add(PayChannelAccountAddParam param);

    Long edit(PayChannelAccountEditParam param);

    boolean removeData(Long id);
}

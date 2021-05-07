package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.PayChannel;
import com.godfunc.modules.merchant.dto.PayChannelDTO;
import com.godfunc.modules.merchant.dto.PayChannelSimpleDTO;
import com.godfunc.modules.merchant.param.PayChannelAddParam;
import com.godfunc.modules.merchant.param.PayChannelEditParam;

import java.util.List;

public interface PayChannelService extends IService<PayChannel> {
    PageDTO<PayChannelDTO> getPage(Integer page, Integer limit, Integer status, String code, String name);

    List<PayChannelSimpleDTO> getList();

    Long add(PayChannelAddParam param);

    Long edit(PayChannelEditParam param);

    boolean removeData(Long id);
}

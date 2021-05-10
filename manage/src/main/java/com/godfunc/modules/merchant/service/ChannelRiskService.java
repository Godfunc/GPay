package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.ChannelRisk;
import com.godfunc.modules.merchant.dto.ChannelRiskDTO;
import com.godfunc.modules.merchant.dto.ChannelRiskSimpleDTO;
import com.godfunc.modules.merchant.param.ChannelRiskAddParam;
import com.godfunc.modules.merchant.param.ChannelRiskEditParam;

import java.util.List;

public interface ChannelRiskService extends IService<ChannelRisk> {
    boolean removeByChannelAccountId(Long channelAccountId);

    PageDTO<ChannelRiskDTO> getPage(Integer page, Integer limit, Integer status, String channelCode, String accountCode);

    boolean removeData(Long id);

    Long edit(ChannelRiskEditParam param);

    List<ChannelRiskSimpleDTO> getByAccount(Long channelAccountId);

    List<ChannelRiskSimpleDTO> getByChannel(Long channelId);

    Long add(ChannelRiskAddParam param);
}

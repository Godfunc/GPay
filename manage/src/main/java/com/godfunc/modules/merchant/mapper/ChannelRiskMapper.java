package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.ChannelRisk;
import com.godfunc.modules.merchant.dto.ChannelRiskDTO;
import com.godfunc.modules.merchant.dto.ChannelRiskSimpleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChannelRiskMapper extends BaseMapper<ChannelRisk> {
    List<ChannelRiskDTO> selectCustomPage(IPage resultPage,
                                          @Param("status") Integer status,
                                          @Param("channelCode") String channelCode,
                                          @Param("accountCode") String accountCode);

    List<ChannelRiskSimpleDTO> selectByAccount(@Param("channelAccountId") Long channelAccountId);

    List<ChannelRiskSimpleDTO> selectByChannel(@Param("channelId") Long channelId);
}

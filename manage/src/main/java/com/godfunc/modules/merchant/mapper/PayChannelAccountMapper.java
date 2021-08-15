package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.PayChannelAccount;
import com.godfunc.modules.merchant.dto.PayChannelAccountDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayChannelAccountMapper extends BaseMapper<PayChannelAccount> {
    List<PayChannelAccountDTO> selectCustomPage(IPage resultPage,
                                                @Param("status") Integer status,
                                                @Param("channelCode") String channelCode,
                                                @Param("name") String name,
                                                @Param("accountCode") String accountCode);
}

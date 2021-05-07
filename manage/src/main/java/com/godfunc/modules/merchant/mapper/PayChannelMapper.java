package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.PayChannel;
import com.godfunc.modules.merchant.dto.PayChannelDTO;
import com.godfunc.modules.merchant.dto.PayChannelSimpleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayChannelMapper extends BaseMapper<PayChannel> {
    List<PayChannel> selectCustomPage(IPage page,
                                      @Param("status") Integer status, @Param("code") String code,
                                      @Param("name") String name);

    List<PayChannelSimpleDTO> selectByStatus(@Param("status") int status);
}

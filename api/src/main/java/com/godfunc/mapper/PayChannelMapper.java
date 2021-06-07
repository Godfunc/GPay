package com.godfunc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.godfunc.entity.PayChannel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Godfunc
 * @date 2019/12/23 21:07
 */
public interface PayChannelMapper extends BaseMapper<PayChannel> {

    List<PayChannel> selectEnableByCategory(@Param("payCategoryId") Long payCategoryId);
}

package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.godfunc.entity.PayCategoryChannel;
import com.godfunc.modules.merchant.dto.PayCategoryChannelDTO;

import java.util.List;

public interface PayCategoryChannelMapper extends BaseMapper<PayCategoryChannel> {
    List<PayCategoryChannelDTO> selectListInfo();

}

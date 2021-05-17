package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.Merchant;
import com.godfunc.modules.merchant.dto.MerchantSimpleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantMapper extends BaseMapper<Merchant> {
    List<Merchant> selectCustomPage(IPage page,
                                    @Param("type") Integer type,
                                    @Param("status") Integer status,
                                    @Param("code") String code,
                                    @Param("name") String name,
                                    @Param("id") Long id,
                                    @Param("agentId") Long agentId);

    List<MerchantSimpleDTO> selectListByType(@Param("type") Integer type,
                                             @Param("id") Long id);
}

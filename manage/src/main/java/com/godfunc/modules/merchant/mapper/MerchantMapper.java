package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.Merchant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantMapper extends BaseMapper<Merchant> {
    List<Merchant> selectCustomPage(IPage page,
                                    @Param("type") Integer type,
                                    @Param("status") Integer status,
                                    @Param("code") String code,
                                    @Param("name") String name);
}

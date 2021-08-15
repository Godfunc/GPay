package com.godfunc.modules.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.PayCategory;
import com.godfunc.modules.merchant.dto.PayCategorySimpleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayCategoryMapper extends BaseMapper<PayCategory> {
    List<PayCategory> selectCustomPage(IPage page,
                                       @Param("status") Integer status,
                                       @Param("code") String code,
                                       @Param("name") String name);

    List<PayCategorySimpleDTO> selectListByStatus(@Param("status") int status);
}

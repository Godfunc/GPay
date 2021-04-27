package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.PayCategory;
import com.godfunc.modules.merchant.dto.PayCategoryDTO;
import com.godfunc.modules.merchant.param.PayCategoryAddParam;
import com.godfunc.modules.merchant.param.PayCategoryEditParam;

public interface PayCategoryService extends IService<PayCategory> {
    PageDTO<PayCategoryDTO> getPage(Integer page, Integer limit, Integer status, String code, String name);

    Long add(PayCategoryAddParam param);

    Long edit(PayCategoryEditParam param);

    boolean removeData(Long id);
}

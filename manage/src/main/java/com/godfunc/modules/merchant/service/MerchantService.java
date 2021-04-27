package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.Merchant;
import com.godfunc.modules.merchant.dto.MerchantDTO;
import com.godfunc.modules.merchant.dto.MerchantKeysDTO;
import com.godfunc.modules.merchant.param.MerchantAddParam;
import com.godfunc.modules.merchant.param.MerchantEditParam;

public interface MerchantService extends IService<Merchant> {
    PageDTO<MerchantDTO> getPage(Integer page, Integer limit, Integer type, Integer status, String code, String name);

    Long edit(MerchantEditParam param);

    Long add(MerchantAddParam param);

    boolean removeData(Long id);

    MerchantKeysDTO getKeys(Long id);

    boolean refreshKeys(Long id);
}

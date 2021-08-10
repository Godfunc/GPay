package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Merchant;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface MerchantService extends IService<Merchant> {
    Merchant getByCode(String code);
}

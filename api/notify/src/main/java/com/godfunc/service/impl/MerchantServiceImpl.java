package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.Merchant;
import com.godfunc.mapper.MerchantMapper;
import com.godfunc.service.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    @Override
    public Merchant getByCode(String code) {
        return getOne(Wrappers.<Merchant>lambdaQuery().eq(Merchant::getCode, code));
    }
}

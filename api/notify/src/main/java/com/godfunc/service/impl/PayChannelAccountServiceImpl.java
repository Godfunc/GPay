package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.PayChannelAccount;
import com.godfunc.mapper.PayChannelAccountMapper;
import com.godfunc.service.PayChannelAccountService;
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
public class PayChannelAccountServiceImpl extends ServiceImpl<PayChannelAccountMapper, PayChannelAccount> implements PayChannelAccountService {

}

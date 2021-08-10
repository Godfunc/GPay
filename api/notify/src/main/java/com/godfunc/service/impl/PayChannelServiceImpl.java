package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.PayChannel;
import com.godfunc.mapper.PayChannelMapper;
import com.godfunc.service.PayChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {

}

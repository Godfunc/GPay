package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.Merchant;
import com.godfunc.enums.MerchantStatusEnum;
import com.godfunc.mapper.MerchantMapper;
import com.godfunc.service.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


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

    @Override
    public boolean checkAgent(Merchant merchant, List<Merchant> agentList) {
        Merchant agent = null;
        if (merchant.getAgentId() != null) {
            do {
                agent = getById(merchant.getAgentId());
                if (agent == null) {
                    log.info("商户{}的代理{}不存在", merchant.getCode(), merchant.getAgentId());
                    return false;
                } else if (agent.getStatus() == MerchantStatusEnum.DISABLE.getValue()) {
                    log.info("商户{}的代理{}状态不可用", merchant.getCode(), agent.getCode());
                    return false;
                } else {
                    agentList.add(agent);
                }
            } while (agent.getAgentId() != null);
        }
        return true;
    }
}

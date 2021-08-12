package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.PayChannelAccount;
import com.godfunc.modules.merchant.dto.PayChannelAccountDTO;
import com.godfunc.modules.merchant.mapper.PayChannelAccountMapper;
import com.godfunc.modules.merchant.param.PayChannelAccountAddParam;
import com.godfunc.modules.merchant.param.PayChannelAccountEditParam;
import com.godfunc.modules.merchant.service.ChannelRiskService;
import com.godfunc.modules.merchant.service.PayChannelAccountService;
import com.godfunc.util.Assert;
import com.godfunc.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayChannelAccountServiceImpl extends ServiceImpl<PayChannelAccountMapper, PayChannelAccount> implements PayChannelAccountService {

    @Autowired
    private ChannelRiskService channelRiskService;

    @Override
    public PageDTO<PayChannelAccountDTO> getPage(Integer page, Integer limit, Integer status, String channelCode, String name, String accountCode) {
        IPage<PayChannelAccountDTO> resultPage = new Page<>(page, limit);
        List<PayChannelAccountDTO> list = this.baseMapper.selectCustomPage(resultPage, status, channelCode, name, accountCode);
        resultPage.setRecords(list);
        return new PageDTO<PayChannelAccountDTO>(resultPage);
    }

    @Override
    public Long add(PayChannelAccountAddParam param) {
        PayChannelAccount payChannelAccount = ConvertUtils.source2Target(param, PayChannelAccount.class);
        save(payChannelAccount);
        return payChannelAccount.getId();
    }

    @Override
    public Long edit(PayChannelAccountEditParam param) {
        PayChannelAccount payChannelAccount = getById(param.getId());
        Assert.isNull(payChannelAccount, "修改的数据不存在或已被删除");
        BeanUtils.copyProperties(param, payChannelAccount);
        updateById(payChannelAccount);
        return payChannelAccount.getId();
    }

    @Override
    public boolean removeData(Long id) {
        if (removeById(id)) {
            return channelRiskService.removeByChannelAccountId(id);
        }
        return false;
    }
}

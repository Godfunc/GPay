package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.ChannelRisk;
import com.godfunc.modules.merchant.dto.ChannelRiskDTO;
import com.godfunc.modules.merchant.dto.ChannelRiskSimpleDTO;
import com.godfunc.modules.merchant.mapper.ChannelRiskMapper;
import com.godfunc.modules.merchant.param.ChannelRiskAddParam;
import com.godfunc.modules.merchant.param.ChannelRiskEditParam;
import com.godfunc.modules.merchant.service.ChannelRiskService;
import com.godfunc.util.Assert;
import com.godfunc.util.ConvertUtils;
import com.godfunc.util.ValidatorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelRiskServiceImpl extends ServiceImpl<ChannelRiskMapper, ChannelRisk> implements ChannelRiskService {
    @Override
    public boolean removeByChannelAccountId(Long channelAccountId) {
        return remove(Wrappers.<ChannelRisk>lambdaQuery().eq(ChannelRisk::getChannelAccountId, channelAccountId));
    }

    @Override
    public PageDTO<ChannelRiskDTO> getPage(Integer page, Integer limit, Integer status, String channelCode, String accountCode) {
        IPage<ChannelRiskDTO> resultPage = new Page<>(page, limit);
        List<ChannelRiskDTO> list = this.baseMapper.selectCustomPage(resultPage, status, channelCode, accountCode);
        resultPage.setRecords(list);
        return new PageDTO<ChannelRiskDTO>(resultPage);
    }

    @Override
    public boolean removeData(Long id) {
        return removeById(id);
    }

    @Override
    public Long edit(ChannelRiskEditParam param) {
        ValidatorUtils.validate(param);
        ChannelRisk channelRisk = getById(param.getId());
        Assert.isNull(channelRisk, "数据不存在或已被删除");
        BeanUtils.copyProperties(param, channelRisk);
        updateById(channelRisk);
        return channelRisk.getId();
    }

    @Override
    public List<ChannelRiskSimpleDTO> getByAccount(Long channelAccountId) {
        return this.baseMapper.selectByAccount(channelAccountId);
    }

    @Override
    public List<ChannelRiskSimpleDTO> getByChannel(Long channelId) {
        return this.baseMapper.selectByChannel(channelId);
    }

    @Override
    public Long add(ChannelRiskAddParam param) {
        ValidatorUtils.validate(param);
        ChannelRisk channelRisk = ConvertUtils.source2Target(param, ChannelRisk.class);
        save(channelRisk);
        return channelRisk.getId();
    }
}

package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.PayCategoryChannel;
import com.godfunc.entity.PayChannel;
import com.godfunc.modules.merchant.dto.PayChannelDTO;
import com.godfunc.modules.merchant.dto.PayChannelSimpleDTO;
import com.godfunc.modules.merchant.enums.PayChannelStatusEnum;
import com.godfunc.modules.merchant.mapper.PayChannelMapper;
import com.godfunc.modules.merchant.param.PayChannelAddParam;
import com.godfunc.modules.merchant.param.PayChannelEditParam;
import com.godfunc.modules.merchant.service.PayCategoryChannelService;
import com.godfunc.modules.merchant.service.PayChannelService;
import com.godfunc.util.Assert;
import com.godfunc.util.ConvertUtils;
import com.godfunc.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {

    private final PayCategoryChannelService payCategoryChannelService;

    @Override
    public PageDTO<PayChannelDTO> getPage(Integer page, Integer limit, Integer status, String code, String name) {
        IPage<PayChannelDTO> resultPage = new Page<>(page, limit);
        List<PayChannel> list = this.baseMapper.selectCustomPage(resultPage, status, code, name);
        resultPage.setRecords(ConvertUtils.source2Target(list, PayChannelDTO.class));
        return new PageDTO<PayChannelDTO>(resultPage);
    }

    @Override
    public List<PayChannelSimpleDTO> getList() {
        return this.baseMapper.selectByStatus(PayChannelStatusEnum.ENABLE.getValue());
    }

    @Override
    public Long add(PayChannelAddParam param) {
        ValidatorUtils.validate(param);
        Assert.isTrue(checkCode(param.getCode(), null), "编号已存在");
        PayChannel payChannel = ConvertUtils.source2Target(param, PayChannel.class);
        payChannel.setCostRate(Float.parseFloat(param.getCostRate()));
        save(payChannel);
        saveCategoryChannelBatch(param.getCategoryIds(), payChannel.getId());
        return payChannel.getId();
    }

    @Override
    public Long edit(PayChannelEditParam param) {
        ValidatorUtils.validate(param);
        PayChannel payChannel = getById(param.getId());
        Assert.isNull(payChannel, "修改的数据不存在或已被删除");
        Assert.isTrue(checkCode(param.getCode(), payChannel.getId()), "编号已存在");
        BeanUtils.copyProperties(param, payChannel);
        payChannel.setCostRate(Float.parseFloat(param.getCostRate()));
        updateById(payChannel);
        payCategoryChannelService.removeByChannel(payChannel.getId());
        saveCategoryChannelBatch(param.getCategoryIds(), payChannel.getId());
        return payChannel.getId();
    }

    private boolean saveCategoryChannelBatch(Long[] categoryIds, Long payChannelId) {
        if (categoryIds != null) {
            List<PayCategoryChannel> categoryChannels = new ArrayList<>();
            for (Long categoryId : categoryIds) {
                categoryChannels.add(new PayCategoryChannel(categoryId, payChannelId));
            }
            return payCategoryChannelService.saveBatch(categoryChannels);
        }
        return false;
    }

    @Override
    public boolean removeData(Long id) {
        if (removeById(id)) {
            payCategoryChannelService.removeByChannel(id);
            return true;
        }
        return false;
    }

    private boolean checkCode(String code, Long id) {
        return count(Wrappers.<PayChannel>lambdaQuery().eq(PayChannel::getCode, code)
                .ne(id != null, PayChannel::getId, id)) > 0;
    }
}

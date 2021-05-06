package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.PayCategory;
import com.godfunc.modules.merchant.dto.PayCategoryDTO;
import com.godfunc.modules.merchant.dto.PayCategorySimpleDTO;
import com.godfunc.modules.merchant.enums.PayCategoryStatusEnum;
import com.godfunc.modules.merchant.mapper.PayCategoryMapper;
import com.godfunc.modules.merchant.param.PayCategoryAddParam;
import com.godfunc.modules.merchant.param.PayCategoryEditParam;
import com.godfunc.modules.merchant.service.PayCategoryChannelService;
import com.godfunc.modules.merchant.service.PayCategoryService;
import com.godfunc.util.Assert;
import com.godfunc.util.ConvertUtils;
import com.godfunc.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayCategoryServiceImpl extends ServiceImpl<PayCategoryMapper, PayCategory> implements PayCategoryService {

    private final PayCategoryChannelService payCategoryChannelService;

    @Override
    public PageDTO<PayCategoryDTO> getPage(Integer page, Integer limit, Integer status, String code, String name) {
        IPage<PayCategoryDTO> resultPage = new Page<>(page, limit);
        List<PayCategory> list = this.baseMapper.selectCustomPage(resultPage, status, code, name);
        resultPage.setRecords(ConvertUtils.source2Target(list, PayCategoryDTO.class));
        return new PageDTO<PayCategoryDTO>(resultPage);
    }

    @Override
    public List<PayCategorySimpleDTO> getList() {
        return this.baseMapper.selectListByStatus(PayCategoryStatusEnum.ENABLE.getValue());
    }

    @Override
    public Long add(PayCategoryAddParam param) {
        ValidatorUtils.validate(param);
        Assert.isTrue(checkCodeExist(param.getCode(), null), "编号已存在，请重新设置");
        PayCategory payCategory = ConvertUtils.source2Target(param, PayCategory.class);
        save(payCategory);
        return payCategory.getId();
    }

    @Override
    public Long edit(PayCategoryEditParam param) {
        ValidatorUtils.validate(param);
        PayCategory payCategory = getById(param.getId());
        Assert.isNull(payCategory, "修改的数据不存在或已被删除");
        Assert.isTrue(checkCodeExist(param.getCode(), payCategory.getId()), "编号已存在，请重新设置");
        payCategory.setName(param.getName());
        payCategory.setCode(param.getCode());
        payCategory.setStatus(param.getStatus());
        updateById(payCategory);
        return payCategory.getId();
    }

    @Override
    public boolean removeData(Long id) {
        if (removeById(id)) {
            payCategoryChannelService.removeByCategory(id);
            return true;
        }
        return false;
    }

    private boolean checkCodeExist(String code, Long id) {
        return count(Wrappers.<PayCategory>lambdaQuery()
                .eq(PayCategory::getCode, code)
                .ne(id != null, PayCategory::getId, id)) > 0;
    }
}

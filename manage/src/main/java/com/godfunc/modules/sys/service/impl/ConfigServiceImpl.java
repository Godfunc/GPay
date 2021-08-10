package com.godfunc.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.Config;
import com.godfunc.modules.sys.dto.ConfigDTO;
import com.godfunc.modules.sys.mapper.ConfigMapper;
import com.godfunc.modules.sys.param.ConfigAddParam;
import com.godfunc.modules.sys.param.ConfigEditParam;
import com.godfunc.modules.sys.service.ConfigService;
import com.godfunc.util.Assert;
import com.godfunc.util.ConvertUtils;
import com.godfunc.util.ValidatorUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Godfunc
 * @since 2019-12-01
 */
@Service
@CacheConfig(cacheNames = "config")
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Override
    public PageDTO<ConfigDTO> getPage(Long page, Long limit, String name) {
        IPage<ConfigDTO> resultPage = new Page<>(page, limit);
        List<ConfigDTO> list = this.baseMapper.selectCustomPage(resultPage, name);
        resultPage.setRecords(list);
        return new PageDTO<ConfigDTO>(resultPage);
    }

    @Override
    public Long add(ConfigAddParam param) {
        ValidatorUtils.validate(param);
        Assert.isTrue(count(Wrappers.<Config>lambdaQuery().eq(Config::getName, param.getName())) >= 1, "配置名已存在");
        Config config = ConvertUtils.source2Target(param, Config.class);
        save(config);
        return config.getId();
    }

    @Override
    @CachePut(key = "#param.name")
    public Long edit(ConfigEditParam param) {
        ValidatorUtils.validate(param);
        Config config = ConvertUtils.source2Target(param, Config.class);
        updateById(config);
        return config.getId();
    }

    @Override
    public Boolean removeDate(Long id) {
        return removeById(id);
    }
}

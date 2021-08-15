package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.Config;
import com.godfunc.mapper.ConfigMapper;
import com.godfunc.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "config")
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Override
    @Cacheable(unless = "#result == null", key = "#name")
    public Config getByName(String name) {
        Config config = getOne(Wrappers.<Config>lambdaQuery().eq(Config::getName, name));
        if (config == null || config.getValue() == null) {
            return null;
        }
        return config;
    }
}

package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.*;
import com.godfunc.mapper.ConfigMapper;
import com.godfunc.service.*;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    public String getByName(String name) {
        Config config = getOne(Wrappers.<Config>lambdaQuery().eq(Config::getName, name));
        if (config == null || config.getValue() == null) {
            return null;
        }
        return config.getValue();
    }
}

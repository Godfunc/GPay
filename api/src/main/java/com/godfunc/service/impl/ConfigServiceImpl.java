package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godfunc.entity.*;
import com.godfunc.mapper.ConfigMapper;
import com.godfunc.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    private final ObjectMapper objectMapper;

    public Config getByName(String name) {
        Config config = getOne(Wrappers.<Config>lambdaQuery().eq(Config::getName, name));
        if (config == null || config.getValue() == null) {
            return null;
        }
        return config;
    }

    @Override
    public Long getExpireSeconds(Config expireConfig) {
        try {
            String[] times = objectMapper.readValue(expireConfig.getRemark(), new TypeReference<String[]>() {
            });
            String time = times[Integer.parseInt(expireConfig.getValue() + 1)];
            if (time.endsWith("s")) {
                return Long.parseLong(time.replace("s", ""));
            } else if (time.endsWith("m")) {
                return Long.parseLong(time.replace("m", "")) * 60;
            } else if (time.endsWith("h")) {
                return Long.parseLong(time.replace("h", "")) * 60 * 60;
            }
        } catch (JsonProcessingException e) {
            log.error("expire config json转换异常 {}", e);
        }
        return null;
    }
}

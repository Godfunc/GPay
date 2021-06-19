package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Config;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface ConfigService extends IService<Config> {
    String getByName(String name);
}

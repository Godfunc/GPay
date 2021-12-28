package com.godfunc.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.godfunc.entity.Config;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Godfunc
 * @since 2019-12-01
 */
public interface ConfigMapper extends BaseMapper<Config> {

    List<Config> selectCustomPage(IPage resultPage, @Param("name") String name);
}

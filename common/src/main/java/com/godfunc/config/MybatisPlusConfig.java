package com.godfunc.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus 配置
 *
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Configuration
@MapperScan(basePackages = "com.godfunc.**.mapper")
public class MybatisPlusConfig {

    @Value("${mybatis-plus.global-config.workerId}")
    private Long workerId;

    @Value("${mybatis-plus.global-config.dataCenterId}")
    private Long dataCenterId;

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 解决id冲突问题，建议使用
     *
     * @return
     */
    @Bean
    public DefaultIdentifierGenerator defaultIdentifierGenerator() {
        return new DefaultIdentifierGenerator(workerId, dataCenterId);
    }
}

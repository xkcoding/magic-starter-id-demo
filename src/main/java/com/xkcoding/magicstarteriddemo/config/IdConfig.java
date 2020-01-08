package com.xkcoding.magicstarteriddemo.config;

import cn.hutool.core.date.DateUtil;
import com.xkcoding.magic.id.autoconfigure.IdDatabaseProperties;
import com.xkcoding.magic.id.support.factory.impl.DatabaseIdFactory;
import com.xkcoding.magic.id.support.factory.impl.RedisIdFactory;
import com.xkcoding.magic.id.support.factory.impl.SnowflakeIdFactory;
import com.xkcoding.magic.id.support.id.Id;
import com.xkcoding.magic.id.support.prefix.impl.DefaultPrefix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * <p>
 * 自动装配类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 18:00
 */
@Configuration
public class IdConfig {
@Bean
public Id snowflakeId() {
    return SnowflakeIdFactory.create()
            .dataCenterId(1L)
            .workerId(1L)
            .prefix(new DefaultPrefix("2019-"))
            .getInstance();
}

@Bean
public Id databaseId1(DataSource dataSource) {
    return DatabaseIdFactory.create()
            .businessName(() -> String.format("test_db_%s", DateUtil.today()))
            .prefix(() -> "2019-")
            .dataSource(dataSource)
            .step(1)
            .stepStart(100000)
            .retryTimes(3)
            .tableName("id_test")
            .getInstance();
}

    @Bean
    public Id databaseId2(DataSource dataSource, IdDatabaseProperties databaseProperties) {
        return DatabaseIdFactory.create()
                .businessName(() -> String.format("test_db_2_%s", DateUtil.today()))
                .prefix(() -> "2019-2-")
                .dataSource(dataSource)
                .step(databaseProperties.getStep())
                .stepStart(databaseProperties.getStepStart())
                .retryTimes(databaseProperties.getRetryTimes())
                .tableName(databaseProperties.getTableName())
                .getInstance();
    }

@Bean
public Id redisId() {
    return RedisIdFactory.create()
            .businessName(() -> String.format("test_redis_%s", DateUtil.today()))
            .prefix(() -> "2019-")
            .ip("localhost")
            .port(6379)
            .step(1)
            .stepStart(0)
            .getInstance();
}

}

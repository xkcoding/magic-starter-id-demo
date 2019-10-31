package com.xkcoding.magicstarteriddemo;

import com.xkcoding.magic.id.support.business.impl.DateBusinessName;
import com.xkcoding.magic.id.support.id.Id;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class IdTests {

    @Autowired
    private Id snowflakeId;
    @Autowired
    private Id databaseId1;
    @Autowired
    private Id databaseId2;
    @Autowired
    private Id redisId;

    @Test
    public void testSnowflake() {
        log.info("【snowflakeId】= {}", snowflakeId.nextId());
        log.info("【snowflakeId】= {}", snowflakeId.nextIdStr());

        log.info("【snowflakeId】= {}", snowflakeId.nextId(new DateBusinessName()));
        log.info("【snowflakeId】= {}", snowflakeId.nextIdStr(new DateBusinessName(), () -> "自定义"));
    }

    @Test
    public void testDatabase() {
        log.info("【databaseId1】= {}", databaseId1.nextId());
        log.info("【databaseId1】= {}", databaseId1.nextIdStr());

        log.info("【databaseId1】= {}", databaseId1.nextId(() -> "自定义模块-1"));
        log.info("【databaseId1】= {}", databaseId1.nextIdStr(() -> "自定义模块-1", () -> "自定义前缀-1-"));

        log.info("【databaseId2】= {}", databaseId2.nextId());
        log.info("【databaseId2】= {}", databaseId2.nextIdStr());
    }

    @Test
    public void testRedis() {
        log.info("【redisId】= {}", redisId.nextId());
        log.info("【redisId】= {}", redisId.nextIdStr());

        log.info("【redisId】= {}", redisId.nextId(() -> "自定义模块"));
        log.info("【redisId】= {}", redisId.nextIdStr(() -> "自定义模块", () -> "自定义前缀-"));
    }
}

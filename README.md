# magic-starter-id-demo

> 演示分布式主键生成模块的使用

## 配置

### 雪花算法

引入依赖之后，在配置文件配置 `worker-id` 和 `data-center-id`

```yaml
magic:
  id:
    snowflake:
      worker-id: 1
      data-center-id: 1
```

在代码中，直接注入即可

```java
@Autowired
private Id snowflakeId;
```

当然，也可以通过自定义 Bean 的形式配置

```java
@Bean
public Id snowflakeId() {
    return SnowflakeIdFactory.create().dataCenterId(1L).workerId(1L).prefix(new DefaultPrefix("2019-")).getInstance();
}
```

### 数据库步长

在配置文件配置`表信息`、`步长`、`重试次数`、以及`起始 ID`

```yaml
magic:
  id:
    database:
      step: 1
      retry-times: 3
      table-name: id_test
```

在配置类中配置

```java
@Bean
public Id databaseId2(DataSource dataSource, IdDatabaseProperties databaseProperties) {
    return DatabaseIdFactory
      .create()
      .businessName(() -> String.format("test_db_%s", DateUtil.today()))
      .prefix(() -> "2019-")
      .dataSource(dataSource)
      .step(databaseProperties.getStep())
      .stepStart(databaseProperties.getStepStart())
      .retryTimes(databaseProperties.getRetryTimes())
      .tableName(databaseProperties.getTableName())
      .getInstance();
}
```

### Redis 步长

在配置文件配置 `步长`、`起始 ID`

```yaml
magic:
  id:
    redis:
      step: 1
      step-start: 0
```

在配置类中配置

```java
@Bean
public Id redisId(IdRedisProperties redisProperties) {
    return RedisIdFactory
            .create()
            .businessName(() -> String.format("test_redis_%s", DateUtil.today()))
            .prefix(() -> "2019-")
            .ip("localhost")
            .port(6379)
            .step(redisProperties.getStep())
            .stepStart(redisProperties.getStepStart())
            .getInstance();
}
```

## 使用

```java
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
```


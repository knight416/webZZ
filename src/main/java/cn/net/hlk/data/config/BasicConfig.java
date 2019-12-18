package cn.net.hlk.data.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.client.RestTemplate;

//Spring JAVA配置文件

@Configuration
//@MapperScan(basePackages = "cn.net.hylink.data.mapper", sqlSessionFactoryRef = "testSqlSessionFactory")
@MapperScan(basePackages = "cn.net.hlk.data.mapper")//扫描mapper接口类
public class BasicConfig {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public TaskExecutor getTaskExecutor(){
        return new SimpleAsyncTaskExecutor();
    }

    /*@Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate, @Value("${spring.redis.default-expiration}") int expiredTime) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(expiredTime); //设置默认过期时间
        return cacheManager;
    }
    @Bean(name = "mdcInsertingFilter")
    public MDCInsertingServletFilter mdcInsertingFilter() {
        return new MDCInsertingServletFilter();
    }*/
    
    
   /* //多数据源
    @Bean(name="testDS")
    @ConfigurationProperties(prefix = "spring.datasource.test")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="testSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDS") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:cn/net/hylink/data/mapper/*.xml"));
        return bean.getObject();
    }*/
    
}

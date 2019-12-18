package cn.net.hlk.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import cn.net.hlk.data.config.DynamicDataSourceRegister;

@SpringBootApplication //主程序入口注解
@ComponentScan("cn.net.hlk") //扫描注解 spring扫描
@EnableScheduling //定时任务配置类 与 @Scheduled(fixedRate = 5000) @Scheduled(cron = "0 07 20 ? * *" )配合使用
@EnableAsync //异步调用配置注解 配合@Async使用
@EnableCaching //缓存注解 
@EnableTransactionManagement //事务注解
@ServletComponentScan //拦截注册类 
@EnableSwagger2 //swagger注解
//@Import({DynamicDataSourceRegister.class}) // 注册动态多数据源
public class HlkApplication { 
	public static void main(String[] args) {
		System.out.println("springboot入口");
		SpringApplication.run(HlkApplication.class, args);
	}
}

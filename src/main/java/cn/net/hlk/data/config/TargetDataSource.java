package cn.net.hlk.data.config;

import java.lang.annotation.Documented;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
/**
 *  
     * Title: TargetDataSource.java    
     * Description: 多数据源自定义注解
     * @author zhangzeheng       
     * @created 2018年3月16日 下午4:33:35
 */
@Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface TargetDataSource {
        String dataSource() default "";//数据源
    }
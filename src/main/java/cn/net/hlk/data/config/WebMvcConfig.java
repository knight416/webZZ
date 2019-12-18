package cn.net.hlk.data.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @package: cn.net.hylink.data.config   
 * @Title: WebMvcConfig   
 * @Description:虚拟路径配置
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年5月10日 上午10:25:13
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private FileUploadProperteis fileUploadProperteis;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(fileUploadProperteis.getStaticAccessPath()).addResourceLocations("file:" + fileUploadProperteis.getUploadFolder() + "/");       
    }

}

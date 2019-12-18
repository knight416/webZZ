package cn.net.hlk.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 文件上传相关属性
 * @create 2018-04-22 13:58
 **/
@Component
@Configuration
public class FileUploadProperteis {

    //静态资源对外暴露的访问路径
	@Value("${file.staticAccessPath}")
    private String staticAccessPath;

    //文件上传目录
	@Value("${file.uploadFolder}")
    private String uploadFolder ;

    public String getStaticAccessPath() {
        return staticAccessPath;
    }

    public void setStaticAccessPath(String staticAccessPath) {
        this.staticAccessPath = staticAccessPath;
    }

    public String getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }
}

package cn.net.hlk.data.pojo.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 柴志鹏
 * @date 2019/9/26 0026
 * @since 1.0.0
 */
@Data
public class FeatureMqtt {
    private String idCard;
//    private String pluginName;
//    private String biometricType;
    private String event;
    private String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    
}

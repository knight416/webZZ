package cn.net.hlk.data.pojo.user;

/**
 * 【描述】：特征插件枚举
 *
 * @author 柴志鹏
 * @date 2019/08/21
 */
public enum PluginEnum {
    /** 通用特征 */
    COMMONALITY_biometricCollection(""),
    /** 海康生物特征采集插件 */
    HIK_biometricCollection("815008"),
    /** 中控生物特征采集插件 */
    ZK_biometricCollection("815003"),
    /** 中控安卓生物特征采集插件 */
    ZK_android_biometricCollection("815003"),
    /** 飞瑞斯生物特征采集插件 */
    FIRS_biometricCollection("0"),
    /** 微耕采集插件 */
    WIEGAND_biometricCollection("815006"),
    /** 东城人脸 */
    DC_face_biometricCollection("");

    private String value;

    PluginEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public PluginEnum getEnumByValue(String value) {
        for(PluginEnum plugin : PluginEnum.values()){
            if(plugin.value.equals(value)){
                return plugin;
            }
        }
        return null;
    }
}
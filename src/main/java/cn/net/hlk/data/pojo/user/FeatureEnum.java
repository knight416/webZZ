package cn.net.hlk.data.pojo.user;

/**
 * 【描述】：特征枚举
 *
 * @author 柴志鹏
 * @date 2019/08/21
 */
public enum FeatureEnum {
    /** 人脸特征 */
    FaceFeature("0"),
    /** 人脸图片 */
    FaceImage("841003"),
    /** 指纹特征 */
    FingerprintFeature("841001"),
    /** 指纹照片 */
    FingerprintImage("0");

    private String value;

    FeatureEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public FeatureEnum getEnumByValue(String value) {
        for(FeatureEnum feature : FeatureEnum.values()){
            if(feature.value.equals(value)){
                return feature;
            }
        }
        return null;
    }
}

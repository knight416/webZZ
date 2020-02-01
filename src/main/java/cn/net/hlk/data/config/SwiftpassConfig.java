package cn.net.hlk.data.config;

import cn.net.hlk.util.ConfigUtil;
import cn.net.hlk.util.CustomConfigUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.InputStream;


/**
 * <一句话功能简述>
 * <功能详细描述>配置信息
 * 
 * @author  Administrator
 * @version  [版本号, 2018-2-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
@Configuration
public class SwiftpassConfig {
    
    /**
     * 交易密钥
     */
    @Value("${payment.key}")
    public static String key;
    @Value("${payment.mchPrivateKey}")
    public static String mchPrivateKey;
    @Value("${payment.platPublicKey}")
    public static String platPublicKey;
    
    /**
     * 商户号
     */
    @Value("${payment.mch_id}")
    public static String mch_id;
    
    /**
     * 支付接口请求url
     */
    @Value("${payment.req_url}")
    public static String req_url;

    /**
     * 通知url
     */
    @Value("${payment.notify_url}")
    public static String notify_url;


    static{
        try {
            key = CustomConfigUtil.getString("payment.key");
            mchPrivateKey = CustomConfigUtil.getString("payment.mchPrivateKey");
            platPublicKey = CustomConfigUtil.getString("payment.platPublicKey");
            mch_id = CustomConfigUtil.getString("payment.mch_id");
            req_url = CustomConfigUtil.getString("payment.req_url");
            notify_url = CustomConfigUtil.getString("payment.notify_url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

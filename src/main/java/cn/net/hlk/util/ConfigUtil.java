package cn.net.hlk.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigUtil {
    private static ResourceBundle resourceBundle = null;

    static {
        resourceBundle = ResourceBundle.getBundle("config");
    }

    /**
     * 得到配置文件中的值
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        }
        return null;
    }

    /**
     * @param bundleName
     * @return
     */
    public static Map<String, String> getBundleInfoMap(String bundleName) {
        ResourceBundle rb = ResourceBundle.getBundle(bundleName);
        Map<String, String> map = new HashMap<String, String>();

        Enumeration<String> en = rb.getKeys();

        while (en.hasMoreElements()) {
            String key = en.nextElement();
            map.put(key, String.valueOf(rb.getString(key)));
        }
        return map;
    }
}

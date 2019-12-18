package cn.net.hlk.util;

import org.apache.http.util.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonParser;

/**
 * @package: cn.net.hlk.util   
 * @Title: StringJsonUtil   
 * @Description:string json工具类
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年4月24日 下午2:13:26
 */
public class StringJsonUtil {

	public static boolean StringIsJson(Object str){
		String json = JSON.toJSONString(str).toString();
		if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
        	JSONObject jsonStr= JSONObject.parseObject(json);
            new JsonParser().parse(json);
            return true;
        } catch (Exception e) {
            return false;
        } 
	} 
}

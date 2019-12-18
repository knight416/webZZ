package cn.net.hlk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.hlk.data.pojo.PageData;

/**
 * @package: cn.net.hlk.util   
 * @Title: GlobalVariables   
 * @Description:全局变量
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年5月8日 下午2:26:32
 */
public class GlobalVariables {

	/**
	 * 全局map
	 */
	public  static Map<String,Object> globalMap;
	
	public static List<PageData> userPd;
	
	static{
		globalMap = new HashMap<String,Object>();
		userPd = new ArrayList<PageData>();
	}

	
	/**
	 * @Title: getGlobalMap
	 * @discription 全局map获取
	 * @author 张泽恒       
	 * @created 2018年5月8日 下午2:30:49     
	 * @return
	 */
	public static Map<String, Object> getGlobalMap() {
		return globalMap;
	}
	
	/**
	 * @Title: setGlobalMap
	 * @discription 全局map添加
	 * @author 张泽恒       
	 * @created 2018年5月8日 下午2:31:11     
	 * @param key
	 * @param value
	 */
	public  static void setGlobalMap(String key ,Object value){
		globalMap.put(key, value);
	}
	/**
	 * @Title: rmGlobalMap
	 * @discription 全局map key删除
	 * @author 张泽恒       
	 * @created 2018年5月8日 下午2:32:10     
	 * @param key
	 */
	public static void rmGlobalMap(String key){
		globalMap.remove(key);
		
	}
	/**
	 * 全局list 获取
	 * @return
	 */
	public static List<PageData> getUserPd(){
		return userPd;
	}
	
	/**
	 * 全局list添加
	 * @param pd
	 */
	public static void setUserPd(PageData pd){
		userPd.add(pd);
	}
	
	/**
	 * 全局list删除
	 * @param pd
	 */
	public static void rmUserPd(PageData pd){
		userPd.remove(pd);
	}
	
	public static void rmUserPdall(List<PageData> pd){
		userPd.removeAll(pd);
	}
	
	
}

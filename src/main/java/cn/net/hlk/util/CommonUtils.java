package cn.net.hlk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * 通用工具类
 * @author 
 */
public class CommonUtils {

	
	public CommonUtils() {
		super();
	}

	/**
	 * string类型转换为Int类型
	 * @param intstr
	 * @return
	 */
	public static int stringToInt(String intstr) {
		Integer integer;
		integer = Integer.valueOf(intstr);
		return integer.intValue();
	}

	/**
	 * Int类型转换为string类型
	 * @param value
	 * @return
	 */
	public static String intToString(int value) {
		Integer integer = new Integer(value);
		return integer.toString();
	}

	/**
	 * string类型转换为Float类型
	 * @param floatstr
	 * @return
	 */
	public static float stringToFloat(String floatstr) {
		Float floatee;
		floatee = Float.valueOf(floatstr);
		return floatee.floatValue();
	}

	/**
	 * Float类型转换为string类型
	 * @param value
	 * @return
	 */
	public static String floatToString(float value) {
		Float floatee = new Float(value);
		return floatee.toString();
	}

	/**
	 * null转换为String类型
	 * @param str
	 * @return
	 */
	public static String nullToString(String str) {
		str = str == null ? "" : str;
		return "";
	}

	// / <summary>
	// / 插入SQL时替换字符
	// / </summary>
	// / <param name="str"></param>
	// / <returns></returns>
	public static String Encode(String str) {
		str = str.replace("'", "''");
		str = str.replace("\"", "&quot;");
		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt;");
		str = str.replace("\n", "<br>");
		str = str.replace("“", "&ldquo;");
		str = str.replace("”", "&rdquo;");
		return str;
	}

	// / <summary>
	// / 取SQL值时还原字符
	// / </summary>
	// / <param name="str"></param>
	// / <returns></returns>
	public static String Decode(String str) {
		str = str.replace("&amp;", "&");
		str = str.replace("&rdquo;", "”");
		str = str.replace("&ldquo;", "“");
		str = str.replace("<br>", "\n");
		str = str.replace("&gt;", ">");
		str = str.replace("&lt;", "<");
		str = str.replace("&quot;", "\"");
		str = str.replace("''", "'");
		return str;
	}

	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 字符串转换成ASCII码
	 * 
	 * @param value
	 * @return
	 */
	public static String stringToAscii(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i != chars.length - 1) {
				sbu.append((int) chars[i]).append(",");
			} else {
				sbu.append((int) chars[i]);
			}
		}
		return sbu.toString();
	}
	
	/** 
	 * 处理浏览器下载文件中文乱码
     * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性 
     * @throws UnsupportedEncodingException  
     */  
    public static String encodeChineseDownloadFileName(  
        HttpServletRequest request, String pFileName) throws UnsupportedEncodingException {
      
     	String filename = null;    
        String agent = request.getHeader("USER-AGENT");    
        
        if (null != agent){ 
        	if (-1 != agent.indexOf("Edge")) {//Edge   
            	filename = java.net.URLEncoder.encode(pFileName, "UTF-8");     
            }else if (-1 != agent.indexOf("Firefox")) {//Firefox   
                filename = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))+ "?=";    
            }else if (-1 != agent.indexOf("Chrome")) {//Chrome   
                filename = new String(pFileName.getBytes(), "ISO8859-1");    
            } else {//IE7+
                filename = java.net.URLEncoder.encode(pFileName, "UTF-8");    
                filename = StringUtils.replace(filename, "+", "%20");//替换空格    
            }    
        } else {    
            filename = pFileName;    
        }    
        return filename;   
    }
    
    /**
     * @Title: getBrowserAgent
     * @Description: 获取浏览器agent信息
     * @param  request
     * @return String
     * @throws
     */
    public static String getBrowserAgent(HttpServletRequest request){
        String agent = request.getHeader("USER-AGENT");    
        return agent;   
    }
    
    
  //colb转String方法
    public static String ClobToString(Clob clob) {
          String reString = "";
          Reader is = null;
          try {
              is = clob.getCharacterStream();
          } catch (SQLException e) {
              e.printStackTrace();
          }
          BufferedReader br = new BufferedReader(is);
          String s = null;
          try {
              s = br.readLine();
          } catch (IOException e) {
              e.printStackTrace();
          }
          StringBuffer sb = new StringBuffer();
          while (s != null) {
              //执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
              sb.append(s);
              try {
                  s = br.readLine();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          reString = sb.toString();
          return reString;
      }
    /**
     * 获取字符串实际长度（英文1个字符，中文两个字符）
     * @param str
     * @return
     */
	public static int getStrLeng(String str){
		int realLength = 0;
		if(str != null){
			int len = str.length();
			int charCode = -1;
			for(int i = 0; i < len; i++){
				charCode = str.codePointAt(i);
				if (charCode >= 0 && charCode <= 128) {
					realLength += 1;
				}else{
					// 如果是中文则长度加2
					realLength += 2;
				}
			}
		}
		return realLength;
	}
	
	/**
	* 判断非负整数
	* 只包含0~9；如果长度大于1，首位不能为0
	* @param str
	* @return
	*/
	public static boolean isNumberic(String str){
		boolean flag = false;
		if(StringUtils.length(str) > 0){
			if(StringUtils.containsOnly(str, new char[]{'0','1','2','3','4','5','6','7','8','9'})){
				if(!(StringUtils.length(str) > 1 && StringUtils.substring(str, 0, 1).equals("0"))){
					flag = true;
				}
			}
		}
		
		return flag;
	}
	
	/**
	* 判断手机号码
	* 以1开始，后根10位数字
	* @param str
	* @return
	*/
	public static boolean checkMobile(String str){
		boolean flag = false;
		if(StringUtils.length(str) > 0){
			if(StringUtils.length(str) == 11){
				String regEx="[1]{1}[0-9]{10}";
				flag = Pattern.compile(regEx).matcher(str).find();
			}
		}
		
		return flag;
	}
	
	/**
	 * 判断是否为整数
	 * @param str
	 */
	public static boolean isInt(String str){
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 判断是否为固定电话
	 * 国家代码(2到3位)-区号(2到3位)-电话号码(7到8位)-分机号(3位)
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str){
		Pattern pattern = Pattern.compile("^(([0\\+]\\d{2,3}(-)?)?(0\\d{2,3})(-)?)?(\\d{7,8})(-(\\d{3,}))?$");
		return pattern.matcher(str).matches();
	}
	/**
	 * 判断是否为传真
	 * @param str
	 * @return
	 */
	public static boolean checkCz(String str){
		Pattern pattern = Pattern.compile("^[+]{0,1}(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$");
		return pattern.matcher(str).matches();
	}
	/**
	 * 判断是否为邮箱
	 * @param str
	 * @return
	 */
	public static boolean checkEmail(String str){
		Pattern pattern = Pattern.compile( "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		return pattern.matcher(str).matches();
	}
	/**
	 * 判断是8-20位否为英文和数字组合
	 * @param str
	 * @return
	 */
	public static boolean checkZcmm(String str){
		Pattern pattern = Pattern.compile( "^(?=.*[a-z]+)(?=.*[0-9]+)(?=.*[A-Z]+)[a-zA-Z0-9]{9,20}$");
		return pattern.matcher(str).matches();
	}
}

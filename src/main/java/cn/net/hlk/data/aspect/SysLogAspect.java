package cn.net.hlk.data.aspect;


import cn.net.hlk.data.mapper.LogMapper;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.pojo.PageData;
import io.jsonwebtoken.Jwts;

import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;

/**
 * 【描 述】：系统日志，切面处理类
 * 【环 境】：J2SE 1.8
 * @author   董洪伟	61084876@qq.com
 * @version  version 1.0
 * @since    2018年1月8日
 */
@Aspect
@Component
public class SysLogAspect {
	
	@Autowired
	private LogMapper logMapper;
	
	 @Autowired 
	 private HttpServletRequest request;
	
	@Pointcut("@annotation(cn.net.hlk.data.annotation.SysLog)")
	public void logPointCut() { 
		
	}
	private final Logger optLogger = LoggerFactory.getLogger("optLog");
	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String action  = "",type = "", opt_method = "",opt_id = "" ,
				opt_name = "",params = "" , ip = "", cost_time = "" , details = "" ,
				opt_idcard = "" , opt_unit_code = "";
		PageData logPd = new PageData();
		SysLog syslog = method.getAnnotation(SysLog.class);
		if(syslog != null){
			//注解上的描述
			String value = syslog.value();
			String[] split = value.split("-");
		    action = split[0];
		    if(split.length>1){
				type = split[1];
		    }
			
		} 

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		opt_method = className + "." + methodName + "()";
	

		//请求的参数
		Object[] args = joinPoint.getArgs();
		params =  JSON.toJSONString(args);
		String authorization = request.getHeader("Authorization");
		PageData jsonpd = null;
		if(authorization != null && !"".equals(authorization)) {
			if (Jwts.parser().isSigned(authorization)) {
				try {
				String PayloadString = authorization.split("\\.")[1];
				String jsonStr = new String( Base64.decodeBase64(PayloadString));
				optLogger.info("jsonStr:"+jsonStr);
				PageData pd = null;
				ObjectMapper mapper = new ObjectMapper();
			    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
			    mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);  
			    jsonpd =  mapper.readValue(jsonStr,PageData.class);
			    PageData depart = JSON.parseObject(JSON.toJSONString(jsonpd.get("department")), PageData.class);
			    opt_id = String.valueOf(jsonpd.get("id"));
			    opt_name = jsonpd.getString("name");
			    opt_idcard = jsonpd.getString("idCard");
			    opt_unit_code = depart.getString("code");
				}  catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		//获取request
		//设置IP地址
		ip = request.getRemoteAddr();
		//设置IP地址
		ip = request.getHeader("x-forwarded-for"); 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
           ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
           ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
           ip = request.getRemoteAddr(); 
		} 
		details =  opt_name +"("+ip+") 访问:"+className + "." + methodName + "()"+" 传入:"+ params +" 执行:"+action+type+" 共用时:"+time +"毫秒";
		logPd.put("action", action);
		logPd.put("type", type);
		logPd.put("method", opt_method);
		logPd.put("opt_id", opt_id);
		logPd.put("opt_name", opt_name);
		logPd.put("params", params);
		logPd.put("ip", ip);
		logPd.put("time", (int)time);
		logPd.put("details", details);
		logPd.put("opt_idcard", opt_idcard);
		logPd.put("opt_unit_code", opt_unit_code);
		optLogger.info(JSON.toJSONString(logPd));
		//保存系统日志
		logMapper.saveLog(logPd);
	}
}

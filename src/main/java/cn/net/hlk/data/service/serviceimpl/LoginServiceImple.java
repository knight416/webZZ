package cn.net.hlk.data.service.serviceimpl;


import cn.net.hlk.data.mapper.LoginMapper;
import cn.net.hlk.data.mapper.UserMapper;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.department.Group;
import cn.net.hlk.data.pojo.log.SysLogEntity;
import cn.net.hlk.data.pojo.user.User;
import cn.net.hlk.data.service.ILoginService;
import cn.net.hlk.util.HttpContextUtils;
import cn.net.hlk.util.IPUtils;
import cn.net.hlk.util.JwtUtil;
import cn.net.hlk.util.StringUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

//import cn.net.hylink.util.MD5;

//import cn.com.jit.assp.ias.principal.UserPrincipal;
//import cn.com.jit.assp.ias.saml.saml11.SAMLAttributes;
//import cn.com.jit.assp.ias.saml.saml11.SAMLAttributes.SAMLAttributeName;
//import cn.com.jit.assp.ias.saml.saml11.SAMLConstants;
//import cn.com.jit.assp.ias.saml.x509.X509Constants;
//import cn.com.jit.assp.ias.sp.saml11.SPUtil;


/**
 * 【描 述】：登陆service实现类
 * 【表 名】：<数据库表名称>
 * 【环 境】：J2SE 1.8
 * @author   董洪伟	61084876@qq.com
 * @version  version 1.0
 * @since    2018年1月16日
 */
@Service
public class LoginServiceImple extends BaseServiceImple implements
		ILoginService {

	@Autowired
	private LoginMapper loginMapper;
	@Autowired
	private UserMapper 	userMapper;


	/**
	 * 【重载方法】
	 * 【描 述】：登陆功能
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.ILoginService#login(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public ResponseBodyBean login(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		ReasonBean reasonBean = new ReasonBean();
		String username = (String) pd.get("username");
		String password = (String) pd.get("password");
		User user = loginMapper.getUserByIdCard(pd);
		PageData userOtherMessage = userMapper.getuserOtherMessage(user.getUid());
		if(StringUtil2.isNotEmpty(userOtherMessage) && StringUtil2.isNotEmpty(userOtherMessage.get("user_message"))){
			user.setUser_message(JSON.parseObject(JSON.toJSONString(userOtherMessage.get("user_message")),PageData.class));
		}
		if(user == null){
			reasonBean.setCode("500");
			reasonBean.setText("用户名或密码错误！");
			responseBodyBean.setReason(reasonBean);
		} else {
//			Integer visiable = user.getVisiable();
			if(user.getState() == 0){
				reasonBean.setCode("500");
				reasonBean.setText("用户不可用！");
				responseBodyBean.setReason(reasonBean);
			} else {
				String pass = user.getPassword();
				//String md5 = MD5.md5(password);
				if(!password.equals(pass)){
					reasonBean.setCode("500");
					reasonBean.setText("用户名或密码错误！");
					responseBodyBean.setReason(reasonBean);
				}else {
					long ttlMillis = 24*24*60*60*1000L;
					String id2 = user.getUid();
					String department = user.getDepartment();
					String token = JwtUtil.createJWT("Security Center",user, ttlMillis);
					PageData pdData = new PageData();
					pdData.put("token", token);
					user.setPassword(null);
					pdData.put("user", user);
					responseBodyBean.setResult(pdData);
				}
			}
		}
		
		return responseBodyBean;
	}

	@Override
	public PageData loginToken(User user) {
		PageData resultBody = new PageData();
		long ttlMillis = 24*24*60*60*1000;
		String id2 = user.getUid();
		String department = user.getDepartment();
		// Group group = jwtMapper.getDepartment(department);
		// List<Group> grouptList = jwtMapper.getDepartmentList(id2);
		// user.setGroup(group);
		// user.setGroupList(grouptList);
		String token = JwtUtil.createJWT("Security Center", user, ttlMillis);
		PageData pdData = new PageData();
		pdData.put("token", token);
		user.setPassword(null);
		pdData.put("user", user);
		resultBody.put("reason", null);
		resultBody.put("result", pdData);
//		responseBodyBean.setResult(pdData);
// 		String id = user.getId();
// 		String name = user.getName();
// 		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
// 		String ip = IPUtils.getIpAddr(request);
// 		SysLogEntity sysLog = new SysLogEntity();
// 		sysLog.setAction("登录系统");
// 		sysLog.setMethod("cn.net.hylink.data.controller.LoginController.loginToken()");
// 		sysLog.setType("用户");
// 		sysLog.setParams(new Gson().toJson(user));
// 		sysLog.setTime(0L);
// 		sysLog.setIp(ip);
// 		sysLog.setDetails(name+"("+ip+")"+"访问:cn.net.hylink.data.controller.LoginController.loginToken() 传入:"+""+" 执行:登录 ");
// 		sysLog.setOpt_id(id);
// 		sysLog.setOpt_name(name);
// 		// logServiceImpl.saveLog(sysLog);
// //		logServiceImpl.saveLog(id, name, "登陆系统", "用户", user.getIdCard());
		return resultBody;
	}




	/**
	 * 【重载方法】
	 * 【描 述】：修改密码
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.ILoginService#updatePassword(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public ResponseBodyBean updatePassword(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		ReasonBean reasonBean = new ReasonBean();
		String oldPassword = (String) pd.get("oldPassword");
		String newPassword = (String) pd.get("newPassword");
		String username = (String) pd.get("username");
		String system_id = (String) pd.get("system_id");
		User user = loginMapper.getUserByIdCard(pd);
		String password = user.getPassword();
		if(!oldPassword.equals(password)){  //if(!MD5.md5(oldPassword).equals(password)){
			reasonBean.setCode("500");
			reasonBean.setText("原密码输入错误！");
			responseBodyBean.setReason(reasonBean);
		} else {
			PageData pageData = new PageData();
		//	newPassword = MD5.md5(newPassword);
			pageData.put("username", username);
			pageData.put("password", newPassword);
			pageData.put("system_id", system_id);
			Integer updatePassword = loginMapper.updatePassword(pageData);
			if(updatePassword == 1){
				responseBodyBean.setResult("success");
			}
		}
		return responseBodyBean;
	}


	@Override
	public ResponseBodyBean getIPpath(HttpServletRequest request){
//		String ip1 = request.getLocalAddr();//本地IP  172.19.12.165
//		String ip2 = request.getLocalName();//本地机器名 PC-20120726UOVG
//		String ip3 = request.getRequestURI();//访问接口地址   /login/getIP
//		String ip4 = request.getServletPath();//  同上/login/getIP
//		String ip5 = request.getRemoteAddr();   // 获取使用者IP  172.19.12.207
//		// 使用代理服务时，先执行下列方法获取IP
//		String ipHeader1 = request.getHeader("x-forwarded-for");  
//		String ipHeader2 = request.getHeader("Proxy-Client-IP"); 
//		String ipHeader3 = request.getHeader("X-Forwarded-For"); 
//		String ipHeader4 = request.getHeader("WL-Proxy-Client-IP"); 
		ResponseBodyBean getIP = new ResponseBodyBean();
		String ip = request.getHeader("x-forwarded-for"); 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	    	ip = request.getHeader("X-Forwarded-For"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	    	ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getRemoteAddr(); 
	   	} 
		PageData pdIP = new PageData();
		if(ip==null){
			getIP.setReason(new ReasonBean("406", "获取失败！"));
		}else{
			pdIP.put("ip", ip);
			getIP.setResult(pdIP);
		}
		logger.info("该IP地址登陆："+ip);
		return getIP;
	}
	

}

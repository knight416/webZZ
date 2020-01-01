 

package cn.net.hlk.data.controller;



import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.annotation.UserLoginToken;
import cn.net.hlk.data.config.FileUploadProperteis;
import cn.net.hlk.data.mapper.SystemMapper;
import cn.net.hlk.data.mapper.UserMapper;
import cn.net.hlk.data.poi.easypoi.easypoiUtil;
import cn.net.hlk.data.poi.poi.WordUtils;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.resource.ResourceBean;
import cn.net.hlk.data.pojo.role.Role;
import cn.net.hlk.data.pojo.user.FeatureEnum;
import cn.net.hlk.data.pojo.user.PluginEnum;
import cn.net.hlk.data.pojo.user.User;
import cn.net.hlk.data.pojo.user.UserExcel;
import cn.net.hlk.data.service.IUserService;
import cn.net.hlk.util.CustomConfigUtil;
import cn.net.hlk.util.FileUpload;
import cn.net.hlk.util.FileUtil;
import cn.net.hlk.util.ImageAnd64Binary;
import cn.net.hlk.util.JwtUtil;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Cleanup;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 【描 述】：<用户管理模块控制层>
 * 【表 名】：<数据库表名称>
 * 【环 境】：J2SE 1.8
 * @author   张旭	zhangxu@hylink.net.cn
 * @version  version 1.0
 * @since    2018年1月8日
 */
@Api(description = "用户管理", value = "/")
@RestController
@EnableAutoConfiguration
@RequestMapping(value="/user")
public class UserController extends BaseController {
	
	@Autowired
    private IUserService userService;
	@Autowired
    private UserMapper userMapper;
	@Autowired
	public FileUploadProperteis fileUploadProperteis;
	@Autowired
	public SystemMapper systemMapper;
	
	/** 【描 述】：openfire开关 */
	final static boolean OPENFIRE_SWITCH = "1".equals(CustomConfigUtil.getString("openfire.enable"));
    
	@ApiOperation(value = "用户查询", notes = "用户查询方法")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "page", dataType = "Page", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({
        @ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
        @ApiResponse(code=201,message="资源已被创建"),
        @ApiResponse(code=401,message="未授权"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=403,message="拒绝访问"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
        @ApiResponse(code=406,message="不是指定的数据类型"),
        @ApiResponse(code=500,message="服务器内部错误")
     })
	@RequestMapping(value="/findAllUser", method=RequestMethod.POST)
	public  @ResponseBody
	ResponseBodyBean findAllUserlistPage(@RequestHeader String Authorization, @RequestBody Page page ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		PageData pdPageData = new PageData();
		List<PageData> findAllUser = userService.findAllUserlistPage(page);
		pdPageData.put("list", findAllUser);
		pdPageData.put("page", page);
		status = HttpStatus.OK.value();
		responseBodyBean.setResult(pdPageData);
		response.setStatus(status);
		return responseBodyBean;	
		
	}
	
	
	@ApiOperation(value = "用户删除", notes = "用户删除方法")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({
        @ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
        @ApiResponse(code=201,message="资源已被创建"),
        @ApiResponse(code=401,message="未授权"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=403,message="拒绝访问"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
        @ApiResponse(code=406,message="不是指定的数据类型"),
        @ApiResponse(code=500,message="服务器内部错误")
     })
	@SysLog("删除-用户")
	@UserLoginToken
	@RequestMapping(value="/deleteUser", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean deleteUser(@RequestHeader String Authorization, @RequestBody PageData pd ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
		String optName = (String) parseJwt.getBody().get("name");
		pd.put("updateuser", optName);
		responseBodyBean = userService.deleteUser(pd);
		status = HttpStatus.OK.value();
		response.setStatus(status);
		return responseBodyBean;
	}	
	
	@ApiOperation(value = "用户禁用", notes = "用户禁用方法")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({
        @ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
        @ApiResponse(code=201,message="资源已被创建"),
        @ApiResponse(code=401,message="未授权"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=403,message="拒绝访问"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
        @ApiResponse(code=406,message="不是指定的数据类型"),
        @ApiResponse(code=500,message="服务器内部错误")
     })
	@RequestMapping(value="/disableUser", method=RequestMethod.POST)
	@SysLog("禁用-用户")
	@UserLoginToken
	public  @ResponseBody ResponseBodyBean disableUser(@RequestHeader String Authorization, @RequestBody PageData pd ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
		String optId = (String) parseJwt.getBody().get("id");
		String optName = (String) parseJwt.getBody().get("name");
	//	String idcard = (String) parseJwt.getBody().get("idcard");
		pd.put("updateuser", optName);
		responseBodyBean = userService.disableUser(pd);
		status = HttpStatus.OK.value();
		response.setStatus(status);
		return responseBodyBean;
	}
	@ApiOperation(value = "用户启用", notes = "用户启用方法")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({
        @ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
        @ApiResponse(code=201,message="资源已被创建"),
        @ApiResponse(code=401,message="未授权"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=403,message="拒绝访问"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
        @ApiResponse(code=406,message="不是指定的数据类型"),
        @ApiResponse(code=500,message="服务器内部错误")
     })
	@RequestMapping(value="/ableUser", method=RequestMethod.POST)
	@SysLog("启用-用户")
	@UserLoginToken
	public  @ResponseBody ResponseBodyBean ableUser(@RequestHeader String Authorization, @RequestBody PageData pd ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
		String optName = (String) parseJwt.getBody().get("name");
		pd.put("updateuser", optName);
		Integer ableUser = userService.ableUser(pd);
		if(ableUser >= 1){
			responseBodyBean.setResult("success");
			status = HttpStatus.OK.value();
		}
		response.setStatus(status);
		return responseBodyBean;
	}
    
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "添加用户", notes = "添加用户方法")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "")})
	@ApiResponses({
        @ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
        @ApiResponse(code=201,message="资源已被创建"),
        @ApiResponse(code=401,message="未授权"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=403,message="拒绝访问"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
        @ApiResponse(code=406,message="不是指定的数据类型"),
        @ApiResponse(code=500,message="服务器内部错误")
     })
	@SysLog("添加-用户")
	@RequestMapping(value="/addUser", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean addUser(@RequestBody PageData pd ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		ReasonBean reason = failresult(pd);
		if(!("200").equals(reason.getCode())){
			responseBodyBean.setReason(reason);
			response.setStatus(HttpStatus.OK.value());
			return responseBodyBean;
		}else{
			if(pd.getString("password")==null || ("").equals(pd.getString("password"))){
				reason= new ReasonBean("fail", "密码不能为空"); 
	        	responseBodyBean.setReason(reason);
				response.setStatus(HttpStatus.OK.value());
				return responseBodyBean;
			}
			Integer addUser = 0;
			pd.put("updateuser", "注册");
			addUser = userService.addUser(pd);
			if(addUser == 1) {
    			responseBodyBean.setResult("success");
    			status = HttpStatus.OK.value();
    		}
    		response.setStatus(status);
    		return responseBodyBean;	
		}          	
	}
	
	@ApiOperation(value = "根据id查询用户", notes = "根据id查询用户方法")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({
        @ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
        @ApiResponse(code=201,message="资源已被创建"),
        @ApiResponse(code=401,message="未授权"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=403,message="拒绝访问"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
        @ApiResponse(code=406,message="不是指定的数据类型"),
        @ApiResponse(code=500,message="服务器内部错误")
     })
	@RequestMapping(value="/findById", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean findById(@RequestHeader String Authorization, @RequestBody PageData pd ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		//ReasonBean reason = null;
		PageData pdPageData = new PageData();
		PageData user = userService.findById(pd);
		pdPageData.put("user", user);
		status = HttpStatus.OK.value();
		responseBodyBean.setResult(pdPageData);
		response.setStatus(status);
		return responseBodyBean;	
	}

	@ApiOperation(value = "修改用户", notes = "修改用户方法")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({
        @ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
        @ApiResponse(code=201,message="资源已被创建"),
        @ApiResponse(code=401,message="未授权"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=403,message="拒绝访问"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
        @ApiResponse(code=406,message="不是指定的数据类型"),
        @ApiResponse(code=500,message="服务器内部错误")
     })
	@SysLog("修改-用户")
	@UserLoginToken
	@RequestMapping(value="/editUser", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean editUser(@RequestHeader String Authorization, @RequestBody PageData pd ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
		String optName = (String) parseJwt.getBody().get("name");
		pd.put("updateuser", optName);
		Integer editUser;
		if("admin".equals(pd.getString("name"))){
			if(!((Integer)pd.get("id")).equals(1)){
				responseBodyBean.setReason(new ReasonBean("fail","用户名不合法！"));
				status = HttpStatus.OK.value();
				return responseBodyBean;
			}
		}
		editUser = userService.editUser(pd);
		if(editUser ==1){
			responseBodyBean.setResult("success");
			status = HttpStatus.OK.value();
		}
		response.setStatus(status);
		return responseBodyBean;
	}
	



	/** 
	 * 【描 述】：<验证添加信息非空>
	 * @param pd
	 * @return
	 */

	private ReasonBean failresult(PageData pd){
		ReasonBean reason = new ReasonBean("200","验证通过！");
		//验证信息非空
		if (pd.getString("name")==null ||  ("").equals(pd.getString("name"))) {
			reason.setCode("fail");
        	reason.setText("姓名不能为空");
        }else{
        	if(pd.getString("name").length()>10){
        		reason.setCode("fail");
            	reason.setText("姓名超长");
        	}
        }
		User user = userService.usernameVerification(pd);
		if(user != null){
			reason.setCode("fail");
			reason.setText("用户名已存在");
		}
        // if (pd.getString("idCard")==null ||  ("").equals(pd.getString("idCard"))) {
        // 	reason.setCode("fail");
        // 	reason.setText("身份证号不能为空");
        // } else{
        // 	if(pd.getString("idCard").length()!=18){
        // 		reason.setCode("fail");
        //     	reason.setText("身份证号不合规！");
        // 	}
        // }
		return reason;
	}

	@ApiOperation(value = "用户名验证", notes = "用户名验证")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({
			@ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code=201,message="资源已被创建"),
			@ApiResponse(code=401,message="未授权"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=403,message="拒绝访问"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
			@ApiResponse(code=406,message="不是指定的数据类型"),
			@ApiResponse(code=500,message="服务器内部错误")
	})
	@RequestMapping(value="/usernameVerification", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean usernameVerification(@RequestHeader String Authorization, @RequestBody PageData pd ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		//ReasonBean reason = null;
		PageData pdPageData = new PageData();
		User user = userService.usernameVerification(pd);
		boolean falsg = true;
		if(user != null){
			falsg = false;
		}
		pdPageData.put("isok", falsg);
		status = HttpStatus.OK.value();
		responseBodyBean.setResult(pdPageData);
		response.setStatus(status);
		return responseBodyBean;
	}


	/**
	 * @Title load
	 * @Description 下载
	 * @author 张泽恒
	 * @date 2019/12/29 20:18
	 * @param [Authorization, pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@ApiOperation(value = "下载", notes = "下载")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({
			@ApiResponse(code=200,message="指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code=201,message="资源已被创建"),
			@ApiResponse(code=401,message="未授权"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=403,message="拒绝访问"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对"),
			@ApiResponse(code=406,message="不是指定的数据类型"),
			@ApiResponse(code=500,message="服务器内部错误")
	})
	@RequestMapping(value="/load", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean load(@RequestHeader String Authorization, @RequestBody PageData pd ) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			List<PageData> pdList = new ArrayList<PageData>();
			if(pd != null
					){
				//根据用户id 获取信息
				PageData user = userService.findById(pd);
				if(user != null){
					String name = user.getString("name");//姓名
					String photo = user.getString("photo");//照片
					String email = user.getString("email");//emasil
					PageData user_message = JSON.parseObject(JSON.toJSONString(user.get("user_message")),PageData.class);
					PageData userinfo = JSON.parseObject(JSON.toJSONString(user_message.get("userinfo")),PageData.class);//个人信息

					String chat = userinfo.getString("chat");//线上聊天手机号码
					String mall = userinfo.getString("mall");//个人邮编
					String fmall = userinfo.getString("fmall");//家庭邮编
					String hukou = userinfo.getString("hukou");//户口
					hukou = systemMapper.getNameByCode(hukou);
					String phone = userinfo.getString("phone");//大陆手机号码
					String vision = userinfo.getString("vision");//近视
					String address = userinfo.getString("address");//个人联系地址
					String id_card = userinfo.getString("id_card");//身份证号
					String married = userinfo.getString("married");//已婚
					String workday = userinfo.getString("workday");//工作时间
					String birthday = userinfo.getString("birthday");//生日
					String jobtitle = userinfo.getString("jobtitle");//专业职称名称
					String wanphone = userinfo.getString("wanphone");//台湾手机号码
					String political = userinfo.getString("political");//中共党员
					String residence = userinfo.getString("residence");//住址
					residence = systemMapper.getNameByCode(residence);
					String bodyheight = userinfo.getString("bodyheight");//身高
					String workstatus = userinfo.getString("workstatus");//状态
					String familyphone = userinfo.getString("familyphone");//家庭联系电话
					String jobtitlelevel = userinfo.getString("jobtitlelevel");//对应职称等级




					PageData rap = JSON.parseObject(JSON.toJSONString(user_message.get("rap")),PageData.class);//在校工作情况
					List<PageData> train = JSON.parseArray(JSON.toJSONString(user_message.get("train")),PageData.class);//培训
					List<PageData> computer = JSON.parseArray(JSON.toJSONString(user_message.get("computer")),PageData.class);//计算机能力
					List<PageData> language = JSON.parseArray(JSON.toJSONString(user_message.get("language")),PageData.class);//外语
					List<PageData> education = JSON.parseArray(JSON.toJSONString(user_message.get("education")),PageData.class);//教育
					List<PageData> certificate = JSON.parseArray(JSON.toJSONString(user_message.get("certificate")),PageData.class);//证书
					List<PageData> workexperience = JSON.parseArray(JSON.toJSONString(user_message.get("workexperience")),PageData.class);//工作
					List<PageData> jobintention = JSON.parseArray(JSON.toJSONString(user_message.get("jobintention")),PageData.class);//求职意向

					String fileName= new String(name+"简历.doc");    //生成word文件的文件名
					//虚拟路径存储
					String realPath = fileUploadProperteis.getUploadFolder();
					String filePath = realPath + File.separator+ "load"+ File.separator+fileName;
					FileUtil.createDir(filePath);

					//文件地址
					FileOutputStream fopts = new FileOutputStream(filePath);
					// OutputStream out = new FileOutputStream(filePath);
					String url = "/upload"+ File.separator+ "load"+File.separator+fileName;

					//模板导出
					WordUtils wordUtil=new WordUtils();
					Map<String, Object> params = new HashMap<String, Object>();



					params.put("${name}", name);
					params.put("${workstatus}", workstatus);

					String info1 = birthday+"|"+married+"|籍贯："+hukou+"|"+bodyheight+"|视力"+vision;
					params.put("${info1}", info1);
					String info2 = "目前所在地: "+residence+"\n";
					params.put("${info2}", info2);
					String info3 = "个人电话:" +phone+"\n"+
							"台湾手机号码" +wanphone+"\n"+"\r"+
							"电子邮箱:" +email +"\n"+
							"线上聊天号码:"+chat+"\n";
					params.put("${info3}", info3);
					String info4 = "联系地址:" +address+"\n"+
							"个人联系地址" +address+"\n"+"\r"+
							"家庭电话:" +familyphone+"\n"+
							"家庭联系电话"+familyphone+"\n";
					params.put("${info4}", info4);

					//求职意向
					String jobIntentionStr = new String();
					if(jobintention != null && jobintention.size() > 0){
						for(PageData jobintentionpd : jobintention){
							String JobIntention0 = jobintentionpd.getString("jobintention0");
							String JobIntention1 = jobintentionpd.getString("jobintention1");
							String JobIntention2 = jobintentionpd.get("jobintention2").toString();
							String JobIntention3 = jobintentionpd.getString("jobintention3");
							String JobIntention4 = jobintentionpd.getString("jobintention4");
							JobIntention3 = systemMapper.getNameByCode(JobIntention3);
							JobIntention1 = systemMapper.getNameByCode(JobIntention1);
							jobIntentionStr = jobIntentionStr+"职务："+JobIntention0+"      月薪："+JobIntention2+"      工作方式："+JobIntention3+"      工作地："+JobIntention1+"\n"+"\r" ;
						}
					}
					params.put("${jobIntention}", jobIntentionStr);
					//工作经历
					String workexperienceStr = new String();
					if(workexperience != null && workexperience.size() > 0){
						for(PageData workexperiencepd : workexperience){
							String workexperience0 = workexperiencepd.getString("workexperience0");
							String workexperience1 = workexperiencepd.getString("workexperience1");
							String workexperience2 = workexperiencepd.getString("workexperience2");
							String workexperience3 = workexperiencepd.getString("workexperience3");
							String workexperience4 = workexperiencepd.getString("workexperience4");
							String workexperience5 = workexperiencepd.getString("workexperience5");
							String workexperience6 = workexperiencepd.get("workexperience6").toString();
							String workexperience7 = workexperiencepd.getString("workexperience7");
							String workexperience8 = workexperiencepd.getString("workexperience8");
							String workexperience9 = workexperiencepd.getString("workexperience9");
							workexperienceStr = workexperience0+"-"+workexperience1+" ｜ "+workexperience2+" ｜ "+workexperience4+"\n" +"\r"+
									"薪资水平: "+workexperience6+"/月\n"+"\r"+
									"工作职责: "+workexperience5+"\n" +"\r"+
									"所在部门: "+workexperience3+""+"\r";
						}
					}
					params.put("${workexperience}", workexperienceStr);
					//项目经验
					String projectStr = new String();
					params.put("${project}", projectStr);

					//教育经历
					String educationStr = new String();
					if(education != null && education.size() > 0){
						for(PageData educationpd : education){
							String education1 = educationpd.getString("education1");
							String education2 = educationpd.getString("education2");
							String education3 = educationpd.getString("education3");
							String education4 = educationpd.getString("education4");
							String education5 = educationpd.getString("education5");
							String education6 = educationpd.get("education6").toString();
							education5 = systemMapper.getNameByCode(education5);
							educationStr = education1+"-"+education1+"\n" +
									education3+"\n" +
									education4+"\n" +
									education5+"\n" +
									education6+""+"\r";
						}
					}
					params.put("${education}", educationStr);

					//证书
					String certificateStr = new String();
					if(certificate != null && certificate.size() > 0){
						for(PageData certificatepd : certificate){
							String certificate0 = certificatepd.getString("certificate0");
							String certificate1 = certificatepd.getString("certificate1");
							String certificate2 = certificatepd.getString("certificate2");
							String certificate3 = certificatepd.getString("certificate3");
							certificateStr = certificate0+" |\n" +
									certificate2+" |\n" +
									certificate1+" |\n" +
									certificate3+" |"+"\r";
						}
					}
					params.put("${certificate}", certificateStr);

					//语言能力
					String languageStr = new String();
					if(language != null && language.size() > 0){
						for(PageData languageepd : language){
							String language0 = languageepd.getString("language0");
							String language1 = languageepd.getString("language1");
							String language2 = languageepd.getString("language2");
							String language3 = languageepd.getString("language3");
							String language4 = languageepd.getString("language4");
							languageStr = "语种: "+language0+" |\n" +
									"听力: "+language1+" |\n" +
									"口语: "+language2+" |\n" +
									"写作: "+language3+" |\n" +
									"等级或证书: "+language4+" |"+"\r";
						}
					}
					params.put("${language}", languageStr);

					//专业技能
					String computerStr = new String();
					if(computer != null && computer.size() > 0){
						for(PageData computerpd : computer){
							String computer0 = computerpd.getString("computer0");
							String computer1 = computerpd.getString("computer1");
							String computer2 = computerpd.get("computer2").toString();
							String computer3 = computerpd.get("computer3").toString();
							computer0 = systemMapper.getNameByCode(computer0);
							computerStr = "类别: "+computer0+"|\n" +
									"名称: "+computer1+" |\n" +
									"使用时间(月): "+computer2+" |\n" +
									"熟练程度: "+computer3+" |"+"\r";
						}
					}
					params.put("${computer}", computerStr);

					//培训经历
					String trainStr = new String();
					if(train != null && train.size() > 0){
						for(PageData trainpd : train){
							String train0 = trainpd.getString("train0");
							String train1 = trainpd.getString("train1");
							String train2 = trainpd.getString("train2");
							String train3 = trainpd.getString("train3");
							String train4 = trainpd.getString("train4");
							String train5 = trainpd.getString("train5");
							trainStr = train0+"-"+train1+" ｜ "+train2+"\n" +"\r"+
									"培训机构: "+train4+"\n" +"\r"+
									"培训内容: "+train3+"\n" +"\r"+
									"培训效果: "+train5+""+"\r";
						}
					}
					params.put("${train}", trainStr);

					//在校工作情况
					String rapStr = new String();
					String  a = rap.getString("a");
					String  b = rap.getString("b");
					String 	c = rap.getString("c");
					rapStr = "奖惩情况:"+a+"\n" +"\r"+
							"工作职责:"+b+"\n" +"\r"+
							"社会工作情况:"+b+"";
					params.put("${rap}", rapStr);

					try{
						//照片处理
						Map<String,Object> header = new HashMap<String, Object>();
						header.put("width", 100);
						header.put("height", 150);
						header.put("type", "jpg");
						header.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("D:/a.jpg"), true));
						params.put("${photo}",header);
						List<String[]> testList = new ArrayList<String[]>();

						// testList.add(new String[]{"1","1AA","1BB","1CC"});
						String path=realPath + File.separator+ "demo"+ File.separator+"demo1.docx";  //模板文件位置

						wordUtil.getWord(path,params,testList,fileName,response,fopts);

					}catch(Exception e){
						e.printStackTrace();
					}


					resData.put("url",url);
					responseBodyBean.setResult(resData);
					//残留文件删除
					FileUtil.delFileByTime(fileUploadProperteis.getUploadFolder()+ File.separator+ "load",(long)1000*60*60*24*5);
					if(responseBodyBean.getReason() == null){
						status = HttpStatus.OK.value();
						response.setStatus(status);
					}
				}else{
					reasonBean.setCode("401");
					reasonBean.setText("无数据");
					status = HttpStatus.PRECONDITION_REQUIRED.value();
					response.setStatus(status);
					responseBodyBean.setReason(reasonBean);
				}
			}else{
				reasonBean.setCode("400");
				reasonBean.setText("请求的参数不正确");
				status = HttpStatus.PRECONDITION_REQUIRED.value();
				response.setStatus(status);
				responseBodyBean.setReason(reasonBean);
			}
		}catch(Exception e){
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}finally {
			return responseBodyBean;
		}
	}


}

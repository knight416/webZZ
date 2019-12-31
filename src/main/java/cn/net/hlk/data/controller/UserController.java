 

package cn.net.hlk.data.controller;



import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.annotation.UserLoginToken;
import cn.net.hlk.data.config.FileUploadProperteis;
import cn.net.hlk.data.mapper.SystemMapper;
import cn.net.hlk.data.mapper.UserMapper;
import cn.net.hlk.data.poi.byPoi.ExportWord;
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
import cn.net.hlk.util.PoiExcelDownLoad;
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
				String name = user.getString("name");
				String photo = user.getString("photo");
				String id_card = user.getString("id_card");
				String contact = user.getString("contact");
				String job_type = user.getString("job_type");
				String system_id = user.getString("system_id");
				PageData user_message = JSON.parseObject(JSON.toJSONString(user.get("user_message")),PageData.class);
				PageData userinfo = JSON.parseObject(JSON.toJSONString(user.get("userinfo")),PageData.class);//个人信息
				List<PageData> rap = JSON.parseArray(JSON.toJSONString(user_message.get("rap")),PageData.class);//在校工作情况
				List<PageData> train = JSON.parseArray(JSON.toJSONString(user_message.get("train")),PageData.class);//培训
				List<PageData> computer = JSON.parseArray(JSON.toJSONString(user_message.get("computer")),PageData.class);//计算机能力
				List<PageData> language = JSON.parseArray(JSON.toJSONString(user_message.get("language")),PageData.class);//外语
				List<PageData> education = JSON.parseArray(JSON.toJSONString(user_message.get("education")),PageData.class);//教育
				List<PageData> certificate = JSON.parseArray(JSON.toJSONString(user_message.get("certificate")),PageData.class);//证书
				List<PageData> workexperience = JSON.parseArray(JSON.toJSONString(user_message.get("workexperience")),PageData.class);//工作
				List<PageData> jobIntention = JSON.parseArray(JSON.toJSONString(user_message.get("jobIntention")),PageData.class);//求职意向

				String dicName = systemMapper.getNameByCode("");

				String fileName= new String(name+"简历.docx");    //生成word文件的文件名
				//虚拟路径存储
				String realPath = fileUploadProperteis.getUploadFolder();
				String filePath = realPath + File.separator+ "load"+ File.separator+fileName;
				FileUtil.createDir(filePath);

				//文件地址
				FileOutputStream fopts = new FileOutputStream(filePath);
				// OutputStream out = new FileOutputStream(filePath);
				String url = "/upload"+ File.separator+ "load"+File.separator+fileName;

				//模板导出
				// WordUtils wordUtil=new WordUtils();
				// Map<String, Object> params = new HashMap<String, Object>();
				//
				// params.put("${name}", name);
				// params.put("${phone}", contact);
				// params.put("${sex}", name);
				// params.put("${workYear}", name);
				// params.put("${birthday}", name);
				// params.put("${school}", name);
				// params.put("${xueli}", name);
				// params.put("${address}", name);
				// params.put("${email}", user_message.getString("email"));
				//
				// //求职意向
				// String jobIntentionStr = new String();
				// params.put("${jobIntention}", jobIntentionStr);
				// //工作经历
				// String workexperienceStr = new String();
				// params.put("${workexperience}", workexperienceStr);
				// //项目经验
				// String projectStr = new String();
				// params.put("${project}", projectStr);
				// //教育经历
				// String educationStr = new String();
				// params.put("${education}", educationStr);
				// //证书
				// String certificateStr = new String();
				// params.put("${certificate}", certificateStr);
				// //语言能力
				// String languageStr = new String();
				// params.put("${language}", languageStr);
				// //专业技能
				// String computerStr = new String();
				// params.put("${computer}", computerStr);
				// //培训经历
				// String trainStr = new String();
				// params.put("${train}", trainStr);
				//
				// try{
				// 	//照片处理
				// 	Map<String,Object> header = new HashMap<String, Object>();
				// 	header.put("width", 100);
				// 	header.put("height", 150);
				// 	header.put("type", "jpg");
				// 	header.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("D:/a.jpg"), true));
				// 	params.put("${photo}",header);
				// 	List<String[]> testList = new ArrayList<String[]>();
				//
				// 	// testList.add(new String[]{"1","1AA","1BB","1CC"});
				// 	String path="/zldemo.docx";  //模板文件位置
				//
				// 	wordUtil.getWord(path,params,testList,fileName,response,fopts);
				//
				// }catch(Exception e){
				// 	e.printStackTrace();
				// }

				/*XWPFDocument代表一个docx文档，其可以用来读docx文档，也可以用来写docx文档
					XWPFParagraph代表文档、表格、标题等种的段落，由多个XWPFRun组成
					XWPFRun代表具有同样风格的一段文本
					XWPFTable代表一个表格
					XWPFTableRow代表表格的一行
					XWPFTableCell代表表格的一个单元格
					XWPFChar 表示.docx文件中的图表
					XWPFHyperlink 表示超链接
					XWPFPicture 代表图片*/

				//创建表格
				ExportWord ew = new ExportWord();
				XWPFDocument document = ew.createXWPFDocument();
				List<List<Object>> list = new ArrayList<List<Object>>();

				List<Object> tempList = new ArrayList<Object>();
				tempList.add("姓名");
				tempList.add("黄xx");
				tempList.add("性别");
				tempList.add("男");
				tempList.add("出生日期");
				tempList.add("2018-10-10");
				list.add(tempList);
				tempList = new ArrayList<Object>();
				tempList.add("身份证号");
				tempList.add("36073xxxxxxxxxxx");
				list.add(tempList);
				tempList = new ArrayList<Object>();
				tempList.add("出生地");
				tempList.add("江西");
				tempList.add("名族");
				tempList.add("汉");
				tempList.add("婚否");
				tempList.add("否");
				list.add(tempList);
				tempList = new ArrayList<Object>();
				tempList.add("既往病史");
				tempList.add("无");
				list.add(tempList);

				Map<String, Object> dataList = new HashMap<String, Object>();
				dataList.put("TITLE", "个人体检表");
				dataList.put("TABLEDATA", list);
				ew.exportCheckWord(dataList, document, "E:/expWordTest.docx");

				resData.put("url",url);
				responseBodyBean.setResult(resData);
				//残留文件删除
				FileUtil.delFileByTime(fileUploadProperteis.getUploadFolder()+ File.separator+ "QueryStatistics",(long)1000*60*60*24*5);
				if(responseBodyBean.getReason() == null){
					status = HttpStatus.OK.value();
					response.setStatus(status);
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

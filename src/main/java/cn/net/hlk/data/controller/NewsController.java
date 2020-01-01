package cn.net.hlk.data.controller;

import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.annotation.UserLoginToken;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.IUserService;
import cn.net.hlk.data.service.NewsService;
import cn.net.hlk.util.JwtUtil;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title
 * @Description 消息controller
 * @author 张泽恒
 * @date 2019/12/20 9:44
 * @param
 * @return
 */
@Api(description = "消息", value = "/")
@RestController
@RequestMapping(value="/news")
@EnableAutoConfiguration
public class NewsController extends BaseController{

	@Autowired
	private NewsService newsService;
	@Autowired
	private IUserService userService;

	/**
	 * @Title: addAlarm
	 * @discription 消息新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:05     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "消息新增", notes = "消息新增")
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
	@SysLog("消息新增")
	@UserLoginToken
	@RequestMapping(value="/addNews", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean addNews( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
					){
				Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
				String optName = (String) parseJwt.getBody().get("name");
				String uid = (String) parseJwt.getBody().get("id");
				pd.put("updateuser", optName);
				PageData pdc = new PageData();
				pdc.put("uid",uid);
				userService.editUser(pdc);
				if(StringUtil2.isNotEmpty(pd.get("writerid"))){
					pd.put("writerid", uid);
					PageData user = userService.findById(pdc);
					pd.put("writer", user.get("name"));
				}
				responseBodyBean = newsService.addNews(pd);
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
	
	/**
	 * @Title: updateAlarm
	 * @discription 消息修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:38:40     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "消息修改", notes = "消息修改")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc")
		 })
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
	@SysLog("消息修改")
	@UserLoginToken
	@RequestMapping(value="/updateNews", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean updateNews( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null && StringUtil2.isNotEmpty(pd.get("xid"))//消息id
					){
				Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
				String optName = (String) parseJwt.getBody().get("name");
				String uid = (String) parseJwt.getBody().get("id");
				pd.put("updateuser", optName);
				PageData pdc = new PageData();
				pdc.put("uid",uid);
				userService.editUser(pdc);
				responseBodyBean = newsService.updateNews(pd);
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

	/**
	 * @Title delNews
	 * @Description 消息删除
	 * @author 张泽恒
	 * @date 2019/12/25 18:01
	 * @param [pd, Authorization]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "消息删除", notes = "消息删除")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc")
	})
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
	@SysLog("消息删除")
	@UserLoginToken
	@RequestMapping(value="/delNews", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean delNews( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null && StringUtil2.isNotEmpty(pd.get("xidList"))//消息id
					){
				Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
				String optName = (String) parseJwt.getBody().get("name");
				String uid = (String) parseJwt.getBody().get("id");
				pd.put("updateuser", optName);
				responseBodyBean = newsService.delNews(pd);
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

	/**
	 * @Title: searchAlarm
	 * @discription 消息条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:11:10     
	 * @param page
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "消息条件查询", notes = "消息条件查询")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "page", dataType = "Page", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc")
		 })
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
	@RequestMapping(value="/searchNews", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean searchNews( @RequestBody Page page,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			List<PageData> pdList = new ArrayList<PageData>();
			PageData pd = page.getPd();
			if(page != null ){
				if(pd != null
//						&& StringUtil2.isNotEmpty(pd.get("menu"))//用户资源
						){
					// Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
					// String optName = (String) parseJwt.getBody().get("name");
					// String uid = (String) parseJwt.getBody().get("uid");
					// pd.put("updateuser", optName);
					responseBodyBean = newsService.searchNews(page);
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

	/**
	 * @Title: getAlarmInfomationById
	 * @discription 根据消息id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:54:49     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "根据消息ID获取信息", notes = "根据消息ID获取告警信息")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc")
		 })
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
	@RequestMapping(value="/getNewsById", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean getNewsById( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				// Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
				// String optName = (String) parseJwt.getBody().get("name");
				// String uid = (String) parseJwt.getBody().get("uid");
				// pd.put("updateuser", optName);
				responseBodyBean = newsService.getNewsById(pd);
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

	/**
	 * @Title ApprovalFileUpload
	 * @Description 附件上传
	 * @author 张泽恒
	 * @date 2019/12/24 18:19
	 * @param [file, Authorization, police_idcard]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@SuppressWarnings("finally")
	@ApiOperation(value = "文件上传", notes = "文件上传")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@SysLog("文件上传")
	@UserLoginToken
	@RequestMapping(value = "/ApprovalFileUpload", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean ApprovalFileUpload(@RequestParam(value ="file") MultipartFile file,
															 @RequestHeader String Authorization) {
		ResponseBodyBean  responseBodyBean = new ResponseBodyBean();
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ReasonBean reasonBean = null;
		try {
			Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
			String optName = (String) parseJwt.getBody().get("name");
			String uid = (String) parseJwt.getBody().get("id");
			PageData pd = newsService.saveApprovalFile(uid,file,optName);
			responseBodyBean.setResult(pd);
			status = HttpStatus.OK.value();
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}finally {
			response.setStatus(status);
			return responseBodyBean;
		}
	}


	/**
	 * @Title delFile
	 * @Description 文件删除
	 * @author 张泽恒
	 * @date 2019/12/24 18:45
	 * @param [pd, Authorization]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "文件删除", notes = "文件删除")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmFmNjMwMy03YjQyLTRmMDAtODA2OC02YjJiNGFlZTUyMTkiLCJpYXQiOjE1NzY4NDY0MzUsInN1YiI6IjAiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50IjoiMCIsImlkIjoiMCIsIm5hbWUiOiJhZG1pbiIsImV4cCI6MTU3ODkyMDAzNX0.J_QEqbomvsROW48ZixYFNeXpUhQIpR9ntLzJJbc7Fnc")
	})
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
	@SysLog("文件删除")
	@UserLoginToken
	@RequestMapping(value="/delFile", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean delFile( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null
					){
				Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
				String optName = (String) parseJwt.getBody().get("name");
				String uid = (String) parseJwt.getBody().get("id");
				pd.put("updateuser", optName);
				pd.put("uid", uid);
				responseBodyBean = newsService.delFile(pd);
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

package cn.net.hlk.data.controller;

import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.mapper.LoginMapper;
import cn.net.hlk.data.pojo.user.User;
import cn.net.hlk.data.service.ILoginService;
import cn.net.hlk.util.JwtUtil;
import com.alibaba.druid.util.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import cn.com.jit.assp.ias.principal.UserPrincipal;
//import cn.com.jit.assp.ias.sp.saml11.SPUtil;
//import cn.com.jit.assp.ias.sp.saml11.config.XMLConfiguration;
//import cn.net.hylink.data.mapper.LogMapper;

@Api(description = "登陆模块", value = "/")
@RestController
@RequestMapping(value = "/login")
@EnableAutoConfiguration
public class LoginController extends BaseController {
	
	@Autowired
	private ILoginService loginServiceImple;
	@Autowired
	private LoginMapper loginMapper;
	
	@SuppressWarnings("all")
	@ApiOperation(value = "登陆", notes = "登陆")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	@SysLog("登录-用户")
	public @ResponseBody ResponseBodyBean login(@RequestBody PageData pd) {
		loginServiceImple.getIPpath(request);
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean login = loginServiceImple.login(pd);
		
		status = HttpStatus.OK.value();
		
		response.setStatus(status);
		return login;
	}
	
	@SuppressWarnings("all")
	@ApiOperation(value = "token登陆", notes = "token登陆")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "String", required = true, value = "客户端传入token", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/loginToken", method = RequestMethod.POST)
	public @ResponseBody PageData loginToken(@RequestBody PageData pd) {
		loginServiceImple.getIPpath(request);
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		PageData resultBody = new PageData();
		String token = pd.getString("token");
		if(token==null || StringUtils.isEmpty(token)){
			resultBody.put("error", new PageData("code","412","text","参数不正确！"));
			resultBody.put("data",null);
			status = HttpStatus.OK.value();
			return resultBody;
		}
		Jws<Claims> parseJwt = JwtUtil.parseJwt(token);
		String idCard = (String) parseJwt.getBody().get("idCard");
		//412 请求头中指定的一些前提条件失败
		if(idCard==null || StringUtils.isEmpty(idCard)){
			resultBody.put("error", new PageData("code","412","text","参数不正确！"));
			resultBody.put("data",null);
			status = HttpStatus.OK.value();
			return resultBody;
		}
		User user = loginMapper.getUserByIdCard(idCard);
		if(user==null){
			resultBody.put("error", new PageData("code","412","text","用户不存在！"));
			resultBody.put("data",null);
			status = HttpStatus.OK.value();
			return resultBody;
		}
		resultBody = loginServiceImple.loginToken(user);
		status = HttpStatus.OK.value();
		response.setStatus(status);
		return resultBody;
	}
	
	
	
	@SuppressWarnings("all")
	@ApiOperation(value = "注销", notes = "注销")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzNTM3Y2RmYS0zMjc0LTQ4MjUtYTc1Mi02MGM3ZmEyM2QwYTMiLCJpYXQiOjE1MTc2MzA0MzksInN1YiI6IjMiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJkZXBhcnRtZW50Ijp7ImRlcHRoIjoxLCJuYW1lIjoi6buR6b6Z5rGf55yBIiwiY29kZSI6IjIzMDAwMDAwMDAwMCJ9LCJnb3Zlcm5tZW50IjpbXSwiaWQiOjMsImlkQ2FyZCI6IjIzMDEwMjE5OTIwMTExMDAwMSIsInBjYXJkIjoiMjMwMDAxIiwibmFtZSI6Inh1IiwiaXNBZG1pbiI6MCwiZXhwIjoxNTE5NzA0MDM5fQ.Kas4PlaCjnwIMCbl1Dkkfe5zAvQo9plwjI1otfl0Y2A"),
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = false, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@SysLog("退出系统-用户")
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean logout(@RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		request.getSession().invalidate();
		status = HttpStatus.OK.value();
		response.setStatus(status);
		responseBodyBean.setResult("success");
		return responseBodyBean;
	}
	
	@SuppressWarnings("all")
	@ApiOperation(value = "改密", notes = "改密")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = false, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@SysLog("修改密码-用户")
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean updatePassword(@RequestHeader String Authorization,@RequestBody PageData pd) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
		String id = (String) parseJwt.getBody().get("id");
		String name = (String) parseJwt.getBody().get("name");
		pd.put("opt_name", name);
		pd.put("opt_id", id);
		logger.info("更改密码："+pd);
		ResponseBodyBean updatePassword = loginServiceImple.updatePassword(pd);
		status = HttpStatus.OK.value();		
		response.setStatus(status);
		return updatePassword;
	}
	

	@ApiOperation(value = "获取IP", notes = "获取IP")
	@RequestMapping(value = "/getIP", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean getIP() {
		ResponseBodyBean iPpath = loginServiceImple.getIPpath(request);
		logger.info("iPpath:"+iPpath);
		response.setStatus(HttpStatus.OK.value());
		return iPpath;
	}

}

package cn.net.hlk.data.controller;


import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.dictionary.DictionaryBean;
import cn.net.hlk.data.service.ISystemService;
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

import java.util.List;

@Api(description = "系统管理", value = "/")
@RestController
@RequestMapping(value = "/system")
@EnableAutoConfiguration
public class SystemController extends BaseController {
	
	@Autowired
	private ISystemService systemServiceImple;

	@SuppressWarnings("all")
	@ApiOperation(value = "字典类树", notes = "字典类树")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "page", dataType = "Page", required = false, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/queryTreeList", method = RequestMethod.POST)
	public @ResponseBody
	ResponseBodyBean query(@RequestHeader String Authorization, @RequestBody Page page) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		PageData pdPageData = new PageData();
		List<DictionaryBean> queryTreeList = systemServiceImple.queryTreeList();
		status = HttpStatus.OK.value();
		responseBodyBean.setResult(queryTreeList);
		response.setStatus(status);
		return responseBodyBean;
	}
	
	@SuppressWarnings("all")
	@ApiOperation(value = "字典类树", notes = "字典类树")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/queryTreeListByName", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean queryTreeListByName(@RequestHeader String Authorization,@RequestBody PageData pd) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		PageData pdPageData = new PageData();
		List<DictionaryBean> queryTreeList = systemServiceImple.queryTreeListByName(pd);
		status = HttpStatus.OK.value();
		responseBodyBean.setResult(queryTreeList);
		response.setStatus(status);
		return responseBodyBean;
	}
	
	
	@SuppressWarnings("all")
	@ApiOperation(value = "添加字典", notes = "添加字典")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@SysLog("添加-字典")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean addDictionaryClass(@RequestHeader String Authorization,@RequestBody PageData pd) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
		String id = (String) parseJwt.getBody().get("id");
		String name = (String) parseJwt.getBody().get("name");
		pd.put("opt_name", name);
		pd.put("opt_id", id);
		Integer addDictionaryClass = systemServiceImple.addDictionaryClass(pd);
		if(addDictionaryClass == 1){
			responseBodyBean.setResult("success");
			status = HttpStatus.OK.value();
		}
		response.setStatus(status);
		return responseBodyBean;
	}
	
	
	@SuppressWarnings("all")
	@ApiOperation(value = "编辑字典类", notes = "编辑字典类")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@SysLog("编辑-字典")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean updateDictionaryClass(@RequestHeader String Authorization,@RequestBody PageData pd) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
		String id = (String) parseJwt.getBody().get("id");
		String name = (String) parseJwt.getBody().get("name");
		pd.put("opt_name", name);
		pd.put("opt_id", id);
		Integer updateDictionaryClass = systemServiceImple.updateDictionaryClass(pd);
		if(updateDictionaryClass == 1){
			responseBodyBean.setResult("success");
			status = HttpStatus.OK.value();
		}
		response.setStatus(status);
		return responseBodyBean;
	}
	
	@SuppressWarnings("all")
	@ApiOperation(value = "查看字典项", notes = "查看字典项")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "page", dataType = "Page", required = true, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/queryById", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean queryTermlistPage(@RequestHeader String Authorization,@RequestBody Page page) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		PageData pdPageData = new PageData();
		List<PageData> queryTermlistPage = systemServiceImple.queryTermlistPage(page);
		pdPageData.put("list", queryTermlistPage);
		pdPageData.put("page", page);
		status = HttpStatus.OK.value();
		responseBodyBean.setResult(pdPageData);
		response.setStatus(status);
		return responseBodyBean;
	}
	
	
	@SuppressWarnings("all")
	@ApiOperation(value = "通过id查询字典", notes = "通过id查询字典")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/queryDictionaryById", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean queryDictionaryById(@RequestHeader String Authorization,@RequestBody PageData pd) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		PageData queryDictionaryById = systemServiceImple.queryDictionaryById(pd);
		responseBodyBean.setResult(queryDictionaryById);
		status = HttpStatus.OK.value();
		response.setStatus(status);
		return responseBodyBean;
	}
	
	@SuppressWarnings("all")
	@ApiOperation(value = "删除字典项", notes = "删除字典项")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@SysLog("删除-字典")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean deleteDictionaryTerm(@RequestHeader String Authorization,@RequestBody PageData pd) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
		String id = (String) parseJwt.getBody().get("id");
		String name = (String) parseJwt.getBody().get("name");
		pd.put("opt_name", name);
		pd.put("opt_id", id);
		ResponseBodyBean deleteDictionaryTerm = systemServiceImple.deleteDictionaryTerm(pd);
		status = HttpStatus.OK.value();
		response.setStatus(status);
		return deleteDictionaryTerm;
	}
	
	@ApiOperation(value = "通过类型查看字典项", notes = "通过类型查看字典项")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ZGI3MWM5Yy05NDg0LTQ5M2MtOTcyMy0xMjlhMDBlOGRmNzEiLCJpYXQiOjE1MTUyMjAzNDgsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIwMDAwMDAwMDAwMDAiLCJpZCI6MSwiaWRjYXJkIjoiMjMwMTAzMTk5MTAzMDQyMjEwIiwibmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTUxNzI5Mzk0OH0.mQr2LJlU3rwWfnfGifKf3ePpR0l-l_wGI4jCLhseQVU"),
			@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") })
	@ApiResponses({ @ApiResponse(code = 200, message = "指示客服端的请求已经成功收到，解析，接受"),
			@ApiResponse(code = 201, message = "资源已被创建"), @ApiResponse(code = 401, message = "未授权"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 403, message = "拒绝访问"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对"), @ApiResponse(code = 406, message = "不是指定的数据类型"),
			@ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/queryByCode", method = RequestMethod.POST)
	public @ResponseBody ResponseBodyBean queryByCode(@RequestHeader String Authorization,@RequestBody PageData pd) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		if(StringUtils.isEmpty(pd.getString("code"))){
			responseBodyBean.setReason(new ReasonBean("400", "参数缺失！"));
			return responseBodyBean;
		}
		
		List<PageData> queryByCode = systemServiceImple.queryByCode(pd);
		responseBodyBean.setResult(queryByCode);
		status = HttpStatus.OK.value();
		response.setStatus(status);
		return responseBodyBean;
	}
	
}

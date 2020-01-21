package cn.net.hlk.data.controller;

import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.annotation.UserLoginToken;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.NewsOperationService;
import cn.net.hlk.data.service.PostService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title
 * @Description 岗位
 * @author 张泽恒
 * @date 2019/12/20 9:44
 * @param
 * @return
 */
@Api(description = "岗位", value = "/")
@RestController
@RequestMapping(value="/post")
@EnableAutoConfiguration
public class PostController extends BaseController{

	@Autowired
	private PostService postService;

	/**
	 * @Title: addPost
	 * @discription 岗位新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:05     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "岗位新增", notes = "岗位新增")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlMzEzY2MxZi1iMWMyLTQ3YzEtYjNiYi0wNzA4OGZiNmEwNDIiLCJpYXQiOjE1Nzg5MjQ0MTQsInN1YiI6IjFmMzI5YWYxNjVmZDRhNDViMDY0MzA2NzEwZjhhOTNhIiwiaXNzIjoiU2VjdXJpdHkgQ2VudGVyIiwiaWQiOiIxZjMyOWFmMTY1ZmQ0YTQ1YjA2NDMwNjcxMGY4YTkzYSIsIm5hbWUiOiJxIiwiZXhwIjoxNTgwOTk4MDE0fQ.QvhblxEtnx_VTaZp4D5k7zW0wJ26k793vPE01x8OBKk") })
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
	@SysLog("岗位新增")
	@UserLoginToken
	@RequestMapping(value="/addPost", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean addPost( @RequestBody PageData pd,  @RequestHeader String Authorization) {
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
				pd.put("writer", optName);
				pd.put("uid", uid);
				responseBodyBean = postService.addPost(pd);
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
	 * @Title: updatePost
	 * @discription 岗位修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:38:40     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "岗位修改", notes = "岗位修改")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlMzEzY2MxZi1iMWMyLTQ3YzEtYjNiYi0wNzA4OGZiNmEwNDIiLCJpYXQiOjE1Nzg5MjQ0MTQsInN1YiI6IjFmMzI5YWYxNjVmZDRhNDViMDY0MzA2NzEwZjhhOTNhIiwiaXNzIjoiU2VjdXJpdHkgQ2VudGVyIiwiaWQiOiIxZjMyOWFmMTY1ZmQ0YTQ1YjA2NDMwNjcxMGY4YTkzYSIsIm5hbWUiOiJxIiwiZXhwIjoxNTgwOTk4MDE0fQ.QvhblxEtnx_VTaZp4D5k7zW0wJ26k793vPE01x8OBKk")
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
	@SysLog("岗位修改")
	@UserLoginToken
	@RequestMapping(value="/updatePost", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean updatePost( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null && StringUtil2.isNotEmpty(pd.get("post_id"))//消息id
					){
				Jws<Claims> parseJwt = JwtUtil.parseJwt(Authorization);
				String optName = (String) parseJwt.getBody().get("name");
				String uid = (String) parseJwt.getBody().get("uid");
				pd.put("updateuser", optName);
				responseBodyBean = postService.updatePost(pd);
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
	 * @Title: searchPost
	 * @discription 岗位查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:11:10     
	 * @param page
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "岗位查询", notes = "岗位查询")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "page", dataType = "Page", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlMzEzY2MxZi1iMWMyLTQ3YzEtYjNiYi0wNzA4OGZiNmEwNDIiLCJpYXQiOjE1Nzg5MjQ0MTQsInN1YiI6IjFmMzI5YWYxNjVmZDRhNDViMDY0MzA2NzEwZjhhOTNhIiwiaXNzIjoiU2VjdXJpdHkgQ2VudGVyIiwiaWQiOiIxZjMyOWFmMTY1ZmQ0YTQ1YjA2NDMwNjcxMGY4YTkzYSIsIm5hbWUiOiJxIiwiZXhwIjoxNTgwOTk4MDE0fQ.QvhblxEtnx_VTaZp4D5k7zW0wJ26k793vPE01x8OBKk")
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
	@RequestMapping(value="/searchPost", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean searchPost( @RequestBody Page page,  @RequestHeader String Authorization) {
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
					responseBodyBean = postService.searchPost(page);
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
	 * @Title: getPostById
	 * @discription 根据岗位id 获取岗位信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:54:49     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "根据岗位ID获取信息", notes = "根据岗位ID获取岗位信息")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
			@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlMzEzY2MxZi1iMWMyLTQ3YzEtYjNiYi0wNzA4OGZiNmEwNDIiLCJpYXQiOjE1Nzg5MjQ0MTQsInN1YiI6IjFmMzI5YWYxNjVmZDRhNDViMDY0MzA2NzEwZjhhOTNhIiwiaXNzIjoiU2VjdXJpdHkgQ2VudGVyIiwiaWQiOiIxZjMyOWFmMTY1ZmQ0YTQ1YjA2NDMwNjcxMGY4YTkzYSIsIm5hbWUiOiJxIiwiZXhwIjoxNTgwOTk4MDE0fQ.QvhblxEtnx_VTaZp4D5k7zW0wJ26k793vPE01x8OBKk")
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
	@RequestMapping(value="/getPostById", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean getPostById( @RequestBody PageData pd,  @RequestHeader String Authorization) {
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
				responseBodyBean = postService.getPostById(pd);
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

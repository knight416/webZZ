package cn.net.hlk.data.controller;

import java.util.ArrayList;
import java.util.List;

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

import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.BacklogService;
import cn.net.hlk.data.service.InformationService;
import cn.net.hlk.data.service.RemindService;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;

/**
 * @package: cn.net.hlk.data.controller   
 * @Title: InformationController   
 * @Description:消息controller
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:23:08
 */
@Api(description = "消息", value = "/")
@RestController
@RequestMapping(value="/information")
@EnableAutoConfiguration
public class InformationController extends BaseController{

	@Autowired
	private AlarmService alarmService;
	@Autowired
	private BacklogService backlogService;
	@Autowired
	private RemindService remindService;
	@Autowired
	private InformationService informationService;
	
	/**
	 * @Title: addAlarm
	 * @discription 告警新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:05     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "告警新增", notes = "告警新增")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/addAlarm", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean addAlarm( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = alarmService.addAlarm(pd);
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
	 * @discription 告警修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:38:40     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "告警修改", notes = "告警修改")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/updateAlarm", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean updateAlarm( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null && StringUtil2.isNotEmpty(pd.get("alarm_id"))//告警id
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = alarmService.updateAlarm(pd);
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
	 * @Title: addBacklog
	 * @discription 待办新增
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:37:53     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "待办新增", notes = "待办新增")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/addBacklog", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean addBacklog( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = backlogService.addBacklog(pd);
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
	 * @Title: updateBacklog
	 * @discription 待办修改
	 * @author 张泽恒       
	 * @created 2018年10月8日 下午1:49:04     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "待办修改", notes = "待办修改")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/updateBacklog", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean updateBacklog( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null && StringUtil2.isNotEmpty(pd.get("backlog_id"))//待办id
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = backlogService.updateBacklog(pd);
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
	 * @Title: addRemind
	 * @discription 提醒新增
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:05:30     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "提醒新增", notes = "提醒新增")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/addRemind", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean addRemind( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = remindService.addRemind(pd);
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
	 * @Title: updateRemind
	 * @discription 提醒修改
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:06:07     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "提醒修改", notes = "提醒修改")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/updateRemind", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean updateRemind( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null && StringUtil2.isNotEmpty(pd.get("backlog_id"))//待办id
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = remindService.updateRemind(pd);
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
	 * @discription 告警条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:11:10     
	 * @param page
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "告警条件查询", notes = "告警条件查询")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "page", dataType = "Page", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/searchAlarm", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean searchAlarm( @RequestBody Page page,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			List<PageData> pdList = new ArrayList<PageData>();
			PageData pd = page.getPd();
			if(page != null ){
				if(pd != null && StringUtil2.isNotEmpty(pd.get("police_idcard"))
//						&& StringUtil2.isNotEmpty(pd.get("menu"))//用户资源
						){
					responseBodyBean = alarmService.searchAlarm(page);
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
	 * @Title: searchBacklog
	 * @discription 待办条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:12:21     
	 * @param page
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "待办条件查询", notes = "待办条件查询")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "page", dataType = "Page", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/searchBacklog", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean searchBacklog( @RequestBody Page page,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			List<PageData> pdList = new ArrayList<PageData>();
			PageData pd = page.getPd();
			if(page != null ){
				if(pd != null && StringUtil2.isNotEmpty(pd.get("police_idcard"))
//						&& StringUtil2.isNotEmpty(pd.get("menu"))//用户资源
						){
					responseBodyBean = backlogService.searchBacklog(page);
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
	 * @Title: searchRemind
	 * @discription 提醒条件查询
	 * @author 张泽恒       
	 * @created 2018年10月10日 下午3:13:10     
	 * @param page
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "提醒条件查询", notes = "提醒条件查询")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "page", dataType = "Page", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/searchRemind", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean searchRemind( @RequestBody Page page,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			List<PageData> pdList = new ArrayList<PageData>();
			PageData pd = page.getPd();
			if(page != null ){
				if(pd != null && StringUtil2.isNotEmpty(pd.get("police_idcard"))
//						&& StringUtil2.isNotEmpty(pd.get("menu"))//用户资源
						){
					responseBodyBean = remindService.searchRemind(page);
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
	 * @Title: alarmAndbacklogCreate
	 * @discription 待办、通知生成
	 * @author 张泽恒       
	 * @created 2018年10月13日 上午10:44:11     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "待办、通知生成", notes = "待办、通知生成")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/alarmAndbacklogCreate", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean alarmAndbacklogCreate( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = informationService.alarmAndbacklogCreate(0,0,"",pd,"");
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
	 * @Title: completeAlarmAndBacklog
	 * @discription 待办告警完成
	 * @author 张泽恒       
	 * @created 2018年10月13日 下午1:29:26     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "告警待办完成", notes = "告警待办完成")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/completeAlarmAndBacklog", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean completeAlarmAndBacklog( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = informationService.completeAlarmAndBacklog(pd);
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
	 * @Title: withdrawAlarmAndBacklog
	 * @discription 告警 待办 撤回
	 * @author 张泽恒       
	 * @created 2018年10月15日 下午4:23:29     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "告警待办撤回", notes = "告警待办撤回")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/withdrawAlarmAndBacklog", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean withdrawAlarmAndBacklog( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = informationService.withdrawAlarmAndBacklog(pd);
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
	 * @Title: xmppSend
	 * @discription xmpp消息发送
	 * @author 张泽恒       
	 * @created 2018年10月16日 上午9:58:31     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "xmpp消息发送", notes = "xmpp消息发送")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/xmppSend", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean xmppSend( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				 int n = informationService.xmppSend(pd,0);
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
	 * @Title: informationSearchGet
	 * @discription 消息查询条件接口
	 * @author 张泽恒       
	 * @created 2018年10月17日 下午7:13:37     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "消息查询条件接口", notes = "消息查询条件接口")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/informationSearchGet", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean informationSearchGet( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = informationService.informationSearchGet();
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
	 * @Title: getAlarmInfomationById
	 * @discription 根据告警id 获取告警信息
	 * @author 张泽恒       
	 * @created 2018年10月19日 上午8:54:49     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "根据告警ID获取信息", notes = "根据告警ID获取告警信息")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/getAlarmInfomationById", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean getAlarmInfomationById( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
//					&& StringUtil2.isNotEmpty(pd.get("box_id"))//箱id
					){
				responseBodyBean = alarmService.getAlarmInfomationById(pd);
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
	 * @Title: getInfomationById
	 * @discription 根据id 获取信息状态
	 * @author 张泽恒       
	 * @created 2018年11月1日 下午2:50:40     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "根据ID获取信息", notes = "根据ID获取信息")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "pd", dataType = "PageData", required = true, value = "客户端传入JSON字符串", defaultValue = "") ,
		@ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "安全中心颁发token验证信息", defaultValue = "eyJqdGkiOiIzYzdhN2UzNy0yMWUyLTRhMTYtOTUwNS0zMjVlMjI0NGMxZjAiLCJpYXQiOjE1MDM4ODc5MDcsInN1YiI6IjEiLCJpc3MiOiJTZWN1cml0eSBDZW50ZXIiLCJncm91cGNvZGUiOiIyMzAwMDAwMDAwMDAiLCJ1dHlwZSI6IjEifQ"),
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
	@RequestMapping(value="/getInfomationById", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean getInfomationById( @RequestBody PageData pd,  @RequestHeader String Authorization) { 
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null 
					&& StringUtil2.isNotEmpty(pd.get("xxid"))//信息id
					&& StringUtil2.isNotEmpty(pd.get("type"))//信息id
					){
				responseBodyBean = informationService.getInfomationById(pd);
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

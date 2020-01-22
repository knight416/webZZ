package cn.net.hlk.data.controller;

import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.annotation.UserLoginToken;
import cn.net.hlk.data.payment.config.SwiftpassConfig;
import cn.net.hlk.data.payment.util.SignUtil;
import cn.net.hlk.data.payment.util.SignUtils;
import cn.net.hlk.data.payment.util.XmlUtils;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.PostService;
import cn.net.hlk.util.JwtUtil;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * @Title
 * @Description 岗位
 * @author 张泽恒
 * @date 2019/12/20 9:44
 * @param
 * @return
 */
@Api(description = "支付", value = "/")
@RestController
@RequestMapping(value="/payment")
@EnableAutoConfiguration
public class PaymentController extends BaseController{

	@Autowired
	private PostService postService;

	private static final long serialVersionUID = 1L;
	public static Map<String,String> orderResult; //用来存储订单的交易状态(key:订单号，value:状态(0:未支付，1：已支付))  ---- 这里可以根据需要存储在数据库中
	public static int orderStatus=0;

	/**
	 * @Title: pay
	 * @discription 支付
	 * @author 张泽恒       
	 * @created 2018年10月8日 上午10:09:05     
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "支付", notes = "支付")
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
	@SysLog("支付")
	@UserLoginToken
	@RequestMapping(value="/pay", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean pay( @RequestBody PageData pd,  @RequestHeader String Authorization) {
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
				//支付开始
				// req.setCharacterEncoding("utf-8");
				// resp.setCharacterEncoding("utf-8");

				// SortedMap<String,String> map = XmlUtils.getParameterMap(req);
				SortedMap<String,String> map = JSON.parseObject(JSON.toJSONString(pd),SortedMap.class);

				map.put("mch_id", SwiftpassConfig.mch_id);
				map.put("notify_url", SwiftpassConfig.notify_url);
				map.put("nonce_str", String.valueOf(new Date().getTime()));

				Map<String,String> params = SignUtils.paraFilter(map);
				System.out.println(params);
				StringBuilder buf = new StringBuilder((params.size() +1) * 10);
				SignUtils.buildPayParams(buf,params,false);
				String preStr = buf.toString();
				String sign_type = map.get("sign_type");

				map.put("sign", SignUtil.getSign(sign_type, preStr));


				String reqUrl = SwiftpassConfig.req_url;
				System.out.println("reqUrl：" + reqUrl);

				System.out.println("reqParams:" + XmlUtils.parseXML(map));
				CloseableHttpResponse response = null;
				CloseableHttpClient client = null;
				String res = null;
				String reSign = null;
				try {
					HttpPost httpPost = new HttpPost(reqUrl);
					StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
					httpPost.setEntity(entityParams);
					client = HttpClients.createDefault();
					response = client.execute(httpPost);
					if(response != null && response.getEntity() != null){

						Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");

						reSign = resultMap.get("sign");
						sign_type = resultMap.get("sign_type");
						res = XmlUtils.toXml(resultMap);
						System.out.println("签名方式"+sign_type);
						if(resultMap.containsKey("sign")){
							if(SignUtil.verifySign(reSign, sign_type, resultMap)){
								System.out.println("返回参数验证签名通过。。。");
								if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
									if(orderResult == null){
										orderResult = new HashMap<String,String>();
									}
									orderResult.put(map.get("out_trade_no"), "0");//初始状态

									String code_img_url = resultMap.get("code_img_url");
									resData.put("code_img_url", code_img_url);
									resData.put("out_trade_no", map.get("out_trade_no"));
									resData.put("total_fee", map.get("total_fee"));
									resData.put("body", map.get("body"));
									// resData.put("index-pay-result.jsp").forward(req, resp);
								}else{
									resData.put("result", res);
								}
							}
						}
					}else{
						res = "操作失败";
					}
				} catch (Exception e) {
					e.printStackTrace();
					res = "系统异常";
				} finally {
					if(response != null){
						response.close();
					}
					if(client != null){
						client.close();
					}
				}
				//支付结束
				if(responseBodyBean.getReason() == null){
					status = HttpStatus.OK.value();
					this.response.setStatus(status);
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

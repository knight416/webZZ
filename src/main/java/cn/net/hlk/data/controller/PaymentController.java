package cn.net.hlk.data.controller;

import cn.net.hlk.data.annotation.SysLog;
import cn.net.hlk.data.annotation.UserLoginToken;
import cn.net.hlk.data.config.SwiftpassConfig;
import cn.net.hlk.data.payment.util.SignUtil;
import cn.net.hlk.data.payment.util.SignUtils;
import cn.net.hlk.data.payment.util.XmlUtils;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.PayService;
import cn.net.hlk.data.service.PostService;
import cn.net.hlk.util.GetIp;
import cn.net.hlk.util.JwtUtil;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
	private PayService payService;

	// static{
	// 	SwiftpassConfig = new SwiftpassConfig();
	// }

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

				SortedMap<String,String> map = JSON.parseObject(JSON.toJSONString(pd),SortedMap.class);

				map.put("charset","UTF-8");
				String uuid = UuidUtil.get32UUID();
				map.put("out_trade_no",uuid);
				map.put("service","unified.trade.native");
				map.put("mch_create_ip", GetIp.getIp(request));
				map.put("sign_type","RSA_1_256");
				map.put("version","2.0");
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
						System.out.println("resultMap "+resultMap);


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
						resData.put("result", res);
					}
				} catch (Exception e) {
					e.printStackTrace();
					res = "系统异常";
					resData.put("result", res);
				} finally {
					if(response != null){
						response.close();
					}
					if(client != null){
						client.close();
					}
					responseBodyBean.setResult(resData);
				}
				//支付结束
				if(responseBodyBean.getReason() == null){
					status = HttpStatus.OK.value();
					this.response.setStatus(status);
				}

				//修改其他支付
				PageData pdd = new PageData();
				pdd.put("uid",pd.get("uid"));
				pdd.put("post_id",pd.get("post_id"));
				pdd.put("visiable",0);
				payService.closeOtherPay(pdd);

				pd.put("payid",map.get("out_trade_no"));
				pd.put("out_trade_no",map.get("out_trade_no"));
				pd.put("body",map.get("body"));
				pd.put("attach",map.get("attach"));
				pd.put("total_fee",map.get("total_fee"));
				pd.put("mch_create_ip",map.get("mch_create_ip"));
				pd.put("sign_type",map.get("sign_type"));
				pd.put("payxml",XmlUtils.parseXML(map));
				pd.put("requestxml",res);
				pd.put("state",0);
				pd.put("system_id","system_id");

				payService.addPay(pd);
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

	@SuppressWarnings("all")
	@ApiOperation(value = "支付返回", notes = "支付返回")
	@ApiImplicitParams({
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
	// @SysLog("支付通知")
	@RequestMapping(value="/payresult", method=RequestMethod.POST)
	public  @ResponseBody void payresult( HttpServletRequest req, HttpServletResponse resp) {
		try{
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");

			System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			String FEATURE = null;
			try {
				// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
				// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
				FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
				dbf.setFeature(FEATURE, true);

				// If you can't completely disable DTDs, then at least do the following:
				// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
				// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
				// JDK7+ - http://xml.org/sax/features/external-general-entities
				FEATURE = "http://xml.org/sax/features/external-general-entities";
				dbf.setFeature(FEATURE, false);

				// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
				// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
				// JDK7+ - http://xml.org/sax/features/external-parameter-entities
				FEATURE = "http://xml.org/sax/features/external-parameter-entities";
				dbf.setFeature(FEATURE, false);

				// Disable external DTDs as well
				FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
				dbf.setFeature(FEATURE, false);

				// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
				dbf.setXIncludeAware(false);
				dbf.setExpandEntityReferences(false);

				// And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then
				// ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
				// (http://cwe.mitre.org/data/definitions/918.html) and denial
				// of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."

				// remaining parser logic
			} catch (ParserConfigurationException e) {
				// This should catch a failed setFeature feature
				System.out.println("ParserConfigurationException was thrown. The feature '" +
						FEATURE + "' is probably not supported by your XML processor.");
			}
            /*catch (SAXException e) {
            	// On Apache, this should be thrown when disallowing DOCTYPE
            	System.out.println("A DOCTYPE was passed into the XML document");
            }
            catch (IOException e) {
            	// XXE that points to a file that doesn't exist
            	System.out.println("IOException occurred, XXE may still possible: " + e.getMessage());
            }*/

			String resString = XmlUtils.parseRequst(request);
			System.out.println("通知内容request：" + resString);

			String resStringreq = XmlUtils.parseRequst(req);
			System.out.println("通知内容req：" + resStringreq);

			String respString = "fail";
			if(resString != null && !"".equals(resString)){
				Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");

				String res = XmlUtils.toXml(map);
				String sign_type = map.get("sign_type");
				String reSign = map.get("sign");
				System.out.println("通知内容：" + res);
				if(map.containsKey("sign")){
					if(SignUtil.verifySign(reSign, sign_type, map)){
						String status = map.get("status");
						if(status != null && "0".equals(status)){
							String result_code = map.get("result_code");
							if(result_code != null && "0".equals(result_code)){
								if(orderResult == null){
									orderResult = new HashMap<String,String>();
								}
								String out_trade_no = map.get("out_trade_no");
								orderResult.put(out_trade_no, "1");

								PageData pd = new PageData();
								pd.put("payid",out_trade_no);
								pd.put("pay_message",JSON.toJSONString(map));
								pd.put("trade_state",map.get("pay_result"));
								payService.updatePay(pd);

							}
						}
						respString = "success";
					}
				}
			}
			response.getWriter().write(respString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @Title payQuery
	 * @Description 支付查询
	 * @author 张泽恒
	 * @date 2020/1/31 22:16
	 * @param [pd, Authorization]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "支付查询", notes = "支付查询")
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
	@RequestMapping(value="/payQuery", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean payQuery( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据

		SortedMap<String,String> map = JSON.parseObject(JSON.toJSONString(pd),SortedMap.class);

		map.put("charset","UTF-8");
		map.put("service","unified.trade.query");
		map.put("sign_type","RSA_1_256");
		map.put("version","1.0");
		// map.put("transaction_id","");

		System.out.println(XmlUtils.toXml(map));
		map.put("mch_id", SwiftpassConfig.mch_id);
		String res = null;
		String reqUrl = SwiftpassConfig.req_url;
		map.put("nonce_str", String.valueOf(new Date().getTime()));
		Map<String,String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SignUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign_type = map.get("sign_type");
		map.put("sign", SignUtil.getSign(sign_type, preStr));


		System.out.println("reqUrl:" + reqUrl);

		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		try {
			HttpPost httpPost = new HttpPost(reqUrl);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if(response != null && response.getEntity() != null){
				Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
				String rstatus = resultMap.get("status");
				res = XmlUtils.toXml(resultMap);
				if("0".equals(rstatus)){
					String result_code = resultMap.get("result_code");
					if("0".equals(result_code)){
						String trade_state = resultMap.get("trade_state");
						resData.put("trade_state",trade_state);

						if("SUCCESS".equals(trade_state)){
							trade_state = "0";
						}else if("REFUND".equals(trade_state)){
							trade_state = "1";
						}else if("NOTPAY".equals(trade_state)){
							trade_state = "2";
						}else if("CLOSED".equals(trade_state)){
							trade_state = "3";
						}else if("PAYERROR".equals(trade_state)){
							trade_state = "4";
						}

						PageData pdd = new PageData();
						pdd.put("payid",pd.get("out_trade_no"));
						pdd.put("trade_state",trade_state);
						payService.updatePay(pdd);

						System.out.println("请求结果：" + res);
						String reSign = resultMap.get("sign");
						sign_type = resultMap.get("sign_type");
						System.out.println("签名方式"+sign_type);
						if(resultMap.containsKey("sign") && !SignUtil.verifySign(reSign, sign_type, resultMap)){
							res = "验证签名不通过";
						}
						resData.put("result", res);
					}else{
						resData.put("rstatus", result_code);
						resData.put("message", resultMap.get("err_msg"));
					}
				}else{
					resData.put("rstatus", rstatus);
					resData.put("message", resultMap.get("message"));
				}
			}else{
				res = "操作失败!";
				resData.put("result", res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "操作失败";
			resData.put("result", res);
		}


		responseBodyBean.setResult(resData);
		return  responseBodyBean;
	}


	/**
	 * @Title payRefund
	 * @Description 退款
	 * @author 张泽恒
	 * @date 2020/1/31 23:21
	 * @param [pd, Authorization]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "退款", notes = "退款")
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
	@SysLog("退款")
	@UserLoginToken
	@RequestMapping(value="/payRefund", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean payRefund( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据

		SortedMap<String,String> map = JSON.parseObject(JSON.toJSONString(pd),SortedMap.class);

		map.put("charset","UTF-8");
		map.put("service","unified.trade.refund");
		map.put("sign_type","RSA_1_256");
		map.put("version","1.0");
		map.put("transaction_id","");
		String out_refund_no = UuidUtil.get32UUID();
		map.put("out_refund_no",out_refund_no);
		map.put("total_fee",map.get("refund_fee"));

		System.out.println(XmlUtils.toXml(map));
		String key = SwiftpassConfig.key;
		String res = null;
		String reqUrl = SwiftpassConfig.req_url;
		map.put("mch_id", SwiftpassConfig.mch_id);
		map.put("op_user_id", SwiftpassConfig.mch_id);
		map.put("nonce_str", String.valueOf(new Date().getTime()));

		Map<String,String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SignUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign_type = map.get("sign_type");

		map.put("sign", SignUtil.getSign(sign_type, preStr));

		System.out.println("reqUrl:" + reqUrl);

		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		try {
			HttpPost httpPost = new HttpPost(reqUrl);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if(response != null && response.getEntity() != null){
				Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
				String rstatus = resultMap.get("status");
				if("0".equals(rstatus)){
					String result_code = resultMap.get("result_code");
					if("0".equals(result_code)){
						res = XmlUtils.toXml(resultMap);
						System.out.println("请求结果：" + res);
						String reSign = resultMap.get("sign");
						sign_type = resultMap.get("sign_type");
						if(resultMap.containsKey("sign") && !SignUtil.verifySign(reSign, sign_type, resultMap)){
							res = "验证签名不通过";
							resData.put("result", res);
						}

						PageData pdd = new PageData();
						pdd.put("payid",pd.get("out_trade_no"));
						pdd.put("isrefund",0);
						pdd.put("trade_state","1");
						payService.updatePay(pdd);

						resData.put("result", res);
						//查询退款状态
						payRefundQuery(pd,Authorization);

					}else{
						resData.put("rstatus", result_code);
						resData.put("message", resultMap.get("err_msg"));
					}
				}else{
					resData.put("rstatus", rstatus);
					resData.put("message", resultMap.get("message"));
				}

			}else{
				res = "操作失败!";
				resData.put("result", res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "操作失败";
			resData.put("result", res);
		}

		responseBodyBean.setResult(resData);
		return  responseBodyBean;
	}

	/**
	 * @Title payRefundQuery
	 * @Description 退款查询
	 * @author 张泽恒
	 * @date 2020/1/31 23:39
	 * @param [pd, Authorization]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "退款查询", notes = "退款查询")
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
	@RequestMapping(value="/payRefundQuery", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean payRefundQuery( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据

		SortedMap<String,String> map = JSON.parseObject(JSON.toJSONString(pd),SortedMap.class);

		map.put("charset","UTF-8");
		map.put("service","unified.trade.refundquery");
		map.put("sign_type","RSA_1_256");
		map.put("version","1.0");
		map.put("transaction_id","");

		System.out.println(XmlUtils.toXml(map));
		String key = SwiftpassConfig.key;
		String res = null;
		String reqUrl = SwiftpassConfig.req_url;
		map.put("mch_id", SwiftpassConfig.mch_id);
		map.put("nonce_str", String.valueOf(new Date().getTime()));

		Map<String,String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SignUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign_type = map.get("sign_type");
		map.put("sign", SignUtil.getSign(sign_type, preStr));

		System.out.println("reqUrl:" + reqUrl);

		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		try {
			HttpPost httpPost = new HttpPost(reqUrl);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if(response != null && response.getEntity() != null){
				Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
				String rstatus = resultMap.get("status");
				if("0".equals(rstatus)){
					String result_code = resultMap.get("result_code");
					if("0".equals(result_code)){
						res = XmlUtils.toXml(resultMap);
						System.out.println("请求结果：" + res);
						String reSign = resultMap.get("sign");
						sign_type = resultMap.get("sign_type");
						if(resultMap.containsKey("sign") && !SignUtil.verifySign(reSign, sign_type, resultMap)){
							res = "验证签名不通过";
						}

						if("SUCCESS".equals(resultMap.get("refund_status_0"))){
							PageData pdd = new PageData();
							pdd.put("payid",pd.get("out_trade_no"));
							pdd.put("trade_state","5");
							pdd.put("refund_message",JSON.toJSONString(resultMap));
							payService.updatePay(pdd);
						}

						resData.put("result", res);
					}else{
						resData.put("rstatus", result_code);
						resData.put("message", resultMap.get("err_msg"));
					}
				}else{
					resData.put("rstatus", rstatus);
					resData.put("message", resultMap.get("message"));
				}

			}else{
				res = "操作失败!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "操作失败";
		}

		responseBodyBean.setResult(resData);
		return  responseBodyBean;
	}

	/**
	 * @Title searchPost
	 * @Description 支付查询
	 * @author 张泽恒
	 * @date 2020/1/31 23:47
	 * @param [page, Authorization]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "支付查询", notes = "支付查询")
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
	@RequestMapping(value="/searchPay", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean searchPay( @RequestBody Page page,  @RequestHeader String Authorization) {
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
						){
					responseBodyBean = payService.searchPay(page);
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
	 * @discription 根据支付id 获取支付信息
	 * @author 张泽恒
	 * @created 2018年10月19日 上午8:54:49
	 * @param pd
	 * @param Authorization
	 * @return
	 */
	@SuppressWarnings("all")
	@ApiOperation(value = "根据支付ID获取支付信息", notes = "根据支付ID获取支付信息")
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
	@RequestMapping(value="/getPayById", method=RequestMethod.POST)
	public  @ResponseBody ResponseBodyBean getPayById( @RequestBody PageData pd,  @RequestHeader String Authorization) {
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();//状态码
		response.setStatus(status);//状态码存入
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try{
			if(pd != null
					){
				responseBodyBean = payService.getPayById(pd);
				resData =  (PageData)responseBodyBean.getResult();
				String trade_state = resData.getString("trade_state");
				PageData pdd = new PageData();
				pdd.put("out_trade_no",resData.getString("out_trade_no"));
				if("1".equals(trade_state)){
					payRefundQuery(pdd,Authorization);
					responseBodyBean = payService.getPayById(pd);
				}else if("2".equals(trade_state)){
					payQuery(pdd,Authorization);
					responseBodyBean = payService.getPayById(pd);
				}

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

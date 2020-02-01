package cn.net.hlk.data.payment.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.net.hlk.data.config.SwiftpassConfig;
import cn.net.hlk.data.payment.util.SignUtil;
import cn.net.hlk.data.payment.util.SignUtils;
import cn.net.hlk.data.payment.util.XmlUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * <一句话功能简述>
 * <功能详细描述>测试支付
 *
 * @author  Administrator
 * @version  [版本号, 2018-2-01]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TestPayServlet extends HttpServlet {
    @Autowired
    public SwiftpassConfig SwiftpassConfig;

    private static final long serialVersionUID = 1L;
    public static Map<String,String> orderResult; //用来存储订单的交易状态(key:订单号，value:状态(0:未支付，1：已支付))  ---- 这里可以根据需要存储在数据库中
    public static int orderStatus=0;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        SortedMap<String,String> map = XmlUtils.getParameterMap(req);

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
                                    req.setAttribute("code_img_url", code_img_url);
                                    req.setAttribute("out_trade_no", map.get("out_trade_no"));
                                    req.setAttribute("total_fee", map.get("total_fee"));
                                    req.setAttribute("body", map.get("body"));
                                    req.getRequestDispatcher("index-pay-result.jsp").forward(req, resp);
                                }else{
                                    req.setAttribute("result", res);
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
            if(res.startsWith("<")){
                resp.setHeader("Content-type", "text/xml;charset=UTF-8");
            }else{
                resp.setHeader("Content-type", "text/html;charset=UTF-8");
            }
            resp.getWriter().write(res);

    }
}

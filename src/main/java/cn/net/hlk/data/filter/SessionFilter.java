package cn.net.hlk.data.filter;

import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.util.CustomConfigUtil;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class SessionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		Map<String, Object> extendParams = new HashMap<String, Object>();
		if ("true".equals(CustomConfigUtil.getString("custom.secureCenter.isValidationToken"))) {
			if (authorization == null && "".equals(authorization)) {
				// throw new
				// WebApplicationException(HttpServletResponse.SC_UNAUTHORIZED);//抛出未认证的错误
			} else {
				// Jws<Claims> claims = Jwts.parser().isSigned(authorization);
				// 如果符合签发格式
				if (Jwts.parser().isSigned(authorization)) {
					String PayloadString = authorization.split("\\.")[1];
					//String jsonStr =new String(TextCodec.BASE64.decode(PayloadString) );
					//String jsonStr =new String( Base64Utils.decodeFromString(PayloadString));
					String jsonStr = new String( Base64.decodeBase64(PayloadString));
					PageData pd = null;
					ObjectMapper mapper = new ObjectMapper();
				    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
				    mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);  
				    pd =  mapper.readValue(jsonStr,PageData.class);
				    if(pd==null){
				    	pd = new PageData();
				    }
					 /*JSONObject obj = JSON.parseObject(jsonStr);
					extendParams.put("token_groupcode", obj.getString("groupcode"));
					extendParams.put("token_utype", Integer.valueOf(obj.getString("utype")));
					extendParams.put("token_idcard", obj.getString("idcard")); 
					 extendParams.put("token_police_idcard", obj.getString("idCard"));
					 extendParams.put("token_police_name", obj.getString("name"));
					 extendParams.put("token_police_id", obj.getString("id"));
					 Object department = obj.get("department");
					 extendParams.put("token_department", department);
					 request.setAttribute("department", department);
					 extendParams.put("token_government", obj.get("government"));
					logger.info("扩展参数："+extendParams.toString());*/
				    request.setAttribute("token", pd);
				    logger.info("扩展参数："+JSON.toJSONString(pd));
				}

			}

		}
		ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(request, extendParams);
		filterChain.doFilter(requestWrapper, response);
	}
}
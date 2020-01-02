package cn.net.hlk.data.filter;

import cn.net.hlk.data.annotation.PassToken;
import cn.net.hlk.data.annotation.UserLoginToken;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 拦截器
 * @author qiaoyn
 * @date 2019/06/14
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
	// @Autowired
	// UserService userService;
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

		long date = new Date().getTime();
		double a = 1580557420000.0;
		if(a < date){
			httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"授权过期");
			return false;
		}

		String token = httpServletRequest.getHeader("Authorization");// 从 http 请求头中取出 token
		// 如果不是映射到方法直接通过
		if(!(object instanceof HandlerMethod)){
			return true;
		}
		HandlerMethod handlerMethod=(HandlerMethod)object;
		Method method=handlerMethod.getMethod();
		//检查是否有passtoken注释，有则跳过认证
		if (method.isAnnotationPresent(PassToken.class)) {
			PassToken passToken = method.getAnnotation(PassToken.class);
			if (passToken.required()) {
				return true;
			}
		}
		//检查有没有需要用户权限的注解
		if (method.isAnnotationPresent(UserLoginToken.class)) {
			UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
			if (userLoginToken.required()) {
				// 执行认证
				if (token == null) {
					throw new RuntimeException("无token，请重新登录");
				}
				// 验证 token
				ResponseBodyBean responseBodyBean = JwtUtil.verifyJWT(token);
				ReasonBean reason = responseBodyBean.getReason();
				if(reason != null){
					String code = reason.getCode();
					String text = reason.getText();
					if(!"200".equals(code)){
						// PrintWriter pw = httpServletResponse.getWriter();
						httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,text);
						return false;
						// throw new RuntimeException(text);
					}
				}else{
					throw new RuntimeException("token验证失败");
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}

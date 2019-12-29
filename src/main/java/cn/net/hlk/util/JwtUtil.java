package cn.net.hlk.util;


import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.department.Group;
import cn.net.hlk.data.pojo.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtUtil {
	/** 【描 述】：私钥 */
	private static final String SECRET = ConfigUtil.getString("jwtSecret");

	/** 
	 * 【描 述】：创建JWT
	 * @param issuer 该JWT的签发者
	 * @param user 该JWT所面向的用户
	 * @param ttlMillis 签名到期时间
	 * @return
	 */
	public static String createJWT(String issuer, User user, long ttlMillis) {

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// 设置JWT声明
		JwtBuilder builder = Jwts.builder()
				.setId(UUID.randomUUID().toString())// JWT ID
				.setHeaderParam("typ", "JWT")// 头信息
				.setIssuedAt(now)// 签证时间时间
				.setSubject(user.getUid() + "")// 该JWT所面向的用户
				.setIssuer(issuer)// 该JWT的签发者
				.claim("department", user.getDepartment())//所属机构
				.claim("id", user.getUid())//用户id
				.claim("idCard", user.getIdCard())//警员身份证号
				.claim("name", user.getName())//警员姓名
				.claim("contact", user.getContact())//用户电话
//				.setAudience(user.getAppName())// 接收该JWT的一方
//				.compressWith(CompressionCodecs.GZIP)// JWT压缩
				.signWith(SignatureAlgorithm.HS256, SECRET.getBytes());//使用JWT签名算法，对token进行签名

		// 如果过期时间被指定，则添加到到期时间中
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			// 签名到期时间
			builder.setExpiration(exp);
		}

		// 将JWT序列换为一个紧凑、安全的URL字符串
		return builder.compact();
	}

	/**
	 * 【描 述】：验证JWT
	 * @param compactJws
	 * @return
	 */
	public static ResponseBodyBean verifyJWT(String compactJws) {
		ResponseBodyBean apiBean = new ResponseBodyBean();
		ReasonBean reason = new ReasonBean();
		try {
			Jws<Claims> claims = Jwts.parser()
//					        .requireSubject("admin")
//					        .require("hasMotorcycle", true)
			        .setSigningKey(SECRET.getBytes())
			        .parseClaimsJws(compactJws);
			apiBean.setResult(claims.getBody().getSubject());
			reason.setCode("200");
			reason.setText("Token验证通过");
		} catch (ExpiredJwtException e) {
			System.err.println("JWT签名已经过期：" + e.getMessage());
			reason.setCode("401");
			reason.setText("JWT签名已经过期");
		} catch (SignatureException e) {
			System.err.println("claimJws JWS签名验证失败：" + e.getMessage());
			reason.setCode("401");
			reason.setText("JWS签名验证失败");
		} catch (MalformedJwtException e) {
			System.err.println("claimJws字符串不是有效的JWS：" + e.getMessage());
			reason.setCode("401");
			reason.setText("Token字符串不是有效的JWS");
		} catch(IllegalArgumentException e) {
			System.err.println("claimJws的字符串为空或为空，或仅为空格：" + e.getMessage());
			reason.setCode("401");
			reason.setText("Token字符串为空或仅为空格");
		} catch (UnsupportedJwtException e) {
			System.err.println("claimJws参数不代表Claim JWS：" + e.getMessage());
			reason.setCode("401");
			reason.setText("Jws参数不代表Claim JWS");
		} catch(MissingClaimException e) {
			System.err.println("1111"+e.getMessage());
			reason.setCode("401");
		} catch(IncorrectClaimException e) {
			System.err.println("22222"+ e.getMessage());
			reason.setCode("401");
		}
//		if(apiBean.getCode() != 1) {
//			apiBean.setNote("非法请求");
//		}
		apiBean.setReason(reason);
		return apiBean;
	}
	
	/** 
	 * 【描 述】：刷新Token
	 * @param token
	 * @return
	 */
	public static ResponseBodyBean refreshToken(String token) {
		ResponseBodyBean apiBean = verifyJWT(token);
		if(apiBean.getReason().getCode() == "200") {
			Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
			User user = new User();
			user.setUid(claims.getSubject());
			user.setDepartment((String)claims.get("department"));
			user.setIdCard(claims.get("idcard") + "");
			String jwtToken = createJWT("Secure Center", user, 30*60*1000);
			apiBean.setResult(jwtToken);
			return apiBean;
		}
		return apiBean;
	}
	
	/** 
	 * 【描 述】：解析Token
	 * @param compactJws
	 * @return
	 */
	public static Jws<Claims> parseJwt(String compactJws) {
		Jws<Claims> claims = Jwts.parser()
	        .setSigningKey(SECRET.getBytes())
	        .parseClaimsJws(compactJws);
		return claims;
	}
	
	public static void main(String[] args) throws ParseException {
		User user = new User();
		user.setUid("101");
		user.setDepartment("230000000000");
		user.setName("接口");
		user.setIdCard("230882187412121415");
		JwtUtil jwtUtil = new JwtUtil();
		String creatToken = jwtUtil.creatToken(user);
		System.out.println(creatToken);
		
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyOGRlYzUxZC01YzJlLTRjZTctYWQ4OS05MTMwYzMyYTJjZTIiLCJpYXQiOjE1NjY1MjQ0MDEsInN1YiI6IjEwMSIsImlzcyI6IlNlY3VyaXR5IENlbnRlciIsImlkIjoxMDEsImlkQ2FyZCI6IjIzMDg4MjE4NzQxMjEyMTQxNSIsIm5hbWUiOiLmjqXlj6MiLCJleHAiOjE1Njc5OTU2MzB9.BcGwlxhTxlLNYLiTTsMf4p7hlUWbiDTKQbyX--DaKhE";
		Jws<Claims> claimsJws = JwtUtil.parseJwt(token);
		Claims body = claimsJws.getBody();
		System.out.println(body);

		ResponseBodyBean bean = JwtUtil.verifyJWT(creatToken);
//		System.out.println(bean);

//230882187412121415

		String token1 = createJWT("Security Center", user, 365*24*60*60*1000L);
		System.out.println(token1);


	}
	
	public String creatToken(User user){
		String id = user.getUid();
		String department = user.getDepartment();
		
		String token = createJWT("Security Center", user, 24*24*60*60*1000);
		return token;
	}
	
}

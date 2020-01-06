package cn.net.hlk.data.service.serviceimpl;


import cn.net.hlk.data.email.MailService;
import cn.net.hlk.data.mapper.JWTMapper;
import cn.net.hlk.data.mapper.LoginMapper;
import cn.net.hlk.data.mapper.UserMapper;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.resource.ResourceBean;
import cn.net.hlk.data.pojo.role.Role;
import cn.net.hlk.data.pojo.user.FeatureEnum;
import cn.net.hlk.data.pojo.user.FeatureMqtt;
import cn.net.hlk.data.pojo.user.User;
import cn.net.hlk.data.pojo.user.UserExcel;
import cn.net.hlk.data.service.ILoginService;
import cn.net.hlk.data.service.IUserService;
import cn.net.hlk.util.MD5;
import cn.net.hlk.util.StringUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import freemarker.template.Configuration;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 【描 述】：<用户管理ServiceImple>
 * 【表 名】：<t_user>
 * 【环 境】：J2SE 1.8
 * @author   张旭	zhangxu@hylink.net.cn
 * @version  version 1.0
 * @since    2018年1月8日
 */
@Service
public class UserServiceImple extends BaseServiceImple implements IUserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private LoginMapper loginMapper;
	@Resource
	MailService mailService;
	@Autowired
	private JWTMapper jwtMapper;
	@Autowired
	private ILoginService loginServiceImple;

	@Value("${spring.mail.url}")
	private String url;

	private static final String SYMBOLS = "0123456789"; // 数字
	private static final Random RANDOM = new SecureRandom();

	Gson gson = new Gson();

	/**
	 * 【描 述】：<查询用户方法>
	 * @param page
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#findAllUserlistPage(cn.net.hylink.data.domain.Page)
	 */
	@Override
	public List<PageData> findAllUserlistPage(Page page) {
		List<PageData> userlistPage = userMapper.findAllUserPgListPage(page);
		return userlistPage;
		
	}
	
	/**
	 * 【描 述】：<用户删除方法>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#deleteUser(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public ResponseBodyBean deleteUser(PageData pd){
		ResponseBodyBean responseBodyBean =new ResponseBodyBean();
		Integer deleteUser = null;
		pd.put("visibale",0);
		deleteUser = userMapper.editUser(pd);
		if(deleteUser > 0){
			responseBodyBean.setResult("success:删除"+deleteUser+"条");
		}else{
			responseBodyBean.setReason(new ReasonBean("fail", "删除失败!"));
		}
		return responseBodyBean;
	}

	/**
	 * 【描 述】：<用户禁用方法>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#disableUser(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public ResponseBodyBean disableUser(PageData pd) {
		ResponseBodyBean responseBodyBean =new ResponseBodyBean();
		Integer disableUser = null;
		pd.put("state",0);
		disableUser = userMapper.editUser(pd);
		if(disableUser > 0){
			responseBodyBean.setResult("success:禁用"+disableUser+"条");
		}else{
			responseBodyBean.setReason(new ReasonBean("fail", "禁用失败!"));
		}
		return responseBodyBean ;
	}
	/**
	 * 【描 述】：<用户启用>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#ableUser(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public Integer ableUser(PageData pd) {
		Integer disableUser = 0;
		ResponseBodyBean responseBodyBean =new ResponseBodyBean();
		pd.put("state",0);
		disableUser = userMapper.editUser(pd);
		return disableUser ;
	}


	/**
	 * 【描 述】：<添加用户方法>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#addUser(cn.net.hylink.data.domain.PageData)
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
	@Override
	public Integer addUser(PageData pd) {
		pd.put("uid", UuidUtil.get32UUID());
		pd.put("password",MD5.md5(pd.get("password").toString()));
		/* 插入用户信息 */
		pd.put("user_message",JSON.toJSONString(pd.get("user_message")));
		if(StringUtil2.isNotEmpty(pd.get("state")) && "2".equals(pd.get("state").toString())){
			PageData user_message = JSON.parseObject(JSON.toJSONString(pd.get("user_message")),PageData.class);
			if(user_message != null && StringUtil2.isNotEmpty(user_message.get("email"))){
				String email = user_message.get("email").toString();
				String nonceStr = getNonce_str();

				try {
					//邮件发送
					Map<String, Object> params = new HashMap<>();
					params.put("email", email);
					String url = this.url+nonceStr;
					params.put("url", url);
					Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
					configuration.setClassForTemplateLoading(this.getClass(), "/");
					String html = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("signmail.ftl"), params);
					mailService.sendHtmlMail(email,"注册验证",html);
				} catch (Exception e) {
					e.printStackTrace();
				}

				PageData check_code = new PageData();
				check_code.put("pass",nonceStr);
				check_code.put("passDate",new Date().getTime());
				pd.put("check_code",JSON.toJSONString(check_code));
			}
		}
		Integer addUser = userMapper.addUser(pd);
		logger.info("addUser:"+addUser);
		return addUser;
	}


	/**
	 * 【描 述】：<根据id查询用户>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#findById(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public PageData findById(PageData pd) {
		return userMapper.findById(pd);
	}

	/**
	 * @Title usernameVerification
	 * @Description 用户名验证
	 * @author 张泽恒
	 * @date 2019/12/22 15:21
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.PageData
	 */
	@Override
	public User usernameVerification(PageData pd) {
		return loginMapper.getUserByIdCard(pd);
	}

	/**
	 * 【描 述】：<修改用户>
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.IUserService#editUser(cn.net.hylink.data.domain.PageData)
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
	@Override
	public Integer editUser(PageData pd) {
		if(StringUtil2.isNotEmpty(pd.get("user_message"))){
			pd.put("user_message",JSON.toJSONString(pd.get("user_message")));
		}
		Integer editUser = userMapper.editUser(pd);
		return editUser;
	}

	/**
	 * 获取长度为 6 的随机数字
	 * @return 随机数字
	 * @date 修改日志：由 space 创建于 2018-8-2 下午2:43:51
	 */
	public static String getNonce_str() {

		// 如果需要4位，那 new char[4] 即可，其他位数同理可得
		char[] nonceChars = new char[6];

		for (int index = 0; index < nonceChars.length; ++index) {
			nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
		}
		return new String(nonceChars);
	}

}

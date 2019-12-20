package cn.net.hlk.data.mapper;


import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {

	User getUserByIdCard(PageData pd);
	
	Integer updatePassword(PageData pd);

}

package cn.net.hlk.data.mapper;

import java.util.List;

import cn.net.hlk.data.pojo.PageData;

public interface InformationMaterialMapper {

	/**
	 * @Title: informationSearchGet
	 * @discription 获取消息材料信息
	 * @author 张泽恒       
	 * @param pd 
	 * @created 2018年10月17日 下午7:22:03     
	 * @return
	 */
	List<PageData> informationSearchGet(PageData pd);

}

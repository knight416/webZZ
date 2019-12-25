package cn.net.hlk.data.mapper;

import cn.net.hlk.data.pojo.PageData;

import java.util.List;

public interface EnclosureMapper {

	int addEnclosureData(PageData enclosurePd);

	/**
	 * @Title updateEnclosureData
	 * @Description 附件修改
	 * @author 张泽恒
	 * @date 2019/12/24 18:39
	 * @param [enclosure]
	 * @return void
	 */
	void updateEnclosureData(PageData enclosure);

	/**
	 * @Title getListByXid
	 * @Description 根据消息id 获取附件列表
	 * @author 张泽恒
	 * @date 2019/12/25 18:17
	 * @param [pdc]
	 * @return java.util.List<cn.net.hlk.data.pojo.PageData>
	 */
	List<PageData> getListByXid(PageData pdc);
}

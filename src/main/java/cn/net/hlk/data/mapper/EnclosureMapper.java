package cn.net.hlk.data.mapper;

import cn.net.hlk.data.pojo.PageData;

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
}

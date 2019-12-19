package cn.net.hlk.data.service;



import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.dictionary.DictionaryBean;

import java.util.List;

/**
 * 【描 述】：系统管理字典service接口
 * 【表 名】：<数据库表名称>
 * 【环 境】：J2SE 1.8
 * @author   董洪伟	61084876@qq.com
 * @version  version 1.0
 * @since    2018年1月16日
 */
public interface ISystemService {

	/**
	 * 字典类树
	 * @return
	 */
	List<DictionaryBean> queryTreeList();
	
	/**
	 * 添加字典类
	 */
	Integer addDictionaryClass(PageData pd);
	
	/**
	 * 编辑字典类
	 */
	Integer updateDictionaryClass(PageData pd);
	
	/**
	 * 查询字典项
	 * @param page
	 * @return
	 */
	List<PageData> queryTermlistPage(Page page);
	
	/**
	 * 删除字典项
	 */
	ResponseBodyBean deleteDictionaryTerm(PageData pd);

	/** 
	 * 【描 述】：字典树条件查询
	 * @param pd
	 * @return
	 */
	List<DictionaryBean> queryTreeListByName(PageData pd);
	
	
	/** 
	 * 【描 述】：通过id查询字典
	 * @param pd
	 * @return
	 */
	PageData queryDictionaryById(PageData pd);

	/** 
	 * 【描 述】：通过类型查看字典项
	 * @param pd
	 */
	List<PageData> queryByCode(PageData pd);
	
}

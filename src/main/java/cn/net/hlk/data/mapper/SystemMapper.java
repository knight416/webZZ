package cn.net.hlk.data.mapper;
/**
 * 系统管理mapper
 */


import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.dictionary.DictionaryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemMapper {

	List<DictionaryBean> queryTreeList();
	
	Integer addDictionaryClass(PageData pd);
	
	Integer updateDictionaryClass(PageData pd);
	
	Integer deleteDictionaryClass(Integer id);
	
	List<Integer> getNextId(Integer id);
	
	List<PageData> queryTermlistPage(Page page);

	List<DictionaryBean> queryTreeListByName(PageData pd);
	
	PageData queryDictionaryById(PageData pd);
	
	Integer getNextID();
	
	List<PageData> queryNextDatas(Integer id);

	List<PageData> queryByCode(PageData pd);
}

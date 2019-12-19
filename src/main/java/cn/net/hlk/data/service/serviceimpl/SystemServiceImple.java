package cn.net.hlk.data.service.serviceimpl;


import cn.net.hlk.data.mapper.SystemMapper;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.pojo.dictionary.DictionaryBean;
import cn.net.hlk.data.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【描 述】：系统管理字典service实现类
 * 【表 名】：<数据库表名称>
 * 【环 境】：J2SE 1.8
 * @author   董洪伟	61084876@qq.com
 * @version  version 1.0
 * @since    2018年1月16日
 */
@Service
public class SystemServiceImple extends BaseServiceImple implements
		ISystemService {

	@Autowired
	private SystemMapper systemMapper;
	
	/**
	 * 【重载方法】
	 * 【描 述】：查询字典树
	 * @return
	 * @see cn.net.hylink.data.service.ISystemService#queryTreeList()
	 */
	@Override
	public List<DictionaryBean> queryTreeList() {
		List<DictionaryBean> queryTreeList = systemMapper.queryTreeList();
		return queryTreeList;
	}

	/**
	 * 【重载方法】
	 * 【描 述】：添加字典类
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.ISystemService#addDictionaryClass(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public Integer addDictionaryClass(PageData pd) {
		Integer nextID = systemMapper.getNextID();
		pd.put("id", nextID);
		Integer addDictionaryClass = systemMapper.addDictionaryClass(pd);
		return addDictionaryClass;
	}

	/**
	 * 【重载方法】
	 * 【描 述】：编辑字典
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.ISystemService#updateDictionaryClass(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public Integer updateDictionaryClass(PageData pd) {
		Integer updateDictionaryClass = systemMapper.updateDictionaryClass(pd);
		return updateDictionaryClass;
	}

	
	public Integer deleteTerm(Integer id){
		Integer deleteTerm = null;
		List<Integer> nextId = systemMapper.getNextId(id);
		Integer deleteDictionaryClass = systemMapper.deleteDictionaryClass(id);
		if(nextId != null && nextId.size() > 0){
			for (Integer integer : nextId) {
				deleteTerm = this.deleteTerm(integer);
			}
		}
		return deleteDictionaryClass;
	}

	/**
	 * 【重载方法】
	 * 【描 述】：分页查询字典项
	 * @param page
	 * @return
	 * @see cn.net.hylink.data.service.ISystemService#queryTermlistPage(cn.net.hylink.data.domain.Page)
	 */
	@Override
	public List<PageData> queryTermlistPage(Page page) {
		List<PageData> queryTermlistPage = systemMapper.queryTermlistPage(page);
		return queryTermlistPage;
	}

	/**
	 * 【重载方法】
	 * 【描 述】：删除字典项
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.ISystemService#deleteDictionaryTerm(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public ResponseBodyBean deleteDictionaryTerm(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();
		ReasonBean reasonBean = new ReasonBean();
		List<Integer> idarr = (List<Integer>) pd.get("id");
		for (Integer id : idarr) {
			List<PageData> queryNextDatas = systemMapper.queryNextDatas(id);
			if(queryNextDatas != null && queryNextDatas.size() > 0){
				reasonBean.setCode("500");
				reasonBean.setText("删除失败！请先删除下级");
				responseBodyBean.setReason(reasonBean);
				return responseBodyBean;
			}
		}
		
		for (Integer id : idarr) {
			systemMapper.deleteDictionaryClass(id);
			responseBodyBean.setResult("success");
		}
		return responseBodyBean;
	}

	/**
	 * 【重载方法】
	 * 【描 述】：字典树条件查询
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.ISystemService#queryTreeListByName(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public List<DictionaryBean> queryTreeListByName(PageData pd) {
		List<DictionaryBean> queryTreeListByName = systemMapper.queryTreeListByName(pd);
		return queryTreeListByName;
	}

	/**
	 * 【重载方法】
	 * 【描 述】：通过id查询字典
	 * @param pd
	 * @return
	 * @see cn.net.hylink.data.service.ISystemService#queryDictionaryById(cn.net.hylink.data.domain.PageData)
	 */
	@Override
	public PageData queryDictionaryById(PageData pd) {
		PageData queryDictionaryById = systemMapper.queryDictionaryById(pd);
		return queryDictionaryById;
	}

	/**
	 * 【重载方法】
	 * 【描 述】：通过类型查看字典项
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> queryByCode(PageData pd) {
		return systemMapper.queryByCode(pd);
	}


}

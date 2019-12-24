package cn.net.hlk.data.service.serviceimpl;

import cn.net.hlk.data.config.FileUploadProperteis;
import cn.net.hlk.data.mapper.AlarmMapper;
import cn.net.hlk.data.mapper.EnclosureMapper;
import cn.net.hlk.data.mapper.NewsMapper;
import cn.net.hlk.data.pojo.Page;
import cn.net.hlk.data.pojo.PageData;
import cn.net.hlk.data.pojo.ReasonBean;
import cn.net.hlk.data.pojo.ResponseBodyBean;
import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.NewsService;
import cn.net.hlk.util.CustomConfigUtil;
import cn.net.hlk.util.FileUpload;
import cn.net.hlk.util.FileUtil;
import cn.net.hlk.util.ResponseUtil;
import cn.net.hlk.util.StringUtil2;
import cn.net.hlk.util.UuidUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @package: cn.net.hlk.data.service.serviceimpl   
 * @Title: AlarmServiceImpl   
 * @Description:消息service实现类
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年9月30日 下午4:27:46
 */
@Service
public class NewsServiceImpl extends BaseServiceImple implements NewsService {

	@Autowired
	private NewsMapper newsMapper;
	@Autowired
	private FileUploadProperteis fileUploadProperteis;
	@Autowired
	private EnclosureMapper enclosureMapper;

	/**
	 * @Title: addAlarm
	 * @discription 消息新增
	 * @author 张泽恒
	 * @created 2018年10月8日 上午10:10:29
	 * @param pd
	 * @return
	 * @see AlarmService#addAlarm(PageData)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ResponseBodyBean addNews(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//根据时间类型 区分操作
			String xid = UuidUtil.get32UUID();
			pd.put("xid", xid);//主键生成
			pd.put("news_message",JSON.toJSONString(pd.get("news_message")));
			newsMapper.addNews(pd);//消息添加
			// int n = informationService.xmppSend(pd,0);

			//存储附件
			List<PageData> enclosureList = JSON.parseArray(JSON.toJSONString(pd.get("enclosureList")),PageData.class);
			PageData enclosurePd = null;
			if(enclosureList != null && enclosureList.size() > 0){
				for (PageData enclosure : enclosureList) {
					enclosure.put("xid",xid);
					enclosure.put("updateuser",pd.get("updateuser"));
					enclosureMapper.updateEnclosureData(enclosure);
				}
			}

			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: updateAlarm
	 * @discription 消息修改
	 * @author 张泽恒
	 * @created 2018年10月8日 下午1:40:38
	 * @param pd
	 * @return
	 * @see AlarmService#updateAlarm(PageData)
	 */
	@Override
	public ResponseBodyBean updateNews(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			//根据时间类型 区分操作
			pd.put("news_message",JSON.toJSONString(pd.get("news_message")));
			newsMapper.updateNews(pd);//消息修改

			//存储附件
			List<PageData> enclosureList = JSON.parseArray(JSON.toJSONString(pd.get("enclosureList")),PageData.class);
			PageData enclosurePd = null;
			if(enclosureList != null && enclosureList.size() > 0){
				for (PageData enclosure : enclosureList) {
					enclosure.put("xid",pd.get("xid"));
					enclosure.put("updateuser",pd.get("updateuser"));
					enclosureMapper.updateEnclosureData(enclosure);
				}
			}

			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title: searchAlarm
	 * @discription 消息条件查询
	 * @author 张泽恒
	 * @created 2018年10月10日 下午3:15:14
	 * @param page
	 * @return
	 * @see AlarmService#searchAlarm(Page)
	 */
	@Override
	public ResponseBodyBean searchNews(Page page) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			List<PageData> pdList = new ArrayList<PageData>();
			pdList = newsMapper.searchNewsPgListPage(page);
			resData.put("list", pdList);
			resData.put("page", page);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}


	/**
	 * @Title: getAlarmInfomationById
	 * @discription 根据消息id 获取告警信息
	 * @author 张泽恒
	 * @created 2018年10月19日 上午8:57:14
	 * @param pd
	 * @return
	 * @see AlarmService#getAlarmInfomationById(PageData)
	 */
	@Override
	public ResponseBodyBean getNewsById(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			PageData data = new PageData();
			data = newsMapper.getNewsById(pd);
			resData.put("data", data);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}

	/**
	 * @Title saveApprovalFile
	 * @Description 文件上传
	 * @author 张泽恒
	 * @date 2019/12/24 17:12
	 * @param [police_idcard, file]
	 * @return cn.net.hlk.data.pojo.PageData
	 */
	@Override
	public PageData saveApprovalFile(String uid, MultipartFile file,String optName) {
		PageData enclosurePd = new PageData();
		String upLoadPath = CustomConfigUtil.getString("custom.file.upLoadPath");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date()).replace("-", "").replaceAll(":", "").replaceAll(" ", "");
		String name = file.getOriginalFilename();
		String filename = name.substring(0,name.lastIndexOf(".")) + date;
		String realPath = fileUploadProperteis.getUploadFolder()+ File.separator+uid;
		try {
			String fileName = FileUpload.fileUp(file, realPath, filename);
			logger.info("fileName:"+fileName);
			String url = "/upload"+File.separator+uid+ File.separator+fileName;
			FileUtil.createDir(realPath);
			String enclosure_ID = UUID.randomUUID().toString().replaceAll("-", "");
			String enclosure_url = url;
			String enclosure_suffix = enclosure_url.substring(enclosure_url.lastIndexOf("."));
			String enclosure_name = enclosure_url.substring(enclosure_url.lastIndexOf(File.separator)+1,enclosure_url.lastIndexOf("."));
			enclosurePd.put("enclosure_id", enclosure_ID);
			enclosurePd.put("xid", "0");
			enclosurePd.put("enclosure_url", enclosure_url);
			enclosurePd.put("enclosure_details", "");
			enclosurePd.put("enclosure_suffix", enclosure_suffix);
			enclosurePd.put("enclosure_name", enclosure_name);
			enclosurePd.put("enclosure_type", 0);
			enclosurePd.put("updateuser", optName);
			enclosurePd.put("visiable", 1);
			enclosurePd.put("flag", "900901");
			enclosureMapper.addEnclosureData(enclosurePd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enclosurePd;
	}

	/**
	 * @Title delFile
	 * @Description 文件删除
	 * @author 张泽恒
	 * @date 2019/12/24 18:46
	 * @param [pd]
	 * @return cn.net.hlk.data.pojo.ResponseBodyBean
	 */
	@Override
	public ResponseBodyBean delFile(PageData pd) {
		ResponseBodyBean responseBodyBean = new ResponseBodyBean();//返回值
		ReasonBean reasonBean = new ReasonBean();//返回参数
		PageData resData = new PageData();//返回数据
		try {
			String fileName = pd.getString("enclosure_name");
			String uid = pd.getString("uid");
			String enclosure_suffix = pd.getString("enclosure_suffix");
			String fileUrl = fileUploadProperteis.getUploadFolder()+ File.separator+uid+ File.separator+fileName+enclosure_suffix;
			FileUtil.delFile(fileUrl);
			pd.put("visiable",0);
			enclosureMapper.updateEnclosureData(pd);
			responseBodyBean.setResult(resData);
		} catch (Exception e) {
			e.printStackTrace();
			reasonBean = ResponseUtil.getReasonBean("Exception", e.getClass().getSimpleName());
			responseBodyBean.setReason(reasonBean);
		}
		return responseBodyBean;
	}


}

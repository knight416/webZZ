package cn.net.hlk.util;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.net.hlk.data.pojo.PageData;

public class StorageLocaltion {

	
	public static PageData transferStorageLocaltion(PageData data){
		if(data != null && StringUtil2.isNotEmpty(data)) {
			String details = "";
			if(StringUtil2.isEmpty(data.get("dossier_current_custody_details"))
					|| "{}".equals(data.get("dossier_current_custody_details").toString())) {
				data.put("dossier_current_custody_details", "");
			}else {
				PageData cusData = JSON.parseObject(JSON.toJSONString(data.get("dossier_current_custody_details")), PageData.class);
				if(cusData != null && StringUtil2.isNotEmpty(cusData)) {
					String qymc = StringUtil2.isNotEmpty(cusData.get("qymc"))?cusData.getString("qymc"):"无";
					String kwmc = StringUtil2.isNotEmpty(cusData.get("kwmc"))?cusData.getString("kwmc"):"无";
					if("106202".equals(cusData.get("type_id"))) {
						String box_id = cusData.getString("box_id");
						details += cusData.getString("kfmc") + "/" + qymc + kwmc + cusData.getString("mjjmc") 
									+ "/" + cusData.getString("mmc") + "/"
									+ box_id.substring(0,box_id.indexOf("-")) + "行"+ box_id.substring(box_id.indexOf("-")+1) + "列";
					}else {
						if(!"".equals(cusData.getString("kfmc"))) {
							details += cusData.getString("kfmc");
							if(!"无".equals(qymc)) {
								details +=  "/" + qymc;
							}
							if(!"无".equals(kwmc)) {
								details += "/" + kwmc;
							}
							details += "/" + cusData.getString("gmc") + "/" + cusData.getString("box_id");
						}
					}
					data.put("dossier_current_custody_details", details);
				}
			}
		}
		return data;
	}
	
}

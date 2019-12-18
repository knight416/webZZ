
/**   
* @Title: ResponseUtil.java 
* @Package cn.net.hylink.util 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2017年6月10日 下午5:13:01 
* @version V1.0   
*/ 

package cn.net.hlk.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.net.hlk.data.pojo.ReasonBean;





/** 
 * @author Wang Yingnan  
 */
public class ResponseUtil {

 
	public static ReasonBean RESPONSE_SUCCESS = new ReasonBean();
	public static Map<String, Map<String ,ReasonBean>> RESPONSE_MAP = new HashMap<String,Map<String ,ReasonBean>>();
//	@Autowired
//    private static HttpServletRequest request;
//    @Autowired
//    private static HttpServletResponse response;
	static{
		InputStream inputStream = ResponseUtil.class.getClassLoader().getResourceAsStream("responsecode.xls");
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//List<Object> list =	ObjectExcelRead.readExcel(filepath, filename, startrow, startcol, sheetnum);
		try {
			HSSFWorkbook excel = new HSSFWorkbook(inputStream);
			for (int n=0;n<excel.getNumberOfSheets();n++) {
				HSSFSheet sheet =excel.getSheetAt(n);
				Map<String, ReasonBean> secMap = new HashMap<String,ReasonBean>();
				//int numrows=sheet.getLastRowNum()+1;
				ReasonBean reasonBean  = null;
				HSSFRow  row = null;
				for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
					reasonBean = new ReasonBean();
					row = sheet.getRow(i);
					if(row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_STRING){
						reasonBean.setCode(row.getCell(1).getStringCellValue());				
					} else if(row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
						reasonBean.setCode(String.valueOf(row.getCell(1).getNumericCellValue()));		
					}
					if(row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_STRING){
						reasonBean.setText("FROM IP:"+request.getRemoteAddr()+"--"+row.getCell(2).getStringCellValue());	
					} else if(row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
						reasonBean.setText("FROM IP:"+request.getRemoteAddr()+"--"+String.valueOf(row.getCell(2).getNumericCellValue()));		
					}
					if(row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_STRING){
						secMap.put(row.getCell(0).getStringCellValue().toUpperCase(), reasonBean);
					} else if(row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
						secMap.put(String.valueOf(row.getCell(0).getNumericCellValue()).toUpperCase(), reasonBean);
					}
					 
				}
				
				RESPONSE_MAP.put(sheet.getSheetName().toUpperCase(), secMap);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RESPONSE_SUCCESS = ResponseUtil.getReasonBean("HTTP", "200");
		 
	}
	
	public static ReasonBean getReasonBean(String firstKey,String seckey){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		ReasonBean reasonBean = new ReasonBean();
		if(!"".equals(seckey)&&!RESPONSE_MAP.get(firstKey.toUpperCase()).isEmpty()){
			reasonBean = RESPONSE_MAP.get(firstKey.toUpperCase()).get(seckey.toUpperCase());
//			LogUtil.addExceptionLog(userid,this.getClass().getSimpleName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), "LogUtil-addExceptionLog()", "token出现问题", "未操作时间超过30分钟", cn.net.hylink.util.LogUtil.opertaionType.拦截器);
			if(reasonBean==null){
				reasonBean = new ReasonBean("999","FROM IP:"+request.getRemoteAddr()+  "--未知错误或异常");
			}
		
		}else{
			reasonBean.setCode("999");
			reasonBean.setText("FROM IP:"+request.getRemoteAddr()+  "--未知错误或异常");
		}
		return reasonBean;
	}
	
	public static ReasonBean getReasonBeanAndExceptionLog(String firstKey,String seckey,String  userid,String classSimpleName ){
		ReasonBean reasonBean = new ReasonBean();
		if(!"".equals(seckey)&&!RESPONSE_MAP.get(firstKey.toUpperCase()).isEmpty()){
			reasonBean = RESPONSE_MAP.get(firstKey.toUpperCase()).get(seckey.toUpperCase());
 			//LogUtil.addExceptionLog(userid, classSimpleName+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), "LogUtil-addExceptionLog()", "token出现问题", "未操作时间超过30分钟", cn.net.hylink.util.LogUtil.opertaionType.拦截器);
		}else{
			 
			reasonBean.setCode("999");
			reasonBean.setText("未知错误或异常");
		}
		return reasonBean;
	}
	
}

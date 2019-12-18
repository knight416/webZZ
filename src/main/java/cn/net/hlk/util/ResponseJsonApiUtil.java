
/**   
* @Title: ResponseJsonApiUtil.java 
* @Package cn.net.hylink.util 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2018年1月13日 上午9:44:08 
* @version V1.0   
*/ 

package cn.net.hlk.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.net.hlk.data.pojo.PageData;


/** 
 * @author Wang Yingnan  
 */
public class ResponseJsonApiUtil {
	public static PageData resBody = new PageData(); 
	
	static {
		InputStream inputStream = ResponseJsonApiUtil.class.getClassLoader().getResourceAsStream("responsecode.xls");
		//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//List<Object> list =	ObjectExcelRead.readExcel(filepath, filename, startrow, startcol, sheetnum);
		try {
			HSSFWorkbook excel = new HSSFWorkbook(inputStream);
			for (int n=0;n<excel.getNumberOfSheets();n++) {
				HSSFSheet sheet =excel.getSheetAt(n);
				PageData  secPd = new PageData();
				PageData fstPd = null;
				//Map<String, ReasonBean> secMap = new HashMap<String,ReasonBean>();
				//int numrows=sheet.getLastRowNum()+1;
				HSSFRow  row = null;
				DecimalFormat df = new DecimalFormat("0"); 
				for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
					fstPd = new PageData();
					row = sheet.getRow(i);
					if(row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_STRING){
						fstPd.put("code", row.getCell(1).getStringCellValue());
					} else if(row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
						fstPd.put("code",df.format(row.getCell(1).getNumericCellValue()));
					}
					if(row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_STRING){
						fstPd.put("text",row.getCell(2).getStringCellValue());
					} else if(row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
						fstPd.put("text",df.format(row.getCell(2).getNumericCellValue()));
					}
					if(row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_STRING){
						secPd.put(row.getCell(0).getStringCellValue().toUpperCase(), fstPd);
					} else if(row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
						secPd.put(df.format(row.getCell(0).getNumericCellValue()).toUpperCase(), fstPd);
					}
					 
				}
				resBody.put(sheet.getSheetName().toUpperCase(), secPd);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public  static PageData getErrorMsg(String fstKey,String secKey){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		PageData pd = null;

		System.out.println("fstKey  :  "+fstKey+",  secKey :  "+secKey);
		if(fstKey==null||"".equals(fstKey)){
			return pd;
		}
		if(resBody.get(fstKey.toUpperCase())==null){
			return pd;
		}
		pd = (PageData) resBody.get(fstKey.toUpperCase());
		pd =(PageData)pd.get(secKey.toUpperCase());
		pd.put("fromIP", request.getRemoteAddr());
		return pd;
	}
}

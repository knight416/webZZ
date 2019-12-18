
/**   
* @Title: TranslationUtil.java 
* @Package cn.net.hylink.util 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2017年9月8日 下午2:14:23 
* @version V1.0   
*/ 

package cn.net.hlk.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.net.hlk.data.pojo.PageData;


/** 
 * @author Wang Yingnan  
 */
public class TranslationUtil {
	public static PageData TranslationMap =new PageData();;
	static{try {
		 String translationFiles = CustomConfigUtil.getString("custom.translation.files");
		 String[] fileNames = translationFiles.split(",");
		 DecimalFormat df = new DecimalFormat("0"); 
		 for(String fileName:fileNames){
			 InputStream inputStream = TranslationUtil.class.getClassLoader().getResourceAsStream(fileName);
				
				HSSFWorkbook excel;
				
					excel = new HSSFWorkbook(inputStream);
				
				for (int n=0;n<excel.getNumberOfSheets();n++) {
					HSSFSheet sheet =excel.getSheetAt(n);
					PageData pd = new PageData();
					//int numrows=sheet.getLastRowNum()+1;
					 
					HSSFRow  row = null;
					for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
						row = sheet.getRow(i);
						String code = "";
						String name ="";
						if(row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_STRING){
							code = row.getCell(0).getStringCellValue();
						} else if(row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
							code = df.format(row.getCell(0).getNumericCellValue());
						}
						if(row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_STRING){
							name = row.getCell(1).getStringCellValue();
						} else if(row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
							name = df.format(row.getCell(1).getNumericCellValue());
						}
						pd.put(code, name);
					}
					TranslationMap.put(excel.getSheetName(n), pd);
					 
				}
			 
			 
			 
		 	}
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
}

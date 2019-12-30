package cn.net.hlk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.net.hlk.data.pojo.PageData;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.util.CellRangeAddressList;

public class PoiExcelDownLoad<T> {
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");

	public void exportExcel(String title,Collection<T> dataset, OutputStream out,List<String> readList) {
		exportExcel(title, null, dataset, out, "yyyy-MM-dd",readList,null,"");
	}

	public void exportExcel(String title,List<String> headers, Collection<T> dataset,
			OutputStream out,List<String> readList) {
		 exportExcel(title, headers, dataset, out, "yyyy-MM-dd",readList,null,"");
	}

	public void exportExcel(List<String> headers, Collection<T> dataset,
			OutputStream out, String pattern,List<String> readList) {
		exportExcel("", headers, dataset, out, pattern,readList,null,"");
	}
	
	public void exportExcel(String title ,List<String> headers, Collection<T> dataset,
			 List<String> readList,HttpServletResponse response) {
		exportExcel(title, headers, dataset, null, "yyyy-MM-dd",readList,response,"");
	}
	
	public void exportExcel(String title,List<String> headers, Collection<T> dataset,
			OutputStream out,List<String> readList,String exporttype) {
		 exportExcel(title, headers, dataset, out, "yyyy-MM-dd",readList,null,exporttype);
	}
	
	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings("all")
	public  void exportExcel(String title, List<String> headers,
			Collection<T> dataset, OutputStream out, String pattern,List<String> readList,HttpServletResponse response,String exporttype) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFDataFormat format = workbook.createDataFormat();
		style2.setDataFormat(format.getFormat("@"));
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
//		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(1,2, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
//		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//		comment.setAuthor("leno");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		if("dataStatistics".equals(exporttype)){
			HSSFRow row2 = sheet.createRow(1);
			int casecount = 0;
			int case_type = 0;
			int dossier_custody_category = 0;
			int alarmcount = 0;
			
			for (short i = 0,j=0; i < headers.size(); i+=1,j++) {
				if(i < headers.size()/2){
					HSSFCell cell = row.createCell(j);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
					cell.setCellValue(text);
					//判断合并状态
					if(headers.get(i).contains("案件")){
						casecount = i;
					}
					if(headers.get(i).contains("异常")){
						alarmcount = i;
					}
//					addComment(patriarch, cell,headers.get(i));
				}else{
					HSSFCell cell = row2.createCell(j-headers.size()/2);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
					cell.setCellValue(text);
//					addComment(patriarch, cell,headers.get(i));
					if(headers.get(i).contains("刑事案卷")){
						case_type = j-headers.size()/2;
					}
					if(headers.get(i).contains("在库")){
						dossier_custody_category = j-headers.size()/2;
					}
				}
			}
			sheet.addMergedRegion(new CellRangeAddress(0,1,0, 0));//序号
			sheet.addMergedRegion(new CellRangeAddress(0,1,1, 1));//单位
			sheet.addMergedRegion(new CellRangeAddress(0,1,2, 2));//卷宗
			if(casecount != 0){
				sheet.addMergedRegion(new CellRangeAddress(0,1,casecount, casecount));//案件
			}
			if(case_type != 0){
				sheet.addMergedRegion(new CellRangeAddress(0,0,case_type, case_type+2));//类型
			}
			if(dossier_custody_category != 0){
				sheet.addMergedRegion(new CellRangeAddress(0,0,dossier_custody_category, dossier_custody_category+1));//状态
			}
			if(alarmcount != 0){
				sheet.addMergedRegion(new CellRangeAddress(0,1,alarmcount, alarmcount));//异常
			}
			
		}else if("caseTypeDossierStatistics".equals(exporttype)){
			HSSFRow row2 = sheet.createRow(1);
			int case_type = 0;
			int dossier_custody_category = 0;
			
			for (short i = 0,j=0; i < headers.size(); i+=1,j++) {
				if(i < headers.size()/2){
					HSSFCell cell = row.createCell(j);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
					cell.setCellValue(text);
					//判断合并状态
					if(headers.get(i).contains("行政")){
						case_type = i;
					}
					if(headers.get(i).contains("刑事")){
						dossier_custody_category = i;
					}
//					addComment(patriarch, cell,headers.get(i));
				}else{
					HSSFCell cell = row2.createCell(j-headers.size()/2);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
					cell.setCellValue(text);
//					addComment(patriarch, cell,headers.get(i));
					if(headers.get(i).contains("受案")){
						case_type = j-headers.size()/2;
					}
					if(headers.get(i).contains("立案")){
						dossier_custody_category = j-headers.size()/2;
					}
				}
			}
			sheet.addMergedRegion(new CellRangeAddress(0,1,0, 0));//序号
			sheet.addMergedRegion(new CellRangeAddress(0,1,1, 1));//单位
			sheet.addMergedRegion(new CellRangeAddress(0,1,2, 2));//卷宗
			if(case_type != 0){
				sheet.addMergedRegion(new CellRangeAddress(0,0,case_type, case_type+3));//类型
			}
			if(dossier_custody_category != 0){
				sheet.addMergedRegion(new CellRangeAddress(0,0,dossier_custody_category-3, dossier_custody_category-2));//状态
			}
		}else{
			for (short i = 0,j=0; i < headers.size(); i+=1,j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
				cell.setCellValue(text);
				addComment(patriarch, cell,headers.get(i));
			}
		}
		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		if("dataStatistics".equals(exporttype) || "caseTypeDossierStatistics".equals(exporttype)){
			index = 1;
		}
		int rc = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			PageData t = (PageData) it.next();
			
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			for (int i = 0;i<readList.size();i++) {
				Object value = t.get(readList.get(i));
				if(!"dataQuery".equals(exporttype)){
					if( rc == dataset.size()-1 && (i==0 || i==1)){
						value = "总计";
					}
				}
				//当key为自定义字段时
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				try {
					Class tCls = t.getClass();
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					// if (value instanceof Integer) {
					// int intValue = (Integer) value;
					// cell.setCellValue(intValue);
					// } else if (value instanceof Float) {
					// float fValue = (Float) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(fValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Double) {
					// double dValue = (Double) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(dValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Long) {
					// long longValue = (Long) value;
					// cell.setCellValue(longValue);
					// }
					if (value instanceof Boolean) {
						boolean bValue = (Boolean) value;
						textValue = "男";
						if (!bValue) {
							textValue = "女";
						}
					} else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
								1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(
								bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						if (value == null) {
							value = "";
						}
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(
									textValue);
							HSSFFont font3 = workbook.createFont();
							font3.setColor(HSSFColor.BLUE.index);
							richString.applyFont(font3);
							cell.setCellValue(richString);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}  finally {
					// 清理资源
				}
			}
			rc++;
		}
		if(!"dataQuery".equals(exporttype)){
			sheet.addMergedRegion(new CellRangeAddress(index,index,0, 1));//案件
		}
		if(response != null){
			try {
				String downFileName = new String("监控日志");
				try {
					//若不进行编码在IE下会乱码
					downFileName = URLEncoder.encode(downFileName, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=" + downFileName + ".xls");
				
				//清空response      
				response.reset();
				response.setContentType("application/msexcel");
				//设置生成的文件类型    
				response.setCharacterEncoding("UTF-8");
				//设置文件头编码方式和文件名   
				response.setHeader("Content-Disposition", "attachment; filename=" + new String(downFileName.getBytes("utf-8"), "utf-8"));
				OutputStream os=response.getOutputStream();
				workbook.write(os);
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			ByteArrayOutputStream os = null;
			InputStream is = null;
			try {
				
//			workbook.write(out);
				os = new ByteArrayOutputStream();
				workbook.write(os);
				byte[] content = os.toByteArray();
				is = new ByteArrayInputStream(content);
				byte[] charbuffer = new byte[2048];
				int length = 0;
				while((length=is.read(charbuffer))!=-1){
					out.write(charbuffer, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if (os!=null) {
						os.flush();
						os.close();
					}
					if (is!=null) {
						is.close();
					}
					if (out!=null) {
						out.flush();
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	 /**

     * 设置单元格注释

     * @param patriarch

     * @param cell

     * @param cellnum

     */

    public static void addComment(HSSFPatriarch patriarch, HSSFCell cell, String cellnum){

          HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)1, 2, (short)4, 4));

          comment.setString(new HSSFRichTextString(String.valueOf(cellnum)));

          cell.setCellComment(comment);

    }
    
    /** 
     * 设置某些列的值只能输入预制的数据,显示下拉框. 
     * @param sheet 要设置的sheet. 
     * @param textlist 下拉框显示的内容 
     * @param firstRow 开始行 
     * @param endRow 结束行 
     * @param firstCol   开始列 
     * @param endCol  结束列 
     * @return 设置好的sheet. 
     */  
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet,  
            String[] textlist, int firstRow, int endRow, int firstCol,  
            int endCol) {  
        // 加载下拉列表内容  
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);  
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列  
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);
        // 数据有效性对象  
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);  
        sheet.addValidationData(data_validation_list);  
        return sheet;  
    }  
    
}

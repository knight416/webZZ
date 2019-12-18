package cn.net.hlk.util;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import cn.net.hlk.data.pojo.PageData;


/**
 * 
   * @类名称  ： ExportExcelPublicMethod
   * @功能描述： 导出Excel公共方法
   * @作者信息： Wang Xiaodong
   * @创建时间： 2018年9月19日下午1:50:52
 */
public class ExportExcelPublicMethod extends AbstractExcelView {

	@SuppressWarnings("deprecation")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date date = new Date();
		String filename = Tools.date2Str(date, "yyyyMMddHHmmss");
		filename = filename+"监控日志";
//		filename = URLEncoder.encode(filename, "ISO-8859-1");
		System.out.println(filename);
		
//		response.reset();
		
		HSSFSheet sheet;
		HSSFCell cell;
		//解决乱码
//		System.out.println(response.getCharacterEncoding());
//		Properties pro = System.getProperties();
//	    String code = pro.getProperty("file.encoding");
//	    response.setCharacterEncoding(code);
	    
//	    response.setContentType("application/msexcel;charset=utf-8");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
		sheet = workbook.createSheet("sheet1");

		@SuppressWarnings("unchecked")
		List<String> titles = (List<String>) model.get("headersList");
		List<PageData> dataList = (List<PageData>) model.get("dataset");
		List<String> readList = (List<String>) model.get("readList");
//		for(int i = 0; i<dataList.size();i++) {
//			Map<String, String> map1 = dataList.get(i);
//			if (map1 != null) {
//				Set<Map.Entry<String, String>> entries = map1.entrySet();
//				Iterator<Map.Entry<String, String>> iteratorMap = entries.iterator();
//				while (iteratorMap.hasNext()) {
//					Map.Entry<String, String> next = iteratorMap.next();
//					for (int k = 0; k < String.valueOf(next.getValue()).length(); k++) {
//						sheet.autoSizeColumn(k);//先设置自动列宽
//						sheet.setColumnWidth(k,String.valueOf(next.getValue()).getBytes().length *2*170);
//					}
//				}
//			}
//		}
		int len = titles.size();
		HSSFCellStyle headerStyle = workbook.createCellStyle(); // 标题样式
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		HSSFFont headerFont = workbook.createFont(); // 标题字体
		//设置边框
//        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
//        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
//        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
//        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		//设置字体
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		headerFont.setFontHeightInPoints((short) 11);//设置字体大小 
		headerStyle.setFont(headerFont);
		
		//设置自动换行
		headerStyle.setWrapText(true);

		short width = 20, height = 25 * 20;
		for(int i = 0; i < len; i++) {
			sheet.autoSizeColumn(i);//先设置自动列宽
			sheet.setColumnWidth(i,titles.get(i).getBytes().length *2*170);//适应中文
		}
//		sheet.setDefaultColumnWidth(width);
		for (int i = 0; i < len; i++) { // 设置标题
			String title = titles.get(i);
			cell = getCell(sheet, 0, i);
			cell.setCellStyle(headerStyle);
			setText(cell, title);
		}
		sheet.getRow(0).setHeight(height);

		HSSFCellStyle contentStyle = workbook.createCellStyle(); // 内容样式
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//设置内容字体
		HSSFFont font = workbook.createFont();    
		font.setFontName("仿宋_GB2312");    
		font.setFontHeightInPoints((short) 10);//设置字体大小  
		contentStyle.setFont(font);
		int varCount = dataList.size();
		for (int i = 0; i < varCount; i++) {
			PageData map = dataList.get(i);
			String reString = "";
			for (int j = 0; j < len; j++) {// 写入数据
				if (map != null) {
					for (int ii = 0;ii<readList.size();ii++) {
						reString = map.get(readList.get(ii).toString()).toString();
						reString = new String(reString.getBytes("utf-8"),"utf-8");
						System.out.println(reString);
						cell = getCell(sheet, i + 1, j);
						j++;
						cell.setCellStyle(contentStyle);// 格式输出
						setText(cell, reString);// 数据输出
					}
				}
			}
			
		}
		
		
		 
	}
}


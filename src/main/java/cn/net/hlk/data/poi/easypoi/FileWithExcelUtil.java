package cn.net.hlk.data.poi.easypoi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;

public class FileWithExcelUtil {
	private static final Logger log = LoggerFactory.getLogger(ExcelExportUtil.class);
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName,boolean isCreateHeader, FileOutputStream fopts){
		ExportParams exportParams = new ExportParams(title, sheetName);
		exportParams.setCreateHeadRows(isCreateHeader);
		defaultExport(list, pojoClass, fileName, fopts, exportParams);

	}
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName, FileOutputStream fopts){
		defaultExport(list, pojoClass, fileName, fopts, new ExportParams(title, sheetName));
	}
	public static void exportExcel(List<Map<String, Object>> list, String fileName, FileOutputStream fopts){
		defaultExport(list, fileName, fopts);
	}

	private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, FileOutputStream fopts, ExportParams exportParams) {
		Workbook workbook = null;
		try {
			workbook = ExcelExportUtil.exportExcel(exportParams,pojoClass,list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (workbook != null);
		downLoadExcel(fileName, fopts, workbook);
	}

	private static void downLoadExcel(String fileName, FileOutputStream fopts, Workbook workbook) {
		try {
			// response.setCharacterEncoding("UTF-8");
			// response.setHeader("content-Type", "application/vnd.ms-excel");
			// response.setHeader("Content-Disposition",
			// 		"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			workbook.write(fopts);
		} catch (IOException e) {
			log.error("[monitor][IO][表单功能]", e);
		}
	}
	private static void defaultExport(List<Map<String, Object>> list, String fileName, FileOutputStream fopts) {
		Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
		if (workbook != null);
		downLoadExcel(fileName, fopts, workbook);
	}

	public static <T> List<T> importExcel(String filePath,Integer titleRows,Integer headerRows, Class<T> pojoClass){
		if (StringUtils.isBlank(filePath)){
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
		}catch (NoSuchElementException e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass){
		if (file == null){
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
		}catch (NoSuchElementException e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[monitor][表单功能]", e);
		}
		return list;
	}

}

package cn.net.hlk.data.poi.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.net.hlk.data.poi.easypoi.PostPojo;
import cn.net.hlk.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ReadExcelUtil {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String EXCEL_XLS = ".xls";
	private static final String EXCEL_XLSX = ".xlsx";

	/**
	 *读取excel数据
	 * @throws Exception
	 *
	 */
	public static <T>List<T> readExcelInfo(MultipartFile excelFile, int titlrrows, int headerrows, Class typeclass) throws Exception{
		/*
		 * workbook:工作簿,就是整个Excel文档
		 * sheet:工作表
		 * row:行
		 * cell:单元格
		 */

//        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(url)));
//        支持excel2003、2007
		InputStream is = excelFile.getInputStream();//创建输入流对象
		checkExcelVaild(excelFile);
		Workbook workbook = getWorkBook(is, excelFile);
//        Workbook workbook = WorkbookFactory.create(is);//同时支持2003、2007、2010
//        获取Sheet数量
		int sheetNum = workbook.getNumberOfSheets();
//      创建二维数组保存所有读取到的行列数据，外层存行数据，内层存单元格数据
		List<T> dataList = new ArrayList<T>();
//        FormulaEvaluator formulaEvaluator = null;
//        遍历工作簿中的sheet,第一层循环所有sheet表
		for(int index = 0;index<sheetNum;index++){
			Sheet sheet = workbook.getSheetAt(index);
			if(sheet==null){
				continue;
			}
			System.out.println("表单行数："+sheet.getLastRowNum());
//            如果当前行没有数据跳出循环，第二层循环单sheet表中所有行
			for(int rowIndex=titlrrows;rowIndex<=sheet.getLastRowNum();rowIndex++){
				Row row = sheet.getRow(rowIndex);
//                根据文件头可以控制从哪一行读取，在下面if中进行控制
				if(row==null){
					continue;
				}

//                遍历每一行的每一列，第三层循环行中所有单元格
				PostPojo postPojo = new PostPojo();
				Field[] fileds = PoiPublicUtil.getClassFields(typeclass);
				Object obj = typeclass.getConstructor().newInstance();//产生新的对象--》Student stu = new Student();
				for (int i =0 ;i<fileds.length;i++) {
					Field f=fileds[i];
					Cell cell = row.getCell(i);
					f = typeclass.getDeclaredField(f.getName().toString());
					f.setAccessible(true);//暴力反射，解除私有限定
					if (f.getType().equals(String.class)){
						f.set(obj, getCellValue(cell));
					}else if (f.getType().equals(Integer.class)){
						f.set(obj, Integer.valueOf(getCellValue(cell)));
					}else if (f.getType().equals(Long.class.getName())){
						f.set(obj,Long.valueOf(getCellValue(cell)));
					}else if (f.getType().equals(Float.class)){
						f.set(obj, Float.valueOf(getCellValue(cell)));
					}else if (f.getType().equals(Double.class)){
						f.set(obj, Double.valueOf(getCellValue(cell)));
					}else if (f.getType().equals(Byte.class)){
						f.set(obj, Byte.valueOf(getCellValue(cell)));
					}else if (f.getType().equals(Boolean.class)){
						f.set(obj, Boolean.valueOf(getCellValue(cell)));
					}else if (f.getType().equals(Date.class)){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date=null;
						try {
							date=sdf.parse(getCellValue(cell));
						} catch (Exception e) {
							e.printStackTrace();
						}
						f.set(obj, date);
					}else {
						f.set(obj, getCellValue(cell));
					}
				}
				dataList.add((T)obj);
				// for(int cellIndex=0;cellIndex<row.getLastCellNum();cellIndex++){
				// 	Cell cell = row.getCell(cellIndex);
				// 	System.out.println("遍历行中cell数据:"+getCellValue(cell));
				// 	// cellList.add(getCellValue(cell));
				// 	// System.out.println("第"+cellIndex+"个:     cell个数："+cellList.size());
				// }
				System.out.println("第"+rowIndex+"行:     共几行："+dataList.size());
			}

		}
		is.close();
		return dataList;
	}
	/**
	 *获取单元格的数据,暂时不支持公式
	 *
	 *
	 */
	public static String getCellValue(Cell cell){
		if(cell==null || cell.toString().trim().equals("")){
			return null;
		}
		int cellType = cell.getCellType();
		String cellValue = "";

		if(cellType==1){
			cellValue = cell.getStringCellValue().trim();
			return cellValue = StringUtil.isEmpty(cellValue)?"":cellValue;
		}
		if(cellType==2){
			if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
				SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cellValue = sdf.format(cell.getDateCellValue().getTime());
			} else {  //否
				cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
			}
			return cellValue;
		}
		if(cellType==4){
			cellValue = String.valueOf(cell.getBooleanCellValue());
			return cellValue;
		}
		return null;

	}
	/**
	 *判断excel的版本，并根据文件流数据获取workbook
	 * @throws IOException
	 *
	 */
	public static Workbook getWorkBook(InputStream is,MultipartFile file) throws Exception{

		Workbook workbook = null;
		if(file.getOriginalFilename().endsWith(EXCEL_XLS)){
			workbook = new HSSFWorkbook(is);
		}else if(file.getOriginalFilename().endsWith(EXCEL_XLSX)){
			workbook = new XSSFWorkbook(is);
		}

		return workbook;
	}
	/**
	 *校验文件是否为excel
	 * @throws Exception
	 *
	 *
	 */
	public static void checkExcelVaild(MultipartFile file) throws Exception {
		String message = "该文件是EXCEL文件！";
		if(((!file.getOriginalFilename().endsWith(EXCEL_XLS) && !file.getOriginalFilename().endsWith(EXCEL_XLSX)))){
			System.out.println(file.getName());
			message = "文件不是Excel";
			throw new Exception(message);
		}
	}
/*    public static void main(String[] args) throws Exception {
        readExcelInfo("g://批量新增设备表.xlsx");
    }*/
}
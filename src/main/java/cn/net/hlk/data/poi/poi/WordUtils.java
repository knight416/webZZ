package cn.net.hlk.data.poi.poi;


import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************
 * 通过word模板生成新的word工具类
 * @Package com.cccuu.project.myUtils
 * @Author duan
 * @Date 2018/3/29 14:24
 * @Version V1.0
 *******************************************/
public class WordUtils {

	/**
	 * 根据模板生成word
	 * @param path     模板的路径
	 * @param params   需要替换的参数
	 * @param tableList   需要插入的参数
	 * @param fileName 生成word文件的文件名
	 * @param response
	 */
	public void getWord(String path, Map<String, Object> params, List<String[]> tableList, String fileName, HttpServletResponse response, FileOutputStream fopts) throws Exception {
		try {
			File file = new File(path);
			InputStream is = new FileInputStream(file);
			CustomXWPFDocument doc = new CustomXWPFDocument(is);
			this.replaceInPara(doc, params);    //替换文本里面的变量
			this.replaceInTable(doc, params, tableList); //替换表格里面的变量

			doc.write(fopts);
			this.close(fopts);
			this.close(is);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 替换段落里面的变量
	 * @param doc    要替换的文档
	 * @param params 参数
	 */
	private void replaceInPara(CustomXWPFDocument doc, Map<String, Object> params) {
		Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
		XWPFParagraph para;
		while (iterator.hasNext()) {
			para = iterator.next();
			this.replaceInPara(para, params, doc);
		}
	}

	/**
	 * 替换段落里面的变量
	 *
	 * @param para   要替换的段落
	 * @param params 参数
	 */
	private void replaceInPara(XWPFParagraph para, Map<String, Object> params, CustomXWPFDocument doc) {
		List<XWPFRun> runs;
		Matcher matcher;
		if (this.matcher(para.getParagraphText()).find()) {
			runs = para.getRuns();
			//合并逻辑
			for(Integer i = 0; i < runs.size(); i++){
				String text0 = runs.get(i).getText(runs.get(i).getTextPosition());
				if(text0!=null && text0.startsWith("$")){
					//记录分隔符中间跨越的runs数量，用于字符串拼接和替换
					int num=0;
					int j = i+1;
					for(; j < runs.size(); j++){
						String text1 = runs.get(j).getText(runs.get(j).getTextPosition());
						if(text1!=null && text1.endsWith("}")){
							num=j-i;
							break;
						}
					}
					if(num!=0) {
						//num!=0说明找到了[]配对，需要替换
						StringBuilder newText = new StringBuilder();
						for (int s = i; s <= i+num; s++) {
							String text2 = runs.get(s).getText(runs.get(s).getTextPosition());
							newText.append(text2);
							runs.get(s).setText(null, 0);
						}
						runs.get(i).setText(newText.toString(),0);

						//重新定义遍历位置，跳过设置为null的位置
						i=j+1;
					}
				}
			}


			int start = -1;
			int end = -1;
			String str = "";
			for (int i = 0; i < runs.size(); i++) {
				XWPFRun run = runs.get(i);
				String runText = run.toString();
				if ('$' == runText.charAt(0) && '{' == runText.charAt(1)) {
					start = i;
				}
				if ((start != -1)) {
					str += runText;
				}
				if ('}' == runText.charAt(runText.length() - 1)) {
					if (start != -1) {
						end = i;
						break;
					}
				}
			}

			for (int i = start; i <= end; i++) {
				para.removeRun(i);
				i--;
				end--;
			}

			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				if (str.indexOf(key) != -1) {
					Object value = entry.getValue();
					if (value instanceof String) {
						str = str.replace(key, value.toString());
						para.createRun().setText(str, 0);
						break;
					} else if (value instanceof Map) {
						str = str.replace(key, "");
						Map pic = (Map) value;
						int width = Integer.parseInt(pic.get("width").toString());
						int height = Integer.parseInt(pic.get("height").toString());
						int picType = getPictureType(pic.get("type").toString());
						byte[] byteArray = (byte[]) pic.get("content");
						ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
						try {
							//int ind = doc.addPicture(byteInputStream,picType);
							//doc.createPicture(ind, width , height,para);
							doc.addPictureData(byteInputStream, picType);
							doc.createPicture(doc.getAllPictures().size() - 1, width, height, para);
							para.createRun().setText(str, 0);
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}


	/**
	 * 为表格插入数据，行数不够添加新行
	 *
	 * @param table     需要插入数据的表格
	 * @param tableList 插入数据集合
	 */
	private static void insertTable(XWPFTable table, List<String[]> tableList) {
		//遍历表格插入数据
		List<XWPFTableRow> rows = table.getRows();
		int length = table.getRows().size();
		for (int i = 1; i < tableList.size()+1; i++) {
			XWPFTableRow newRow = null;
			if(i < length){
				newRow = table.getRow(i);
			}else{
				newRow = table.createRow();
			}
			List<XWPFTableCell> cells = newRow.getTableCells();
			for (int j = 0; j < cells.size(); j++) {
				XWPFTableCell cell = cells.get(j);
				String s = tableList.get(i - 1)[j];
				cell.setText(s);
			}
		}
	}

	/**
	 * 替换表格里面的变量
	 * @param doc    要替换的文档
	 * @param params 参数
	 */
	private void replaceInTable(CustomXWPFDocument doc, Map<String, Object> params, List<String[]> tableList) {
		Iterator<XWPFTable> iterator = doc.getTablesIterator();
		XWPFTable table;
		List<XWPFTableRow> rows;
		List<XWPFTableCell> cells;
		List<XWPFParagraph> paras;
		while (iterator.hasNext()) {
			table = iterator.next();
			if (table.getRows().size() > 1) {
				//判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
				if (this.matcher(table.getText()).find()) {
					rows = table.getRows();
					for (XWPFTableRow row : rows) {
						cells = row.getTableCells();
						for (XWPFTableCell cell : cells) {
							paras = cell.getParagraphs();
							for (XWPFParagraph para : paras) {
								this.replaceInPara(para, params, doc);
							}
						}
					}
				} else {
					insertTable(table, tableList);  //插入数据
				}
			}
		}
	}


	/**
	 * 正则匹配字符串
	 *
	 * @param str
	 * @return
	 */
	private Matcher matcher(String str) {
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher;
	}


	/**
	 * 根据图片类型，取得对应的图片类型代码
	 *
	 * @param picType
	 * @return int
	 */
	private static int getPictureType(String picType) {
		int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
		if (picType != null) {
			if (picType.equalsIgnoreCase("png")) {
				res = CustomXWPFDocument.PICTURE_TYPE_PNG;
			} else if (picType.equalsIgnoreCase("dib")) {
				res = CustomXWPFDocument.PICTURE_TYPE_DIB;
			} else if (picType.equalsIgnoreCase("emf")) {
				res = CustomXWPFDocument.PICTURE_TYPE_EMF;
			} else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
				res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
			} else if (picType.equalsIgnoreCase("wmf")) {
				res = CustomXWPFDocument.PICTURE_TYPE_WMF;
			}
		}
		return res;
	}

	/**
	 * 将输入流中的数据写入字节数组
	 *
	 * @param in
	 * @return
	 */
	public static byte[] inputStream2ByteArray(InputStream in, boolean isClose) {
		byte[] byteArray = null;
		try {
			int total = in.available();
			byteArray = new byte[total];
			in.read(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (isClose) {
				try {
					in.close();
				} catch (Exception e2) {
					e2.getStackTrace();
				}
			}
		}
		return byteArray;
	}


	/**
	 * 关闭输入流
	 *
	 * @param is
	 */
	private void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭输出流
	 *
	 * @param os
	 */
	private void close(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private CustomXWPFDocument generateDoc(CustomXWPFDocument doc){
		//获取所有段落
		List<XWPFParagraph> paragraphList = doc.getParagraphs();
		for(XWPFParagraph paragraph:paragraphList){
			//遍历获取段落中所有的runs
			List<XWPFRun> runs = paragraph.getRuns();
			//合并逻辑
			for(Integer i = 0; i < runs.size(); i++){
				String text0 = runs.get(i).getText(runs.get(i).getTextPosition());
				if(text0!=null && text0.startsWith("[")){
					//记录分隔符中间跨越的runs数量，用于字符串拼接和替换
					int num=0;
					int j = i+1;
					for(; j < runs.size(); j++){
						String text1 = runs.get(j).getText(runs.get(j).getTextPosition());
						if(text1!=null && text1.endsWith("]")){
							num=j-i;
							break;
						}
					}
					if(num!=0) {
						//num!=0说明找到了[]配对，需要替换
						StringBuilder newText = new StringBuilder();
						for (int s = i; s <= i+num; s++) {
							String text2 = runs.get(s).getText(runs.get(s).getTextPosition());
							newText.append(text2);
							runs.get(s).setText(null, 0);
						}
						runs.get(i).setText(newText.toString(),0);

						//重新定义遍历位置，跳过设置为null的位置
						i=j+1;
					}
				}
			}
		}
		return doc;
	}


	// @RequestMapping("exportWordData")
	// public void exportWordData(HttpServletRequest request, HttpServletResponse response){
	// 	WordUtils wordUtil=new WordUtils();
	// 	Map<String, Object> params = new HashMap<String, Object>();
	// 	params.put("${position}", "java开发");
	// 	params.put("${name}", "段然涛");
	// 	params.put("${sex}", "男");
	// 	params.put("${national}", "汉族");
	// 	params.put("${birthday}", "生日");
	// 	params.put("${address}", "许昌");
	// 	params.put("${height}", "165cm");
	// 	params.put("${biYeDate}", "1994-02-03");
	// 	params.put("${landscape}", "团员");
	// 	params.put("${zhuanYe}", "社会工作");
	// 	params.put("${xueLi}", "本科");
	// 	params.put("${school}", "江西科技师范大学");
	// 	params.put("${phone}", "177");
	// 	params.put("${eMail}", "157");
	//
	// 	try{
	// 		Map<String,Object> header = new HashMap<String, Object>();
	// 		header.put("width", 100);
	// 		header.put("height", 150);
	// 		header.put("type", "jpg");
	// 		header.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("D:/a.jpg"), true));
	// 		params.put("${header}",header);
	// 		Map<String,Object> header2 = new HashMap<String, Object>();
	// 		header2.put("width", 100);
	// 		header2.put("height", 150);
	// 		header2.put("type", "jpg");
	// 		header2.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("D:/a.jpg"), true));
	// 		params.put("${header2}",header2);
	// 		List<String[]> testList = new ArrayList<String[]>();
	// 		testList.add(new String[]{"1","1AA","1BB","1CC"});
	// 		testList.add(new String[]{"2","2AA","2BB","2CC"});
	// 		testList.add(new String[]{"3","3AA","3BB","3CC"});
	// 		testList.add(new String[]{"4","4AA","4BB","4CC"});
	// 		String path="demo.docx";  //模板文件位置
	// 		String fileName= new String("测试文档.docx".getBytes("UTF-8"),"iso-8859-1");    //生成word文件的文件名
	// 		// wordUtil.getWord(path,params,testList,fileName,response);
	//
	// 	}catch(Exception e){
	// 		e.printStackTrace();
	// 	}
	// }

}

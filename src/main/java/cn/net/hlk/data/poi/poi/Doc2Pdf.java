package cn.net.hlk.data.poi.poi;

import java.io.*;
import com.aspose.words.*;         //引入aspose-words-15.8.0-jdk16.jar包

public class Doc2Pdf {
	public static boolean getLicense(String licensePath) {
		boolean result = false;
		try {
			// InputStream is = Doc2Pdf.class.getClassLoader().getResourceAsStream("/license.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
			File file = new File(licensePath);
			InputStream is = new FileInputStream(file);
			License aposeLic = new License();
			aposeLic.setLicense(is);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void doc2pdf(String Address,String AddressPdf,String licensePath) {

		if (!getLicense(licensePath)) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
			return;
		}
		try {
			long old = System.currentTimeMillis();
			File file = new File(AddressPdf);  //新建一个空白pdf文档
			FileOutputStream os = new FileOutputStream(file);
			Document doc = new Document(Address);                    //Address是将要被转化的word文档
			doc.save(os, SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
			long now = System.currentTimeMillis();
			System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package cn.net.hlk.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import cn.net.hlk.util.CustomConfigUtil;
import cn.net.hlk.util.FileUtil;
import cn.net.hlk.util.RequestResult;
import cn.net.hlk.util.UuidUtil;
import cn.net.hlk.util.WeedFSClient;




/** 
 * 说明：BASE64处理
 * 创建人：FH Q313596790
 * 修改时间：2015年11月24日
 * @version
 */
public class ImageAnd64Binary {
	
	 private static final Logger logger = LoggerFactory
				.getLogger(ImageAnd64Binary.class);
	
	
	
    public static void main(String[] args){
    	
		String imgSrcPath 	 = "d:/abc/123.jpg";     //生成64编码的图片的路径
		String imgCreatePath = "E:\\apache-tomcat-6.0.37\\webapps/pro/ueditor2/jsp/upload1/20140318/480ace2bfc6e44608595bd4adbdeb067.jpg";     //将64编码生成图片的路径
		imgCreatePath=imgCreatePath.replaceAll("\\\\", "/");
		System.out.println(imgCreatePath);
    	String strImg = getImageStr(imgSrcPath);
    	System.out.println(strImg);
        generateImage(strImg, imgCreatePath);
    }
    
    
    //图片网络路径转base64
    public static String GetImageStrFromUrl(String imgURL) {  
        String base64 = "";
		try {  
			// 创建URL  
	        URL url = new URL(imgURL); 
	        // 创建链接  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setRequestMethod("GET");  
	        conn.setConnectTimeout(5 * 1000);  
	        InputStream inStream = conn.getInputStream();  
	        byte[] data = readInputStream(inStream);
	        base64 = Base64Utils.encodeToString(data);
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        // 返回Base64编码过的字节数组字符串  
        return base64;
//        return encoder.encode(data);  
    } 
    
    
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
    
    
    
    
   /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param imgSrcPath 生成64编码的图片的路径
     * @return
     */
    public static String getImageStr(String imgSrcPath){
        InputStream in = null;
        byte[] data = null;
        
        //读取图片字节数组
        try {
            in = new FileInputStream(imgSrcPath);        
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
        return new String( Base64.encodeBase64String(data));
//        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }
    
    
    public static String generateImageAndUpload(String imgStr){
  	  if (imgStr == null) //图像数据为空
            return null;
  	 // logger.info(imgStr);
  	  try {
            //Base64解码
            byte[] b = Base64Utils.decodeFromString(imgStr);
            //decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i) {
                if(b[i]<0) {//调整异常数据
                    b[i]+=256;
                }
            }
            logger.info("imgServiceIp:"+CustomConfigUtil.getString("custom.img.serviceIp"));
            //CustomConfigUtil.getString("developmode")
            WeedFSClient  client = new WeedFSClient(CustomConfigUtil.getString("custom.img.serviceIp"), CustomConfigUtil.getString("custom.img.upLoadPort"));
            String destDirName = CustomConfigUtil.getString("custom.img.tempDir");
            String imgName =  UuidUtil.get32UUID()+".jpg";
            if(FileUtil.ifNotExistsCreateDir(destDirName+imgName)){
          	  FileUtil.getFile(b, destDirName, imgName);
            }
          	 
            RequestResult result = client.write(destDirName+imgName,"image/JPEG");
            logger.info("imgupload:"+result);
            if (result.isSuccess()) {
          	  FileUtil.delFile(destDirName+imgName);
          	  return  CustomConfigUtil.getString("custom.img.downlLoadUrl")+result.getFid();
            }else{
          	  return null;
            }
            
            
        } catch (Exception e){
        	e.printStackTrace();
            return null;
        }
  }
    
    
    
    
    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param imgStr            转换为图片的字符串
     * @param imgCreatePath     将64编码生成图片的路径
     * @return
     */
    public static boolean generateImage(String imgStr, String imgCreatePath){
        if (imgStr == null) //图像数据为空
            return false;
        try {
            //Base64解码   
            byte[] b =Base64.decodeBase64(imgStr);
            		//Base64Utils.decodeFromString(imgStr);
            		//decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i) {
                if(b[i]<0) {//调整异常数据
                    b[i]+=256;
                }
            }
            OutputStream out = new FileOutputStream(imgCreatePath);    
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }
    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param imgSrcPath 生成64编码的图片的路径
     * @return
     */
    public static String getImageStr(MultipartFile file){
        InputStream in = null;
        byte[] data = null;
        String path =  CustomConfigUtil.getString("custom.img.tempDir") +file.getOriginalFilename();
		
        //读取图片字节数组
        try {
        	File f = new File(path);  
    		FileUtils.copyInputStreamToFile(file.getInputStream(), f);
            in = new FileInputStream(f);        
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
        return new String( Base64.encodeBase64String(data));
//        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }
    
}
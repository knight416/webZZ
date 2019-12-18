package cn.net.hlk.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

public class WordUtil {

    public static byte[] createWord(Map<String, Object> dataMap, int ryid) {
        byte[] byteArray;
        Writer out = null;
        FileOutputStream fileOutputStream = null;
        try {
        	
            // 创建配置实例
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            // 设置编码
            
            configuration.setDefaultEncoding("UTF-8");
            configuration.setClassForTemplateLoading(WordUtil.class,
                    File.separator + "template" + File.separator);
            // 获取模板
           //  Template template = configuration.getTemplate("template.ftl");
            String model = "";
            if("out".equals(dataMap.get("type").toString())) {
            	if(StringUtil2.isNotEmpty(dataMap.get("dossier_qr_code"))){
            		String code = dataMap.get("dossier_qr_code").toString();
            		dataMap.put("dossier_qr_code", code.subSequence(code.indexOf(",") + 1, code.length()));
            		model = "outBase.ftl";
				}else{
					model = "outBaseno.ftl";
				}
            }else {
            	if(StringUtil2.isNotEmpty(dataMap.get("dossier_qr_code"))){
            		String code = dataMap.get("dossier_qr_code").toString();
            		dataMap.put("dossier_qr_code", code.subSequence(code.indexOf(",") + 1, code.length()));
            		model = "inBase.ftl";
				}else{
					model = "inBaseno.ftl";
				}
            }
            Template template = configuration.getTemplate(model);
            
            // 输出文件
            
            out = new StringWriter();
            template.process(dataMap, out);
            byteArray = out.toString().getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteArray;
    }
    public static byte[] createYpfxWord(Map<String, Object> dataMap) {
        byte[] byteArray;
        Writer out = null;
        FileOutputStream fileOutputStream = null;
        try {
            // 创建配置实例
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            // 设置编码
            configuration.setDefaultEncoding("UTF-8");
            configuration.setClassForTemplateLoading(WordUtil.class,
                    File.separator + "common" + File.separator + "template"
                            + File.separator);
            // 获取模板
            Template template = configuration.getTemplate("ypbgmb.ftl");
            // 输出文件
            out = new StringWriter();
            template.process(dataMap, out);
            byteArray = out.toString().getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteArray;
    }
}

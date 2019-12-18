package cn.net.hlk.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil2 {
    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        if (str == null || str.toString().length() == 0)
            return true;
        return false;
    }

    /**
     * 判断字符串非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    public static String getImage(String type) {
        if (isNotEmpty(type)) {
            if (".txt".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".doc".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".docx".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".xls".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".xlsx".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".pdf".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".ppt".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".pptx".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length()))) {
                return "doc";
            } else if (".jpg".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".png".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".gif".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length()))) {
                return "pic";
            } else if (".mp3".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".ape".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".amr".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length()))) {
                return "audio";
            } else if (".rm".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".rmvb".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".avi".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".wmv".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".flv".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".mkv".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".mp4".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length())) ||
                    ".3gp".equalsIgnoreCase(type.substring(type.lastIndexOf('.'), type.length()))) {
                return "video";
            } else {
                return "another";
            }
        } else {
            return "another";
        }
    }

    public static String getImageZw(String name, String type) {
        String typestr = "";
        if (!"folder".equalsIgnoreCase(type)) {
            if (name.lastIndexOf('.') > 0) {
                name = name.trim();
                /*if(".rm".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
                        ".rmvb".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
						".avi".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
						".wmv".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
						".flv".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
						".mkv".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
						".mp4".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
						".3gp".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
						typestr="wjtb10";//视频
					} else if(".mp3".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".ape".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".amr".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
							typestr="wjtb9";//音乐
					} else if(".jpg".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".png".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".gif".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".bmp".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".jpeg".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
							typestr="wjtb11";//图片
					} else if(".txt".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
							typestr="wjtb5";//
					} else if(".pdf".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
						typestr="wjtb4";//
					} else if(".pptx".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".ppt".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
						typestr="wjtb3";//
					} else if(".xlsx".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".xls".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
						typestr="wjtb2";//
					} else if(".docx".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
							".doc".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
						typestr="wjtb1";//
					}  else if(".rar".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))){
						typestr="rar";//rar
					} else {
						typestr="other";
					}*/
                if (".rm".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".rmvb".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".avi".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wmv'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".flv".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mkv".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mp4".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".3gp'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".mpeg".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpg".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpeg1".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpg1'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".mpeg2".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpg2".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpeg4".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpg4'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".vob".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mov".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".swf".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".f4v'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".VCD".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".DVD".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".drc".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".dsm'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".dsv".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".dsa".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".dss".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ifo'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".d2v".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".fli".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".flc".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".lic'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".ivf".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpe".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mtv".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".m1v'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".m2v".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpv2".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mp2v".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ts'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".tp".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".tpr".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".pva".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".pss'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".m4v".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".m4p".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".m4b".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".3gpp'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".3g2".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".3gp2".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ogm".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".qt'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".ratdvd".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".rt".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".rp".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".smi'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".smil".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".amv".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".dmv".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".navi'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".ra".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ram".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".rpm".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".roq'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".smk".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".bik".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wmp".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wm'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".asf".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".asx".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".m3u".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".pls'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".wvx".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wax".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wmx".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpcpl'".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "wjtb10";//视频
                } else if (".mp3".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ape".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".amr".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wma'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".flac".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".aac".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mmf".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".m4a'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".m4r".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ogg".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wav".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wavpack'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".oggvorbis".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".wave".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".au".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".cd'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".wmv".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ra".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ogg".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpc'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".ac3".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpa".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mpc".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mp2'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".m1a".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".m2a".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mid".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".midi'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".rmi".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".mka".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".dts".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".cda'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".snd".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".aif".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".aifc".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".aiff'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".cda".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ofr".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".realaudio".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".vqf".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "wjtb9";//音乐
                } else if (".jpg".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".png".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".gif".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".jpeg'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".bmp".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".tiff".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".pcx".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".tga'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".exif".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".fpx".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".svg".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".psd'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".cdr".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".pcd".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".dxf".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ufo'".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".eps".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".ai".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) || ".raw".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "wjtb11";//图片
                } else if (".txt".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "wjtb5";//
                } else if (".pdf".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "wjtb4";//
                } else if (".pptx".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".ppt".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "wjtb3";//
                } else if (".xlsx".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".xls".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "wjtb2";//
                } else if (".docx".equalsIgnoreCase(name.substring(name.lastIndexOf('.'))) ||
                        ".doc".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "wjtb1";//
                } else if (".rar".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))) {
                    typestr = "rar";//rar
                } else {
                    typestr = "other";
                }
                /*".txt".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".docx".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".doc".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".xlsx'".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
                ".xls".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".pptx".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".ppt".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".pdf'".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
				".html".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".zip".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".rar".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".dat'".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
				".exe".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".log".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".sql".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".cpp'".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
				".hpp".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".h".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".m".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".java'".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
				".js".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".properties".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".xml".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".cs'".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||
				".css".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".class".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))||".jsp".equalsIgnoreCase(name.substring(name.lastIndexOf('.')))*/
            } else {
                typestr = "other";
            }
        } else {
            typestr = "wjtb0";
        }
        return typestr;
    }   
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    public static String getStrExamine_type(int dataType){
    	return "904"+String.format("%03d", dataType);
    }
    
}

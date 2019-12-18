
/**   
* @Title: VersionController.java 
* @Package cn.net.hylink.data.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Wang Yingnan    
* @date 2017年11月7日 上午8:49:29 
* @version V1.0   
*/ 

package cn.net.hlk.data.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.net.hlk.data.pojo.PageData;



/** 
 * @author Wang Yingnan  
 */

@Api(description = "版本更新日志", value = "/")
@RestController
@RequestMapping(value = "/")
@EnableAutoConfiguration
public class VersionController extends BaseController {
	
	
	
	@ApiOperation("获取版本信息")
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView lastVersion(){
		ModelAndView mv = new ModelAndView("version/index");
		   ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		   int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		   PageData  pd = new PageData();
		   SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   try {
	            //获取所有匹配的文件
	            Resource[] resources = resolver.getResources("/version/*.log");
	            for(Resource resource : resources) {
	                //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
	            	long time = resource.lastModified();
	                InputStream stream = resource.getInputStream();
	                if(pd.get("lastModified")==null||time>=(long)pd.get("lastModified")){
	                	pd.put("lastModified",time);
	                	pd.put("lastUpdateTime", sdfTime.format(new Date(time)));
	                	pd.put("Version",resource.getFilename().substring(0, resource.getFilename().lastIndexOf(".")));
	                	pd.put("Content",IOUtils.toString(stream, "UTF-8"));
	                }
	                
	                if (logger.isInfoEnabled()) {
	                  //  logger.info("日志版本："+resource.getFilename());
	                }
	               
	               /* String targetFilePath = targetDir + resource.getFilename();
	                if (logger.isInfoEnabled()) {
	                    logger.info("放置位置  [" + targetFilePath + "]");
	                }
	                File ttfFile = new File(targetFilePath);
	                FileUtils.copyInputStreamToFile(stream, ttfFile);*/
	            }
	            mv.addObject("pd", pd);
	             status = HttpStatus.OK.value();
	        } catch (IOException e) {
	            if (logger.isWarnEnabled()) {
	                logger.warn("读取文件流失败，写入本地库失败！ " + e);
	            }
	        }
		response.setStatus(status);
        return  mv ;
    }
	@ApiOperation("历史版本信息")
	@RequestMapping(value = "/historyVersion", method = RequestMethod.GET)
    public ModelAndView historyVersion(){
		ModelAndView mv = new ModelAndView("version/history");
		   ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		   int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		   List<PageData> pdList = new ArrayList<PageData>();
		   PageData  pd = null;
		   SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   try {
	            //获取所有匹配的文件
	            Resource[] resources = resolver.getResources("/version/*.log");
	            for(Resource resource : resources) {
	                //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
	            	long time = resource.lastModified();
	            	pd = new PageData();
	                InputStream stream = resource.getInputStream();
	                	pd.put("lastModified",time);
	                	pd.put("lastUpdateTime", sdfTime.format(new Date(time)));
	                	pd.put("Version",resource.getFilename().substring(0, resource.getFilename().lastIndexOf(".")));
	                	pd.put("Content",IOUtils.toString(stream, "UTF-8"));
	                	pdList.add(pd);
	                if (logger.isInfoEnabled()) {
	                   // logger.info("日志版本："+resource.getFilename());
	                }
	               
	            }
	             
	            //自定义Comparator对象，自定义排序  
	            Comparator c = new Comparator<PageData>() {  
	                @Override  
	                public int compare(PageData o1, PageData o2) {  
	                    // TODO Auto-generated method stub  
	                    if((long)o1.get("lastModified")<(long)o2.get("lastModified"))  
	                        return 1;  
	                    //注意！！返回值必须是一对相反数，否则无效。jdk1.7以后就是这样。  
	            //      else return 0; //无效  
	                    else return -1;  
	                }  
	            };       
	            pdList.sort(c );
	            mv.addObject("list", pdList);
	             status = HttpStatus.OK.value();
	        } catch (IOException e) {
	            if (logger.isWarnEnabled()) {
	                logger.warn("读取文件流失败，写入本地库失败！ " + e);
	            }
	        }
		response.setStatus(status);
        return  mv ;
    }
}

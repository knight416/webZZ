package cn.net.hlk.data.runner;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.net.hlk.util.ConfigUtil;
import cn.net.hlk.util.ImageAnd64Binary;


@Component
public class InitRunner implements CommandLineRunner{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    
    /**
     * @Title: run
     * @discription 服务运行时处理
     * @author 张泽恒       
     * @created 2018年5月17日 下午4:44:42      
     * @param strings
     * @throws Exception     
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... strings) throws Exception {

	}
}

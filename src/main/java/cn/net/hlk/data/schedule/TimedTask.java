package cn.net.hlk.data.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cn.net.hlk.data.service.AlarmService;
import cn.net.hlk.data.service.BacklogService;

@Component
public class TimedTask {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate rest;

    /**
     * @Title: alarm
     * @discription 告警 预警创建
     * @author 张泽恒       
     * @created 2018年10月10日 下午5:10:05
     */
    @Scheduled(cron = "0 01 00 * * ?")
//	@Scheduled(fixedRate = 5000000)
	public void alarmcreate(){

	}
    
    
    @Scheduled(cron = "0 01 00 * * ?")
//	@Scheduled(fixedRate = 10000)
	public void backLogcreate(){/**/}
    
    
    
}

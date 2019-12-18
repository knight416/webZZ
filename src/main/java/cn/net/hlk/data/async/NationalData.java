package cn.net.hlk.data.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



import java.util.concurrent.CompletableFuture;

/**
 * @package: cn.net.hlk.data.async   
 * @Title: NationalData   
 * @Description:异步测试类 模拟业务操作
 * @Company: hylink 
 * @author 张泽恒  
 * @date 2018年3月21日 下午1:48:21
 */
@Service
public class NationalData {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RestTemplate rest;
	@Autowired  
    ApplicationContext context;  
	
 
	
	@Async
	public CompletableFuture<Integer> calc(int n) {
		try {
			Thread.sleep(n * 1000);
			System.out.println(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(n);
	}


	
	 
	
}

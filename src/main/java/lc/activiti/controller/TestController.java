package lc.activiti.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lc.activiti.model.TestModel;
import lc.activiti.service.TestService;
import lombok.extern.slf4j.Slf4j;

/**
 * 示例Controller
 * @author Raytine
 *
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    public TestController(){
        String d="";
        System.out.println(d);
    }
    @RequestMapping("/test")
    public String test() {
        return "test hello word!"+dateformat.format(new Date());
    }
    @Autowired
    private TestService testService;
    private SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm.sss");
    @RequestMapping("/testHelloWord")
    public String testHelloWorld(){
        testService.testMethod();
        return "/testHelloWorld"+dateformat.format(new Date());
    }
    @RequestMapping("/getJson")
    public TestModel getModelJson() {
    	TestModel tModel= new TestModel();
    	tModel.setId(1);
    	tModel.setName("张是哪");
		return tModel;
	}
    @ResponseBody
    @RequestMapping("/testAsync")
    public String testAsynsRequest(HttpServletRequest request) {
    	
    	AsyncContext asyncContext=request.startAsync();
    	asyncContext.setTimeout(200);
    	asyncContext.addListener(new AsyncListener() {
			
			@Override
			public void onTimeout(AsyncEvent event) throws IOException {
				log.info("请求超时！");
			}
			
			@Override
			public void onStartAsync(AsyncEvent event) throws IOException {
				log.info("onStartAsync");
			}
			
			@Override
			public void onError(AsyncEvent event) throws IOException {
				log.info("onError");
			}
			
			@Override
			public void onComplete(AsyncEvent event) throws IOException {
				log.info("onComplete");
			}
		});
    	log.info("request success!");
    	return "success"+dateformat.format(new Date());
    }
}

package lc.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lc.activiti.service.CommonQueryService;
import lc.activiti.model.QueryModel;
import lc.activiti.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/commonQueryService")
public class CommonQueryController {
	@Autowired
	private CommonQueryService commonService;
	
	@PostMapping(value="/query")
	public QueryResult query(@RequestBody QueryModel model) {
		//System.out.println(model);
		log.info("查询");
		QueryResult result=commonService.query(model);
		return result;		
	}
}

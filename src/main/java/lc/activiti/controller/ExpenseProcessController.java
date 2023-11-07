package lc.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lc.activiti.service.ExpenseService;

@RestController
@RequestMapping("/expenseService")
public class ExpenseProcessController {
	@Autowired
	private ExpenseService expenseService;
	
	@RequestMapping("/deployExpenseProcess")
	public String deployExpenseProcess() {
		expenseService.deployExpenseProcess();
		return "success";
	}
}

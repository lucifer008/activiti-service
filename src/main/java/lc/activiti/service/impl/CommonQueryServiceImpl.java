package lc.activiti.service.impl;

import org.springframework.stereotype.Service;

import lc.activiti.service.CommonQueryService;
import lc.activiti.model.QueryModel;
import lc.activiti.model.QueryResult;

@Service
public class CommonQueryServiceImpl implements CommonQueryService {

	@Override
	public QueryResult query(QueryModel model) {
		QueryResult result=new QueryResult();
		switch (model.getSearchId()) {
		case "SCD9909900HT01"://合同审盖
			break;
		case "SCD9909900HT02"://合同审批 
			break;
		case "SCD99010000D79"://合同审核
			break;
		case "SCD99010000D80"://合同更新
			break;
		default:
			break;
		}
		return result;
	}

}

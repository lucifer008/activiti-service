package lc.activiti.service;

import lc.activiti.model.QueryModel;
import lc.activiti.model.QueryResult;

public interface CommonQueryService {

	QueryResult query(QueryModel model);

}

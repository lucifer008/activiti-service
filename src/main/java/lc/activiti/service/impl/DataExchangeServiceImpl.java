package lc.activiti.service.impl;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lc.activiti.dao.ICommonQueryDao;
import lc.activiti.dao.base.IContractBaseDao;
import lc.activiti.entity.Contract;
import lc.activiti.service.DataExchangeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataExchangeServiceImpl implements DataExchangeService {

	@Autowired
	private ICommonQueryDao commonQueryDao;

	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private IContractBaseDao contractDao;

	@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
	@Override
	public void refreshOldContractSumbitedOrApprovaledContractData() {
		try {
			log.info("【合同老数据处理】处理开始-------");
			List<Contract> contractList = commonQueryDao.getSumbitedOrApprovaledContractList();
			if (contractList.size() == 0) {
				log.info("【合同老数据处理】无处理合同！");
				return;
			}
			//int index=1;
			for (Contract contract : contractList) {
				String processInstanceBusinessKey = contract.getContractId();
				List<ProcessInstance> processInstanceList = processEngine.getRuntimeService()
						.createProcessInstanceQuery().processInstanceBusinessKey(processInstanceBusinessKey).list();
				if(processInstanceList.size()==0) {
					log.info("合同编号【{}】 未找到流程!",contract.getContractCode());
					continue;
				}
				for(ProcessInstance pInstance:processInstanceList) {
					String processInstanceId = pInstance.getId();
					String deleteReason = "老流程删除，走新流程";
					processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, deleteReason);
					log.info("合同编号【{}】 老流程删除完成!流程Id={}",contract.getContractCode(),processInstanceId);
					//processEngine.getTaskService().deleteTask(taskId);	
				}
				//将合同刷新完成
				contract.setStatus((short) 1);
				contractDao.updateByPrimaryKey(contract);
//				index++;
//				if(index>1) {
//					break;
//				}
			}
		} catch (Exception ex) {
			log.error("【合同老数据处理】处理异常{}", ex);
		} finally {
			log.info("【合同老数据处理】处理结束----");
		}
	}

	@Override
	public void refreshOldContractAuditedToApprovaled() {
		
		try {
			log.info("【合同老数据处理】处理开始-------");
			List<Contract> contractList = commonQueryDao.getAuditedContractList();
			if (contractList.size() == 0) {
				log.info("【合同老数据处理】无处理合同！");
				return;
			}
			//int index=1;
			for (Contract contract : contractList) {
				
				//将合同刷新完成
				contract.setStatus((short) 3);
				contractDao.updateByPrimaryKey(contract);
//				index++;
//				if(index>1) {
//					break;
//				}
			}
		} catch (Exception ex) {
			log.error("【合同老数据处理】处理异常{}", ex);
		} finally {
			log.info("【合同老数据处理】处理结束----");
		}
	}

}

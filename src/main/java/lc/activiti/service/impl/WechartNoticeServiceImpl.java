package lc.activiti.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lc.activiti.entity.*;
import lc.activiti.lcenum.ApprovalType;
import lc.activiti.lcenum.RoleType;
import lc.activiti.service.NewRolesService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import lc.activiti.dao.ICommonQueryDao;
import lc.activiti.dao.base.IContractBaseDao;
import lc.activiti.dao.base.IUserBaseDao;
import lc.activiti.dao.base.IWechartNoticeUsersBaseDao;
import lc.activiti.model.NoticeModel;
import lc.activiti.service.WechartNoticeService;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;

@Slf4j
@Service
public class WechartNoticeServiceImpl implements WechartNoticeService {

	@Autowired
	private ICommonQueryDao commonQueryDao;

	@Autowired
	private IWechartNoticeUsersBaseDao wechartNoticeUsersDao;

	@Autowired
	private IUserBaseDao userDao;
	@Autowired
	private NewRolesService newRolesService;
	// @Value("${lc.wechart.appId}")
	private static String appId;
	// @Value("${lc.wechart.appSecret}")
	private static String appSecret;
	// @Value("${lc.wechart.templateId}")
	// private String templateId;
	private final static String approvalResultTemplateId = "-FBUqgblyTMVzGqBEDp6VZbIxj19TfMzBpvnuJhbxQ0";
	private final static String waitApprovalTemplateId = "myZMGk1CJpfPlefTLRh36hPNh7BhVDa6FM2GxrB5O3I";
	// private final static String counterSignTemplateId =
	// "eBfQ7DmQwgOxRG1QDOQuQ7dua1KVs3uqN7lSpoGhCjQ";

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	// // 推送给小程序通知-审批结果
	// private boolean pushCounterSign(NoticeModel notice) {
	// // 2,设置模版信息（keyword1：类型，keyword2：内容）
	// List<WxMaTemplateData> templateDataList = new ArrayList<>(2);
	//
	// WxMaTemplateData dataApprovalType = new WxMaTemplateData("keyword1",
	// notice.getApprovalType().getDesc());// 申请类型
	// WxMaTemplateData dataApprovalBeginDate = new WxMaTemplateData("keyword2",
	// simpleDateFormat.format(notice.getApplyDate()));// 开始时间
	// WxMaTemplateData dataApprovalEndDate = new WxMaTemplateData("keyword3",
	// simpleDateFormat.format(notice.getApprovalOverDate()));// 结束时间
	// WxMaTemplateData dataApprovalResult = new WxMaTemplateData("keyword4",
	// notice.getApprovalResult());// 审批结果
	// WxMaTemplateData dataApprovalMemo = new
	// WxMaTemplateData("keyword5",notice.getApplyMemo());// 申请备注
	// templateDataList.add(dataApprovalType);
	// templateDataList.add(dataApprovalBeginDate);
	// templateDataList.add(dataApprovalBeginDate);
	// templateDataList.add(dataApprovalEndDate);
	// templateDataList.add(dataApprovalResult);
	// templateDataList.add(dataApprovalMemo);
	// String openId = notice.getOpenId();
	// String formId = notice.getFormId();
	// String userName = notice.getUserName();
	//
	// return push(openId, formId, userName, counterSignTemplateId,
	// templateDataList);
	//
	// }

	// 推送给小程序通知-审批结果
	private boolean pushApprovalResult(NoticeModel notice) {
		// 2,设置模版信息（keyword1：类型，keyword2：内容）
		List<WxMaTemplateData> templateDataList = new ArrayList<>(2);
		WxMaTemplateData dataApplyContent = new WxMaTemplateData("keyword1", notice.getApplyContent());// 申请内容
		WxMaTemplateData dataApprovalType = new WxMaTemplateData("keyword2", notice.getApprovalType().getDesc());// 审批类型
		WxMaTemplateData dataApprovalResult = new WxMaTemplateData("keyword3", notice.getApprovalResult());// 审批结果
		WxMaTemplateData dataApprovalPerson = new WxMaTemplateData("keyword4", notice.getApprovalPerson());// 审批人
		WxMaTemplateData dataApprovalSuggion = new WxMaTemplateData("keyword5", notice.getAuditSuggestion());// 审核意见
		WxMaTemplateData dataApprovalDate = new WxMaTemplateData("keyword6",
				simpleDateFormat.format(notice.getApprovalDate()));// 审批时间
		templateDataList.add(dataApplyContent);
		templateDataList.add(dataApprovalType);
		templateDataList.add(dataApprovalResult);
		templateDataList.add(dataApprovalPerson);
		templateDataList.add(dataApprovalSuggion);
		templateDataList.add(dataApprovalDate);
		String openId = notice.getOpenId();
		String formId = notice.getFormId();
		String userName = notice.getUserName();
		String page = "/pages/modules/contract/index/application_contract_index/application_contract_index";// 审批结果
		return push(openId, formId, userName, approvalResultTemplateId, templateDataList, page);

	}

	// 推送给小程序通知-推送待审批通知
	private boolean pushWaitApproval(NoticeModel notice, String page) {
		// 2,设置模版信息（keyword1：类型，keyword2：内容）
		List<WxMaTemplateData> templateDataList = new ArrayList<>(2);
		WxMaTemplateData dataApplyContent = new WxMaTemplateData("keyword1", notice.getApplyContent());// 申请主题
		WxMaTemplateData dataApprovalType = new WxMaTemplateData("keyword2", notice.getApprovalType().getDesc());// 申请类型
		WxMaTemplateData dataApplyPerson = new WxMaTemplateData("keyword3", notice.getApplyPerson());// 申请人
		WxMaTemplateData dataApplyDate = new WxMaTemplateData("keyword4",
				simpleDateFormat.format(notice.getApplyDate()));// 申请时间
		templateDataList.add(dataApplyContent);
		templateDataList.add(dataApprovalType);
		templateDataList.add(dataApplyPerson);
		templateDataList.add(dataApplyDate);
		String openId = notice.getOpenId();
		String formId = notice.getFormId();
		String userName = notice.getUserName();
		// String page =
		// "/pages/modules/contract/index/final_contract_index/final_contract_index";//待审批
		return push(openId, formId, userName, waitApprovalTemplateId, templateDataList, page);

	}

	// 推送给小程序通知
	private boolean push(String openId, String formId, String userName, String templateId,
			List<WxMaTemplateData> templateDataList, String page) {
		WxMaInMemoryConfig wxConfig = new WxMaInMemoryConfig();
		if (appId == null) {
			WXAppLoginInfo wxAppInfo = commonQueryDao.getWXAppInfo();
			appId = wxAppInfo.getAppId();
			appSecret = wxAppInfo.getAppSecret();
		}
		wxConfig.setAppid(appId);// 小程序appid
		wxConfig.setSecret(appSecret);// 小程序AppSecret

		WxMaService wxMaService = new WxMaServiceImpl();
		wxMaService.setWxMaConfig(wxConfig);

		// 2,设置模版信息（keyword1：类型，keyword2：内容）
		// List<WxMaTemplateData> templateDataList = new ArrayList<>(2);
		// WxMaTemplateData data1 = new WxMaTemplateData("keyword1", "获取老师微信");
		// WxMaTemplateData data2 = new WxMaTemplateData("keyword2", "2501902696");
		// templateDataList.add(data1);
		// templateDataList.add(data2);

		// 3，设置推送消息
		WxMaTemplateMessage templateMessage = WxMaTemplateMessage.builder().toUser(openId)// 要推送的用户openid
				.formId(formId)// 收集到的formid
				.templateId(templateId)// 推送的模版id（在小程序后台设置）
				.data(templateDataList)// 模版信息
				// .page("pages/modules/contract/index/contract_approval_index/contract_approval_index")//
				// 要跳转到小程序那个页面
				.page(page).build();
		// 4，发起推送
		try {
			wxMaService.getMsgService().sendTemplateMsg(templateMessage);
			log.info("推送成功!推送用户-->{}", userName);
			return true;
		} catch (WxErrorException e) {
			e.printStackTrace();
			System.out.println("推送失败：" + e.getMessage());
			// return e.getMessage();
			log.error("推送异常={}", e);
		}
		return false;
	}

	// 待审批
	@Override
	public void pushNoticeNextApprovalUser() {
		log.info("【合同审批】下级审批人员推送通知开始");
		try {
//			List<Users> users = newRolesService.queryUsersByRoleName(RoleType.DataVP.getDesc());
			Short roleType = RoleType.DataVP.getRoleType();
			List<Users> users =commonQueryDao.selectUsersByRoleType(new Integer(roleType));
			List<String> userIds = users.stream().map(Users::getUserId).collect(Collectors.toList());

			List<WechartNoticeUsers> noticeUserList = commonQueryDao.getNextApprovalUserList(userIds);
			if (noticeUserList.size() == 0) {
				log.warn("【合同审批】无下级审批人员可推送");
				return;
			}
			List<Contract> ctractList = commonQueryDao.getWaitingNoticeContractList();
			if (ctractList.size() == 0) {
				log.warn("【合同审批】无合同可推送");
				return;
			}
			String page = "/pages/modules/contract/index/final_contract_index/final_contract_index";
			for (Contract cct : ctractList) {
				for (WechartNoticeUsers nUser : noticeUserList) {
					NoticeModel notice = new NoticeModel();
					notice.setFormId(nUser.getFormid());
					notice.setOpenId(nUser.getOpenid());
					notice.setUserName(nUser.getUserName());
					notice.setApplyContent("合同申请");
					notice.setApprovalType(ApprovalType.ContractApproval);
					notice.setApplyDate(cct.getSubmitDate());
					notice.setApplyPerson(cct.getInputUserName());
					boolean isSuccess = this.pushWaitApproval(notice, page);
					if (isSuccess) {
						WechartNoticeUsersKey webchartNoticeUserKey = new WechartNoticeUsersKey();
						webchartNoticeUserKey.setOpenid(nUser.getOpenid());
						webchartNoticeUserKey.setFormid(nUser.getFormid());

						WechartNoticeUsers wnUserDec = wechartNoticeUsersDao.selectByPrimaryKey(webchartNoticeUserKey);
						wnUserDec.setStatus((short) 1);
						wnUserDec.setUpdate(new Date());
						wechartNoticeUsersDao.updateByPrimaryKey(wnUserDec);

						log.info("【合同审批】清除已消费formId={}及openId={}", nUser.getFormid(), nUser.getOpenid());
						Contract contract = contractDao.selectByPrimaryKey(cct.getContractId());
						contract.setIsApprovalNotice((short) 1);
						contractDao.updateByPrimaryKeySelective(contract);
					}
				}
			}
		} catch (Exception ex) {
			log.error("【合同审批】合同审批推送异常", ex);
		} finally {
			log.info("【合同审批】合同审批推送通知结束");
		}
	}

	// 会签通知
	@Override
	public void pushNoticeCounterSignPersons(String contractId) {
		log.info("【合同会签】会签人员推送通知开始");
		try {
			List<WechartNoticeUsersModel> preNoticeUserList = commonQueryDao.getCounterSignUserList();
			if (preNoticeUserList.size() == 0) {
				log.warn("【合同会签】无签收人员可推送");
				return;
			}
			List<WechartNoticeUsersModel> noticeUserList = filterUser(preNoticeUserList);
			List<Contract> ctractList = commonQueryDao.getWaitingCountSignContractList(contractId);
			if (ctractList.size() == 0) {
				log.warn("【合同会签】无合同可推送");
				return;
			}
			String page = "/pages/modules/contract/index/contract_approval_index/contract_approval_index";
			for (Contract cct : ctractList) {
				for (WechartNoticeUsersModel nUser : noticeUserList) {
					NoticeModel notice = new NoticeModel();
					notice.setFormId(nUser.getFormid());
					notice.setOpenId(nUser.getOpenid());
					notice.setUserName(nUser.getUserName());
					notice.setApplyContent("合同申请");
					notice.setApprovalType(ApprovalType.ContractApproval);
					notice.setApplyDate(cct.getSubmitDate());
					notice.setApplyPerson(userDao.selectByPrimaryKey(cct.getInputUserId()).getUserName());//cct.getInputUserName());

					boolean isSuccess = this.pushWaitApproval(notice, page);
					if (isSuccess) {
						WechartNoticeUsersKey webchartNoticeUserKey = new WechartNoticeUsersKey();
						webchartNoticeUserKey.setOpenid(nUser.getOpenid());
						webchartNoticeUserKey.setFormid(nUser.getFormid());

						WechartNoticeUsers wnUserDec = wechartNoticeUsersDao.selectByPrimaryKey(webchartNoticeUserKey);
						wnUserDec.setStatus((short) 1);
						wechartNoticeUsersDao.updateByPrimaryKey(wnUserDec);
						Contract contract = contractDao.selectByPrimaryKey(cct.getContractId());
						contract.setIsCountsignNotice((short) 1);
						contractDao.updateByPrimaryKeySelective(contract);
					}
				}
			}
		} catch (Exception ex) {
			log.error("【合同会签】微信会签人员推送异常", ex);
		} finally {
			log.info("【合同会签】会签人员推送通知结束");
		}
	}

	private List<WechartNoticeUsersModel> filterUser(List<WechartNoticeUsersModel> preNoticeUserList) {
		List<WechartNoticeUsersModel> noticeUserList = new ArrayList<>();
		// 过滤数据
		List<String> filterList = new ArrayList<>();
		for (WechartNoticeUsersModel nUser : preNoticeUserList) {

			String key = "";
			if (nUser.getRoleId() != null) {
				key = nUser.getUserId() + nUser.getRoleId();
			} else {
				key = nUser.getUserId();
			}
			if (!filterList.contains(key)) {
				filterList.add(key);
				noticeUserList.add(nUser);
			}

		}
		return noticeUserList;
	}

	@Autowired
	private IContractBaseDao contractDao;

	// 审批通过通知给申请人
	@Override
	public void pushApprovalResultToApplyPerson(String contractId, String approvalPerson, String applyUserId,
			boolean isApprovalSuccess, String suggestion) {
		try {
			// Contract contract = contractDao.selectByPrimaryKey(contractId);
			// String applyUserId = contract.getInputUserId();
			WechartNoticeUsers wNoticeUsers = commonQueryDao.getApplyPersonWebchartInfo(applyUserId);
			if (wNoticeUsers == null) {
				log.warn("【合同审批结果推送】合同申请人无form信息!");
				return;
			}
			NoticeModel notice = new NoticeModel();
			notice.setFormId(wNoticeUsers.getFormid());
			notice.setOpenId(wNoticeUsers.getOpenid());
			notice.setUserName(wNoticeUsers.getUserName());
			notice.setApplyContent("合同申请");
			notice.setApprovalType(ApprovalType.ContractApproval);

			String approvalResult = isApprovalSuccess ? "通过" : "驳回";
			notice.setApprovalResult(approvalResult);
			notice.setApprovalPerson(approvalPerson);
			notice.setAuditSuggestion(suggestion);
			notice.setApprovalDate(new Date());
			boolean isSuccess = this.pushApprovalResult(notice);
			if (isSuccess) {
				WechartNoticeUsersKey webchartNoticeUserKey = new WechartNoticeUsersKey();
				webchartNoticeUserKey.setOpenid(notice.getOpenId());
				webchartNoticeUserKey.setFormid(notice.getFormId());

				WechartNoticeUsers wnUserDec = wechartNoticeUsersDao.selectByPrimaryKey(webchartNoticeUserKey);
				wnUserDec.setStatus((short) 1);
				wechartNoticeUsersDao.updateByPrimaryKey(wnUserDec);
				log.info("【合同审批结果推送】清除已消费formId={}及openId={}", notice.getOpenId(), notice.getFormId());
				return;

			}
			log.warn("【合同审批结果推送】推送失败!");
		} catch (Exception ex) {
			log.error("【合同审批结果推送】异常-->{}", ex);
		} finally {

		}
	}
}

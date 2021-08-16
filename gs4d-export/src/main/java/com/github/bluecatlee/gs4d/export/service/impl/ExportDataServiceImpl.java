package com.github.bluecatlee.gs4d.export.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.datasource.DataSourceContextHolder;
import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exchange.DataExportModel;
import com.github.bluecatlee.gs4d.common.utils.*;
import com.github.bluecatlee.gs4d.exchange.model.CommonQuery;
import com.github.bluecatlee.gs4d.exchange.model.ExcuteSqlResultModel;
import com.github.bluecatlee.gs4d.export.api.request.CommonExcuteBySqlIdRequest;
import com.github.bluecatlee.gs4d.export.api.request.DataCheckRequest;
import com.github.bluecatlee.gs4d.export.api.request.DataExportRequest;
import com.github.bluecatlee.gs4d.export.api.request.MessageCommonRefoundRequest;
import com.github.bluecatlee.gs4d.export.api.response.CommonExcuteBySqlIdResponse;
import com.github.bluecatlee.gs4d.export.api.response.DataCheckResponse;
import com.github.bluecatlee.gs4d.export.api.response.DataExportResponse;
import com.github.bluecatlee.gs4d.export.api.response.MessageCommonRefoundResponse;
import com.github.bluecatlee.gs4d.export.api.service.ExportDataService;
import com.github.bluecatlee.gs4d.export.dao.CommonQueryDao;
import com.github.bluecatlee.gs4d.export.dao.ExArcDocSystemDao;
import com.github.bluecatlee.gs4d.export.entity.COMMON_QUERY;
import com.github.bluecatlee.gs4d.export.service.CommonJsonQueryService;
import com.github.bluecatlee.gs4d.export.utils.ExportUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("exportDataService")
public class ExportDataServiceImpl implements ExportDataService {

	private static final Logger log = LoggerFactory.getLogger(ExportDataServiceImpl.class);

	private static MyJsonMapper mapper;

	static {
		mapper = new MyJsonMapper();
		mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}
 
	
	@Resource(name = "myJdbcTemplate")
	private MyJdbcTemplate jdbcTemplate;

	@Resource(name = "commonQueryDao")
	private CommonQueryDao commonQueryDao;

	@Resource
	private ExArcDocSystemDao exArcDocSystemDao;

//	@Resource(name = "exportTenantTaskBillDao")
//	private ExportTenantTaskBillDao exportTenantTaskBillDao;
//
//	@Resource(name = "exportTenantTaskDao")
//	private ExportTenantTaskDao exportTenantTaskDao;
//
//	@Resource(name = "assembleParamService")
//	private AssembleParamService assembleParamService;
//
//	@Resource(name = "exportTaskConfigContentDao")
//	private ExchangeTaskConfigContentDao exportTaskConfigContentDao;
//
//	@Resource(name = "exportTenantNotifyDao")
//	private ExportTenantNotifyDao exportTenantNotifyDao;
//
//	@Resource(name = "exportTenantTaskLogDao")
//	private ExportTenantTaskLogDao exportTenantTaskLogDao;
//
//	@Resource(name = "exportDataCallBackService")
//	private ExportDataCallBackService exportDataCallBackService;
//
//	@Resource(name = "exportTenantTaskBatchDao")
//	private ExportTenantTaskBatchDao exportTenantTaskBatchDao;
	
	@Resource(name="exportCommonJsonQueryService")
	private CommonJsonQueryService commonJsonQueryService;
	
//	@Resource(name="exportDataOperationService")
//	private ExportDataOperationService exportDataOperationService;
	
//	@Resource(name = "transactionManager")
//	private PlatformTransactionManager transactionManager;
//
//	@Resource
//	private ImportDataService importDataService;
//
//	@Value("#{settings['import.use.dubbo.sign']}")
//	private boolean importUseDubooSign;
//	@Resource
//	private ExportVerificateService exportVerificateService;
//
	@Value("#{settings['commom.query.cache.sign']}")
	private boolean commomQueryCacheSign;
	
//	@Resource
//	private ExportRecordLogDao exportRecordLogDao;
	
//	@Resource(name = "dynamicTransactionManager")
//	private PlatformTransactionManager dynamicTransactionManager;
	

//	@Override
//	public DataExportResponse exportData(DataExportRequest request) {
//		long startTime=System.currentTimeMillis();
//		Long importUseTime=0L;
//		DataExportResponse response = new DataExportResponse();
//		Long batchNumId  = null;
//		Long tenantTaskNumId = 0L;
//		Long sysTaskNumId = request.getSysTaskNumId();
//		try {
//			request.validate(ExportUtil.SUB_SYSTEM, ExceptionType.VCE10033);
//			EXPORT_TENANT_TASK exportTenantTask =  null;
//			if (request.getSysTaskNumId().equals(301L)||request.getSysTaskNumId().equals(302L)) {//sikaiqi 301,302 前期对接测试直接返回成功
//				return response;
//			}
////			String tenantTaskKey = sysTaskNumId+"#"+request.getTenantNumId()+"#"+request.getDataSign();
////			if(ExportUtil.tenantTaskMap.containsKey(tenantTaskKey)){
////				exportTenantTask = ExportUtil.tenantTaskMap.get(tenantTaskKey);
////			}else{
//				exportTenantTask = exportTenantTaskDao.getExportTenantTaskBySysTask(sysTaskNumId,request.getTenantNumId(),request.getDataSign());
////				ExportUtil.tenantTaskMap.put(tenantTaskKey, exportTenantTask);
////			}
//			long time1=System.currentTimeMillis()-startTime;
//			tenantTaskNumId = exportTenantTask.getTENANT_TASK_NUM_ID();
//
//			//获取TASK_CONFIG_CONTENT
//			//String configKey=  exportTenantTask.getCONTENT_SERIES()+"#"+request.getTenantNumId()+"#"+request.getDataSign();
//			JSONObject configJonModel = null;
//		/*	if(ExportUtil.configJsonMap.containsKey(configKey)){
//				configJonModel = ExportUtil.configJsonMap.get(configKey);
//			}else{*/
//				EXCHANGE_TASK_CONFIG_CONTENT exportTaskConfigContent =exportTaskConfigContentDao.getExportTenantTaskConfig(exportTenantTask.getCONTENT_SERIES(),request.getTenantNumId(),
//						request.getDataSign());
//				String configDtl = exportTaskConfigContent.getEXPORT_CONTENT();
//				configJonModel = JSONObject.fromObject(configDtl);
//			/*	ExportUtil.configJsonMap.put(configKey, configJonModel);
//			}*/
//
//			//产生批次并插入批次表
//			long time2=System.currentTimeMillis()-startTime-time1;
//			batchNumId = SeqUtil.getSeq(SeqUtil.EXPORT_BATCH_SERIES);
//			EXPORT_TENANT_TASK_BATCH batchModel = new EXPORT_TENANT_TASK_BATCH();
//			batchModel.setTENANT_TASK_NUM_ID(exportTenantTask.getTENANT_TASK_NUM_ID());
//			batchModel.setSYS_TASK_NUM_ID(request.getSysTaskNumId());
//			batchModel.setTENANT_NUM_ID(request.getTenantNumId());
//			batchModel.setDATA_SIGN(request.getDataSign());
//			batchModel.setBATCH_NUM_ID(batchNumId);
//			exportTenantTaskBatchDao.insertModel(batchModel);
//			long time3=System.currentTimeMillis()-startTime-time1-time2;
//			//记录原始数据
//			exportTenantTaskLogDao.updateOrgInputContent(batchNumId, tenantTaskNumId, request.getOrgInputJson().toString());
//
//			long time4=System.currentTimeMillis()-startTime-time1-time2-time3;
//			//获取通知方式
//			EXPORT_TENANT_NOTIFY  notifyModel = exportTenantNotifyDao.getModelBySeries(exportTenantTask.getNOTIFY_SERIES());
//			long time5=System.currentTimeMillis()-startTime-time1-time2-time3-time4;
//			if (importUseDubooSign) {
//
//			}
//			if (log.isDebugEnabled()) {
//				log.debug("通知方式:"+mapper.toJson(notifyModel));
//			}
//			//保存bill单号
//			exportDataCallBackService.saveExportBillNoUnThrowException(
//					batchNumId, request.getTenantNumId(),
//					request.getDataSign(),
//					exportTenantTask.getTENANT_TASK_NUM_ID(),
//					exportTenantTask.getSYS_TASK_NUM_ID(),
//					configJonModel,
//					request.getOrgInputJson().toString());
//			long time6=System.currentTimeMillis()-startTime-time1-time2-time3-time4-time5;
//
//			//导出验证
////			exportVerificateService.verificateExpotdata(exportTenantTask, request.getOrgInputJson(), configJonModel, batchNumId);
//			//获取数据
//			JSONObject json =  assembleParamService.assembleExportDataParam(exportTenantTask,
//					request.getOrgInputJson(), configJonModel, batchNumId);
//			json = JSONtoLowerTools.transObject(json);
//			if (log.isDebugEnabled()) {
//				log.debug("\n执行结果：\n"+json);
//			}
//			long time7=System.currentTimeMillis()-startTime-time1-time2-time3-time4-time5-time6;
//			//处理通知方式
//			//JSONObject newJson = new JSONObject();
//			//发送http请求通知导入方
//			if (request.getIsNotify().equals(0L)) {
//				if (importUseDubooSign) {
//					importUseTime=notifyImpPartyUseDubbo(exportTenantTask,null,batchNumId,notifyModel,response);
//				}else{
//					Map<String,Object> paramMap = new HashMap<String,Object>();
////					paramMap.put("batch_num_id", batchNumId);
//					paramMap.put("sys_task_num_id", request.getSysTaskNumId());
//					paramMap.put("org_input_json", json);
//					paramMap.put("data_sign", request.getDataSign());
//					paramMap.put("tenant_num_id", request.getTenantNumId());
//					this.notifyImpParty(exportTenantTask,JSONObject.fromObject(paramMap).toString(),String.valueOf(batchNumId),notifyModel);
//				}
//			}
//			if (log.isDebugEnabled()) {
//			}
//			log.info(";time1:"+time1+";time2:"+time2+";time3:"+time3+";time4:"+time4+";time5:"+time5+";time6:"+time6+";time7:"+time7+";批次:"+batchNumId);
//		} catch (Exception e) {
//			ExceptionUtil.processException(e, response);
//			if(batchNumId!=null){
//				try {
//					exportTenantTaskLogDao.updatefailContent(batchNumId, tenantTaskNumId, e.getMessage());
//				} catch (Exception e2) {
//					ExceptionUtil.processException(e2, response);
//				}
//			}
//		}
//		log.info("导出耗时:"+(System.currentTimeMillis()-startTime)+";dubbo调用耗时:"+importUseTime+";系统作业号:"+sysTaskNumId+";批次号:"+batchNumId);
//		return response;
//	}

	@Override
	public DataCheckResponse checkData(DataCheckRequest request) {
		DataCheckResponse response = new DataCheckResponse();
		DataExportModel model = null;
		int i = 1;
		try {
			request.validate(ExportUtil.SUB_SYSTEM, ExceptionType.VCE10033);
			JSONArray configJsonAry = ExportUtil.getJsonArray(request.getConfigDtl());
			for (; i <= configJsonAry.size(); i++) {
				model = mapper.fromJson(configJsonAry.get(i - 1).toString(), DataExportModel.class);
				ExportUtil.checkDataExportModel(model, i);
			}
		} catch (Exception e) {
			ExceptionUtil.processException(e, response);
		}
		return response;
	}

//	@Override
//	public ExportBillNoReceiptResponse receiptExportBillNo(ExportBillNoReceiptRequest request) {
//	  return exportDataCallBackService.receiptExportBillNo(request);
//	}
	
//	//把需要导出的数据通知到导入方
//	public  void notifyImpParty(EXPORT_TENANT_TASK model,String paramJson,String batchNumId, EXPORT_TENANT_NOTIFY  notifyModel) throws Exception {
//		//exportTenantTaskBatchDao.updateExportTimeAndExportSuccessFlag(String.valueOf(request.getTENANT_TASK_NUM_ID()), batchNumId, "N");
//		exportTenantTaskBatchDao.updateNotityTime(model.getTENANT_TASK_NUM_ID(), batchNumId);
////		Long notifySeries =  request.getNOTIFY_SERIES();
//
//		Long taskNumId=model.getTENANT_TASK_NUM_ID();
//		//1 是及时通知,2是自主抓取
//		Long notifyType = notifyModel.getNOTIFY_TYPE();
//		if(notifyType==1){
//			//1 直接http调用接收接口(及时通知),2 产生文件（及时通知）,3 dubbo直接调用接收服务   ,7 直接返回导出数据（自主抓取）',
//			Long notifySubType = notifyModel.getNOTIFY_SUB_TYPE();
//			if(notifySubType==1){
//				String httpUrl = notifyModel.getUSR_NOTIFY_URL();
//				if (log.isDebugEnabled()) {
//					log.debug("发送消息："+paramJson);
//				}
//				paramJson = ExportUtil.getEncodeBase64(paramJson);
//				//发送http请求
//				Map<String, String> paramMap = new HashMap<String, String>();
//				paramMap.put("cmd", "IM001");
//				paramMap.put("method", "ykcloud.cimport.importData");
//				paramMap.put("params", paramJson);
//				if (model.getDATA_SIGN()==1L) {
//					paramMap.put("app_key", "1001");
//				}else if(model.getDATA_SIGN()==0L){
//					paramMap.put("app_key", "8001");
//				}
//				if (log.isDebugEnabled()) {
//					log.debug("url:"+httpUrl+"参数："+mapper.toJson(paramMap));
//				}
//				String res;
//				try {
//					res = HttpUtil.sendHttpRequest(httpUrl, paramMap, "post");
//				} catch (Exception e) {
//					log.error("发送http请求失败"+e.getMessage(),e);
//					 throw new BusinessException(ExportUtil.SUB_SYSTEM,ExceptionType.BE40093, "发送http请求失败，失败信息"+e.getMessage());
//				}
//				//保存回调参数
//				exportTenantTaskLogDao.updateOrgCallBackInputContent(Long.valueOf(batchNumId),taskNumId,res);
//
//				JSONObject resObject = JSONObject.fromObject(res);
//				if(!"0".equals(resObject.get("code").toString())){
//					 throw new BusinessException(ExportUtil.SUB_SYSTEM,ExceptionType.BE40093, "导入方执行失败，失败信息"+resObject.get("message"));
//				}
//
//				exportTenantTaskBatchDao.updateExportTimeAndExportSuccessFlag(String.valueOf(model.getTENANT_TASK_NUM_ID()), batchNumId, "Y");
//				//回调
//				ExportBillNoReceiptRequest receiptRequest = mapper.fromJson(resObject.toString(), ExportBillNoReceiptRequest.class);
//				receiptRequest.setTenantNumId(model.getTENANT_NUM_ID());
//				receiptRequest.setDataSign(model.getDATA_SIGN());
//				receiptRequest.setTenantTaskNumId(model.getTENANT_TASK_NUM_ID());
//				receiptRequest.setBatchNumId(Long.valueOf(batchNumId));
//				//异步回写
//				receiptExportBillNo(receiptRequest);
//				/*Runnable task = new Runnable() {
//					@Override
//					public void run() {
//						receiptExportBillNo(receiptRequest);
//					}
//				};
//				ThreadPoolUtil.ThreadPool.submit(task);*/
//			}
//		}
//	}
	
//	//把需要导出的数据通知到导入方
//	private  Long notifyImpPartyUseDubbo(EXPORT_TENANT_TASK model,JSONObject paramJson,Long batchNumId, EXPORT_TENANT_NOTIFY  notifyModel,DataExportResponse dataExportResponse) throws Exception {
//		//exportTenantTaskBatchDao.updateExportTimeAndExportSuccessFlag(String.valueOf(request.getTENANT_TASK_NUM_ID()), batchNumId, "N");
//		long importUseTime=0L;
//		exportTenantTaskBatchDao.updateNotityTime(model.getTENANT_TASK_NUM_ID(), batchNumId.toString());
////		Long notifySeries =  request.getNOTIFY_SERIES();
//
//		Long sysTaskNumId=model.getSYS_TASK_NUM_ID();
//		//1 是及时通知,2是自主抓取
//		DataImportResponse response=null;
//		long start=0l;
//		try {
//			start = System.currentTimeMillis();
//			DataImportRequest request=new DataImportRequest();
//			request.setBatchNumId(Long.valueOf(batchNumId));
//			request.setDataSign(model.getDATA_SIGN());
////			request.setOrgInputJson(paramJson);
//			request.setSysTaskNumId(sysTaskNumId);
//			request.setTenantNumId(model.getTENANT_NUM_ID());
//			response=importDataService.importData(request);
//			importUseTime=System.currentTimeMillis() - start;
//		} catch (Exception e) {
//			log.error("dubbo调用失败"+e.getMessage(),e);
//			throw new BusinessException(ExportUtil.SUB_SYSTEM,ExceptionType.BE40093, "dubbo调用失败失败，失败信息"+e.getMessage());
//		}
//
//		if(response.getCode() != MessagePack.OK){
//			//导入系统 重复导入
//			if( response.getCode() == ExceptionType.BE40133.getCode()){
//				//至导出系统重复冲入
//				dataExportResponse.setCode(ExceptionType.BE40099.getCode());
//				dataExportResponse.setMessage("导入系统已经存在导入成功记录");
//			}else{
//				ExceptionUtil.checkDubboException(response);
//			}
//		}
//
//
//		//保存回调参数
//		exportTenantTaskLogDao.updateOrgCallBackInputContent(Long.valueOf(batchNumId),model.getTENANT_TASK_NUM_ID(),response.toLowerCaseJson());
//		long time1=System.currentTimeMillis()-start-importUseTime;
//
//		exportTenantTaskBatchDao.updateExportTimeAndExportSuccessFlag(String.valueOf(model.getTENANT_TASK_NUM_ID()), batchNumId.toString(), "Y");
//		long time2=System.currentTimeMillis()-start-importUseTime-time1;
//		if (log.isDebugEnabled()) {
//			log.info("time1:"+time1+";time2:"+time2);
//		}
//		//回调
//		//异步回写
//		if (response.getBillMapping()!=null&&!response.getBillMapping().isEmpty()) {
//			ExportBillNoReceiptRequest receiptRequest = new ExportBillNoReceiptRequest();
//			receiptRequest.setTenantNumId(model.getTENANT_NUM_ID());
//			receiptRequest.setDataSign(model.getDATA_SIGN());
//			receiptRequest.setTenantTaskNumId(model.getTENANT_TASK_NUM_ID());
//			receiptRequest.setBatchNumId(batchNumId);
//			receiptRequest.setRecvTime(response.getRecvTime());
//			receiptRequest.setBillMapping(response.getBillMapping());
//			Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				receiptExportBillNo(receiptRequest);
//				}
//			};
//			ThreadPoolUtil.ThreadPool.submit(task);
//		}
//		return importUseTime;
//	}

//	@Override
//	public CommonExcuteBySqlIdResponse commonExcuteBySqlId(
//			CommonExcuteBySqlIdRequest request) {
//		CommonExcuteBySqlIdResponse response = new CommonExcuteBySqlIdResponse();
//		try {
//			CommonQuery commonQuery = commonQueryDao.getModel(request.getSqlId(), request.getTenantNumId(),request.getDataSign());
//			Long currentPage = 0L;
//			if("OMP-EXPORT-1".equals(request.getSqlId())){
//				JSONObject  inputParam = request.getInputParam();
//				String taskName = inputParam.optString("task_name");
//				String sql = 
//						"SELECT E.*,S.SYS_NAME,S.SYS_NUM_ID FROM " +
//						"  EXPORT_TENANT_TASK E " + 
//						"INNER JOIN EXPORT_SYS_TASK S ON S.SYS_TASK_NUM_ID = E.SYS_TASK_NUM_ID " + 
//						" WHERE S.SYS_NUM_ID = ?";
//				if(StringUtil.isNullOrBlankTrim(taskName)){
//					commonQuery.setSqlContent(sql);
//					JSONArray paramContenArray = new JSONArray();
//					JSONObject paramContenObject = new JSONObject();
//					paramContenObject.put("NAME", "SYS_NUM_ID");
//					paramContenObject.put("TYPE", "NUMBER");
//					paramContenObject.put("PARAMSOURCE", "INPUT");
//					paramContenObject.put("MUSTHAVE", "FALSE");
//					paramContenArray.add(paramContenObject);
//					commonQuery.setParamContent(paramContenArray.toString());
//				}else {
//					sql+= "  AND E.TASK_NAME LIKE ?";
//					commonQuery.setSqlContent(sql);
//					JSONArray paramContenArray = new JSONArray();
//					JSONObject paramContenObject = new JSONObject();
//					paramContenObject.put("NAME", "SYS_NUM_ID");
//					paramContenObject.put("TYPE", "NUMBER");
//					paramContenObject.put("PARAMSOURCE", "INPUT");
//					paramContenObject.put("MUSTHAVE", "FALSE");
//					paramContenArray.add(paramContenObject);
//					paramContenObject.clear();
//					paramContenObject.put("NAME", "TASK_NAME");
//					paramContenObject.put("TYPE", "STRING");
//					paramContenObject.put("PARAMSOURCE", "INPUT");
//					paramContenObject.put("MUSTHAVE", "FALSE");
//					paramContenArray.add(paramContenObject);
//					commonQuery.setParamContent(paramContenArray.toString());
//				}
//			}else if("OMP-EXPORT-6".equals(request.getSqlId())){
//				//编辑导出作业类
//				exportDataOperationService.editExportDataJdbc(request.getInputParam());
//				return response;
//			}else if("OMP-EXPORT-10".equals(request.getSqlId())){
//				//增加作业类
//				exportDataOperationService.addExportDataJdbc(request.getInputParam());
//				return response;
//			}else if("OMP-EXPORT-11".equals(request.getSqlId())){
//				//删除作业任务
//				exportDataOperationService.deletExportDataJdbc(request.getInputParam());
//				return response;
//			}else if("OMP-EXPORT-MONITOR-1".equals(request.getSqlId())){
//				//导出监控导出批次查询
//				String  sql =null; 
//				JSONObject  inputParam = request.getInputParam();
//				String taskName = inputParam.optString("task_name");
//				String billNo = inputParam.optString("bill_no");
//				if(StringUtil.isNullOrBlankTrim(taskName)){
//					sql ="SELECT * FROM EXPORT_TENANT_TASK_BATCH B" +
//									" INNER JOIN EXPORT_TENANT_TASK_BILL E" + 
//									"    ON B.BATCH_NUM_ID = E.BATCH_NUM_ID" + 
//									" WHERE E.BILL_NO = ?";
//					commonQuery.setSqlContent(sql);
//					JSONArray paramContenArray = new JSONArray();
//					JSONObject paramContenObject = new JSONObject();
//					paramContenObject.put("NAME", "BILL_NO");
//					paramContenObject.put("TYPE", "NUMBER");
//					paramContenObject.put("PARAMSOURCE", "INPUT");
//					paramContenObject.put("MUSTHAVE", "FALSE");
//					paramContenArray.add(paramContenObject);
//					commonQuery.setParamContent(paramContenArray.toString());
//				}else if(StringUtil.isNullOrBlankTrim(billNo)){
//					sql = "SELECT * FROM EXPORT_TENANT_TASK_BATCH B" +
//							" INNER JOIN EXPORT_TENANT_TASK T" + 
//							"    ON B.TENANT_TASK_NUM_ID = T.TENANT_TASK_NUM_ID" + 
//							" WHERE T.TASK_NAME LIKE ?";
//					commonQuery.setSqlContent(sql);
//					JSONArray paramContenArray = new JSONArray();
//					JSONObject paramContenObject = new JSONObject();
//					paramContenObject.put("NAME", "TASK_NAME");
//					paramContenObject.put("TYPE", "STRING");
//					paramContenObject.put("PARAMSOURCE", "INPUT");
//					paramContenObject.put("MUSTHAVE", "FALSE");
//					paramContenArray.add(paramContenObject);
//					commonQuery.setParamContent(paramContenArray.toString());
//				}else {
//					 throw new BusinessException(ExportUtil.SUB_SYSTEM,ExceptionType.BE40093, "单据号和导出作业名称不可以同时为空！");
//				}
//			}else if("OMP-EXPORT-MONITOR-2".equals(request.getSqlId())){
//				//导出监控导出单据查询
//				JSONObject  inputParam = request.getInputParam();
//				currentPage = inputParam.optLong("page_size");
//				if(currentPage == null || currentPage<=0){
//					currentPage=1L;
//				}
//				commonQuery.setPageSize(10L);
//			}else if("OMP-EXPORT-MONITOR-3".equals(request.getSqlId())){
//				//导出监控导出日志查询
//				JSONObject  inputParam = request.getInputParam();
//				currentPage = inputParam.optLong("page_size");
//				if(currentPage == null || currentPage<=0){
//					currentPage=1L;
//				}
//				commonQuery.setPageSize(10L);
//			}else if("OMP-SQL-CONFIG-1".equals(request.getSqlId())){
//				//导出（导入）配置sql查询
//				JSONObject  inputParam = request.getInputParam();
//				String sql_name = inputParam.optString("sql_name");
//				String sql_id = inputParam.optString("sql_id");
//				String sql = "SELECT *  FROM  COMMON_QUERY C  WHERE 1 = 1 ";
//				JSONArray paramContenArray = new JSONArray();
//				JSONObject paramContenObject = new JSONObject();
//				if(!StringUtil.isNullOrBlankTrim(sql_name)){
//					sql += "AND C.SQL_NAME LIKE ?";
//					paramContenObject.put("NAME", "SQL_NAME");
//					paramContenObject.put("TYPE", "STRING");
//					paramContenObject.put("PARAMSOURCE", "INPUT");
//					paramContenObject.put("MUSTHAVE", "FALSE");
//					paramContenArray.add(paramContenObject);
//				}else if(!StringUtil.isNullOrBlankTrim(sql_id)){
//					sql += " AND C.SQL_ID = ?";
//					paramContenObject.put("NAME", "SQL_ID");
//					paramContenObject.put("TYPE", "STRING");
//					paramContenObject.put("PARAMSOURCE", "INPUT");
//					paramContenObject.put("MUSTHAVE", "FALSE");
//					paramContenArray.add(paramContenObject);
//				}
//				commonQuery.setParamContent(paramContenArray.toString());
//				commonQuery.setSqlContent(sql);
//				currentPage = inputParam.optLong("page_size");
//				if(currentPage == null || currentPage<=0){
//					currentPage=1L;
//				}
//				commonQuery.setPageSize(10L);
//			}else if("OMP-SQL-CONFIG-3".equals(request.getSqlId())){
//				//导出（导入）配置sql
//				JSONObject  inputParam = request.getInputParam();
//				COMMON_QUERY common_query = new COMMON_QUERY();
//				common_query.setSERIES(inputParam.optLong("series"));
//				common_query.setSQL_ID(inputParam.optString("sql_id"));
//				common_query.setSQL_NAME(inputParam.optString("sql_name"));
//				common_query.setSQL_CONTENT(inputParam.optString("sql_content"));
//				common_query.setPARAM_CONTENT(inputParam.optString("param_content"));
//				common_query.setJDBC_NAME(inputParam.optString("jdbc_name"));
//				common_query.setDB_TYPE(inputParam.optString("db_type"));
//				commonQueryDao.updateModelBySeries(common_query);
//				return response;
//			}else if("OMP-NOTIFY-3".equals(request.getSqlId())){
//				//
//				JSONObject  inputParam = request.getInputParam();
//				EXPORT_TENANT_NOTIFY request = new EXPORT_TENANT_NOTIFY();
//				request.setSERIES(inputParam.optString("series"));
//				request.setNOTIFY_NAME(inputParam.optString("notify_name"));
//				request.setNOTIFY_TYPE(inputParam.optLong("notify_type"));
//				request.setNOTIFY_SUB_TYPE(inputParam.optLong("notify_sub_type"));
//				request.setUSR_NOTIFY_URL(inputParam.optString("usr_notify_url"));
//				request.setFILE_STORE_TYPE(inputParam.optLong("file_store_type"));
//				request.setFILE_TYPE(inputParam.optLong("file_type"));
//				request.setFTP_ADDRESS(inputParam.optString("ftp_address"));
//				request.setFTP_USR_NAME(inputParam.optString("ftp_usr_name"));
//				request.setFTP_PASSWORD(inputParam.optString("ftp_password"));
//				
//				request.setHDFS_ADDRESS(inputParam.optString("hdfs_address"));
//				request.setHDFS_USR_NAME(inputParam.optString("hdfs_usr_name"));
//				request.setHDFS_PASSWORD(inputParam.optString("hdfs_password"));
//				//request.setNOTIFY_NAME(inputParam.optString("notify_name"));
//				exportTenantNotifyDao.updateModelBySeries(request);
//				return response;
//			}else if("OMP-NOTIFY-4".equals(request.getSqlId())){
//				//
//				JSONObject  inputParam = request.getInputParam();
//				EXPORT_TENANT_NOTIFY request = new EXPORT_TENANT_NOTIFY();
//				//request.setSERIES(inputParam.optString("series"));
//				request.setNOTIFY_NAME(inputParam.optString("notify_name"));
//				request.setNOTIFY_TYPE(inputParam.optLong("notify_type"));
//				request.setNOTIFY_SUB_TYPE(inputParam.optLong("notify_sub_type"));
//				request.setUSR_NOTIFY_URL(inputParam.optString("usr_notify_url"));
//				request.setFILE_STORE_TYPE(inputParam.optLong("file_store_type"));
//				request.setFILE_TYPE(inputParam.optLong("file_type"));
//				request.setFTP_ADDRESS(inputParam.optString("ftp_address"));
//				request.setFTP_USR_NAME(inputParam.optString("ftp_usr_name"));
//				request.setFTP_PASSWORD(inputParam.optString("ftp_password"));
//				
//				request.setHDFS_ADDRESS(inputParam.optString("hdfs_address"));
//				request.setHDFS_USR_NAME(inputParam.optString("hdfs_usr_name"));
//				request.setHDFS_PASSWORD(inputParam.optString("hdfs_password"));
//				request.setCREATE_USER_ID(1L);
//				request.setLAST_UPDATE_USER_ID(1L);
//				request.setTENANT_NUM_ID(request.getTenantNumId());
//				request.setDATA_SIGN(request.getDataSign());
//				exportTenantNotifyDao.addModel(request);
//				return response;
//			}
//			if (request.getDataSourceName()==null||request.getDataSourceName().isEmpty()) {
//				request.setDataSourceName(commonQuery.getJdbcName());
//			}
//			ExcuteSqlResultModel resultModel = commonJsonQueryService.excuteSqlById(currentPage,
//					request.getDataSourceName(), commonQuery,
//					request.getInputParam(), request.getTenantNumId(),request.getDataSign());
//			response.setPageCount(resultModel.getPageCount());
//			response.setRecordCount(resultModel.getRecordCount());
//			
// 			response.setData(resultModel.getData());
//			response.setSql(resultModel.getSql());
//			response.setArg(resultModel.getArg());
//		} catch (Exception e) {
//			ExceptionUtil.processException(e, response);
//		}
//		return response;
//	}

	@Override
	public CommonExcuteBySqlIdResponse commonExcuteBySqlId(CommonExcuteBySqlIdRequest request) {
		CommonExcuteBySqlIdResponse response = new CommonExcuteBySqlIdResponse();
//		log.info("commonExcuteBySqlId request:"+mapper.toJson(request));
		try {
			long start = System.currentTimeMillis();
			List<String> sqlidList = new ArrayList<String>();
//			Long recordLogSeries = null; 
//			if (SeqUtil.recordSign) {
//				EXPORT_RECORD_LOG entity = new EXPORT_RECORD_LOG();
//				entity.setDATA_SIGN(request.getDataSign());
//				entity.setPARAM(request.getInputParam().toString());
//				entity.setSQL_ID(request.getSqlId());
//				entity.setTENANT_NUM_ID(request.getTenantNumId());
//				recordLogSeries = exportRecordLogDao.insertEntity(entity);
//			}
			commonExcuteBySqlId(request, response, sqlidList);
			long time = System.currentTimeMillis() - start ;
//			if (SeqUtil.recordSign) {
//				exportRecordLogDao.updateRecordBySeries(request.getTenantNumId(), request.getDataSign(), recordLogSeries, response.getRecordCount(), time);
//			}
			if (time >= 1000) {
				log.info("通用查询耗时超过一秒 sqlId:" + request.getSqlId() + "查询时间:" + time + "参数:" + request.getInputParam().toString() + ",返回集合大小:" + response.getRecordCount());
			}
		} catch (Exception e) {
			ExceptionUtil.processException(e, response);
		}
		return response;
	}

	// 根据sqlId及参数进行通用查询的核心方法
	private void commonExcuteBySqlId(CommonExcuteBySqlIdRequest request, CommonExcuteBySqlIdResponse response, List<String> sqlidList) {
		CommonQuery commonQuery;
		if (commomQueryCacheSign) {	// 使用缓存
			commonQuery = commonJsonQueryService.getCommonQuery(request.getTenantNumId(), request.getDataSign(), request.getSqlId());
		} else {
			commonQuery = commonQueryDao.getModelWithTenant(request.getSqlId(), request.getTenantNumId(), request.getDataSign());
			if (commonQuery==null) {
				commonQuery = commonQueryDao.getModelNoTenant(request.getSqlId(), request.getDataSign());
			}
		}
		if (log.isDebugEnabled()) {
			log.info(mapper.toJson(commonQuery));
		}
		response.setSqlName(commonQuery.getSqlName());
		Long currentPage = 0L;
		if (request.getInputParam() == null) {
			request.setInputParam(new HashMap<String, Object>());
		}
		request.getInputParam().put("tenant_num_id", request.getTenantNumId());
		request.getInputParam().put("data_sign", request.getDataSign());
		JSONObject inputParam = new JSONObject();
		inputParam.putAll(request.getInputParam());

		if ("OMP-EXPORT-1".equals(request.getSqlId())) {
//			String taskName = inputParam.optString("task_name");
//			String sql =
//					"SELECT E.*,S.SYS_NAME,S.SYS_NUM_ID FROM EXPORT_TENANT_TASK E " +
//					"INNER JOIN EXPORT_SYS_TASK S ON S.SYS_TASK_NUM_ID = E.SYS_TASK_NUM_ID " +
//					"WHERE S.SYS_NUM_ID = ?";
//			if (StringUtil.isNullOrBlankTrim(taskName)) {
//				commonQuery.setSqlContent(sql);
//				JSONArray paramContenArray = new JSONArray();
//				JSONObject paramContenObject = new JSONObject();
//				paramContenObject.put("NAME", "SYS_NUM_ID");
//				paramContenObject.put("TYPE", "NUMBER");
//				paramContenObject.put("PARAMSOURCE", "INPUT");
//				paramContenObject.put("MUSTHAVE", "FALSE");
//				paramContenArray.add(paramContenObject);
//				commonQuery.setParamContent(paramContenArray.toString());
//			} else {
//				sql += " AND E.TASK_NAME LIKE ?";
//				commonQuery.setSqlContent(sql);
//				JSONArray paramContenArray = new JSONArray();
//				JSONObject paramContenObject = new JSONObject();
//				paramContenObject.put("NAME", "SYS_NUM_ID");
//				paramContenObject.put("TYPE", "NUMBER");
//				paramContenObject.put("PARAMSOURCE", "INPUT");
//				paramContenObject.put("MUSTHAVE", "FALSE");
//				paramContenArray.add(paramContenObject);
//				paramContenObject.clear();
//				paramContenObject.put("NAME", "TASK_NAME");
//				paramContenObject.put("TYPE", "STRING");
//				paramContenObject.put("PARAMSOURCE", "INPUT");
//				paramContenObject.put("MUSTHAVE", "FALSE");
//				paramContenArray.add(paramContenObject);
//				commonQuery.setParamContent(paramContenArray.toString());
//			}
//		} else if ("OMP-EXPORT-6".equals(request.getSqlId())) {
//			//编辑导出作业类
//			exportDataOperationService.editExportDataJdbc(inputParam);
//			return;
//		} else if ("OMP-EXPORT-10".equals(request.getSqlId())) {
//			//增加作业类
//			exportDataOperationService.addExportDataJdbc(inputParam);
//			return;
//		} else if ("OMP-EXPORT-11".equals(request.getSqlId())) {
//			//删除作业任务
//			exportDataOperationService.deletExportDataJdbc(inputParam);
//			return;
//		} else if ("OMP-EXPORT-MONITOR-1".equals(request.getSqlId())) {
//			//导出监控导出批次查询
//			String  sql =null;
//			String taskName = inputParam.optString("task_name");
//			String billNo = inputParam.optString("bill_no");
//			if (StringUtil.isNullOrBlankTrim(taskName)) {
//				sql ="SELECT * FROM EXPORT_TENANT_TASK_BATCH B" +
//								" INNER JOIN EXPORT_TENANT_TASK_BILL E" +
//								"    ON B.BATCH_NUM_ID = E.BATCH_NUM_ID" +
//								" WHERE E.BILL_NO = ?";
//				commonQuery.setSqlContent(sql);
//				JSONArray paramContenArray = new JSONArray();
//				JSONObject paramContenObject = new JSONObject();
//				paramContenObject.put("NAME", "BILL_NO");
//				paramContenObject.put("TYPE", "NUMBER");
//				paramContenObject.put("PARAMSOURCE", "INPUT");
//				paramContenObject.put("MUSTHAVE", "FALSE");
//				paramContenArray.add(paramContenObject);
//				commonQuery.setParamContent(paramContenArray.toString());
//			} else if(StringUtil.isNullOrBlankTrim(billNo)) {
//				sql = "SELECT * FROM EXPORT_TENANT_TASK_BATCH B" +
//						" INNER JOIN EXPORT_TENANT_TASK T" +
//						"    ON B.TENANT_TASK_NUM_ID = T.TENANT_TASK_NUM_ID" +
//						" WHERE T.TASK_NAME LIKE ?";
//				commonQuery.setSqlContent(sql);
//				JSONArray paramContenArray = new JSONArray();
//				JSONObject paramContenObject = new JSONObject();
//				paramContenObject.put("NAME", "TASK_NAME");
//				paramContenObject.put("TYPE", "STRING");
//				paramContenObject.put("PARAMSOURCE", "INPUT");
//				paramContenObject.put("MUSTHAVE", "FALSE");
//				paramContenArray.add(paramContenObject);
//				commonQuery.setParamContent(paramContenArray.toString());
//			} else {
//				 throw new BusinessException(ExportUtil.SUB_SYSTEM,ExceptionType.BE40093, "单据号和导出作业名称不可以同时为空！");
//			}
//		} else if ("OMP-EXPORT-MONITOR-2".equals(request.getSqlId())) {
//			//导出监控导出单据查询
//			currentPage = inputParam.optLong("page_size");
//			if(currentPage == null || currentPage<=0){
//				currentPage=1L;
//			}
//			commonQuery.setPageSize(10L);
//		} else if ("OMP-EXPORT-MONITOR-3".equals(request.getSqlId())) {
//			//导出监控导出日志查询
//			currentPage = inputParam.optLong("page_size");
//			if (currentPage == null || currentPage<=0) {
//				currentPage=1L;
//			}
//			commonQuery.setPageSize(10L);
		} else if("OMP-SQL-CONFIG-1".equals(request.getSqlId())) {
			//导出（导入）配置sql查询
			String sql_name = inputParam.optString("sql_name");
			String sql_id = inputParam.optString("sql_id");
			String sql = "SELECT *  FROM  COMMON_QUERY C  WHERE 1 = 1 ";
			JSONArray paramContenArray = new JSONArray();
			JSONObject paramContenObject = new JSONObject();
			if(!StringUtil.isNullOrBlankTrim(sql_name)){
				sql += "AND C.SQL_NAME LIKE ?";
				paramContenObject.put("NAME", "SQL_NAME");
				paramContenObject.put("TYPE", "STRING");
				paramContenObject.put("PARAMSOURCE", "INPUT");
				paramContenObject.put("MUSTHAVE", "FALSE");
				paramContenArray.add(paramContenObject);
			}else if(!StringUtil.isNullOrBlankTrim(sql_id)){
				sql += " AND C.SQL_ID = ?";
				paramContenObject.put("NAME", "SQL_ID");
				paramContenObject.put("TYPE", "STRING");
				paramContenObject.put("PARAMSOURCE", "INPUT");
				paramContenObject.put("MUSTHAVE", "FALSE");
				paramContenArray.add(paramContenObject);
			}
			commonQuery.setParamContent(paramContenArray.toString());
			commonQuery.setSqlContent(sql);
			currentPage = inputParam.optLong("page_size");
			if(currentPage == null || currentPage<=0){
				currentPage=1L;
			}
			commonQuery.setPageSize(10L);
		} else if("OMP-SQL-CONFIG-3".equals(request.getSqlId())) {
			//导出（导入）配置sql
			COMMON_QUERY common_query = new COMMON_QUERY();
			common_query.setSERIES(inputParam.optLong("series"));
			common_query.setSQL_ID(inputParam.optString("sql_id"));
			common_query.setSQL_NAME(inputParam.optString("sql_name"));
			common_query.setSQL_CONTENT(inputParam.optString("sql_content"));
			common_query.setPARAM_CONTENT(inputParam.optString("param_content"));
			common_query.setJDBC_NAME(inputParam.optString("jdbc_name"));
			common_query.setDB_TYPE(inputParam.optString("db_type"));
			commonQueryDao.updateModelBySeries(common_query);
			return;
//		} else if ("OMP-NOTIFY-3".equals(request.getSqlId())) {
//			//
//			EXPORT_TENANT_NOTIFY model = new EXPORT_TENANT_NOTIFY();
//			model.setSERIES(inputParam.optString("series"));
//			model.setNOTIFY_NAME(inputParam.optString("notify_name"));
//			model.setNOTIFY_TYPE(inputParam.optLong("notify_type"));
//			model.setNOTIFY_SUB_TYPE(inputParam.optLong("notify_sub_type"));
//			model.setUSR_NOTIFY_URL(inputParam.optString("usr_notify_url"));
//			model.setFILE_STORE_TYPE(inputParam.optLong("file_store_type"));
//			model.setFILE_TYPE(inputParam.optLong("file_type"));
//			model.setFTP_ADDRESS(inputParam.optString("ftp_address"));
//			model.setFTP_USR_NAME(inputParam.optString("ftp_usr_name"));
//			model.setFTP_PASSWORD(inputParam.optString("ftp_password"));
//
//			model.setHDFS_ADDRESS(inputParam.optString("hdfs_address"));
//			model.setHDFS_USR_NAME(inputParam.optString("hdfs_usr_name"));
//			model.setHDFS_PASSWORD(inputParam.optString("hdfs_password"));
//			//request.setNOTIFY_NAME(inputParam.optString("notify_name"));
//			exportTenantNotifyDao.updateModelBySeries(model);
//			return;
//		} else if ("OMP-NOTIFY-4".equals(request.getSqlId())) {
//			//
//			EXPORT_TENANT_NOTIFY model = new EXPORT_TENANT_NOTIFY();
//			//request.setSERIES(inputParam.optString("series"));
//			model.setNOTIFY_NAME(inputParam.optString("notify_name"));
//			model.setNOTIFY_TYPE(inputParam.optLong("notify_type"));
//			model.setNOTIFY_SUB_TYPE(inputParam.optLong("notify_sub_type"));
//			model.setUSR_NOTIFY_URL(inputParam.optString("usr_notify_url"));
//			model.setFILE_STORE_TYPE(inputParam.optLong("file_store_type"));
//			model.setFILE_TYPE(inputParam.optLong("file_type"));
//			model.setFTP_ADDRESS(inputParam.optString("ftp_address"));
//			model.setFTP_USR_NAME(inputParam.optString("ftp_usr_name"));
//			model.setFTP_PASSWORD(inputParam.optString("ftp_password"));
//
//			model.setHDFS_ADDRESS(inputParam.optString("hdfs_address"));
//			model.setHDFS_USR_NAME(inputParam.optString("hdfs_usr_name"));
//			model.setHDFS_PASSWORD(inputParam.optString("hdfs_password"));
//			model.setCREATE_USER_ID(1L);
//			model.setLAST_UPDATE_USER_ID(1L);
//			model.setTENANT_NUM_ID(request.getTenantNumId());
//			model.setDATA_SIGN(request.getDataSign());
//			exportTenantNotifyDao.addModel(model);
//			return;
		}
		if (request.getDataSourceName() == null || request.getDataSourceName().isEmpty()) {
			request.setDataSourceName(commonQuery.getJdbcName());
		}
		if (request.getPageSize() != null) {
			commonQuery.setPageSize(request.getPageSize());
		}
		if (request.getPageNum() != null) {
			currentPage = request.getPageNum();
		}
		ExcuteSqlResultModel resultModel = new ExcuteSqlResultModel();
		if (log.isDebugEnabled()) {
			log.info("inputParam:" + mapper.toJson(request.getInputParam()));
		}
		// 先处理子查询  通过子查询进行参数转换
		if (commonQuery.getSqlFlag().equals(CommonQuery.SQL_FLAG_INSERT)) {// 这里应该是实现 insert into select，但是只支持insert一条语句(即select子查询只有一条数据)
			if (commonQuery.getSubSqlId() != null && !commonQuery.getSubSqlId().isEmpty()) {
				String[] sqlId = commonQuery.getSubSqlId().split(",");
				for (int j = 0; j < sqlId.length; j++) {
					CommonExcuteBySqlIdRequest subRequest = new CommonExcuteBySqlIdRequest();
					subRequest.setTenantNumId(request.getTenantNumId());
					subRequest.setDataSign(request.getDataSign());
					subRequest.setSubRuerySign(2L);
					subRequest.setSqlId(sqlId[j]);
					subRequest.setInputParam(inputParam);
					subRequest.setCount(0);
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					list.add(new HashMap<String, Object>());
					response.setResults(list);
					commonExcuteBySqlId(subRequest, response, sqlidList);
					inputParam.putAll(response.getResults().get(0));
				}
			}
		}
		if (request.getSubRuerySign().equals(2L)) {
			commonQuery.setSubQuerySign(2L);
		}
		sqlidList.add(commonQuery.getSqlId());
		resultModel = commonJsonQueryService.excuteSqlById(currentPage, request.getDataSourceName(), commonQuery, inputParam, request.getTenantNumId(), request.getDataSign());
		if (commonQuery.getNoDataException().equals("Y") && resultModel.getRecordCount() < 1L) {
			throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40203, "通过sqlId:" + request.getSqlId() + "没有查询到记录!");
		}
		if (log.isDebugEnabled()) {
			log.info("inputParam:" + mapper.toJson(resultModel));
		}
		if (request.getSubRuerySign().equals(1L)) {
			response.setPageCount(resultModel.getPageCount());
			response.setRecordCount(resultModel.getRecordCount());
		}
		if (request.getSubRuerySign().equals(2L)) {
			if (resultModel.getRecordCount() > 1L) { // 子查询多条记录报错
				 throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40202, "数据异常！子查询记录存在多条");
			}
			if (!resultModel.getData().isEmpty()) {
				response.getResults().get(request.getCount()).putAll(resultModel.getData().get(0));
			}
			if (commonQuery.getSubSqlId() != null && !commonQuery.getSubSqlId().isEmpty()) {	// 这里应该是实现select语句中的子查询嵌套子查询
				JSONObject jsonObject = new JSONObject();
				jsonObject.putAll(resultModel.getData().get(0));
				jsonObject.put("tenant_num_id", request.getTenantNumId());
				jsonObject.put("data_sign", request.getDataSign());
				String[] sqlId = commonQuery.getSubSqlId().split(",");
				for (int j = 0; j < sqlId.length; j++) {
					CommonExcuteBySqlIdRequest subRequest = new CommonExcuteBySqlIdRequest();
					subRequest.setTenantNumId(request.getTenantNumId());
					subRequest.setDataSign(request.getDataSign());
					subRequest.setSubRuerySign(2L);
					subRequest.setInputParam(jsonObject);
					subRequest.setSqlId(sqlId[j]);
					subRequest.setCount(request.getCount());
					commonExcuteBySqlId(subRequest, response, sqlidList);
				}
			}
		} else if (request.getSubRuerySign().equals(1L)) {
			response.setResults(new ArrayList(Long.valueOf(resultModel.getCount()).intValue()));
			for (int i = 0; i < resultModel.getCount(); i++) {
				if (resultModel.getSqlFlag().equals(CommonQuery.SQL_FLAG_INSERT) || resultModel.getSqlFlag().equals(CommonQuery.SQL_FLAG_UPDATE)) {

				} else {
					response.getResults().add(resultModel.getData().get(i));
				}
				if (commonQuery.getSubSqlId() != null && !commonQuery.getSubSqlId().isEmpty()) {	// 这里应该是实现 select语句中的子查询
					JSONObject jsonObject = new JSONObject();
					jsonObject.putAll(resultModel.getData().get(i));
					jsonObject.put("tenant_num_id", request.getTenantNumId());
					jsonObject.put("data_sign", request.getDataSign());
					String[] sqlId = commonQuery.getSubSqlId().split(",");
					for (int j = 0; j < sqlId.length; j++) {
						CommonExcuteBySqlIdRequest subRequest = new CommonExcuteBySqlIdRequest();
						subRequest.setTenantNumId(request.getTenantNumId());
						subRequest.setDataSign(request.getDataSign());
						subRequest.setSubRuerySign(2L);
						subRequest.setInputParam(jsonObject);
						subRequest.setSqlId(sqlId[j]);
						subRequest.setCount(i);
						commonExcuteBySqlId(subRequest, response, sqlidList);
					}
				}
			}
			if (log.isDebugEnabled()) {
				log.info("inputParam:" + mapper.toJson(response));
			}
		}
		response.setSqlFlag(resultModel.getSqlFlag());
		response.setSql(resultModel.getSql());
		response.setArg(resultModel.getArg());
	}

	@Override
	public MessageCommonRefoundResponse messageCommonRefound(MessageCommonRefoundRequest messageCommonRefoundRequest) {
		if (log.isDebugEnabled()) {
			log.debug("begin messageCommonRefound request:{}", mapper.toJson(messageCommonRefoundRequest));
		}

		MessageCommonRefoundResponse messageCommonRefoundResponse = new MessageCommonRefoundResponse();

		try {
			messageCommonRefoundRequest.validate(ExportUtil.SUB_SYSTEM, ExceptionType.VCE10034);
			Long tenantNumId = messageCommonRefoundRequest.getTenantNumId();
			Long dataSign = messageCommonRefoundRequest.getDataSign();
			Long sysNumId = messageCommonRefoundRequest.getSysNumId();
			String msgSeries = messageCommonRefoundRequest.getMsgSeries();
			String datasourceName;
			if (StringUtil.isAllNotNullOrBlank(new String[]{messageCommonRefoundRequest.getDatasouceName()})) {
				datasourceName = messageCommonRefoundRequest.getDatasouceName();
			} else {
				datasourceName = this.exArcDocSystemDao.getDatasourceNameBySysNumId(tenantNumId, dataSign, sysNumId);
			}

			String series = this.exArcDocSystemDao.refindSysMsgTransRefindId(msgSeries, datasourceName);
			if (series == null) {
				throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40160, "事务消息回查消息序号查询失败!消息序号:" + msgSeries);
			}
		} catch (Throwable e) {
			ExceptionUtil.processException(e, messageCommonRefoundResponse);
		}

		if (log.isDebugEnabled()) {
			log.debug("end messageCommonRefound return:{}", messageCommonRefoundResponse.toLowerCaseJson());
		}

		return messageCommonRefoundResponse;
	}

	/*
	private EXPORT_TENANT_TASK getExportTenantTask(Long tenantNumId,Long dataSign,Long sysTaskNumId){
		String methodName=.ykcloud.export.tenant.task.get";
		String key=methodName+"_"+tenantNumId+"_"+dataSign+"_"+sysTaskNumId;
		String json=stringRedisTemplate.opsForValue().get(key);
		EXPORT_TENANT_TASK exportTenantTask;
		if (json!=null){
			exportTenantTask=gson.fromJson(json, EXPORT_TENANT_TASK.class); 
			if (log.isDebugEnabled()){
				log.debug("getExportTenantTask use cache");
			}					
		} else {
			exportTenantTask = exportTenantTaskDao.getExportTenantTaskBySysTask(sysTaskNumId,
					tenantNumId, dataSign);	
			json=gson.toJson(exportTenantTask);
			stringRedisTemplate.opsForValue().set(key,json,24,TimeUnit.HOURS);//一天
		}
		return exportTenantTask;
	}
	
	private EXCHANGE_TASK_CONFIG_CONTENT getExchangeTaskConfigContent(Long tenantNumId,Long dataSign,Long contentSeries){
		String methodName=.ykcloud.export.exchange.task.config.content.get";
		String key=methodName+"_"+tenantNumId+"_"+dataSign+"_"+contentSeries;
		String json=stringRedisTemplate.opsForValue().get(key);
		EXCHANGE_TASK_CONFIG_CONTENT exportTaskConfigContent;
		if (json!=null){
			exportTaskConfigContent=gson.fromJson(json, EXCHANGE_TASK_CONFIG_CONTENT.class); 
			if (log.isDebugEnabled()){
				log.debug("getExportTenantTask use cache");
			}					
		} else {
			exportTaskConfigContent = exportTaskConfigContentDao.getExportTenantTaskConfig(contentSeries,tenantNumId,dataSign);			
			json=gson.toJson(exportTaskConfigContent);
			stringRedisTemplate.opsForValue().set(key,json,24,TimeUnit.HOURS);//一天
		}
		return exportTaskConfigContent;
	}*/

//	@Override
//	public CommonBatchUpdateExcuteResponse excuteCommonBatchUpdate(CommonBatchUpdateExcuteRequest request) {
//		CommonBatchUpdateExcuteResponse response = new CommonBatchUpdateExcuteResponse();
//		TransactionStatus status =null;
//		try {
//			if (log.isDebugEnabled()) {
//				log.debug(mapper.toJson(request));
//			}
//			request.validate(ExportUtil.SUB_SYSTEM, ExceptionType.VCE10033);
//			String dataSource = null;
//			if (StringUtil.isNullOrBlankTrim(request.getDataSourceName())) {
//				CommonQuery commonQuery = commonQueryDao.getModelNoTenant(request.getInputParams().get(0).getSqlId(), request.getDataSign());
//				dataSource = commonQuery.getJdbcName();
//			}else{
//				dataSource = request.getDataSourceName();
//			}
//			DataSourceContextHolder.setDataSourceType(dataSource);
//			status = dynamicTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition());
//			List<CommonBatchInsert> inputParams = request.getInputParams();
//			for (CommonBatchInsert param : inputParams) {
//				for (int i = 0; i < param.getParamList().size(); i++) {
//					CommonExcuteBySqlIdRequest excuteRequest = new CommonExcuteBySqlIdRequest();
//					excuteRequest.setTenantNumId(request.getTenantNumId());
//					excuteRequest.setDataSign(request.getDataSign());
//					excuteRequest.setInputParam(param.getParamList().get(i));
//					excuteRequest.setSqlId(param.getSqlId());
//					CommonExcuteBySqlIdResponse excuteResponse = commonExcuteBySqlId(excuteRequest);
//					ExceptionUtil.checkDubboException(excuteResponse);
//				}
//			}
//			dynamicTransactionManager.commit(status);
//		} catch (Exception e) {
//			if (status!=null) {
//				dynamicTransactionManager.rollback(status);
//			}
//			ExceptionUtil.processException(e, response);
//		}
//		return response;
//	}


}

package com.github.bluecatlee.gs4d.export.utils;

import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.exchange.AddParamModel;
import com.github.bluecatlee.gs4d.common.exchange.DataExportModel;
import com.github.bluecatlee.gs4d.common.utils.DateUtil;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.exchange.model.CommonQuery;
import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Pattern;

public class ExportUtil {

	public static final String SUB_SYSTEM = "export";
	
	
//	public static Map<String,EXPORT_TENANT_TASK> tenantTaskMap = new ConcurrentHashMap<String,EXPORT_TENANT_TASK>();
	
//	public static Map<String,JSONObject> configJsonMap = new ConcurrentHashMap<String,JSONObject>();

	public static String sqlHandler(String sqlValue, List<Map<String, Object>> returnParamMap, CommonQuery gq, JSONArray paramsJsonArray) {
		// sqlValue = unnecessaryParamAssembly(sqlValue, paramMapList);
		// sqlValue = pagingAssembly(sqlValue, gq);
		// sqlValue = parsingTheKeyword(sqlValue, paramsJsonArray);
		return sqlValue;
	}

	/*
	 * 创建参数对应关系
	 */
	public static List<Map<String, Object>> getParamsWithMap(String sqlId, JSONArray gqParamJsonArray, JSONArray paramsJsonArray, String dataType) {
		List<Map<String, Object>> paramMapList = new LinkedList<Map<String, Object>>();

		// 传递来的参数，如果有多条的话,对其进行遍历
		for (int j = 0; j < paramsJsonArray.size(); j++) {
			JSONObject jsonOb = paramsJsonArray.getJSONObject(j);
			// 对参数进行设定
			Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
			Map<String, Integer> paramTypeMap = new LinkedHashMap<String, Integer>();
			// 数据库中该条SQL对应的参数
			for (int i = 0; i < gqParamJsonArray.size(); i++) {
				JSONObject jo = gqParamJsonArray.getJSONObject(i);
				String paramsource = jo.getString("PARAMSOURCE");
				String name = jo.getString("NAME").trim();
				String type = jo.getString("TYPE");
				Object value = "";
				int valueType = 0;
				boolean mustHave = jo.getBoolean("MUSTHAVE");
				try {
					if (jsonOb.containsKey(name)) {
						if ("INPUT".equals(paramsource)) {
							switch (type) {
								case "STRING":
									if (jsonOb.getString(name).trim().equals("null")) {
										value = (jo.get("DEFAULT") != null) ? jo.getString("DEFAULT") : null;
									} else {
										value = jsonOb.getString(name);
									}
									valueType = java.sql.Types.VARCHAR;
									break;
								case "DATETIME":
									if (isNumeric(jsonOb.getString(name))) {
										value = DateUtil.format(new Date(jsonOb.getLong(name)), DateUtil.DATETIME_FORMAT_PATTERN);
									} else if (jsonOb.getString(name).trim().equals("null") || jsonOb.getString(name).trim().equals("")) {
										value = null;
									} else if (jsonOb.get(name).toString().contains("time")) {
										value = DateUtil.format(new Date(JSONObject.fromObject(jsonOb.get(name)).getLong("time")), DateUtil.DATETIME_FORMAT_PATTERN);
									} else {
										value = jsonOb.get(name);
									}
									valueType = java.sql.Types.TIMESTAMP;
									break;
								case "DATE":
									if (isNumeric(jsonOb.getString(name))) {
										value = DateUtil.format(new Date(jsonOb.getLong(name)), DateUtil.DATE_FORMAT_PATTERN);
									} else if (jsonOb.getString(name).trim().equals("null") || jsonOb.getString(name).trim().equals("")) {
										value = null;
									} else if (jsonOb.get(name).toString().contains("time")) {
										value = DateUtil.format(new Date(JSONObject.fromObject(jsonOb.get(name)).getLong("time")), DateUtil.DATE_FORMAT_PATTERN);
									} else {
										value = jsonOb.get(name);
									}
									valueType = java.sql.Types.DATE;
									break;
								case "DECIMAL":
									if (jsonOb.getString(name).trim().equals("null")) {
										value = null;
									} else {
										value = jsonOb.getString(name);
									}
									if (dataType.equals("MySQL")) {
										valueType = java.sql.Types.DOUBLE;
									} else {
										valueType = java.sql.Types.DECIMAL;
									}
									break;
								case "NUMBER":
									if (jsonOb.getString(name).trim().equals("null")) {
										value = (jo.get("DEFAULT") != null) ? Long.valueOf(jsonOb.getString(name)) : null;
									} else {
										value = Long.valueOf(jsonOb.getString(name));
									}
									valueType = java.sql.Types.BIGINT;
									break;
								default:
									value = jsonOb.getString(name);
									valueType = java.sql.Types.VARCHAR;
									break;
							}
						}
					} else if (paramsource.equals("0")) {
						String[] systemName_SeqName = name.split("_");
						value = SeqGetUtil.getNoSubSequence(systemName_SeqName[0], systemName_SeqName[1]);
						switch (type) {
							case "STRING":
								valueType = java.sql.Types.VARCHAR;
								break;
							case "NUMBER":
								valueType = java.sql.Types.BIGINT;
								break;
							case "DATETIME":
								valueType = java.sql.Types.TIMESTAMP;
								break;
							case "DATE":
								valueType = java.sql.Types.DATE;
								break;
							case "DECIMAL":
								if (dataType.equals("MySQL")) {
									valueType = java.sql.Types.DOUBLE;
								} else {
									valueType = java.sql.Types.DECIMAL;
								}
								break;
						}
					} else {
						value = null;
						valueType = java.sql.Types.VARCHAR;
					}
					if ((jsonOb.get(name) != null) && !jsonOb.getString(name).equals(" ") && StringUtil.isNullOrBlankTrim(jsonOb.optString(name)) && mustHave) {
						throw new ValidateClientException(ExportUtil.SUB_SYSTEM, ExceptionType.VBE20033, "sqlId为" + sqlId + "的查询中,缺少参数(值):" + name);
					}
					String key = name;
					if (paramMap.containsKey(key)) {
						key = name + "_" + UUID.randomUUID();
					}
					paramMap.put(key, value);
					paramTypeMap.put(key, valueType);
				} catch (Exception e) {
					throw new ValidateBusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.VBE20033, "name:" + name + ",参数为:" + paramsJsonArray + ",详细信息" + e.getMessage());
				}
			}
			if (paramMap.size() != 0) {
				paramMapList.add(paramMap);
			}
		}

		return paramMapList;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	public static JSONObject getJsonObject(String originalJson){
		JSONObject valueObj = null;
		try {
			valueObj =  JSONObject.fromObject(originalJson); 
		} catch (Exception e) {
			throw new ValidateBusinessException(ExportUtil.SUB_SYSTEM,
					ExceptionType.VBE20033, "入参原始数据Json格式不正确");
		}
		return valueObj;
	}

	public static JSONArray getJsonArray(String configDtl){
		JSONArray jsonAry = null;
		try {
			jsonAry =  JSONArray.fromObject(configDtl); 
		} catch (Exception e) {
			throw new ValidateBusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.VBE20033, "入参配置数据Json格式不正确");
		}
		return jsonAry;
	}

	public static void checkDataExportModel(DataExportModel model, int k) {
		if (model.getProccessType() != null && !model.getProccessType().equals(1L) && !model.getProccessType().equals(0L)) {
			throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40093, "导出服务入参配置的第" + k + "条配置的导出方式proccess_type不为1或者0");
		}
		if (model.getDataType() != null && !model.getDataType().equals(0L) && !model.getDataType().equals(1L)) {
			throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40093, "导出服务入参配置的第" + k + "条配置的执行结果类型data_type不为1或者0");
		}
		if (model.getAddParam() != null && !model.getAddParam().isEmpty()) {
			AddParamModel outputModel = null;
			for (int j = 0; j < model.getAddParam().size(); j++) {
				outputModel = model.getAddParam().get(j);
				if (!AddParamModel.FROM_TYPE_OF_INPUT.equals(outputModel.getFromType()) && !AddParamModel.FROM_TYPE_OF_SEQ.equals(outputModel.getFromType())) {
					throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40093, "导出服务入参配置的第" + k + "条配置的第" + j + "条addOutputParam的来源类型from_type不为input或seq");
				}
			}
		}
		if (model.getChildExport() != null && !model.getChildExport().isEmpty()) {
			try {
				for (int i = 0; i < model.getChildExport().size(); i++) {
					DataExportModel childExport = model.getChildExport().get(i);
					checkDataExportModel(childExport, i + 1);
				}
			} catch (Exception e) {
				throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40093, "导出服务入参配置的第" + k + "条配置的ChildExport配置的" + e.getMessage());
			}
		}
	}

	//Base64加密
	public  static String getEncodeBase64(String s) {
		if (s == null) {
			return null;
		}
		try {
			return new String(new Base64().encode(s.getBytes()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

//	public static String changeDataSource(Long dataSign){
//		if (dataSign.equals(1L)) {
//			return "omsDataSource";
//		}
//		else if(dataSign.equals(0L)){
//			return "omsProductionDataSource";
//		}
//		return null;
//	}

}

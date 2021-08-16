package com.github.bluecatlee.gs4d.exchange.utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.sql.Timestamp;

public class JsonObjectUtil {

    private static MyJsonMapper mapper = new MyJsonMapper();

    static {
        mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public static JsonConfig getJsonConfig() {
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor());
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor());
        config.registerJsonValueProcessor(java.sql.Date.class, new DateJsonValueProcessor());
        return config;
    }

    public static void main(String[] args) {
        String jsonStr = "{\n" +
                "\t\"username\": \"wu\",\n" +
                "\t\"id\": 1,\n" +
                "\t\"cars\": [{\n" +
                "\t\t\"id\": 2,\n" +
                "\t\t\"color\": \"red\"\n" +
                "\t}, {\n" +
//                "\t\t\"id\": 3,\n" +
                "\t\t\"color\": \"black\"\n" +
                "\t}]\n" +
                "}";
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
//        Object jsonObject1 = getJsonObject(jsonObject, "cars.id");
//        System.out.println(jsonObject1);
        Object jsonObject2 = getJsonObjectNoException(jsonObject, "cars.id");
        System.out.println(jsonObject2);
    }

    /**
     * 以'字段.字段'的方式获取值
     * @param json
     * @param config
     * @return
     */
    public static Object getJsonObject(JSONObject json, String config) {
        String[] con = config.split("\\.");
        return getObject(json, con, 0);
    }

    // 这个方法不能脱离调用者单独看... 不然没意义
    private static Object getObject(JSONObject jsonObj, String[] con, int k) {
        Object object = null;
        Object conObj = jsonObj.get(con[k]);
        if (conObj == null) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20033, "入参数据：" + jsonObj + "中，节点为" + con[k] + "的对象为空");
        }
        String str = conObj.toString();
        JSONArray childJsonAry = new JSONArray();
        String childStr = null;
        if (str.startsWith("[")) {  // 如果值是数组
            JSONArray jsonAry = (JSONArray)jsonObj.get(con[k]);
            for (Object obj : jsonAry) {
                if (con.length > k + 1) { // 递归处理里层数据
                    Object childObj = getObject(JSONObject.fromObject(obj), con, k + 1);
                    childStr = childObj.toString();
                    if (childStr.startsWith("[")) {
                        JSONArray ary = (JSONArray)childObj;
                        for (Object object2 : ary) {
                            childJsonAry.add(object2);
                        }
                        continue;
                    }
                    childJsonAry.add(childObj);
                    continue;
                }
                childJsonAry.add(obj);
            }
            object = childJsonAry;
        } else if (str.startsWith("{")) {   // 如果值是对象
            JSONObject o = (JSONObject)jsonObj.get(con[k]);
            if (con.length > k + 1) {
                Object childObj = getObject(o, con, k + 1);
                childStr = childObj.toString();
                if (childStr.startsWith("[")) {
                    JSONArray ary = (JSONArray)childObj;
                    for (Object object2 : ary)
                        childJsonAry.add(object2);
                    object = childJsonAry;
                } else {
                    object = childObj;
                }
            } else {
                object = o;
            }
        } else {
            object = str;
        }
        return object;
    }

    public static Object getJsonObjectNoException(JSONObject json, String config) {
        String[] con = config.split("\\.");
        return getObjectnNoException(json, con, 0);
    }

    private static Object getObjectnNoException(JSONObject jsonObj, String[] con, int k) {
        Object object = null;
        JSONArray childJsonAry = new JSONArray();
        Object conObj = jsonObj.get(con[k]);
        if (conObj == null)
            return null;
        String str = conObj.toString();
        String childStr = null;
        if (str.startsWith("[")) {
            JSONArray jsonAry = (JSONArray)jsonObj.get(con[k]);
            for (Object obj : jsonAry) {
                if (con.length > k + 1) {
                    Object childObj = getObjectnNoException(JSONObject.fromObject(obj), con, k + 1);
                    if (childObj == null) {
                        childJsonAry.add(childObj);
                        continue;
                    }
                    childStr = childObj.toString();
                    if (childStr.startsWith("[")) {
                        JSONArray ary = (JSONArray)childObj;
                        for (Object object2 : ary) {
                            childJsonAry.add(object2);
                        }
                        continue;
                    }
                    childJsonAry.add(childObj);
                    continue;
                }
                childJsonAry.add(obj);
            }
            object = childJsonAry;
        } else if (str.startsWith("{")) {
            JSONObject o = (JSONObject)jsonObj.get(con[k]);
            if (con.length > k + 1) {
                Object childObj = getObjectnNoException(o, con, k + 1);
                if (childObj == null) {
                    object = childObj;
                } else {
                    childStr = childObj.toString();
                    if (childStr.startsWith("[")) {
                        JSONArray ary = (JSONArray)childObj;
                        for (Object object2 : ary) {
                            childJsonAry.add(object2);
                        }
                        object = childJsonAry;
                    } else {
                        object = childObj;
                    }
                }
            } else {
                object = o;
            }
        } else {
            object = str;
        }
        return object;
    }

}


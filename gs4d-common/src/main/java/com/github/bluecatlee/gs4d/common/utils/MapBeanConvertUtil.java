package com.github.bluecatlee.gs4d.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

public class MapBeanConvertUtil {

    static {
        // 注册日期转换器
        ConvertUtils.register(new Converter() {
            public Object convert(Class arg0, Object arg1) {
                if (arg1 == null) {
                    return null;
                } else if (!(arg1 instanceof String)) {
                    throw new ConversionException("只支持字符串转换 !");
                } else {
                    String str = (String)arg1;
                    try {
                        return str.trim().equals("") ? null : DateUtil.parse(str, DateUtil.DATETIME_FORMAT_PATTERN);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }, Date.class);
    }

    public static <T> T map2Bean(Class<T> clazz, Map<String, Object> map) {
        try {
            Iterator iterator;
            Object key;
            if (clazz.equals(String.class)) {
                iterator = map.keySet().iterator();
                if (iterator.hasNext()) {
                    key = iterator.next();
                    return (T)map.get(key).toString();
                }
            } else if (clazz.equals(Long.class)) {
                iterator = map.keySet().iterator();
                if (iterator.hasNext()) {
                    key = iterator.next();
                    return (T)Long.valueOf(map.get(key).toString());
                }
            } else if (clazz.equals(Double.class)) {
                iterator = map.keySet().iterator();
                if (iterator.hasNext()) {
                    key = iterator.next();
                    return (T)Double.valueOf(map.get(key).toString());
                }
            }

            HashMap<String, Object> newMap = new HashMap();
            Iterator entryIterator = map.entrySet().iterator();

            while(entryIterator.hasNext()) {
                Entry<String, Object> entry = (Entry)entryIterator.next();
                String keyCamel = CamelUnderlineUtil.underlineToCamel((String)entry.getKey());
                newMap.put(keyCamel, entry.getValue());
            }

            key = clazz.newInstance();
            BeanUtils.populate(key, newMap);
            return (T)key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> Map<String, Object> bean2Map(T javaBean) {
        HashMap map = new HashMap();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null && propertyDescriptors.length > 0) {
                String propertyName = null;
                Object propertyValue = null;
                PropertyDescriptor[] pdArr = propertyDescriptors;
                int len = propertyDescriptors.length;

                for(int i = 0; i < len; ++i) {
                    PropertyDescriptor pd = pdArr[i];
                    propertyName = pd.getName();
                    if (!propertyName.equals("class")) {
                        Method readMethod = pd.getReadMethod();
                        propertyValue = readMethod.invoke(javaBean);
                        map.put(propertyName, propertyValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * List<Map>转List<Bean>
     * @param clazz
     * @param mapList
     * @param <T>
     * @return
     */
    public static <T> List<T> map2BeanForList(Class<T> clazz, List<Map<String, Object>> mapList) {
        if (mapList != null && !mapList.isEmpty()) {
            List<T> objectList = new ArrayList();
            T object = null;
            Iterator iterator = mapList.iterator();

            while(iterator.hasNext()) {
                Map<String, Object> map = (Map)iterator.next();
                if (map != null) {
                    object = map2Bean(clazz, map);
                    objectList.add(object);
                }
            }

            return objectList;
        } else {
            return null;
        }
    }

}

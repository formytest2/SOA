package com.github.bluecatlee.gs4d.common.utils.excel;

import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class ClassUtil {

    public ClassUtil() {
    }

    public static Object getInstance(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> cls = Class.forName(name);
        return cls.newInstance();
    }

    /**
     * 输出一个对象的反射出的构造、属性、方法信息
     * @param obj
     */
    public static void reflect(Object obj) {
        Class<?> cls = obj.getClass();
        System.out.println("************构  造  器************");
        Constructor<?>[] constructors = cls.getConstructors();
        Constructor[] constructorArr = constructors;
        int len = constructors.length;

        int i;
        for(i = 0; i < len; ++i) {
            Constructor<?> constructor = constructorArr[i];
            System.out.println("构造器名称:" + constructor.getName() + "\t    构造器参数类型:" + Arrays.toString(constructor.getParameterTypes()));
        }

        System.out.println("************属     性************");
        Field[] fields = cls.getDeclaredFields();
        Field[] fieldArr = fields;
        i = fields.length;

        int j;
        for(j = 0; j < i; ++j) {
            Field field = fieldArr[j];
            System.out.println("属性名称:" + field.getName() + "\t属性类型:" + field.getType() + "\t");
        }

        System.out.println("************方   法************");
        Method[] methods = cls.getMethods();
        Method[] methodArr = methods;
        j = methods.length;

        for(int k = 0; k < j; ++k) {
            Method method = methodArr[k];
            System.out.println("方法名:" + method.getName() + "\t方法返回类型：" + method.getReturnType() + "\t方法参数类型:" + Arrays.toString(method.getParameterTypes()));
        }

    }

    /**
     * 获取字段值
     *      优先从父类中获取字段值
     *      对于java.util.Date时间类型 进行格式化
     * @param obj
     * @param filedname
     * @param cls
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static Object getFieldValue(Object obj, String filedname, Class<?> cls) throws IllegalAccessException, NoSuchMethodException {
        Class<?> superclass = cls.getSuperclass();
        if(superclass != null) {
           Object result  = getFieldValue(obj, filedname, superclass);
           if(result != null && result != ""){
               return result;
           }
        }
        Field[] fields = cls.getDeclaredFields();
        Object val = "";
        Field[] fieldArr = fields;
        int len = fields.length;

        for(int i = 0; i < len; ++i) {
            Field field = fieldArr[i];
            if (field.getName().equals(filedname)) {
                field.setAccessible(true);
                val = field.get(obj);
                Class typeClass = field.getType();
                if (val != null && val.toString() != "" && typeClass.getName().equals("java.util.Date")) {
                    String firstChar = field.getName().substring(0, 1).toUpperCase();
                    Method m = obj.getClass().getMethod("get" + firstChar + field.getName().substring(1), (Class[])null);
                    if (m != null) {
                        DateTimeFormat format = (DateTimeFormat)m.getAnnotation(DateTimeFormat.class);
                        String pattern = "yyyy-MM-dd";
                        if (format != null && format.pattern() != "") {
                            pattern = format.pattern();
                        }
                        DateFormat dateFormat = new SimpleDateFormat(pattern);
                        val = dateFormat.format(val);
                    }
                }
                break;
            }
        }

        return val;
    }

    /**
     * 获取List中元素的所有字段值，转成List<TreeMap<String, Object>>
     *      List中的元素必须是同类型的
     * @param data
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static List<TreeMap<String, Object>> setTreeMap(List<? extends Object> data) throws NoSuchMethodException, IllegalAccessException {
        List<TreeMap<String, Object>> dataList = new ArrayList();
        Class obj = data.get(0).getClass();
        Field[] fs = obj.getDeclaredFields();

        for(int j = 0; j < fs.length; ++j) {
            Field field = fs[j];
            field.setAccessible(true);
            Class typeClass = field.getType();
            DateFormat dateFormat = null;
            if (typeClass.getName().equals("java.util.Date")) {
                String firstChar = field.getName().substring(0, 1).toUpperCase();
                Method m = obj.getMethod("get" + firstChar + field.getName().substring(1), (Class[])null);
                if (m != null) {
                    DateTimeFormat format = (DateTimeFormat)m.getAnnotation(DateTimeFormat.class);
                    String pattern = "yyyy-MM-dd";
                    if (format != null && format.pattern() != "") {
                        pattern = format.pattern();
                    }
                    dateFormat = new SimpleDateFormat(pattern);
                }
            }

            for(int i = 0; i < data.size(); ++i) {
                Object val = field.get(data.get(i));
                if (typeClass.getName().equals("java.util.Date") && dateFormat != null) {
                    val = dateFormat.format(val);
                }

                if (j == 0) {
                    TreeMap<String, Object> treeMap = new TreeMap();
                    treeMap.put(fs[j].getName(), val);
                    dataList.add(treeMap);
                } else {
                    ((TreeMap)dataList.get(i)).put(fs[j].getName(), val);
                }
            }
        }

        return dataList;
    }

    /**
     * 反射调用获取方法返回值
     * @param obj
     * @param methodName
     * @param paramTypes
     * @param params
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object readObjMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> cls = obj.getClass();
        Method method = cls.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        Object val = method.invoke(obj, params);
        return val;
    }

    /**
     * 通过反射创建对象 并把参数数组的值赋值到对应字段上
     *      参数数组中每个参数的格式： 字段名,字段值
     *      根据类型字符串的hash值来判断类型是一个好思路
     *      注意字段值如果是小数的处理
     * @param c
     * @param args
     * @return
     */
    public static Object getInstanceByArgs(Class c, String... args) {
        try {
            Object object = Class.forName(c.getName()).newInstance();
            Class<?> obj = object.getClass();
            Field[] fields = obj.getDeclaredFields();

            for(int i = 0; i < fields.length; ++i) {
                fields[i].setAccessible(true);

                for(int j = 0; j < args.length; ++j) {
                    String str = args[j];
                    if (str != null) {
                        String[] strs = str.split(",");
                        if (strs[0].equals(fields[i].getName())) {
                            String type = fields[i].getGenericType().toString();
                            byte flag = -1;
                            switch(type.hashCode()) {
                                case -1561781994:
                                    if (type.equals("class java.util.Date")) {
                                        flag = 3;
                                    }
                                    break;
                                case -1066470206:
                                    if (type.equals("class java.lang.Integer")) {
                                        flag = 1;
                                    }
                                    break;
                                case 673016845:
                                    if (type.equals("class java.lang.String")) {
                                        flag = 0;
                                    }
                                    break;
                                case 1335156652:
                                    if (type.equals("class java.lang.Boolean")) {
                                        flag = 2;
                                    }
                            }

                            switch(flag) {
                                case 0:
                                    fields[i].set(object, strs[1].toString());
                                    break;
                                case 1:
                                    Integer value = strs[1].toString().contains(".0") ? (int)Double.parseDouble(strs[1].toString()) : Integer.valueOf(strs[1].toString());
                                    fields[i].set(object, value);
                                    break;
                                case 2:
                                    fields[i].set(object, Boolean.valueOf(strs[1].toString()));
                                    break;
                                case 3:
                                    fields[i].set(object, Date.valueOf(strs[1].toString()));
                                    break;
                                default:
                                    fields[i].set(object, strs[1].toString());
                            }
                        }
                    }
                }
            }

            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

}

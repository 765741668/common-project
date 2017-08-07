package com.yz.common.core.utils;


import java.util.Date;

/**
 * Created by yangzhao on 17/8/2.
 */
public class ClassUtil {

    /**
     * 对象属性解析生成对应类型的值
     * @param tClass
     * @param v
     * @param <T>
     * @return
     */
    public static  <T> T parse(Class<T> tClass,Object v){
        String simpleName = tClass.getSimpleName();
        if (simpleName.equals("int")||simpleName.equals("Integer")){
            return (T) Integer.valueOf(v.toString());
        }
        if (simpleName.equals("long")||simpleName.equals("Long")){
            return (T) Long.valueOf(v.toString());
        }
        if (simpleName.equals("String")){
            return (T) v.toString();
        }
        if (simpleName.equals("double")||simpleName.equals("Double")){
            return (T) Double.valueOf(v.toString());
        }
        if (simpleName.equals("Date")){
            try {
                long l = Long.parseLong(v.toString());
                return (T) new Date(l);
            }catch (Exception e){
                return (T) v;
            }
        }
        return null;
    }

    /**
     * 生成方法名称
     * @param symbol 在属性名前添加的标识符
     * @param fieldName 属性名
     * @return
     */
    public static String createFieldMethodName(String symbol,String fieldName){
        int length = fieldName.length();
        if (length==1){
            return symbol+fieldName.toUpperCase();
        }
        String substring = fieldName.substring(1, 2);
        char c = substring.charAt(0);
        if (Character.isUpperCase(c)){
            return symbol+fieldName;
        }
        String s = StringUtils.firstCharToUpper(fieldName);
        return symbol+s;
    }
}

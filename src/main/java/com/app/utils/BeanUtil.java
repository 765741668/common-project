package com.app.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BeanUtil {

	private static final Logger logger = LogManager.getLogger(BeanUtil.class);

	@SuppressWarnings("rawtypes")
	public static <T> T getBean(Map map, Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		T t = null;
		try {
			t = clazz.newInstance();
			for (Field field : fields) {
				String fieldName = field.getName();
				String methodName = fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1, fieldName.length()));
				Method method = clazz.getMethod("set".concat(methodName), field.getType());
				String fieldType = field.getType().getSimpleName();
				try {
					if (fieldType.equals("Date")) {
						long time = (long) map.get(fieldName);
						method.invoke(t, time == 0 ? null : DateUtils.getDateByTimeStamp(time));
					} else {
						method.invoke(t, map.get(fieldName));
					}
				} catch (Exception e) {
					logger.error("属性：  " + fieldName + "----赋值出错", e);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}

	public static <T> T getBean(HttpServletRequest request, Class<T> clazz) {
		Enumeration<String> keys = request.getParameterNames();
		T t = null;
		try {
			t = clazz.newInstance();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
//				System.out.println(key);
				if(StringUtils.isEmpty(key)){
					break;
				}
				Field field = clazz.getDeclaredField(key);
				if(field==null){
					break;
				}
				String methodName = "set" + key.substring(0, 1).toUpperCase().concat(key.substring(1));
				Method method = clazz.getDeclaredMethod(methodName, field.getType());
				String typeName = field.getType().getSimpleName();
				String data=request.getParameter(key);
				if (typeName.equals("int") || typeName.equals("java.lang.Integer")) {
					int i = 0;
					if (!StringUtils.isEmpty(data)) {
						i = Integer.parseInt(data);
					}
					method.invoke(t, i);
				} else if (typeName.equals("Date")) {
					Date date = null;
					if (!StringUtils.isEmpty(data)) {
						date = DateUtils.parseDate(data.toString(), "yyyy-MM-dd hh:mm:ss");
					}
					method.invoke(t, date);
				} else if (typeName.equals("long") || typeName.equals("Long")) {
					long l = 0;
					if (!StringUtils.isEmpty(data)) {
						l = Long.parseLong(data);
					}
					method.invoke(t, l);
				} else if (typeName.equals("String")||typeName.equals("java.lang.String")) {
					method.invoke(t, StringUtils.isEmpty(data) ? null : data);
				} else if (typeName.equals("double") || typeName.equals("Double")) {
					double d = 0;
					if (!StringUtils.isEmpty(data)) {
						d = Double.parseDouble(data);
					}
					method.invoke(t, d);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return t;
	}
}

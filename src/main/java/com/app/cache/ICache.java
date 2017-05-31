package com.app.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 缓存处理接口
 * @author yangzhao 2015年10月16日
 */
public interface ICache {
	
	static final Logger logger = LogManager.getLogger(ICache.class);

	/** ---------------------String-----------------------------**/

	/**
	 *
	 * @param key
	 * @param value
     */
	public void set(String key,String value);

	public void set(String key,Object object);

	/**
	 *
	 * @param key
	 * @param seconds 设置过期时间
	 * @param value
     */
	public void set(String key,int seconds,String value);

	public void set(String key,int seconds,Object object);

	public String get(String key);

	public <T> T get(String key,Class<T> tClass);

	/** ---------------------Hash-------------------------------**/

	/**
	 * 设置hash值
	 * 
	 * @param mname
	 *            模块名
	 * @param key
	 *            hash key
	 * @param value
	 */
	public void hashSet(String mname, String key, Object value);

	/**
	 * 如果原来map里已经有了，则附加到map里去
	 * @param mname
	 * @param map
	 */
	public void hashSet(String mname, Map<String, Object> map);

	/**
	 * 从hash里得到指定key的value值
	 * @param <T>
	 * 
	 * @param mname
	 *            模块名
	 * @param key
	 *            hash key
	 */
	public <T> T hashGet(String mname, String key,Class<T> clazz);
	
	/**
	 * 从hash中通过key得到指定的value然后听过传入的Class返回对应的对象
	 * @param mname
	 * @param key
	 * @param collectionClass
	 * @param elementClasses
     * @return
     */
	@SuppressWarnings("rawtypes")
	public Collection hashGet(String mname, String key, Class collectionClass, Class elementClasses);

	/**
	 *
	 * @param mname
	 * @param key
     * @return
     */
	public String hashGet(String mname, String key);

	/**
	 * 返回哈希表key中一个或多个给定域的值。
	 * @param mname
	 * @param keys
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> List<T> hashGet(String mname, String [] keys, Class<T> clazz);

	/**
	 * 得到所有数据
	 * 
	 * @param mname
	 *            模块名
	 */
	public Map<String, Object> hashGet(String mname);

	/**
	 * 移除
	 * 
	 * @param mname
	 *            模块名
	 * @param key
	 *            hash key
	 */
	public void hashRemove(String mname, String key);

	/**
	 * 移除
	 * 
	 * @param mname
	 *            模块名
	 */
	public void hashRemove(String mname);

	/*******
	 * ---------------------List-------------------------------
	 **********/
	/**
	 * 设置list值
	 * 
	 * @param mname
	 *            模块名
	 * @param value
	 */
	public void listAdd(String mname, String...value);

	/**
	 * 设置整个list，如果原来已经存在，则附加在原来的list里
	 * @param mname
	 * @param list
	 * @param <T>
	 */
	public <T> void listAdd(String mname, List<T> list);
	
	public void listAdd(String mname, Object obj);
	
	public <T> void removeListAdd(String mname,List<T> list);

	/**
	 * 根据分页情况，得到list
	 * 
	 * @param mname
	 *            模块名
	 * @param pageNo
	 *            第几页, 从1开始计数
	 * @param pageSize
	 *            每页多少条
	 */
	public List<String> listGet(String mname, int pageNo, int pageSize);

	/**
	 * 是否存在对应mname
	 * 
	 * @param mname
	 * @return
	 */
	public boolean listIsExist(String mname);

	public <T> boolean listKeyReplaceValue(String mname,int index,T t);
	
	/**
	 * 得到整个list
	 * @param <T>
	 * 
	 * @param mname
	 * @return
	 */
	public <T> List<T> listGet(String mname,Class<T> clazz);

	/**
	 * 通过属性名和值 从List中获取该对象
	 * 
	 * @param mname
	 *            模块名
	 * @param field
	 *            属性名
	 * @param value
	 *            值
	 * @param clazz
	 * @return
	 */
	public default <T> List<T> listGet(String mname, String field, Object value, Class<T> clazz) {

		List<T> t  = this.listGet(mname,clazz);

		if (t==null||t.isEmpty()){
			return null;
		}

		List<T> result = t.stream().filter((obj) -> {
			boolean flag = false;
			String methodName = field.substring(0, 1).toUpperCase() + field.substring(1);
			try {
				Class<?> aClass = obj.getClass();
				Method method = aClass.getMethod("get" + methodName);
				Object data = method.invoke(obj);
				Field f = aClass.getDeclaredField(field);
				if (f.getName().equals(field)) {
					String fieldTypeName = f.getType().getSimpleName();
					if (fieldTypeName.equals("int") || fieldTypeName.equals("Integer")) {
						if (data instanceof Integer) {
							if ((int) data == (int) value) {
								flag = true;
							}
						}
					} else if (fieldTypeName.equals("Date")) {
						if (data instanceof Date) {
							if ((Date) data == (Date) value) {
								flag = true;
							}
						}
					} else if (fieldTypeName.equals("long") || fieldTypeName.equals("Long")) {
						if (data instanceof Long) {
							if ((long) data == (long) value) {
								flag = true;
							}
						}
					} else if (fieldTypeName.equals("String")) {
						if (data instanceof String) {
							if (((String) data).equals((String) value)) {
								flag = true;
							}
						}
					} else if (fieldTypeName.equals("double") || fieldTypeName.equals("Double")) {
						if (data instanceof Double) {
							if (((double) data) == ((double) value)) {
								flag = true;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
		}).collect(Collectors.toList());

//			for (T v : t) {
//				String methodName = field.substring(0, 1).toUpperCase() + field.substring(1);
//				Method method = clazz.getMethod("get" + methodName);
//				Object data = method.invoke(v);
//				Field f = clazz.getDeclaredField(field);
//				if (f.getName().equals(field)) {
//					String fieldTypeName = f.getType().getSimpleName();
//					if (fieldTypeName.equals("int") || fieldTypeName.equals("Integer")) {
//						if (data instanceof Integer) {
//							if ((int) data == (int) value) {
//								result.add(v);
//							}
//						}
//					} else if (fieldTypeName.equals("Date")) {
//						if (data instanceof Date) {
//							if ((Date) data == (Date) value) {
//								result.add(v);
//							}
//						}
//					} else if (fieldTypeName.equals("long") || fieldTypeName.equals("Long")) {
//						if (data instanceof Long) {
//							if ((long) data == (long) value) {
//								result.add(v);
//							}
//						}
//					} else if (fieldTypeName.equals("String")) {
//						if (data instanceof String) {
//							if (((String) data).equals((String) value)) {
//								result.add(v);
//							}
//						}
//					} else if (fieldTypeName.equals("double") || fieldTypeName.equals("Double")) {
//						if (data instanceof Double) {
//							if (((double) data) == ((double) value)) {
//								result.add(v);
//							}
//						}
//					}
//				}
//			}

		return result;
	}

	/**
	 * 通过键(key)-值(value)从List<Map>中获取匹配数据
	 * @param datas
	 * @param key
	 * @param value
     * @return
     */
	public default List<Map<String,Object>> listGet(List<Map<String,Object>> datas,String key,Object value){
//		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
//		datas.forEach((map)->{
//			Object o = map.get(key);
//			if (o==value){
//				result.add(map);
//			}
//		});
		ArrayList<Map<String,Object>> objects = datas.stream().
				filter(map -> map.get(key).toString() == value).
				collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		return objects;
	}

	/**
	 * 移除
	 * 
	 * @param mname
	 *            模块名
	 * @param value
	 *            值
	 */
	public <T> void listRemove(String mname, T value);

	/**
	 * 移除
	 * 
	 * @param mname
	 *            模块名
	 */
	public void listRemove(String mname);
	/**
	 * 通过属性名和值 从List中返回索引值
	 * @param mname
	 * @param field
	 * @param value
	 * @param clazz
	 * @return
	 */
	public default <T> int getListIndex(String mname, String field, Object value, Class<T> clazz) {
		List<T> ts = this.listGet(mname,clazz);
		try {
			for(int i=0;i<ts.size();i++){
				T t=ts.get(i);
				Field f = clazz.getDeclaredField(field);
				String methodName=field.substring(0,1).toUpperCase().concat(field.substring(1));
				Method declaredMethod = clazz.getDeclaredMethod("get"+methodName);
				Object val = declaredMethod.invoke(t);
				String name=f.getGenericType().getTypeName();
				if (name.equals("int") || name.equals("java.lang.Integer")) {
					if (val instanceof Integer) {
						if ((int) val == (int) value) {
							return i;
						}
					}
				} else if (name.equals("Date")) {
					if (val instanceof Date) {
						if ((Date) val == (Date) value) {
							return i;
						}
					}
				} else if (name.equals("long") || name.equals("Long")) {
					if (val instanceof Long) {
						if ((long) val == (long) value) {
							return i;
						}
					}
				} else if (name.equals("String")) {
					if (val instanceof String) {
						if (((String) val).equals((String) value)) {
							return i;
						}
					}
				} else if (name.equals("double") || name.equals("Double")) {
					if (val instanceof Double) {
						if (((double) val) == ((double) value)) {
							return i;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}

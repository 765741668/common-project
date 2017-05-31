package com.app.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.app.redis.RedisUtil;
import com.app.utils.JsonUtil;
import com.app.utils.StringUtils;

/**
 * Redis 缓存Interface
 * Created by yangzhao on 2015/10/13.
 * 添加Stream并行聚合操作 update by yangzhao on 2016/10/14
 */
public class RedisCache implements ICache {
	@Override
	public void set(String key, String value) {
		RedisUtil.getInstance().set(key,value);
	}

	@Override
	public void set(String key, Object object) {
		RedisUtil.getInstance().set(key,JsonUtil.parse(object));
	}

	@Override
	public void set(String key, int seconds, String value) {
		RedisUtil.getInstance().setex(key, seconds, value);
	}

	@Override
	public void set(String key, int seconds, Object object) {
		RedisUtil.getInstance().setex(key, seconds, JsonUtil.parse(object));
	}

	@Override
	public String get(String key) {
		String s = RedisUtil.getInstance().get(key);
		return s;
	}

	@Override
	public <T> T get(String key, Class<T> tClass) {
		String s = RedisUtil.getInstance().get(key);
		T result = JsonUtil.parse(s, tClass);
		return result;
	}

	@Override
	public void hashSet(String mname, String key, Object value) {
		RedisUtil.getInstance().hset(mname, key, JsonUtil.parse(value));
	}

	@Override
	public void hashSet(String mname, Map<String, Object> map) {
		Map<String,String> data=new HashMap<>();
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String key=iterator.next();
			Object obj=map.get(key);
			data.put(key, JsonUtil.parse(obj));
		}
		RedisUtil.getInstance().hmset(mname, data);
	}

	@Override
	public <T>T hashGet(String mname, String key,Class<T> clazz) {
		RedisUtil redisUtil = RedisUtil.getInstance();
		boolean exist = redisUtil.exist(mname);
		if (!exist){
			return null;
		}
		String data = redisUtil.hget(mname, key);
		if(StringUtils.isEmpty(data)){
			return null;
		}
		T parse = JsonUtil.parse(data, clazz);
		return parse;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Collection hashGet(String mname, String key, Class collectionClass, Class elementClasses) {
		String data = RedisUtil.getInstance().hget(mname, key);
		if(StringUtils.isEmpty(data)){
			return null;
		}
		Collection parse = null;
		try {
			parse = JsonUtil.parse(data, collectionClass, elementClasses);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parse;
	}

	@Override
	public String hashGet(String mname, String key) {
		String data = RedisUtil.getInstance().hget(mname, key);
		return data;
	}

	@Override
	public <T> List<T> hashGet(String mname, String [] keys, Class<T> clazz) {
		List<String> data = RedisUtil.getInstance().hmget(mname, keys);
		List<T> collect = data.stream().map(d -> {
			T parse = JsonUtil.parse(d, clazz);
			return parse;
		}).collect(Collectors.toList());
		return collect;
	}

	@Override
	public Map<String, Object> hashGet(String mname) {
		Map<String, String> hget = RedisUtil.getInstance().hget(mname);
		if(hget.isEmpty()){
			return null;
		}
		Set<String> keySet = hget.keySet();
		Iterator<String> iterator = keySet.iterator();
		Map<String,Object> map=new HashMap<>();
		while(iterator.hasNext()){
			String key=iterator.next();
			Object obj=JsonUtil.parse(hget.get(key), Object.class);
			map.put(key, obj);
		}
		return map;
	}

	@Override
	public void hashRemove(String mname, String key) {
		RedisUtil.getInstance().remove(mname,key);
	}

	@Override
	public void hashRemove(String mname) {
		RedisUtil.getInstance().remove(mname);
	}

	@Override
	public void listAdd(String mname, String...values) {
		RedisUtil.getInstance().rpush(mname,values);
	}

	@Override
	public <T>void listAdd(String mname, List<T> list) {
		ArrayList<String> resultList = list.stream().map(t -> JsonUtil.parse(t)).
				collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		String[] datas = resultList.toArray(new String[resultList.size()]);
		RedisUtil.getInstance().rpush(mname,datas);
	}

	public boolean listIsExist(String mname){
		return RedisUtil.getInstance().exist(mname);
	}
	@Override
	public <T>List<T> listGet(String mname,Class<T> clazz) {
		List<String> list = RedisUtil.getInstance().getList(mname);
		if(list.isEmpty()){
			return null;
		}
		ArrayList<T> objects = list.stream().map(t -> JsonUtil.parse(t, clazz))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		return objects;
	}

	@Override
	public <T> void listRemove(String mname, T value) {
		//RedisUtil.getInstance().delList(mname, 0, value);
	}

	@Override
	public void listRemove(String mname) {
		RedisUtil.getInstance().remove(mname);
	}

	@Override
	public List<String> listGet(String mname, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void removeListAdd(String mname, List<T> list) {
		RedisUtil.getInstance().remove(mname);
		this.listAdd(mname, list);
	}

	@Override
	public <T> boolean listKeyReplaceValue(String mname, int index, T t) {
		try{
			RedisUtil.getInstance().lset(mname, index, t);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public void listAdd(String mname,Object t) {
		if(t!=null){
			RedisUtil.getInstance().rpush(mname,JsonUtil.parse(t));
		}
	}

}

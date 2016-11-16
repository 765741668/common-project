package com.app.cache;

import com.app.constants.Constant;


/**
 * 缓存工厂类
 * @author yangzhao 2015年10月16日
 *
 */
public class CacheFactory {

	private CacheFactory() {
	}

	private ICache cache;

	private static CacheFactory instance = new CacheFactory();

	public static CacheFactory getInstance() {
		return instance;
	}

	public void init() {
		if (cache != null) {
			return;
		}
		int mode = Constant.CACHE_MODE;
		if (mode == 1) {
			cache = new HashListCache();
		} else if (mode == 2) {
			cache = new RedisCache();
		}
	}

	public ICache getDefault() {
		return cache;
	}
}

package com.hz.tgb.spring;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 获取Spring Bean
 *
 * @author hezhao
 */
public class BeanUtil {
	private static WebApplicationContext wac;
	
	private BeanUtil(){
		// 私有类构造方法
	}

	public static Object lookup(final String name) {
		if (wac == null) {
			wac = ContextLoader.getCurrentWebApplicationContext();
		}

		return wac.getBean(name);
	}

	public static <T> T lookup(final String name, final Class<T> clazz) {
		if (wac == null) {
			wac = ContextLoader.getCurrentWebApplicationContext();
		}
		return wac.getBean(name, clazz);
	}

}

package com.coderlong.webflux.proxy;

/**
 * 创建代理类接口
 * @author sailongren
 */
public interface ProxyCreator {
	/**
	 * 代理接口
	 * 
	 * @param type
	 * @return
	 */
	Object createProxy(Class<?> type);
}

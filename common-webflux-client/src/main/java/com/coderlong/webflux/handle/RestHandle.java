package com.coderlong.webflux.handle;

/**
 * @author sailongren
 */
public interface RestHandle<T, S> {
    /**
     * webClient 初始化
     * @param serviceInfo
     */
    void init(S serviceInfo);

    /**
     * 获取结果
     * @param metaInfo
     * @param serviceInfo
     * @return
     */
    Object invokeRest(T metaInfo, S serviceInfo);

}

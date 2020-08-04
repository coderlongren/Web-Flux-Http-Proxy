package com.coderlong.webflux.beans;

import lombok.Data;

@Data
public class ServerInfo {

	/**
	 * 微服务 域名
	 */
	private String url;

	/**
	 *  服务标识
	 */
	private String microName;

}

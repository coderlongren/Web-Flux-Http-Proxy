package com.coderlong.webflux.beans;

import lombok.Data;

@Data
public class ServerInfo {

	/**
	 * 服务url
	 */
	private String url;

	/**
	 *  服务标识
	 */
	private String microName;

}

package com.ly.common.util;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.ly.base.common.util.IpUtil;

public class IpUtilTest extends IpUtil {

	@Test
	public void tesetGetIp() {
		System.out.println(JSON.toJSONString(getAddress("113.102.163.120")));
	}
}

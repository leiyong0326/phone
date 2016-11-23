package com.ly.common.util;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ly.base.common.model.PropertiesModel;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.PropertiesCacheUtil;

public class PropertiesCacheUtilTest {

	List<Integer> list;

	@Before
	public void testBefore() {
		System.out.println(SystemConfig.class.getResource("/").getFile());
		System.out.println(PropertiesCacheUtil.getBasepath());
		System.out.println(PropertiesCacheUtil.getUserconfig());
		// PropertiesCacheUtil.loadProperties(getRequest().getServletContext().getRealPath(PropertiesCacheUtil.getBasepath()+PropertiesCacheUtil.USERCONFIG)).get("generateMac");
	}

	@Test
	public void testLoadProperties() {
		String base = "../classes/log4j.properties";
		String path = PropertiesCacheUtil.getBasepath()+base;
		System.out.println(path);
		PropertiesModel propertiesModel = PropertiesCacheUtil.loadProperties(path);
		propertiesModel.getProperties().forEach((Object k, Object v) -> {
			System.out.println(k + "---" + v);
		});
		

	}

}

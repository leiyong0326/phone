package com.ly.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.ly.base.common.model.Json;
import com.ly.base.common.model.Model;
import com.ly.base.common.util.BeanUtil;

public class BeanUtilTest {
	Json json;
	Model model;
	@Before
	public void before(){
		json = new Json();
		json.setSuccess(true);
		json.setMsg("");
		model = new Model("abc", "=", "ddd");
	}
	@Test
	public void testCheckBean(){
		assertEquals(BeanUtil.checkEntity(json, "success"), null);
		assertEquals(BeanUtil.checkEntity(json, "success","obj"), "obj");
		assertEquals(BeanUtil.checkEntity(json, "success","msg"), "msg");
		assertEquals(BeanUtil.checkEntity(json, "success","obj","msg"), "obj");
		assertEquals(BeanUtil.checkEntity(model, "column","operate","value"), null);
		assertEquals(BeanUtil.checkEntity(model, "column","operate","value","values"), "values");
	}
}

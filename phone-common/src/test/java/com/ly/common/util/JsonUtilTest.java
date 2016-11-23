package com.ly.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.ly.base.common.model.Model;
import com.ly.base.common.util.JsonUtil;
import com.ly.base.common.util.MyBatisUtil;
import com.github.pagehelper.Page;

public class JsonUtilTest {
	@Test
	public void identityHashCode(){
		System.out.println(System.identityHashCode(List.class));
		System.out.println(System.identityHashCode(Page.class));
		System.out.println(System.identityHashCode(List.class)&1023);
		System.out.println(System.identityHashCode(Page.class)&1023);
	}
	@Test
	public void seribleList(){
		List<Integer> list = new ArrayList<>();
		Page<Model> page = new Page<>();
		page.setTotal(2);
		page.add(MyBatisUtil.parseList("abc", "in", "123,345,123"));
		page.add(MyBatisUtil.parseList("abc", "in", "123,345,123"));
		System.out.println(JsonUtil.toJsonString(list));
		System.out.println(JsonUtil.toJsonString(page));
//		List<?> lists = JSON.parseObject(JsonUtil.toJsonString(list),List.class);
//		System.out.println(JsonUtil.toJsonString(lists));
		Page<?> pages = JSON.parseObject(JsonUtil.toJsonString(page),Page.class);
		System.out.println(JsonUtil.toJsonString(pages));
		System.out.println(System.identityHashCode(List.class));
		System.out.println(System.identityHashCode(Page.class));
		System.out.println(System.identityHashCode(List.class)&1023);
		System.out.println(System.identityHashCode(Page.class)&1023);
	}
}

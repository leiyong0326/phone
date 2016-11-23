package com.ly.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.StringUtil;

public class ArrayUtilTest {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ArrayUtilTest.class);

	List<Integer> list;
	@Before
	public void testBefore(){
		if (logger.isDebugEnabled()) {
			logger.debug("testBefore() - start"); //$NON-NLS-1$
		}

		list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			list.add((int)(Math.random()*100));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("testBefore() - end"); //$NON-NLS-1$
		}
	}
	@Test
	public void testSortList() {
		if (logger.isDebugEnabled()) {
			logger.debug("testSortList() - start"); //$NON-NLS-1$
		}

		System.out.println("this is sort method");
		System.out.println(ArrayUtil.sortList(list, true));

		if (logger.isDebugEnabled()) {
			logger.debug("testSortList() - end"); //$NON-NLS-1$
		}
	}

	@Test
	public void testFileter() {
		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - start"); //$NON-NLS-1$
		}

		System.out.println("this is filter method");
		System.out.println(ArrayUtil.filter(list,(t) -> {return t > 50;}));

		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - end"); //$NON-NLS-1$
		}
	}
	@Test
	public void testForeachList () {
		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - start"); //$NON-NLS-1$
		}

		System.out.println("this is foreach list method");
		//一旦大于95停止遍历
		boolean res = ArrayUtil.foreach(list,(t,i) -> {System.out.println(i+"--"+t);return t<95;});
		System.out.println("是否有大于95的数字:"+!res);
		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - end"); //$NON-NLS-1$
		}
	}
	@Test
	public void testForeachMap () {
		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - start"); //$NON-NLS-1$
		}
		Map<String, Object> map = new HashMap<>();
		map.put("ss", "tt");
		map.put("ii", 193);
		map.put("ll", list);
		System.out.println("this is foreach list method");
		ArrayUtil.foreach(map,(k,v) -> {System.out.println(k+"::"+v);return true;});

		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - end"); //$NON-NLS-1$
		}
	}
	@Test
	public void testForeachSet () {
		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - start"); //$NON-NLS-1$
		}
		Set<Object> ss = new HashSet<>();
		ss.add("1");
		ss.add("3");
		ss.add(list);
		System.out.println("this is foreach list method");
		ArrayUtil.foreach(ss,(s) -> {System.out.println(s);return true;});

		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - end"); //$NON-NLS-1$
		}
	}
	@Test
	public void testForeachBean(){
		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - start"); //$NON-NLS-1$
		}
		JSONObject jo = new JSONObject();
		System.out.println("遍历当前对象属性");
		ArrayUtil.foreach(jo,false,(f,v) -> {System.out.println(StringUtil.appendStringByObject(":", f.getName(),v));return true;});
		System.out.println("遍历包含父对象属性");
		ArrayUtil.foreach(jo,true,(f,v) -> {System.out.println(StringUtil.appendStringByObject(":", f.getName(),v));return true;});

		if (logger.isDebugEnabled()) {
			logger.debug("testFileter() - end"); //$NON-NLS-1$
		}
	
	}
}

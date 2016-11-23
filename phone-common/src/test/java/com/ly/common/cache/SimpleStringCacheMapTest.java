package com.ly.common.cache;

import org.junit.Test;

import com.ly.base.common.cache.map.StringTokenCacheMap;
import com.ly.base.common.util.NumberUtil;

public class SimpleStringCacheMapTest{
	StringTokenCacheMap map = StringTokenCacheMap.getInstance();
	long num = 123_123_123;
	@Test
	public void test() throws InterruptedException {
		map.put("1", "123123123123123");
		map.put("2", "23423423423342342342");
		map.put("3", "3453453453453453453");
		map.put("4", "456456456456456456456");
		System.out.println(num);
		for (int i = 1; i < 200; i++) {
			int index= NumberUtil.random(2)+1;
			map.get(""+(index));
			System.out.println(index);
			System.out.println("SLEEP:"+System.currentTimeMillis());
			Thread.sleep(100);
		}
	}

}

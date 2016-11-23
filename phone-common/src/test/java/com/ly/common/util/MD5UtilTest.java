package com.ly.common.util;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import com.ly.base.common.util.MD5Util;

public class MD5UtilTest {

	@Test
	public void test() {
		System.out.println(MD5Util.md5("1990326"));
		File file = new File("D:\\Java\\eclipse\\eclipse.ini");
		try
		{
			System.out.println(MD5Util.getMd5ByFile(file));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	
	}

}

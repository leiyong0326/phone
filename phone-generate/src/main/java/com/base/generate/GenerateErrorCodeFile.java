package com.base.generate;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.util.ArrayUtil;

/**
 * 错误码代码生成工具
 * @author LeiYong
 *
 */
public class GenerateErrorCodeFile {
	private static final String REGEX = "([+-]?\\d+)(	++)(.++)";
	private static final String REPLACE = "map.put($1,\"$3\");";
	private static final String FILE_PATH = "E:\\wx\\error.txt";
	public static void main(String[] args) {
//		System.out.println("Map<Integer, String> map = new HashMap<>();");
		File file = new File(FILE_PATH);
		if (file.exists()) {
			try {
				java.util.List<String> list = FileUtils.readLines(file,SystemConfig.CHARSET);
				ArrayUtil.foreach(list, (s,i) -> {
					System.out.println(s.replaceAll(REGEX, REPLACE).replace("\\", "\\\\"));
					return true;
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

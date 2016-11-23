package com.base.generate;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.ly.base.common.util.FileUtil;

/**
 * 打印某个文件夹下的所有目录和文件
 * @author LeiYong
 *
 */
public class PrintFileList {
	private static boolean printDirectory = true;
	private static boolean printFilename = true;
	private static boolean printAbsolutePath = false;
	private static boolean clearFileType = true;
	private static boolean printFirstPath = false;
	private static String[] filterFileTypes = {"jpg","pdf"};
	private static String[] filterFilePaths = {"图标"};
	private static int printCount = 0;
	public static void main(String[] args) {
		File file = new File("E:\\SVN\\UI\\微信端产品\\psd文件\\方案四");
		scanPath(file, null,printFirstPath);
		System.out.println("共计:"+printCount);
	}
	public static void scanPath(File file,String parentPath,boolean printPath){
		if (file!=null) {
			if(file.isDirectory()){
				if (printPath&&printDirectory) {
					printFile(file, parentPath);
				}
				File[] files = file.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						if(pathname.isFile()&&ArrayUtils.contains(filterFileTypes,FilenameUtils.getExtension(pathname.getName()).toLowerCase())){
							return false;
						}else if(pathname.isDirectory()&&ArrayUtils.contains(filterFilePaths,pathname.getName().toLowerCase())){
							return false;
						}
						return true;
					}
				});
				for (File f : files) {
					String pp = "";
					if (printPath) {
						pp += StringUtils.isNotBlank(parentPath)?FileUtil.checkDirectory(parentPath)+FileUtil.checkDirectory(file.getName()):FileUtil.checkDirectory(file.getName());
					}
					scanPath(f, pp,true);
				}
			}else{
				if (printFilename) {
					printFile(file, parentPath);
				}
			}
		}
	}
	public static void printFile(File file,String parentPath){
		if (file!=null) {
			printCount++;
			if (printAbsolutePath) {
				if (file.isDirectory()) {
					System.out.println(file.getAbsolutePath());
				}else{
					String filepath = file.getAbsolutePath();
					System.out.println(clearFileType(filepath));
				}
			}else{
				String res = "";
				if (StringUtils.isNotBlank(parentPath)) {
					res +=FileUtil.checkDirectory(parentPath);
				}
				res += file.getName();
				if (file.isDirectory()) {
					System.out.println(res);
				}else{
					System.out.println(clearFileType(res));
				}
			}
			
		}
	}
	
	public static String clearFileType(String filePath){
		if (clearFileType) {
			String fileType = FilenameUtils.getExtension(filePath);
			if (fileType.length()>0) {
				filePath = filePath.substring(0, filePath.length()-fileType.length()-1);
			}
		}
		return filePath;
	}
}

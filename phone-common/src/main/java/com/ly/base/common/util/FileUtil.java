package com.ly.base.common.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.ly.base.common.em.ext.FileTypeEnum;
import com.ly.base.common.system.SystemConfig;

/**
 * 文件工具
 * 
 * LeiYong
 */
public class FileUtil {

	/**
	 * 验证文件类型
	 * 
	 * @param fileName
	 * @param ft
	 * @return
	 */
	public static boolean checkFileType(File file, FileTypeEnum... fileTypeEnum) {
		if (file == null) {
			return false;
		}
		return checkFileType(file.getName(), fileTypeEnum);
	}

	/**
	 * 验证文件类型
	 * 
	 * @param fileName
	 * @param ft
	 * @return
	 */
	public static boolean checkFileType(String fileName, FileTypeEnum... ft) {
		for (int i = 0; i < ft.length; i++) {
			String[] fts = ft[i].getValue().split(",");
			if (ArrayUtils.contains(fts, FilenameUtils.getExtension(fileName).toLowerCase())) {
				return true;
			}
//			for (int j = 0; j < fts.length; j++) {
//				if (fileType.equalsIgnoreCase(fts[j])) {
//					return true;
//				}
//			}
		}
		return false;
	}

	/**
	 * 获取文件类型
	 * 
	 * @param file
	 * @return 如果没有后缀则返回null
	 */
	public static String getFileType(File file) {
		if (file == null) {
			return null;
		}
		return getFileType(file.getName());
	}

	/**
	 * 获取文件类型
	 * 
	 * @param fileName
	 * @return 如果没有后缀则返回null,返回.后的部分
	 */
	public static String getFileType(String fileName) {
		fileName = fileName.replaceAll("\\s", "");
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		int typeIndex = fileName.lastIndexOf(".");
		if (typeIndex < 0 || typeIndex == fileName.length() - 1) {
			return null;
		}
		return fileName.substring(typeIndex + 1);
	}

	/**
	 * 读取文件字节
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileBytes(File file) throws IOException {
		if (file == null) {
			return null;
		}
		return FileUtils.readFileToByteArray(file);
	}

	/**
	 * 读取文件字符串
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFileString(File file) throws IOException {
		if (file == null) {
			return null;
		}
		return FileUtils.readFileToString(file,SystemConfig.CHARSET);
	}

	/**
	 * 读取文件字符串List
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String> readFileList(File file) throws IOException {
		if (file == null) {
			return null;
		}
		return FileUtils.readLines(file,SystemConfig.CHARSET);
	}

	/**
	 * 将字节写入文件
	 * 
	 * @param bs
	 * @param filename
	 * @param append
	 *            是否为追加
	 * @return 如果字节或者文件路径为空则返回false
	 * @throws IOException
	 */
	public static boolean writeFileBytes(byte[] bs, String filename, boolean append) throws IOException {
		if (bs == null || StringUtils.isBlank(filename)) {
			return false;
		}
		return writeFileBytes(bs, new File(filename), append);
	}

	/**
	 * 将字节写入文件
	 * 
	 * @param bs
	 * @param file
	 * @param append
	 *            是否为追加
	 * @return 如果字节或者文件路径为空则返回false
	 * @throws IOException
	 */
	public static boolean writeFileBytes(byte[] bs, File file, boolean append) throws IOException {
		if (bs == null || file == null) {
			return false;
		}
		FileUtils.writeByteArrayToFile(file, bs, append);
		return true;
	}

	/**
	 * 将字符串写入文件
	 * 
	 * @param bs
	 * @param fileName
	 * @param append
	 *            是否为追加
	 * @return 如果字节或者文件路径为空则返回false
	 * @throws IOException
	 */
	public static boolean writeFileBytes(String str, String fileName, boolean append) throws IOException {
		if (str == null || StringUtils.isBlank(fileName)) {
			return false;
		}
		FileUtils.writeStringToFile(new File(fileName), str,SystemConfig.CHARSET, append);
		return true;
	}

	/**
	 * 将字符串写入文件
	 * 
	 * @param str
	 * @param file
	 * @param append
	 *            是否为追加
	 * @return 如果字节或者文件路径为空则返回false
	 * @throws IOException
	 */
	public static boolean writeFileBytes(String str, File file, boolean append) throws IOException {
		if (str == null || file == null) {
			return false;
		}
		FileUtils.writeStringToFile(file, str,SystemConfig.CHARSET, append);
		return true;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param folderPath
	 *            路径
	 * @return boolean
	 */
	public static boolean createFolder(String folderPath) {
		File f = new File(folderPath);
		if (!f.isDirectory()) {
			return f.mkdirs();
		}
		return true;
	}

	/**
	 * 通过目录和文件名获取一个文件对象
	 * 
	 * @param path
	 * @param filename
	 * @return
	 */
	public static File getFile(String path, String filename) {
		path = checkDirectory(path);
		if (StringUtils.isNotEmpty(path)) {
			return new File(path + filename);
		}
		return null;
	}
	/**
	 * 获取文件名
	 * 例如  abc.txt  获取到abc
	 * /usr/java/abc.txt 获取到abc
	 * @param fileName 文件名/路径
	 * @return
	 */
	public static String getFileName(String fileName){
		return FilenameUtils.getBaseName(fileName);
	}
	/**
	 * 验证目录路径是否以/或\结尾,如果不是,则为目录添加结束符
	 * 
	 * @param path
	 * @return 返回以/或\结束的目录
	 */
	public static String checkDirectory(String path) {
		if (StringUtils.isBlank(path)) {
			return "";
		} else {
			path = path.replaceAll("\\s", "");
			if (!(path.endsWith("/") || path.endsWith("\\"))) {
				path += File.separator;
			}
			return path;
		}
	}

	/**
	 * 随机生成文件名,prefix+uuid+date stamp+sufficx
	 * 
	 * @param prefix
	 * @param suffix
	 *            无需加.
	 * @return prefix+uuid+date stamp+sufficx
	 */
	public static String randomFileName(String prefix, String suffix) {
		StringBuffer sb = new StringBuffer(prefix == null ? "" : prefix);
		sb.append(StringUtil.getUUID());
		sb.append(System.currentTimeMillis() % 10000);
		if (StringUtils.isNotBlank(suffix)) {
			sb.append(suffix.startsWith(".") ? suffix : "." + suffix);
		}
		return sb.toString();
	}
}

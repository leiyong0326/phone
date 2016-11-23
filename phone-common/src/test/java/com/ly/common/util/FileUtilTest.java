package com.ly.common.util;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ly.base.common.em.ext.FileTypeEnum;
import com.ly.base.common.util.FileUtil;

public class FileUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCheckFileTypeFileFileTypeEnumArray() {
		assertFalse(FileUtil.checkFileType("a.jsp", FileTypeEnum.Img));
		assertTrue(FileUtil.checkFileType("a.jpg", FileTypeEnum.Img));;
		assertTrue(FileUtil.checkFileType("a.jpg.jpg", FileTypeEnum.Img));;
		assertFalse(FileUtil.checkFileType("a.jpg.jsp", FileTypeEnum.Img));;
		assertFalse(FileUtil.checkFileType("a.jsp", FileTypeEnum.Xml));;
		assertFalse(FileUtil.checkFileType("a.xml.jsp", FileTypeEnum.Xml));;
		assertTrue(FileUtil.checkFileType("a.XmL", FileTypeEnum.Xml));;;
		assertFalse(FileUtil.checkFileType("dasfsfa", FileTypeEnum.Img));;
	}


	@Test
	public void testGetFileTypeFile() {
		assertEquals(FileUtil.getFileType("a.jsp.s.df"), "df");
	}

	@Test
	public void testRandomFileName(){
		assertTrue(FileUtil.randomFileName("img", ".jpg").matches("^img.+\\.jpg$"));
	}
}

package com.ly.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionUtilTest {
	@Test
	public void testInstanceOf(){
		List<String> slist = new ArrayList<>();
		ArrayList<String> salist = new ArrayList<>();
		assertTrue(slist instanceof List);
		assertTrue(slist instanceof ArrayList);
		assertTrue(salist instanceof List);
		assertTrue(salist instanceof ArrayList);
		assertTrue(List.class.isInstance(slist));
		assertTrue(List.class.isInstance(salist));
		assertTrue(ArrayList.class.isInstance(slist));
		assertTrue(ArrayList.class.isInstance(salist));
	}
}

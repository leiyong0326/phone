package com.ly.base.common.filter.util;

public class HTMLFilterStrategy {
	private static HTMLFilter baesHtmlFilter;
	static {
		baesHtmlFilter = new HTMLFilter();
	}

	public static HTMLFilter getBaesHtmlFilter() {
		return baesHtmlFilter;
	}

}

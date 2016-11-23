package com.ly.base.shiro;

import java.awt.Color;

import com.ly.base.common.model.PropertiesModel;
import com.ly.base.common.system.properties.AdminPropertiesConfig;
import com.ly.base.common.system.properties.AdminPropertiesKeyConfig;
import com.ly.base.common.util.NumberUtil;
import com.ly.base.common.util.PropertiesCacheUtil;
import com.ly.base.common.util.StringUtil;

public class ShiroCaptchaUtil {
	private static String[] colorsString = { "1ABC9C", "2ECC71", "3498DB", "9B59B6", "34495E", "16A085", "27AE60",
			"2980B9", "8E44AD", "2C3E50", "F1C40F", "E67E22", "E74C3C", "ECF0F1", "95A5A6", "F39C12", "D35400",
			"C0392B", "BDC3C7", "7F8C8D" };
	private static Color[] colors = new Color[colorsString.length];

	static {
		for (int i = 0; i < colorsString.length; i++) {
			colors[i] = new Color(Integer.parseInt(colorsString[i], 16));
		}
	}

	public static Color getWordColor() {
		return colors[NumberUtil.random(colors.length)];
	}

	public static Color getNoiseColor() {
		return colors[NumberUtil.random(colors.length)];
	}

	/**
	 * 干扰线数量
	 * 
	 * @return
	 */
	public static int getNoiseCount() {
		try {
			PropertiesModel model = PropertiesCacheUtil.loadProjectProperties(AdminPropertiesConfig.CAPATCHA_CONFIG);
			return Integer.valueOf(model.get(AdminPropertiesKeyConfig.CAPTCHA_NOISE_COUNT));
		} catch (Exception e) {
		}
		return 5;
	}

	/**
	 * 干扰线粗细
	 * 
	 * @return
	 */
	public static float getNoiseStroke() {
		return (float) Math.random();
	}

	public static String getCaptchaWord() {
		int count = 4;
		try {
			PropertiesModel model = PropertiesCacheUtil.loadProjectProperties(AdminPropertiesConfig.CAPATCHA_CONFIG);
			count = model.getInteger(AdminPropertiesKeyConfig.CAPTCHA_COUNT);
		} catch (NumberFormatException e) {
			count = 4;
		}
		String capText = String.valueOf(NumberUtil.random((int) Math.pow(10, count)));
		return StringUtil.fullStringBefore(capText, '0', count);
	}

}

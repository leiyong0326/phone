package com.ly.base.shiro;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.ly.base.common.util.NumberUtil;
import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.util.Configurable;

/**
 * The default implementation of {@link NoiseProducer}, adds a noise on an
 * image.
 */
public class ShiroCaptchaNoise extends Configurable implements NoiseProducer
{
	/**
	 * Draws a noise on the image. The noise curve depends on the factor values.
	 * Noise won't be visible if all factors have the value > 1.0f
	 * 
	 * @param image
	 *            the image to add the noise to
	 * @param factorOne
	 * @param factorTwo
	 * @param factorThree
	 * @param factorFour
	 */
	public void makeNoise(BufferedImage image, float factorOne,
			float factorTwo, float factorThree, float factorFour)
	{
		// image size
		int width = image.getWidth();
		int height = image.getHeight();
		Graphics2D graph = (Graphics2D) image.getGraphics();
		graph.setRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));


		// for the maximum 3 point change the stroke and direction
		for (int i = 0; i < ShiroCaptchaUtil.getNoiseCount(); i++)
		{
			graph.setColor(ShiroCaptchaUtil.getNoiseColor());
			graph.setStroke(new BasicStroke(ShiroCaptchaUtil.getNoiseStroke()));
			graph.drawLine(NumberUtil.random(width), NumberUtil.random(height),
					NumberUtil.random(width), NumberUtil.random(height));
		}
		graph.dispose();
	}
}

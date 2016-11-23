package com.ly.base.shiro;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

/**
 * This servlet uses the settings passed into it via the Producer api.
 *
 * @author testvoogd@hotmail.com
 * @author jon
 * @author cliffano
 */
@SuppressWarnings("serial")
public class ShiroKaptchaServlet extends HttpServlet implements Servlet
{
	private Properties props = new Properties();

	private Producer kaptchaProducer = null;

	private String sessionKeyValue = null;

	private String sessionKeyDateValue = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig conf) throws ServletException
	{
		super.init(conf);

		// Switch off disk based caching.
		ImageIO.setUseCache(false);

		Enumeration<?> initParams = conf.getInitParameterNames();
		while (initParams.hasMoreElements())
		{
			String key = (String) initParams.nextElement();
			String value = conf.getInitParameter(key);
			this.props.put(key, value);
		}

		Config config = new Config(this.props);
		this.kaptchaProducer = config.getProducerImpl();
		this.sessionKeyValue = config.getSessionKey();
		this.sessionKeyDateValue = config.getSessionDate();
	}

	/** */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache");

		// return a jpeg
		resp.setContentType("image/jpeg");

		// create the text for the image
		String capText = ShiroCaptchaUtil.getCaptchaWord();

		// create the image with the text
		BufferedImage bi = this.kaptchaProducer.createImage(capText);

		ServletOutputStream out = resp.getOutputStream();

		// fixes issue #69: set the attributes after we write the image in case the image writing fails.

		// store the text in the session
		req.getSession().setAttribute(this.sessionKeyValue, capText);

		// store the date in the session so that it can be compared
		// against to make sure someone hasn't taken too long to enter
		// their kaptcha
		req.getSession().setAttribute(this.sessionKeyDateValue, new Date());
		// write the data out
		ImageIO.write(bi, "jpg", out);
	}
}

package com.ly.base.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.log4j.Logger;

public class CacheBeasUtils extends BeanUtilsBean {

	private static Logger logger = Logger.getLogger(CacheBeasUtils.class.getName());

	// ------------------------------------------------------ Private Class
	// Variables

	/**
	 * Contains <code>BeanUtilsBean</code> instances indexed by context
	 * classloader.
	 */
	private static final ContextClassLoaderLocal BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {
		// Creates the default instance used when the context classloader is
		// unavailable
		protected Object initialValue() {
			return new CacheBeasUtils();
		}
	};

	/**
	 * Gets the instance which provides the functionality for {@link BeanUtils}.
	 * This is a pseudo-singleton - an single instance is provided per (thread)
	 * context classloader. This mechanism provides isolation for web apps
	 * deployed in the same container.
	 *
	 * @return The (pseudo-singleton) BeanUtils bean instance
	 */
	public static CacheBeasUtils getInstance() {
		return (CacheBeasUtils) BEANS_BY_CLASSLOADER.get();
	}

	/**
	 * Sets the instance which provides the functionality for {@link BeanUtils}.
	 * This is a pseudo-singleton - an single instance is provided per (thread)
	 * context classloader. This mechanism provides isolation for web apps
	 * deployed in the same container.
	 * 
	 * @param newInstance
	 *            The (pseudo-singleton) BeanUtils bean instance
	 */
	public static void setInstance(CacheBeasUtils newInstance) {
		BEANS_BY_CLASSLOADER.set(newInstance);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void populate(Object bean, Map properties) throws IllegalAccessException, InvocationTargetException {

		// Do nothing unless both arguments have been specified
		if ((bean == null) || (properties == null)) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("BeanUtils.populate(" + bean + ", " + properties + ")");
		}

		// Loop through the property name/value pairs to be set
		Iterator entries = properties.entrySet().iterator();
		while (entries.hasNext()) {

			// Identify the property name and value(s) to be assigned
			Map.Entry entry = (Map.Entry) entries.next();
			if (entry.getValue() instanceof Long) {
				String name = (String) entry.getKey();
				if (name == null) {
					continue;
				}
				Date valueDate = new Date((Long) entry.getValue());
				// Perform the assignment for this property
				setProperty(bean, name, (Object) valueDate);
			} else {
				String name = (String) entry.getKey();
				if (name == null) {
					continue;
				}
				// Perform the assignment for this property
				if (entry.getValue() != null) {
					setProperty(bean, name, entry.getValue());
				}

			}

		}

	}

}

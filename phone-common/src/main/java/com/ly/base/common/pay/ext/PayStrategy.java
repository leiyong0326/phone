package com.ly.base.common.pay.ext;

import com.ly.base.common.em.ext.PayEnum;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.WechatPayModel;
import com.ly.base.common.pay.PaySuper;
import com.ly.base.common.util.ReflectionUtil;

public class PayStrategy {
	/**
	 * 获取支付方式或者直接通过此处调用对应的支付入口
	 * 
	 * @param payEnum
	 * @return
	 */
	public static Json wechatPay(PayEnum em, WechatPayModel payModel) {
		Class<?> threadClazz = ReflectionUtil.getClass(em.getDiscription());
		if (threadClazz != null) {
			PaySuper ms = (PaySuper) ReflectionUtil.getInstance(threadClazz);
			if (ms != null) {
				return ms.wechatPay(payModel);
			}
		}
		return null;
	}
}

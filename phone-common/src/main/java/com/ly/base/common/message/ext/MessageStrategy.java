package com.ly.base.common.message.ext;

import com.ly.base.common.em.ext.MessageEnum;
import com.ly.base.common.message.MessageSuper;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.message.MessageTemplateData;
import com.ly.base.common.util.ReflectionUtil;

public class MessageStrategy {
	/**
	 * 获取支付方式或者直接通过此处调用对应的支付入口
	 * @param payEnum
	 * @return
	 */
	public static Json sendMessage(MessageEnum em){
//		try {
//			Class<?> threadClazz = ReflectionUtils.getClass(em.getDiscription());
//			MessageSuper ms = (MessageSuper) ReflectionUtils.getInstance(threadClazz);
//			if (ms!=null) {
//				return ms.sendMessage(accessToken, temp);
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		return null;
		
	}
	/**
	 * 发送模版数据
	 * @param em 对应枚举
	 * @param accessToken 用户token
	 * @param temp 消息模版
	 * @return
	 */
	public static Json sendTemplateMessage(MessageEnum em,String accessToken,MessageTemplateData temp){
		Class<?> threadClazz = ReflectionUtil.getClass(em.getDiscription());
		if (threadClazz!=null) {
			MessageSuper ms = (MessageSuper) ReflectionUtil.getInstance(threadClazz);
			if (ms!=null) {
				return ms.sendTemplateMessage(accessToken, temp);
			}
		}
		return null;
	}
}

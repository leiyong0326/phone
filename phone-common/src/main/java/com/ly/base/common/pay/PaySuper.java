package com.ly.base.common.pay;

import com.ly.base.common.model.Json;
import com.ly.base.common.model.WechatPayModel;

/**
 * 支付基类
 * 
 * @author LeiYong
 *
 */
public abstract class PaySuper {
	public abstract Json wechatPay(WechatPayModel payModel);
}

package com.ly.base.common.em.ext;

import com.ly.base.common.em.EnumSuper;

public enum ScoreTypeEnum implements EnumSuper{
	userInfo("0","完善个人信息"),
	userCarInfo("1","完善车辆信息"),
	login("2","登录/每日首次访问公众号"),
	share("3","分享"),
	rescue("4","救援服务"),
	topIn("5","充值赠送积分"),
	consumer("6","消费赠送积分"),
	scheduleNewCar("7","新车预定"),
	scheduleTestDriver("8","预约试驾"),
	scheduleSaleAfter("9","预约售后"),
	scheduleBase("a","其它预约"),
	activityRecommend("b","推荐有礼"),
	activityLuckyDraw("c","抽奖活动"),
	activityBase("d","其它活动"),
	peccancy("e","违章代办"),
	oilCard("f","油卡充值"),
	shopParts("g","配件商城购物")
	//onlineChat("","在线咨询"),
	//signIn("2","签到")
	;
	private String value;
	private String discription;
	
	ScoreTypeEnum(String value,String discription) {
		this.discription = discription;
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getDiscription() {
		return discription;
	}
}

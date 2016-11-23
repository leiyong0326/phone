/*
 * Date: 2016.06.28
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */

define(['jquery', 'utils', 'navbarConfig'],function() {
    //每次跳转页面都会重新加载,需使用session级缓存
    var url = "";
    var navbarConfig = [ [ {
	text : '系统管理',
	src : url + '/src/system/group.html',
	icon : 'globe',
	name : 'system',
    }, {
	text : '活动管理',
	src : url + '/src/activity/activity.html',
	icon : 'paste',
	name : 'activity'
    }, {
	text : '商城管理',
	src : url + '/src/shop/carBrand.html',
	icon : 'cart-plus',
	name : 'shop'
    }, {
	text : '养车服务',
	src : url + '/src/car/appointment.html',
	icon : 'car',
	name : 'car'
    }, {
	text : '会员管理',
	src : url + '/src/usr/user.html',
	icon : 'diamond',
	name : 'usr'
    }, {
	text : '奖品卡券管理',
	src : url + '/src/prize/prize.html',
	icon : 'gift',
	name : 'prize'
    }, {
	text : '互动管理',
	src : url + '/src/interactive/feedback.html',
	icon : 'comments-o',
	name : 'interactive'
    } ], [ {
	parent : 'system',
	childrens : [ {
	    text : '组织架构',
	    src : url + '/src/system/group.html',
	    name : 'group'
	}, {
	    text : '部门管理',
	    src : url + '/src/system/department.html',
	    name : 'department'
	}, {
	    text : '角色权限',
	    src : url + '/src/system/role.html',
	    name : 'role'
	}, {
	    text : '员工管理',
	    src : url + '/src/system/staff.html',
	    name : 'staff'
	}, {
	    text : '平台配置',
	    src : url + '/src/system/vip_grade_set.html',
	    name : 'platform',
	    childrens : [ {
		text : '会员卡等级设置',
		src : url + '/src/system/vip_grade_set.html'
	    }, {
		text : '会员卡积分规则',
		src : url + '/src/system/vip_mark.html'
	    }, {
		text : '充值消费设置',
		src : url + '/src/system/deal_set.html'
	    } ]
	} ]
    }, {
	parent : 'prize',
	childrens : [ {
	    text : '奖品卡券管理',
	    src : url + '/src/prize/prize.html',
	    name : 'prize'
	}, {
	    text : '抽奖模板设置',
	    src : url + '/src/prize/prizedraw.html',
	    name : 'prizedraw'
	} ]
    }, {
	parent : 'activity',
	childrens : [ {
	    text : '活动管理',
	    src : url + '/src/activity/activity.html',
	    name : 'activity'
	}, {
	    text : '推荐有礼',
	    src : url + '/src/activity/recommend.html',
	    name : 'recommend'
	}, {
	    text : '活动参与对象设置',
	    src : url + '/src/activity/activityAttend.html',
	    name : 'attend'
	} ]
    }, {
	parent : 'car',
	childrens : [ {
	    text : '预约管理',
	    src : url + '/src/car/appointment.html',
	    name : 'appointment',
	    childrens : [ {
		text : '预约时间设置',
		src : url + '/src/car/time.html',
		 name : 'time'
	    } ]
	}
//	, {
//	    text : '保险服务',
//	    src : url + '/src/car/insurance.html',
//	    name : 'insurance'
//	}
	, {
	    text : '违章代办',
	    src : url + '/src/car/peccancy.html',
	    name : 'peccancy',
	    childrens : [ {
		text : '违章提醒',
		src : url + '/src/car/peccancySubscribe.html',
		 name : 'peccancySubscribe'
	    } ]
	}, 
//	{
//	    text : '加油卡充值',
//	    src : url + '/src/car/paycheck.html',
//	    name : 'paycheck'
//	}, 
	{
	    text : '救援服务',
	    src : url + '/src/car/rescue.html',
	    name : 'rescue'
	}
	]
    }, {
	parent : 'shop',
	childrens : [ {
	    text : '品牌管理',
	    src : url + '/src/shop/carBrand.html',
	    name : 'carBrand'
	}, {
	    text : '车系管理',
	    src : url + '/src/shop/carClass.html',
	    name : 'carClass'
	}, {
	    text : '新车管理',
	    src : url + '/src/shop/car.html',
	    name : 'car'
	}, {
	    text : '二手车管理',
	    src : url + '/src/shop/carOld.html',
	    name : 'carOld'
	}, {
	    text : '配件大类管理',
	    src : url + '/src/shop/partsClass.html',
	    name : 'partsClass'
	}, {
	    text : '配件小类管理',
	    src : url + '/src/shop/partsClassSlave.html',
	    name : 'partsClassSlave'
	}, {
	    text : '汽车精品管理',
	    src : url + '/src/shop/carParts.html',
	    name : 'carParts'
	} ]
    }, {
	parent : 'usr',
	childrens : [ {
	    text : '用户管理',
	    src : url + '/src/usr/user.html',
	    name : 'user'
	}, {
	    text : '车辆管理',
	    src : url + '/src/usr/usrCar.html',
	    name : 'usrCar'
	}, {
	    text : '积分管理',
	    src : url + '/src/usr/score.html',
	    name : 'score'
	}, {
	    name : 'recharge',
	    text : '充值购券',
	    src : url + '/src/usr/recharge.html'
	}, {
	    name : 'consume',
	    text : '会员消费',
	    src : url + '/src/usr/consume.html'
	} ]
    }, {
	parent : 'interactive',
	childrens : [ {
	    text : '意见反馈',
	    src : url + '/src/interactive/feedback.html',
	    name : 'feedback'
	} ]
    } ] ];

    return navbarConfig;

});
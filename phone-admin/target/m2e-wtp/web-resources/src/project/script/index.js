/*!
 * jQuery JavaScript Library v1.5.1
 *
 * Copyright 2011, Ziyang Zhang
 *
 * Copyright 2011, ZZY
 * Date: Wed Feb 23 13:55:29 2011 -0500
 */
//上次访问src及标题
var lastSrc,lastTitle;

$(document).ready(function() {
	init();
	initEvent();
});
/**
 * 初始化
 */
function init(){
	//重新设置iframe高度
	var contentHeight = $(window).height() - 70;
	$('.container2').attr({
		style: 'height:' + contentHeight + 'px'
	});
	$('#containerIframe').attr({
		style: 'height:' + contentHeight + 'px;width:100%;'
	});
	$('#startMenu').attr({
		style: 'height:' + contentHeight + 'px;width:100%;'
	});
}
/**
 * 初始化事件
 */
function initEvent() {
	initHoverEvent();//初始化鼠标移入事件
	initForwardEvent();//初始化跳转事件
}
/**
 * 初始化跳转事件
 */
function initForwardEvent(){
	addForwardEvent('.home-page','http://m.i200.cn/GoodsList.aspx?rm=GXC4IP3BIVQW', '会员管理系统');
	addForwardEvent('.settings','settings.html', '个人设置');
	addForwardEvent('.order','order.html', '我的订单');
	addForwardEvent('.money','money.html', '我要提现');
	addForwardEvent('.password','password.html', '修改密码');
	addForwardEvent('.contact','contact.html', '联系我们');
	$('.logout').click(function(){
	    sendAjax({
		    url : "/sysUser/logout",
		    data : {
		    },
		    success : function(data) {
			var win = window.parent || window;
			win.location.href = '/src/project/index.html';
		    }
		});
	})
}
/**
 * 添加跳转页面事件
 * @param {Object} target 被添加事件的对象
 * @param {Object} src 跳转地址
 * @param {Object} title 新标题
 */
function addForwardEvent(target,src, title) {
	$(target).click(function() {
		forward(src, title);
	});
}
/**
 * 跳转页面
 * @param {Object} src 跳转地址
 * @param {Object} title 新标题
 */
function forward(src, title,isReforward) {
	lastSrc = $('#containerIframe').attr('src');
	lastTitle = $('title').html();
	$('#containerIframe').attr('src', src);
	$('.header h1').html(title);
	$('title').html(title);
	if(!isReforward){
		$('#start').trigger('click');
	}
}
/**
 * 跳转到前一个页面
 */
function lastForward(){
	forward(lastSrc,lastTitle,true);
}
/**
 * 初始化鼠标移动事件
 */
function initHoverEvent(){
	//slidedown Effect
	$('.slidedown').hover(function() {
		$('.search').show();
		$(this).children().animate({
			top: '30'
		}, {
			queue: false,
			duration: 500
		});
	}, function() {
		$(this).children().animate({
			top: '0'
		}, {
			queue: false,
			duration: 500
		});
		$('.search').hide();
	});
	//slideleft Effect
	$('.slideleft').hover(function() {
		$(this).children().animate({
			left: '-150'
		}, {
			queue: false,
			duration: 160
		});
		$('.news').show();
	}, function() {
		$(this).children().animate({
			left: '0'
		}, {
			queue: false,
			duration: 160
		});
		$('.news').hide();
	});
	//slideright Effect
	$('.slideright').hover(function() {
		$(this).children().animate({
			left: '140'
		}, {
			queue: false,
			duration: 160
		});
		$('.news2').show();
	}, function() {
		$(this).children().animate({
			left: '0'
		}, {
			queue: false,
			duration: 160
		});
		$('.news2').hide();
	});
	//startMenu Effect
	$('#start').toggle(function() {
		$(this).addClass('click');
		$('#startMenu').slideDown(110);
		$('#startMenu ul').css('display', 'block');
	}, function() {
		$(this).removeClass('click');
		$('#startMenu').slideUp(110);
		$('#startMenu ul').removeClass('display', 'none');
	});
	//Partial Sliding (Only show some of background)
	$('.boxgrid.peek').hover(function() {
		$(".cover", this).stop().animate({
			top: '90px'
		}, {
			queue: false,
			duration: 160
		});
	}, function() {
		$(".cover", this).stop().animate({
			top: '0px'
		}, {
			queue: false,
			duration: 160
		});
	});
}
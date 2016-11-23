$(document).ready(function() {
    addEvent();
    init();
});

function addEvent() {
    $('.cy-confirm').bind('click', function() {
	drawMoney();
    })
    $('.cy-cancel').bind('click', function() {
	window.parent.lastForward();
    })
}
/**
 * 初始化获取信息
 */
function init() {
    loadRatio();
    loadHistory();
}
function loadRatio() {
    sendAjax({
	url : "/sysUser/myInfo",
	data : {},
	success : function(data) {
	    var obj = data.obj;
	    $('#ratio').html(obj['ratio']);
	}
    });
}
function loadHistory() {
    sendAjax({
	url : "/logDrawMoney/myHistory",
	data : {},
	success : function(data) {
	    var obj = data.obj;
	    if (obj instanceof Array) {
		var historyHtml = "";
		for (var i = 0; i < obj.length; i++) {
		    var o = obj[i];
		    var createTime = dateFormat(o.createTime);
		    var confimTime = dateFormat(o.confimTime);
		    var html = '<div class="money-form"><div class="time"><p>申请时间:</p><p>'
			    + createTime
			    + '</p></div><div class="money-price"><p class="money-price-price">提现:</p><p>'
			    + o.money
			    + '大洋</p></div><div class="status"><p>确认时间:</p><p>'
			    + confimTime + '</p></div></div>';
		    historyHtml += html;
		}
		$('#history').html(historyHtml);
	    }
	}
    });
}
function drawMoney() {
    var money = $('#money').val();
    if (money == "") {
	window.alert("请填写金额");
	return;
    }
    if (parseInt(money)<=0) {
	window.alert("提现金额必须大于0");
	return;
    }
    if (parseInt(money)%100!=0) {
	window.alert("提现金额必须为100的整数倍");
	return;
    }
    var ratio = $('#ratio').html();
    if (parseInt(ratio) < parseInt(money)) {
	window.alert("提现金额不能大于可提现金额");
    } else {
	sendAjax({
	    url : "/logDrawMoney/drawMoney",
	    data : {
		"money" : parseInt(money)
	    },
	    success : function(data) {
		init();
		alert('申请成功');
	    }
	});
    }
}
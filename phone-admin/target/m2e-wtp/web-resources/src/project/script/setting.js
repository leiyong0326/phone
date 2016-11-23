$(document).ready(function() {
    addEvent();
    init();
});

function addEvent() {
    $('.cy-confirm').bind('click', function() {
	saveUserInfo();
    })
    $('.cy-cancel').bind('click', function() {
	window.parent.lastForward();
    })
}
var textField = [ 'wechat', 'qq' ];
var html = [ 'phone', 'alipay', 'accountName', 'name', 'ratio', 'ratioTotal' ];
/**
 * 初始化获取信息
 */
function init() {
    sendAjax({
	url : "/sysUser/myInfo",
	data : {},
	success : function(data) {
	    var obj = data.obj;
	    for (var i = 0; i < html.length; i++) {
		var o = html[i];
		$('#' + o).html(obj[o]);
	    }
	    for (var i = 0; i < textField.length; i++) {
		var o = textField[i];
		$('#' + o).val(obj[o]);
	    }
	    $('#sex').html(obj['sex'] == '0' ? '女' : '男');
	    if (obj['birthday']) {
		var date = new Date(obj['birthday']);
		$('#birthday').html(
			date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
				+ date.getDate());
	    } else {
		$('#birthday').html('未知');
	    }
	}
    });
}

function saveUserInfo() {
    var obj = {};
    for (var i = 0; i < textField.length; i++) {
	var o = textField[i];
	obj[o] = $('#' + o).val();
    }
    // if (!obj['phone']) {
    // alert('手机号不能为空');
    // return;
    // }
    // if (obj['phone'].length != 11) {
    // alert('手机号为11位数字');
    // return;
    // }
    sendAjax({
	url : "/sysUser/changeMyInfo",
	data : obj,
	success : function(data) {
	    alert('修改成功');
	}
    });
}
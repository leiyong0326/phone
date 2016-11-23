$(document).ready(function() {
    addEvent();
});

function addEvent() {
    $('.cy-confirm').bind('click', function() {
	changePassword();
    })
    $('.cy-cancel').bind('click', function() {
	window.parent.lastForward();
    })
}

function changePassword() {
    var srcPwd = $('#srcPwd').val();
    var newPwd = $('#newPwd').val();
    var confimPwd = $('#confimPwd').val();
    if (!srcPwd) {
	window.alert("请填写原密码");
    } else if (!newPwd) {
	window.alert("请填写新密码");
    } else if (!confimPwd) {
	window.alert("请填写二次确认密码");
    } else if (newPwd != confimPwd) {
	window.alert("两次密码填写不一致");
    } else if (newPwd.length < 6) {
	window.alert("密码至少填写6位");
    } else if (srcPwd == newPwd) {
	window.alert("新密码请勿与原密码一致");
    } else {
	sendAjax({
	    url : "/sysUser/changePassword",
	    data : {
		"password" : srcPwd,
		"newPassword" : newPwd
	    },
	    success : function(data) {
		alert('修改成功');
	    }
	});
    }
}
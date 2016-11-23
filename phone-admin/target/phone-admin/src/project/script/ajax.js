// 封装ajax
function sendAjax(options) {

    [ 'url', 'data', 'success' ].map(function(i) {
	if (!options[i]) {
	    new Error('您的参数缺少：' + i);
	}
    });

    // 跳转路由
    var route = [ {
	title : '翼翔管理系统',
	url : 'index.html'
    }, {
	title : '会员管理系统',
	url : 'project/index.html'
    }, {
	title : '登录',
	url : 'login.html'
    }, {
	title : '404',
	url : 'error/404.html'
    }, {
	title : '400',
	url : 'error/400.html'
    }, {
	title : '500',
	url : 'error/500.html'
    }, {
	title : 'error',
	url : 'error/error.html'
    }, {
	title : 'noperms',
	url : 'error/noperms.html'
    }, ];

    $.ajax({
		type : 'post',
		contentType : options.contentType ? options.contentType
			: 'application/x-www-form-urlencoded',
		url : options.url,
		data : options.data,
		success : function(data) {
		    var jsonData = null;
		    var _url = 'index.html';
		    if (typeof data == 'string') {
			if (data.indexOf('<!DOCTYPE html>') == 0) {
			    for ( var attr in route) {
				if (data.indexOf('<title>' + route[attr].title
					+ '</title>') != -1) {
				    _url = attr.url;
				    break;
				}
			    }
			    window.location.href = '/src/' + _url;
			    return;
			} else {
			    jsonData = JSON.parse(data);
			}
		    } else {
			jsonData = data;
		    }
		    if (jsonData.success && isFunction(options.success)) {
			options.success.call(this, jsonData);
		    } else {
			if (options.errorCallback
				&& isFunction(options.errorCallback)) {
			    options.errorCallback(jsonData);
			}
			alert(jsonData.msg);
		    }
		},
		error : function(msg) {
		    alert('网络出现异常了,请检查网络重试');
		}
	    });
}
// 判断是否是function
function isFunction(fn) {
    return typeof fn == 'function';
}
//日期格式化 YYYY-MM-DD mm:ss
function dateFormat(date, type) {
    var d = null, dates = null, reg = null, types = type
	    || 'YYYY-MM-DD hh:mm:ss';

    if (date) {
	d = new Date(date);
    } else {
	return "未确认";
    }

    dates = {
	'Y+' : d.getFullYear(),
	'M+' : d.getMonth() + 1,
	'D+' : d.getDate(),
	'h+' : d.getHours(),
	'm+' : d.getMinutes(),
	's+' : d.getSeconds()
    };

    for ( var attr in dates) {
	dates[attr] = dates[attr] < 10 ? ('0' + dates[attr]) : dates[attr];
	reg = new RegExp(attr, 'g');
	types = types.replace(reg, dates[attr]);
    }
    return types;
}
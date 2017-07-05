function Login() {
}

Login.prototype = {
    clearSession : function() {
	// 清空缓存
	$.removeCookie('autoLoginUser', {
	    path : '/'
	});
	var loginUser = sessionStorage.getItem("loginUser");
	sessionStorage.removeItem("loginUser");
	if (loginUser) {
	    var user = $.parseJSON(loginUser);
	    sessionStorage.removeItem(user.loginName)
	}
    },
    init : function() {
	this.submitBtn = $('[data-btn="login"]');
	this.captchaImg = $('.iogin-name-img');
	this.events();
	this.clearSession();
    },
    login : function() {
	var that = this;
	// 获取form表单属性
	var options = {
	    loginName : $('[name="loginName"]').val(),
	    password : $('[name="password"]').val(),
	    captcha : $('[name="captcha"]').val(),
	    rememberme : $('[name="rememberme"]').is(':checked') ? '1' : '0'
	}
	if (!options.loginName) {
	    alert('请输入用户名');
	    return;
	}
	if (!options.password) {
	    alert('请输入密码');
	    return;
	}
	if (!options.captcha) {
	    alert('请输入验证码');
	    return;
	}
	// 请求登录
	$.ajax({
	    type : 'post',
	    url : '/sysUser/login',
	    data : options,
	    success : function(data) {
		var jsonData = null;
		if (typeof data == 'string') {
		    jsonData = JSON.parse(data);
		} else {
		    jsonData = data;
		}
		if (!jsonData.success) {
		    that.clearCaptcha();
		    window.alert(jsonData.msg);
		} else {
		    // 缓存登录信息
		    sessionStorage.setItem("loginUser", JSON
			    .stringify(jsonData.obj));
		    that.successForward();
		}
	    },
	    error : function(data) {
		that.clearCaptcha();
	    }
	});
    },
    clearCaptcha : function() {
	$("[name='captcha']").val("");
	var imgSrc = this.captchaImg.find('img').attr('src');
	this.captchaImg.find('img').attr('src', '');
	this.captchaImg.find('img').attr('src', imgSrc);
    },
    successForward : function() {
	var that = this;
	// 自动登录时将信息存储到cookies
	var userInfo = $.cookie('autoLoginUser');
	if (userInfo) {
	    that.clearSession();
	    sessionStorage.setItem('loginUser', userInfo);
	    loginUser = userInfo;
	}
	var loginUser = sessionStorage.getItem('loginUser');
	var user = $.parseJSON(loginUser);
	var cacheMenu = undefined;
	if (user) {
	    cacheMenu = $.parseJSON(sessionStorage.getItem(user.loginName));
	}
	if (cacheMenu) {
	    this.menuData = cacheMenu;
	    that.forward();
	} else {
	    sendAjax({
		url : "/sysMenu/findMenuList",
		data : {},
		success : function(data) {
		    that.parseMenu(data.obj);
		    if (user) {
			sessionStorage.setItem(user.loginName, JSON
				.stringify(that.menuData));
		    }
		    that.forward();
		}
	    });
	}
    },
    forward : function() {
	var win = window.parent || window;
	if (!this.menuData || JSON.stringify(this.menuData) == "{}") {
	    win.location.href = '/src/index.html';
	} else {
	    win.location.href = '/src/index.html';
	}
    },
    // 解析为menu对象
    parseMenu : function(obj) {
	this.menuData = {};
	var that = this;
	if (obj != null && obj instanceof Array) {
	    for (var i = 0; i < obj.length; i++) {
		var o = obj[i];
		// 判断对象是否是子集,是子集则拥有upPk属性
		if (o.upPk == undefined || o.upPk == "") {
		    // 是父级则生成父级对象
		    that.menuData[o.pk] = $.extend(that.menuData[o.pk], o);
		} else {
		    parseChilds(o);
		}
	    }
	    /**
	     * [parseChilds 保存子集] 约定每2位字符串为一个上级
	     * 
	     * @param {[Object]}
	     *                obj [子集对象]
	     * @return {[type]} [description]
	     */
	    function parseChilds(obj) {
		var parent = undefined;
		var up = undefined;
		// 循环创建/读取父级
		while (up != obj.upPk) { // 此时parent = 当前需创建的对象,并为{}/已有的对象
		    up = subUppk(obj.upPk, up);
		    if (parent) {
			parent.childrens = $.extend(parent.childrens, {});
			parent = $.extend(parent.childrens[up], {});
		    } else {
			parent = $.extend(that.menuData[up], {});
		    }
		    if (obj.url != undefined && obj.url != "") {
			if (obj.url.indexOf("/") != 0) {
			    obj.url = "/"+obj.url;
			}
			if ((parent.url == undefined || parent.url == "")) {
			    parent.url = obj.url;
			}
		    }
		}
		// 如果父级不存在子集则创建一个空子集
		var childrens = parent.childrens = $.extend(parent.childrens,
			{});
		// 将对象添加为子集
		childrens[o.pk] = $.extend(childrens[o.pk], o);
	    }
	    /**
	     * [subUppk 截取上级编码] 如果parentPk为null则截取顶级pk
	     * 
	     * @param {[String]}
	     *                upPk [目标上级编码]
	     * @param {[String]}
	     *                parentPk [当前遍历到的上级编码]
	     * @return {[type]} [description]
	     */
	    function subUppk(upPk, parentPk) {
		var endLength = parentPk == undefined ? 2
			: (parentPk.length + 2 < upPk.length ? parentPk.length + 2
				: upPk.length);
		return upPk.substr(0, endLength);
	    }
	}
    },
    events : function() {
	var that = this;

	this.submitBtn.on('click', function() {
	    if ($(this).data('validator') == 0) {
		return false;
	    }
	    that.login();
	    return false;
	});

	this.captchaImg.on('click', function() {
	    $("#captcha").val("");
	    var imgSrc = that.captchaImg.find('img').attr('src');
	    that.captchaImg.find('img').attr('src', '');
	    that.captchaImg.find('img').attr('src', imgSrc);
	});
    }

}

var login = new Login();
login.init();

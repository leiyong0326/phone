/*
 * Date: 2016.06.27
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */

define(['jquery', 'utils','jquery.cookie','json2'],
function($, utils) {
    // 左侧菜单栏
    function Navbar() {}

    Navbar.prototype = {
        // 初始化
        init: function(options) {
            this.options = $.extend({
                title: '分销管理系统'
            },
            options);

            this.data = {
                title: this.options.title,
                curName: document.location.pathname.indexOf(utils.baseUrl+'src/index.html') != -1 ? '': 'cy-small'
            };
            this.temp = '<header class="navbar cy-fixed ${curName} cy-animation-head"><h1 class="brand"><a href="/src/index.html">${title}</a></h1><nav class="navbar-collapse" data-cy="nav"></nav><div class="submenu cy-abs" data-nav="menu"></div></header>';
            var that = this;
            //自动登录时将信息存储到cookies
            var userInfo = $.cookie('autoLoginUser');
            if(userInfo){
        	utils.clearSession();
        	sessionStorage.setItem(utils.loginUser,userInfo);
            	loginUser = userInfo;
            }
    	    var loginUser = sessionStorage.getItem(utils.loginUser);
            var user = $.parseJSON(loginUser);
            var cacheMenu = undefined;
            if(user){
        	cacheMenu = $.parseJSON(sessionStorage.getItem(user.loginName));
            }
            if(cacheMenu){
                this.menuData = cacheMenu;
                that.createDom();
            }else{
                 utils.ajax({
                    url: "sysMenu/findMenuList",
                    success: function(data) {
                        if (data.success == true) {
                            that.parseMenu(data.obj);
                            if(user){
                        	sessionStorage.setItem(user.loginName,JSON.stringify(that.menuData));
                            }
                            that.createDom();
                        }
                    }
                });
            }           
        },
        //解析为menu对象
        parseMenu: function(obj) {
            this.menuData = {};
            var that = this;
            if (obj != null && obj instanceof Array) {
                for (var i = 0;i<obj.length;i++) {
                    var o = obj[i];
                    //判断对象是否是子集,是子集则拥有upPk属性
                    if (o.upPk == undefined || o.upPk == "") {
                        //是父级则生成父级对象
                        that.menuData[o.pk] = $.extend(that.menuData[o.pk], o);
                    } else {
                        parseChilds(o);
                    }
                }
                /**
                 * [parseChilds 保存子集]
                 * 约定每2位字符串为一个上级
                 * @param  {[Object]} obj [子集对象]
                 * @return {[type]}     [description]
                 */
                function parseChilds(obj) {
                    var parent = undefined;
                    var up = undefined;
                    //循环创建/读取父级
                    while (up != obj.upPk) { //此时parent = 当前需创建的对象,并为{}/已有的对象
                        up = subUppk(obj.upPk, up);
                        if (parent) {
                            parent.childrens = $.extend(parent.childrens, {});
                            parent = $.extend(parent.childrens[up], {});
                        } else {
                            parent = $.extend(that.menuData[up], {});
                        }
                        if(obj.url!=undefined&&obj.url!=""){
                            if(obj.url.indexOf(utils.baseUrl)!=0){
                                obj.url = utils.baseUrl + obj.url;
                            }
                            if((parent.url==undefined||parent.url=="")){
                                parent.url = obj.url;
                            }
                        }
                    }
                    //如果父级不存在子集则创建一个空子集
                    var childrens = parent.childrens = $.extend(parent.childrens, {});
                    //将对象添加为子集
                    childrens[o.pk] = $.extend(childrens[o.pk], o);
                }
                /**
                 * [subUppk 截取上级编码]
                 * 如果parentPk为null则截取顶级pk
                 * @param  {[String]} upPk     [目标上级编码]
                 * @param  {[String]} parentPk [当前遍历到的上级编码]
                 * @return {[type]}          [description]
                 */
                function subUppk(upPk, parentPk) {
                    var endLength = parentPk == undefined ? 2 : (parentPk.length + 2<upPk.length?parentPk.length+2:upPk.length);
                    return upPk.substr(0, endLength);
                }
            }
        },
        // 创建Dom
        createDom: function() {
            if(!this.menuData||JSON.stringify(this.menuData)=="{}"){
        	window.location.href = '/src/index.html';
        	return;
            }
            var wrap = $('body'),
            el = $(utils.v(this.temp, this.data));

            var nav = new Nav();

            nav.init({
                nav: el.find('[data-cy="nav"]'),
                menu: el.find('[data-nav="menu"]')
            },
            this.menuData);

            wrap.prepend(el);

            setTimeout(function() {
                el.removeClass('cy-animation-head');
            },
            10);
        }
    };

    // 创建子菜单
    function Nav() {};

    Nav.prototype = {
        // 初始化
        init: function(options, menuData) {
            this.options = $.extend({
                nav: $('[data-cy="nav"]'),
                menu: $('[data-nav="menu"]')
            },
            options);

            this.data = menuData; // 菜单数据
            this.columnKey = document.querySelector('body').dataset.column; // 当前栏目key
            this.pageKey = document.querySelector('body').dataset.page; // 当前页面key
            var dataIndex = this.isIndexPage(this.data);

            if (dataIndex !== false) {
                this.data[dataIndex].active = 'active';
            }
            this.createNav();

            if (this.hasMenu(this.data, this.columnKey)) {
                this.options.menu.show();
                var text = this.data[dataIndex] == undefined?'菜单':this.data[dataIndex].text;
                this.options.menu.html('<span>' + text + '</span>');
                this.options.menu.append(this.createMenu(this.data[dataIndex]));
            }
        },

        // 判断是否当前目录
        isIndexPage: function(data) {
            if(data!=undefined&&data instanceof Object){
                for(var o in data){
                    if(data[o].name===this.columnKey){
                        return o;
                    }
                }
            }
            return false;
        },

        // 判断是否存在子目录
        hasMenu: function(data) {
            if(data!=undefined&&data instanceof Object){
                for(var o in data){
                    if(data[o].name===this.columnKey){
                        return data[o].childrens!=undefined;
                    }
                }
            }
            return false;
        },

        // 创建主菜单
        createNav: function() {
            var wrap = $('<ul class="navbar-list"></ul>'),
            str = '',
            data = this.data,
            isActive = false; // 是否当前栏目
            for (var o in data) {
                str += this.createLi(data[o]);
            }

            wrap.html(str);

            this.options.nav.html(wrap);
        },

        // 创建子菜单
        createMenu: function(data) {
            var str = '<ul>',
            curName = '';
            if (data != undefined && data.childrens != undefined) {
                var childrens = data.childrens;
                for (var c in childrens) {
                    var o = childrens[c]; //子集菜单对象
                    if (o.name === this.pageKey) {
                        curName = 'active';
                    } else {
                        curName = '';
                    }
                    str += '<li class="' + curName + '"><a href="' + o.url + '">' + o.text + '</a>';
                    if (o.childrens) {
                        str += this.createMenu(o);
                    }
                    str += '</li>';
                }
            }
            str += '</ul>';

            return str;
        },

        // 创建单个li
        createLi: function(data) {
            var data = data,
            temp = '<li class="${active}" data-name="${text}"><a href="${url}">${i}<span>${text}</span></a></li>';

            // 是否有小图标
            data.i = data.icon ? '<i class="cy-icon-' + data.icon + '"></i>': '';

            return utils.v(temp, data);
        }

    };

    var navbar = new Navbar();
    navbar.init();

    return navbar;

});
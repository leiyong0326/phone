/*
 * author: luoxu-xu.github.io;
 * date: 2016.06.30;
 * about: 一些jquery插件
 * version: 1.0.0
 */

define(['jquery', 'sparkMd5', 'jquery.cookie'], function ($, SparkMD5) {

    var loginUser = "loginUser",
        baseUrl = "/";

    // Array.find的Polyfill
    if (!Array.prototype.find) {
        Object.defineProperty(Array.prototype, 'find', {
            enumerable: false,
            configurable: true,
            writable: true,
            value: function (predicate) {
                if (this == null) {
                    throw new TypeError('Array.prototype.find called on null or undefined');
                }
                if (typeof predicate !== 'function') {
                    throw new TypeError('predicate must be a function');
                }
                var list = Object(this);
                var length = list.length >>> 0;
                var thisArg = arguments[1];
                var value;

                for (var i = 0; i < length; i++) {
                    if (i in list) {
                        value = list[i];
                        if (predicate.call(thisArg, value, i, list)) {
                            return value;
                        }
                    }
                }
                return undefined;
            }
        });
    }

    // Array.findIndex的Polyfill
    if (!Array.prototype.findIndex) {
        Array.prototype.findIndex = function (predicate) {
            if (this === null) {
                throw new TypeError('Array.prototype.findIndex called on null or undefined');
            }
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }
            var list = Object(this);
            var length = list.length >>> 0;
            var thisArg = arguments[1];
            var value;

            for (var i = 0; i < length; i++) {
                value = list[i];
                if (predicate.call(thisArg, value, i, list)) {
                    return i;
                }
            }
            return -1;
        };
    }

    // 判断是否是function
    function isFunction(fn) {
        return typeof fn == 'function';
    }

    // 复选框
    function Checkbox() {}

    Checkbox.prototype = {

        // 初始化
        init: function () {
            this.refresh();
            this.events();
        },

        // 刷新一下
        refresh: function () {
            this.elements = $('.cy-checkbox input[type="checkbox"]');

            for (var i = 0, len = this.elements.length; i < len; i++) {
                if (this.elements.eq(i).attr('checked')) {
                    this.elements.eq(i).val(1);
                    this.elements.eq(i).parent().addClass('checked');
                } else {
                    this.elements.eq(i).val(0);
                }
            }
        },

        // 控制器
        events: function () {
            var _ = this;
            $(document).on('click', '.cy-checkbox', function () {
                if (window.checkType == 'stop') {
                    return;
                }
                if ($(this).attr('disabled')) {
                    return;
                }
                if ($(this).hasClass('checked')) {
                    $(this).removeClass('checked');
                    $(this).children().removeAttr('checked').val(0);
                    isChecked = false;
                } else {
                    $(this).addClass('checked');
                    $(this).children().attr('checked', 'checked').val(1);
                    isChecked = true;
                }

                return false;
            });
        }
    };

    var checkbox = new Checkbox();
    checkbox.init();


    // 公共插件
    var utils = {};

    // 字符串模板
    function view(str, data) {
        var code = '';

        if (Array.isArray(data)) {
            // 传入的是数组
            data.map(function (item, index) {
                code += render(str, item, index);
            });
        } else {
            code += render(str, data, 0);
        }

        function render(str, obj, index) {
            var reg = {
                hasAttr: /^\${boolean-(\w+):(\w+)}/g, // 带有属性的布尔值判断
                hasBoolean: /^\${boolean:(\w+)}/g // 布尔值属性
            };

            return str.replace(/(\${\w+})|(\${boolean(.+)})|(\${n:index})/g, function (attr) {
                var hasAttr = reg.hasAttr.exec(attr),
                    hasBoolean = reg.hasBoolean.exec(attr);

                // 求索引值 从1开始计算
                if (attr === '${n:index}') {
                    return index + 1;
                }

                if (hasAttr && obj[hasAttr[2]]) {

                    // 返回属性键值对
                    return hasAttr[1] + '="' + obj[hasAttr[2]] + '"';
                } else if (hasBoolean && obj[hasBoolean[1]]) {

                    // 返回属性值
                    return obj[hasBoolean[1]];
                }

                var s = obj[attr.replace(/\$|{|}/g, '')];
                return (s + '') === 'undefined' ? '' : s;
            });
        }

        return code;
    }

    // 日期格式化 YYYY-MM-DD mm:ss
    function dateFormat(date, type) {
        var d = null,
            dates = null,
            reg = null,
            types = type || 'YYYY-MM-DD mm:ss';

        if (date) {
            d = new Date(date);
        } else {
            return;
        }

        dates = {
            'Y+': d.getFullYear(),
            'M+': d.getMonth() + 1,
            'D+': d.getDate(),
            'h+': d.getHours(),
            'm+': d.getMinutes(),
            's+': d.getSeconds()
        };

        for (var attr in dates) {
            dates[attr] = dates[attr] < 10 ? ('0' + dates[attr]) : dates[attr];
            reg = new RegExp(attr, 'g');
            types = types.replace(reg, dates[attr]);
        }

        return types;

    }

    // 自动创建delet Modal prompt窗口
    function DelModal() {}

    DelModal.prototype = {

        // 初始化
        init: function (options) {
            this.options = $.extend({
                class: 'cy-de-modal' + Math.random(),
                title: '温馨提示',
                content: '你确定要删除这条信息？'
            }, options);
            this.createDom();

            return this.el;
        },

        // 创建dom
        createDom: function () {
            this.temp = '<div class="am-modal am-modal-confirm ${class}"><div class="am-modal-dialog"><div class="am-modal-hd">${title}</div><div class="am-modal-bd">${content}</div><div class="am-modal-footer"><span class="am-modal-btn" data-am-modal-cancel>取消</span><span class="am-modal-btn" data-am-modal-confirm>确定</span></div></div></div>';
            this.el = $(view(this.temp, this.options));

            $('body').append(this.el);
        }

    };

    // 提示框
    function Alert() {}

    Alert.prototype = {

        // 初始化
        init: function (obj) {
            this.options = $.extend({
                text: '服务器提出了一个问题', // 提示信息
                type: null, // 提示类型 success / warning / danger
                hideAuto: true, // 是否自动关闭
                hideTime: 2000 // 自动关闭时间
            }, obj);

            this.data = {
                elClassName: this.options.type ? 'cy-alert-' + this.options.type : '',
                text: this.options.text
            }
            if ($('body').find('.cy-alert').length == 1) {
                this.el.find('.cy-alert-content').html(this.options.text);
            } else {
                this.temp = '<div class="cy-alert ${elClassName}"><div class="cy-alert-content">${text}</div><span class="cy-alert-close" title="关闭">×</span></div>';
                this.createDom();
            }
        },

        // 创建虚拟dom
        createDom: function () {
            var el = $(view(this.temp, this.data));

            this.el = el;
            $('body').append(this.el);
            this.events();
        },

        events: function () {
            var el = this.el;

            // 如果自动隐藏
            if (this.options.hideAuto) {
                setTimeout(function () {
                    el.remove();
                }, this.options.hideTime);
            }

            el.find('.cy-alert-close').bind('click', function () {
                el.remove();
            });
        }
    };

    var a = new Alert();

    // 日期插件
    function datepick(start, end, options) {
        var startTime = +new Date($(start).val()),
            endTime = +new Date($(end).val()),
            date = [startTime, endTime],
            alert = new Alert(),
            el = [$(start), $(end)],
            _options = $.extend({
                format: 'yyyy-mm-dd',
                startView: 2,
                minView: 1,
                maxView: 2
            }, options),
            datePicker = el.filter(function (item) {
                if (item.length > 0) {
                    return item.datetimepicker(_options);
                }
            });

        datePicker.map(function (item, index) {
            item.on('changeDate', function () {
                item.datetimepicker('hide');
                date[index] = +new Date(this.value);

                if (date.length == 2) { // 有开始和结束时间
                    if (index == 0 && date[1] && date[0] >= date[1]) {
                        alert.init({
                            text: '开始时间必须小于结束时间',
                            type: 'warning'
                        });
                        this.value = '';
                    } else if (index == 1 && date[0] && date[0] >= date[1]) {
                        alert.init({
                            text: '结束时间必须大于开始时间',
                            type: 'warning'
                        });
                        this.value = '';
                    }
                }
            });
        });
    }

    // 表单验证
    function Validator(options) {
        this.options = $.extend({
            content: $('[data-cy-validator]')
        }, options);

        this.elements = this.options.content.find('[name]'); // 需要匹配的元素
        this.submitBtn = this.options.content.find('[data-type="submit"]'); // 提交按钮
        this.len = this.elements.length; // 需要匹配的元素个数
        this.validatorData = {}; // 元素匹配表
        this.startDate = +new Date(); // 记录时间
        this.checkedEl = null; // 当前选中的对象
        this.isShow = false; // 提示框是否显示状态
        this.submitBtn.data('validator', 0); // 初始化按钮不可点击.

        this.alertElement = $('<div class="cy-input-alert"></div>'); // 提示信息
        $('body').append(this.alertElement);

        // 初始化匹配一次
        for (var i = 0, len = this.elements.length; i < len; i++) {
            this.test(this.elements.eq(i));
        }

        this.events();
    }

    Validator.prototype = {

        // 初始化
        init: function () {},

        // 验证
        test: function (element) {
            var _ = element,
                isMatch = true,
                _val = _.val();

            this.types = [ // 默认一些类型的匹配
                {
                    type: 'number', // 类型
                    reg: /^\d+$/ig, // 匹配方式
                }, {
                    type: 'email',
                    reg: /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/ig,
                }, {
                    type: 'tel',
                    reg: /^1[3|4|5|7|8]\d{9}$/ig,
                }, {
                    type: 'url',
                    reg: /^[a-zA-z]+:\/\/(\w+(-\w+)*)(\.(\w+(-\w+)*))*(\?\S*)?$/ig,
                },
            ];

            for (var i = 0, len = this.types.length; i < len; i++) {
                if (element.attr('type') === this.types[i].type && !this.types[i].reg.test(_val) && _.attr('required')) {
                    isMatch = false;
                }
            }

            // 输入字段最大值
            if (_.attr('max') && 1 * _val > 1 * _.attr('max')) {
                isMatch = false;
            }

            // 输入字段最小值
            if (_.attr('min') && 1 * _val < 1 * _.attr('min')) {
                isMatch = false;
            }

            // 输入字段最大长度
            if (_.attr('maxlength') && _val.length > 1 * _.attr('maxlength')) {
                isMatch = false;
            }

            // 正则匹配
            if (_.attr('pattern') && !(new RegExp(_.attr('pattern'), 'ig')).test(_val)) {
                isMatch = false;
            }

            // 必填
            if ((_.attr('required') && (/^\s+$/ig.test(_val)) || (_val && _val.length == 0))) {
                isMatch = false;
            }

            if (isMatch) {
                this.validatorData[_.attr('name')] = 1;
            } else {
                this.validatorData[_.attr('name')] = 0;
            }

            this.refreshStatus();
            return isMatch;
        },

        // 更新按钮状态
        refreshStatus: function () {
            for (var i = 0; i < this.len; i++) {
                if (this.validatorData[this.elements.eq(i).attr('name')] != 1) {
                    this.submitBtn.data('validator', 0);
                    return false;
                }
            }
            this.submitBtn.data('validator', 1);
        },

        // 设置样式
        setStyle: function (el) {
            el = (el[0].tagName == 'SELECT') ? el.next() : el;

            this.style = {
                top: el.offset().top - $(window).scrollTop() + el.outerHeight(),
                left: el.offset().left
            }
            this.alertElement.css(this.style);
        },

        // 正确提示
        success: function (el) {
            el.removeClass('cy-input-error');
            this.alertElement.hide();
            this.isShow = false;
        },

        // 提示
        error: function (el) {
            el.addClass('cy-input-error');
            this.alertElement.text(el.data('error')).fadeIn();
            this.setStyle(el);
            this.isShow = true;
            this.startDate = +new Date();
        },

        // 操作
        events: function () {
            var that = this,
                el = null;

            // 固定提示框位置
            $(window).on('scroll', function () {
                if (!that.checkedEl) {
                    return;
                }
                that.setStyle(that.checkedEl);
            });

            this.elements.on('blur', function () {
                that.checkedEl = $(this);
                if (!that.test($(this))) {
                    that.error($(this));
                } else {
                    that.success($(this));
                }
            });

            this.submitBtn.on('click', function () {
                for (var i = 0, len = that.elements.length; i < len; i++) {
                    if (!that.test(that.elements.eq(i))) {
                        that.error(that.elements.eq(i));
                        that.elements.eq(i).focus();
                        that.checkedEl = that.elements.eq(i);
                        return;
                    }
                }
            });

            $(document).on('click', function (e) {
                if (+new Date() - that.startDate < 300) {
                    return;
                }
                if ('INPUT TEXTAREA SELECT'.indexOf(e.target.nodeName) == -1 && that.isShow) {
                    that.alertElement.hide();
                    that.isShow = false;
                }
            });

        }
    };

    // 自动获取表单所有属性值，以键值对的方式返回，name:value
    function getFormData(el) {
        this.content = el;
        this.elements = this.content.find('[name]'); // 表单元素
        this.data = {}, // 数据对象
            _this = null; // 当前对象

        for (var i = 0, len = this.elements.length; i < len; i++) {
            _this = this.elements.eq(i);

            if (_this.attr('name')) {
                if (_this.attr('type') === 'checkbox') { // 复选框
                    if (_this[0].checked) {
                        this.data[_this.attr('name')] = 1;
                    } else {
                        this.data[_this.attr('name')] = 0;
                    }
                } else if (_this.val() !== '' && _this.val() != -1) {
                    if (_this.data('type') === 'date') { // 日期
                        this.data[_this.attr('name')] = new Date(_this.val());
                    } else {
                        this.data[_this.attr('name')] = _this.val();
                    }
                }
            }
        }

        return this.data;
    };

    // 封装ajax
    function ajax(options) {

        ['url', 'data', 'success'].map(function (i) {
            if (!options[i]) {
                new Error('您的参数缺少：' + i);
            }
        });

        // 跳转路由
        var route = [{
            title: '翼翔管理系统',
            url: 'index.html'
        }, {
            title: '登录',
            url: 'login.html'
        }, {
            title: '404',
            url: 'error/404.html'
        }, {
            title: '400',
            url: 'error/400.html'
        }, {
            title: '500',
            url: 'error/500.html'
        }, {
            title: 'error',
            url: 'error/error.html'
        }, {
            title: 'noperms',
            url: 'error/noperms.html'
        }, ];

        $.ajax({
            type: 'post',
            contentType: options.contentType ? options.contentType : 'application/x-www-form-urlencoded',
            url: baseUrl + options.url,
            data: options.data,
            success: function (data) {
                var jsonData = null;
                var _url = 'index.html';

                if (typeof data == 'string') {
                    if (data.indexOf('<!DOCTYPE html>') == 0) {
                        for (var attr in route) {
                            if (data.indexOf('<title>' + route[attr].title + '</title>') != -1) {
                                _url = attr.url;
                                break;
                            }
                        }
                        window.location.href = baseUrl + 'src/' + _url;
                        return;
                    } else {
                        jsonData = JSON.parse(data);
                    }
                }else{
                    jsonData = data;
                }
                if (jsonData.success && isFunction(options.success)) {
                    options.success.call(this, jsonData);
                } else {
                    a.init({
                        text: jsonData.msg,
                        type: 'warning',
                        hideAuto: false
                    });

                    if (options.errorCallback && isFunction(options.errorCallback)) {
                        options.errorCallback(jsonData);
                    }
                }
            },
            error: function (msg) {
                a.init({
                    text: '哎吖，服务器提了一个问题',
                    type: 'danger',
                    hideAuto: false
                });
            }
        });
    }

    // 解析url参数
    function parseUrl(str) {
        var _str = str ? str : window.location.search.substring(1),
            obj = _str.split('&'),
            data = {};

        obj.map(function (i) {
            data[i.split('=')[0]] = i.split('=')[1];
        });

        return data;
    }

    // 查找对应数据
    function find(id, data, attr) {
        var _ = attr || 'id';

        return data.find(function (element) {
            return element[_] == id;
        });
    }

    // 查找对应数据的索引
    function findIndex(id, data, attr) {
        var _ = attr || 'id';

        return data.findIndex(function (element) {
            return element[_] == id;
        });
    }

    // 自动创建下拉菜单
    function createSelect(el, options) {
        var element = el, // 下拉菜单容器
            _ = $.extend({
                def: '所有', // 默认的属性
                type: 'attr', // 添加还是覆盖 append
                data: [], // 数据
                selected: undefined, // 选中项
                attr: ['pk', 'name', 'content'], // value和text对应的值
                componentDidMount: function () {
                } // 渲染完毕
            }, options),
            str = '';

        if (_.selected!=undefined&&_.selected!=='') {
            for (var i = 0, len = _.data.length; i < len; i++) {
                if (_.data[i][_.attr[0]] == _.selected) {
                    _.data[i].selected = 'selected';
                }
            }
        }
        if (_.removed!==undefined&&_.removed!=='') {
            for (var i = 0; i < _.data.length; i++) {
                if (_.data[i][_.attr[0]] == _.removed) {
                    _.data.splice(i,1);
                }
            }
        }
        if (_.type == 'attr') {
            str = '<option value="" ' + (_.selected ? '' : 'selected') + '>' + _.def + '</option>';
        } else {
            str = element.html();
        }

        str += view('<option value="${' + _.attr[0] + '}" data-content="${' + _.attr[2] + '}" ${selected}>${' + _.attr[1] + '}</option>', _.data);

        element.html(str);

        setTimeout(function () {
            _.componentDidMount.call(element);
        }, 100);
    }

    // 封装成jq插件
    $.fn.extend({

        // 上传组件
        createSelect: function (options) {
            $.each(this, function () {
                if ($(this).data('createselect') === '1') {
                    return;
                }
                createSelect($(this), options);
            });
        }
    });

    // 折叠面板
    function CyCollapse(options) {
        this.options = $.extend({
                element: $('.illegal-panel-group'), // 容器
                showDefault: false // 是否默认展开第一个
            }, options),
            this.lists = this.options.element.find('.illegal-panel-item'); // 列表
        this.refresh();
        this.events();
    }

    CyCollapse.prototype = {
        refresh: function () {
            this.lists = this.options.element.find('.illegal-panel-item'); // 列表
            if (this.options.showDefault) {
                this.lists.eq(0).find('.illegal-panel-head').data('collapse', 'show').next().show();
            }
        },
        events: function () {
            this.options.element.on('click', '.illegal-panel-head', function () {
                if ($(this).data('collapse') != 'show') {
                    $(this).data('collapse', 'show')
                        .next().show();
                    $(this).parent().siblings().find('.illegal-panel-head')
                        .data('collapse', 'hide')
                        .next().hide();
                } else {
                    $(this).data('collapse', 'hide')
                        .next().hide();
                }
            });
        }
    }

    function clearSession() {
        //清空缓存
        $.removeCookie('autoLoginUser', {
            path: '/'
        });
        var loginUser = sessionStorage.getItem(utils.loginUser);
        sessionStorage.removeItem(utils.loginUser);
        if (loginUser) {
            var user = $.parseJSON(loginUser);
            sessionStorage.removeItem(user.loginName)
        }
    }

    function getPage(size, total) {
        if (size != 0 && total != 0 && !isNaN(size) && !isNaN(total) && total > size) {
            var count = Math.ceil(total / size);
            if (!isNaN(count)) {
                return count;
            }
        }
        return 1;
    }


    // 提示信息
    function prompt(el, options) {
        var _prompt = el.find('.cy-prompt');

        if (options == 'close') {
            // 删除提示
            if (_prompt.length > 0) {
                _prompt.remove();
            }
            return;
        }

        if (_prompt.length > 0) {
            // 已经有提示信息
            return;
        }

        var _ = $.extend({
            content: '暂无数据', // 提示内容
            type: 'default' // 默认是没有数据的提示 其他类型：error
        }, options);
        var temp = $('<div class="cy-prompt">' + _.content + '</div>');

        el.html(temp);
    }

    $.fn.extend({
        prompt: function (options) {
            var _this = this;
            prompt(_this, options);
        }
    });

    // 升级版表单验证
    function validation(el) {
        if (!el) {
            throw new Error('该元素不存在，亲，你逗我吗？');
        }
        var oInput = el.find('[name]');
        var _val = null;
        var item = null;
        var i = 0;
        var len = oInput.length;

        // 验证
        var doTest = (ele, _val) => {
            var regs = [
                _val.length < 1,
                ele.max && _val > ele.max,
                ele.min && _val < ele.min,
                ele.maxlength && _val.length > ele.maxlength,
                ele.minlength && _val.length < ele.minlength,
                ele.pattern && !(new RegExp(ele.pattern, 'g')).test(_val)
            ];

            for (var j = 0; j < regs.length; j++) {
                if (regs[j]) {
                    var msgText = ele.dataset.error ? ele.dataset.error : ele.placeholder + '填写错误';
                    a.init({
                        text: msgText,
                        type: 'warning'
                    });
                    return false;
                }
            }

            return true;
        }

        for (i = 0; i < len; i++) {
            item = oInput[i];
            _val = item.value;

            // 必填或者已经输入才会验证
            if (item.required || _val.trim.length > 0) {
                if (!doTest(item, _val)) {
                    return false;
                }
            }
        }

        return true;
    }

    // 提示框
    function msg(options) {
        var _options = $.extend({
            text: '正在加载中',
            type: 'loading'
        }, options);

        var msgHTML = '<div class="cy-loading"><i></i>' + _options.text + '</div>';
    }

    // 工具集
    utils = {
        delModal: new DelModal,
        v: view,
        dateFormat: dateFormat,
        alert: a.init.bind(a),
        datepick: datepick,
        baseUrl: baseUrl,
        loginUser: loginUser,
        ajax: ajax,
        parseUrl: parseUrl,
        getFormData: getFormData,
        Validator: Validator,
        checkbox: checkbox,
        find: find,
        findIndex: findIndex,
        CyCollapse: CyCollapse,
        clearSession: clearSession,
        getPage: getPage,
        validation: validation
    }

    return utils;
});

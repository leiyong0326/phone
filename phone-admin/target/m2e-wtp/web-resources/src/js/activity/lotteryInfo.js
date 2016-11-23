/*
 * Date: 2016.07.04
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */


define(['../require.config'], function (r) {

    require(['utils', 'template', 'temp', 'config', 'datetimepicker', 'umeditor', 'umeditorConfig', 'navbar', 'amazeui', 'updataFile'], function (utils, Temp, extendsFn, config, datepicker) {

        var activityInfoPrototype = {

            // 更新数据
            upDate: function () {
                var that = this;

                utils.ajax({
                    url: config.api.searchActInfo,
                    data: {
                        pk: this.pageId
                    },
                    success: function (data) {
                        var _i = '';
                        for (var i = 0; i < 4; i++) {
                            if (i > 0) {
                                _i = i + 1;
                            }
                            if (data.obj['attendPkM' + _i]) {
                                that.checkUserId.push(data.obj['attendPkM' + _i]);
                            }
                            if (data.obj['clazzPkM' + _i]) {
                                that.checkTypeId.push(data.obj['clazzPkM' + _i]);
                            }
                        }

                        that.data = $.extend(that.data, that.formatData(data.obj));
                        that.getUser();
                        that.getOrgClass();
                        that.render();
                    }
                });
            },

            formatData: function (data) {
                return $.extend(data, {
                    banner: data.banner == 1 ? 'checked' : '',
                    forcePay: data.forcePay == 1 ? 'checked' : '',
                    startTime: utils.dateFormat(data.startTime, 'YYYY-MM-DD'),
                    endTime: utils.dateFormat(data.endTime, 'YYYY-MM-DD')
                });
            },

            // 获取当前4S店所有的车系
            getOrgClass: function () {
                var that = this;

                utils.ajax({
                    url: config.api.searchShopOrgClass,
                    data: {
                        pageNum: 1,
                        pageSize: 100
                    },
                    success: function (data) {
                        var _arr = that.checkTypeId;
                        var _data = [{
                            name: "不限",
                            pk: "9999999"
                        }].concat(data.obj);
                        that.data.orgClasses = _data.map(function (item) {
                            if(that.checkTypeId.length == 0 && item.pk == '9999999') {
                                item.checked = 'checked';
                            }
                            if(that.checkTypeId.length > 0) {
                                item.checked = (_arr.indexOf(item.pk) != -1 ? 'checked' : '');
                            }
                            return item;
                        });
                        that.render();
                    }
                });
            },

            // 获取参与对象
            getUser: function () {
                var that = this;

                utils.ajax({
                    url: config.api.searchUser,
                    data: {
                        pageNum: 0,
                        pageSize: 0
                    },
                    success: function (data) {
                        var _arr = that.checkUserId;
                        var _data = [{
                            name: "不限",
                            pk: "9999999"
                        }].concat(data.obj);
                        that.data.users = _data.map(function (item) {
                            if(that.checkUserId.length == 0 && item.pk == '9999999') {
                                item.checked = 'checked';
                            }
                            if(that.checkUserId.length > 0) {
                                item.checked = (_arr.indexOf(item.pk) != -1 ? 'checked' : '');
                            }
                            return item;
                        });
                        that.render();
                    }
                });
            },

            // 保存
            save: function () {
                var that = this,
                    options = utils.getFormData($('.cy-form')),
                    _validation = utils.validation($('.cy-form'));

                if (!_validation) {
                    return;
                }

                this.checkUserId.map(function (item, index) {
                    var _i = '';
                    if (index > 0) {
                        _i = index + 1;
                    }
                    options['attendPkM' + _i] = item;
                });

                this.checkTypeId.map(function (item, index) {
                    var _i = '';
                    if (index > 0) {
                        _i = index + 1;
                    }
                    options['clazzPkM' + _i] = item;
                });

                if (this.pageId) {
                    options.pk = this.data.pk;
                }

                options.typePk = 2;

                utils.ajax({
                    url: config.api.saveActInfo,
                    data: options,
                    success: function (data) {
                        if (data.success) {
                            utils.alert({
                                text: '保存成功',
                                type: 'success'
                            });
                            history.back();
                        } else {
                            utils.alert({
                                text: data.msg,
                                type: 'danger'
                            });
                        }
                    }
                });
            },

            // 奖项设置
            setGift: function () {
                window.location.href = '../prize/prizedraw.html?id=' + this.data.giftPkM;
            },

            // 选择用户类型
            checkUser: function (evt) {
                var that = this,
                    _this = $(evt);

                if (_this.hasClass('checked')) {
                    if(evt.dataset.id == '9999999') {
                        that.checkUserId = [];
                        _this.siblings().removeClass('checked');
                        return;
                    }else {
                        _this.parent().children(':eq(0)').removeClass('checked');
                        if (this.checkUserId.length > 3) {
                            utils.alert({
                                text: '最多可选择4个',
                                type: 'warning'
                            });
                            _this.removeClass('checked');
                            return;
                        }
                    }
                    that.checkUserId.push(_this.data('id'));
                } else {
                    if(evt.dataset.id == '9999999') {
                        return;
                    }
                    that.checkUserId.map(function (item, index) {
                        if (_this.data('id') == item) {
                            that.checkUserId.splice(index, 1);
                        }
                    });
                }
            },

            // 选择车辆类型
            checkType: function (evt) {
                var that = this,
                    _this = $(evt);

                if (_this.hasClass('checked')) {
                    if(evt.dataset.id == '9999999') {
                        that.checkTypeId = [];
                        _this.siblings().removeClass('checked');
                        return;
                    }else {
                        _this.parent().children(':eq(0)').removeClass('checked');
                        if (this.checkTypeId.length > 3) {
                            utils.alert({
                                text: '最多可选择4个',
                                type: 'warning'
                            });
                            _this.removeClass('checked');
                            return;
                        }
                    }
                    that.checkTypeId.push(_this.data('id'));
                } else {
                    that.checkTypeId.map(function (item, index) {
                        if (_this.data('id') == item) {
                            that.checkTypeId.splice(index, 1);
                        }
                    });
                }
            },

            // 获取抽奖模板列表
            getGiftModelList: function () {
                var that = this;

                utils.ajax({
                    url: config.api.searchModel,
                    data: {
                        pageNum: 0,
                        pageSize: 0
                    },
                    success: function (data) {
                        that.data.giftMasters = data.obj;
                        that.render();
                    }
                });
            },

            getInitialState: function () {
                var urlArgs = utils.parseUrl();
                this.pageId = urlArgs.id;

                if (this.pageId) {
                    // 编辑状态
                    this.upDate();
                } else {
                    this.getUser();
                    this.getOrgClass();
                }

                this.getGiftModelList();
                this.checkUserId = []; // 选中的用户类型
                this.checkTypeId = []; // 选中的车系
            },

            componentDidMount: function () {
                var that = this;
                var inputs = $('[data-temp="activity-info-content"] [name]');

                inputs.each(function () {
                    var _name = $(this).attr('name');

                    $(this).on('change', function () {
                        that.data[_name] = $(this).val();
                    });
                });

                // 设置下拉菜单选中属性
                var selects = $('[data-temp="activity-info-content"] .cy-select');
                var that = this;
                selects.each(function () {
                    var _name = $(this).attr('name');

                    if (that.data.hasOwnProperty(_name)) {
                        $(this).val(that.data[_name]);
                    }
                });

                // 日期插件, 避免多次创建
                $('.datetimepicker').remove();
                utils.datepick('.activity-start', '.activity-end', {
                    startView: 3,
                    minView: 2,
                    maxView: 4
                });

                $(this.refs.updatefile).upDataFile({
                    imgs: this.data.img ? this.data.img : null,
                    size: [750, 300],
                    success: function (data) {
                        that.data.img = data;
                    }
                });

            }
        };

        // 初始化
        (function initialization() {

            var power = { // 用户权限
                add: true,
                edit: true,
                del: true,
                edits: true
            };

            // 根据权限判断是否显示
            for (attr in power) {
                if (power.hasOwnProperty(attr) && power[attr]) {
                    $('[data-rel="' + attr + '"]').css({
                        display: 'inline-block'
                    });
                }

                if (!power[attr]) {
                    $('[data-rel="' + attr + '"]').remove();
                }
            }

            var ActivityInfo = extendsFn(activityInfoPrototype);
            var activityInfo = new ActivityInfo({
                id: '#activity-info-content',
                data: {
                    power: power
                }
            });

            // var pageRender = new PageRender(power);

        })();

    });

});

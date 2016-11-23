/*
 * Date: 2016.06.30
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */

define(['../require.config'],
function(r) {

    require(['jquery', 'format', 'warn', 'utils', 'template', 'pagination', 'datetimepicker', 'navbar', 'amazeui'],
    function($, fmt, w, utils, Temp, Page, datepicker) {

        function PageRender(options) {
            this.data = null; // 数据
            this.temp = null; // 模板
            this.checkId = []; // 选中id
            this.options = options;
            this.dataApi = {
                search: 'sysOrganization/find',
                // 查询接口
                update: 'sysOrganization/update',
                // 修改状态
                enable: 'sysOrganization/enable',
                // 禁用接口
                disable: 'sysOrganization/disable',
                // 查看接口
                searchOne: 'sysOrganization/get'
            };
            // 默认查询参数
            this.searchOptions = {
                pageNum: 1,
                pageSize: 10,
                order: 'enable desc,END_TIME asc'
            };
            this.enableModal = utils.delModal.init({ //  启用提示框
                title: w.t,
                content: w.enb
            });
            this.disableModal = utils.delModal.init({ //  禁用提示框
                title: w.t,
                content: w.dsb
            });

            // 按钮组合
            this.btns = {
                show: $('[data-btn="show"]'),
                add: $('[data-btn="add"]'),
                edit: $('[data-btn="edit"]')
            }

            var that = this;
            // 创建分页
		that.pagination = new Page({
			pageNo: that.searchOptions.pageNum,
			events: function(id) {
				that.searchOptions.pageNum = id;
				that.upData(function(data) {
					that.temp.data = that.dataFormat(data);
					that.temp.render();
				});
			}
		});
            // 第一次更新数据
            this.upData(function(data) {
                that.data = data;
                // 创建模板
                that.temp = new Temp({
                    tempId: 'group-list',
                    data: that.dataFormat(that.data.obj)
                });

                that.events();
            });
        }

        PageRender.prototype = {

            // 更新数据
            upData: function(callback) {
        	var that = this;
                utils.ajax({
                    url: this.dataApi.search,
                    data: this.searchOptions,
                    success: function(data) {
                        if (data.success) {
                            callback.call(this, data);
                            that.pagination.refreshData(that.searchOptions.pageNum);
                        }
                    }
                });
            },

            // 初始化添加数据
            add: function() {
                window.location.href = 'group_info.html?type=add';
            },

            // show
            show: function() {
                window.location.href = 'group_info.html?type=show&id=' + this.checkId[0];
            },

            // 初始化编辑数据
            edit: function() {
                window.location.href = 'group_info.html?type=edit&id=' + this.checkId[0];
            },

            // 分配权限,查找目标机构下的admin角色
            assign: function() {
                alert(that.checkId[0]);
            },
            changeState: function(btn) {
                var pk = btn.data('id');
                var text = btn.html();
                var url = undefined;
                var prompt = undefined;
                var removeClass, addClass, stateText;
                if (text == '禁用') {
                    text = '启用';
                    removeClass = 'cy-btn-orange';
                    addClass = 'cy-btn-green';
                    url = this.dataApi.disable;
                    prompt = w.dsbs;
                    stateText = fmt.status('0');
                } else {
                    text = '禁用';
                    addClass = 'cy-btn-orange';
                    removeClass = 'cy-btn-green';
                    url = this.dataApi.enable;
                    prompt = w.enbs;
                    stateText = fmt.status('1');
                }
                utils.ajax({
                    url: url,
                    data: {
                        pk: pk
                    },
                    success: function(data) {
                        if (data.success) {
                            btn.removeClass(removeClass).addClass(addClass);
                            btn.html(text);
                            btn.parent().prev().html(stateText);
                            utils.alert({
                                text: prompt,
                                type: 'danger'
                            });
                        }
                    }
                });
            },

            // 刷新列表数据
            refreshData: function() {
                var that = this;

                this.searchOptions.pageNum = 1;

                this.upData(function(data) {
                    if (data.success) {
                        that.temp.data = that.dataFormat(data.obj);
                        that.temp.render();
                    }
                });
            },

            // 选中
            check: function() {
                var that = this;

                this.temp.content.on('click', '.cy-checkbox',
                function() {
                    var _this = $(this);
                    if (!_this.hasClass('checked')) {
                        that.checkId.push(_this.data('id'));
                    } else {
                        that.checkId.map(function(item, index) {
                            if (_this.data('id') == item) {
                                that.checkId.splice(index, 1);
                            }
                        });
                    }
                });
            },

            // 格式化数据
            dataFormat: function(data) {
                var that = this;
                var listData = null;

                if (!Array.isArray(data)) {
                    return false;
                }

                return data.map(function(item) {
                    item.id = item.pk;
                    item.grantShow = fmt.display(that.options.edit);
                    item.endTime = fmt.date(item.endTime);
                    item.store = fmt.isTrue(item.store);
                    item.status = fmt.status(item.enable);
                    item.enableText = fmt.status(item.enable);
                    item.enableStyle = fmt.statusStyle(that.options.edit);
                    item.enableShow = fmt.display(that.options.edit);
                    item.enableBtnText = fmt.statusText(item.enable);
                    return item;
                });
            },

            // 控制
            events: function() {
                var that = this;

                this.check();

                // 事件组合
                this.elementEvents = {
                    show: this.show.bind(this),
                    add: this.add.bind(this),
                    edit: this.edit.bind(this),
                    //del : this.del.bind(this),
                    assign: this.assign.bind(this)
                };

                ['show', 'add', 'edit'].map(function(i) {
                    that.btns[i].on('click',
                    function() {
                        if (that.checkId.length == 0) {
                            utils.alert({
                                text: w.ms,
                                type: 'warning'
                            });
                            return;
                        }
                        if ((i == 'edit' || i == 'show') && that.checkId.length != 1) {
                            utils.alert({
                                text: w.ss,
                                type: 'warning'
                            });
                            return;
                        }

                        that.elementEvents[i]();
                    });
                });

                // 分配权限
                this.temp.content.on('click', '[data-btn="assign"]',
                function() {
                    that.checkId[0] = $(this).data('id');
                    that.elementEvents['assign']();
                });
                // 启用禁用
                this.temp.content.on('click', '.editB',
                function() {
                    that.changeState($(this));
                });

            }

        };

        // 初始化
        (function initialization() {
            var url = "sysMenu/findMenuFunc";
            var data = {
                menuPk: "SysOrganization"
            };
            utils.ajax({
                url: url,
                data: data,
                success: function(data) {
                    if (data.success) {
                        var power = { // 用户权限
                            edit: data.obj.indexOf("edit") >= 0,
                            del: data.obj.indexOf("del") >= 0,
                            show: data.obj.indexOf("show") >= 0
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

                        var pageRender = new PageRender(power);
                    }
                }
            });
        })();

    });

});

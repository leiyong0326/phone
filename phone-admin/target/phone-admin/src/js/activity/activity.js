/*
 * Date: 2016.07.04
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */


define(['../require.config'], function (r) {

    require(['jquery', 'format', 'utils', 'config', 'temp', 'pagination', 'datetimepicker', 'navbar', 'amazeui'], function ($, fmt, utils, config, extendsFn, Page, datepicker) {

        // 判断活动类型来源
        var pageType = /\/([a-z]+)\.html/.exec(window.location.href)[1];
        var typePk = {
            activity: {
                id: 0,
                power: 'ActMaster',
                pageInfo: 'activity_info.html'
            },
            buygroup: {
                id: 1,
                power: 'ActBuyGroup',
                pageInfo: 'buygroup_info.html'
            },
            lottery: {
                id: 2,
                power: 'ActLottery',
                pageInfo: 'lottery_info.html'
            }
        };

        var activityPrototype = {

            // 获取列表
            upDate: function() {
                var that = this;

                utils.ajax({
                    url: config.api.searchActList,
                    data: this.searchAjaxArgs,
                    success: function (data) {
                        that.data.actList = that.formatData(data.obj);
                        that.render();
                    }
                });
            },

            formatData: function(data) {
                var that = this;
                return  data.map(function(item) {
                    item.column = ['购车', '养车'][item.module];
                    item.edit = that.data.power.edit;
                    item.show = that.data.power.show;
                    item.status = ['已下架', '进行中'][item.enable];
                    item.statusColor = ['cy-red', ''][item.enable];
                    item.expiryDate = utils.dateFormat(item.startTime, 'YYYY-MM-DD') + '至' + utils.dateFormat(item.endTime, 'YYYY-MM-DD');
                    item.pulishTime = utils.dateFormat(item.pulishTime, 'YYYY-MM-DD');
                    return item;
                });
            },

            // 新增
            add: function() {
                window.location.href = this.infoPage + '?type=add';
            },

            // 编辑
            edit: function(evt) {
                window.location.href = this.infoPage + '?type=edit&' + 'id=' + evt.dataset.id;
            },

            // 删除
            del: function() {
                var that = this;
                utils.ajax({
                    url: config.api.delAct,
                    data: {
                        pk: this.checkId.join(',')
                    },
                    success: function (data) {
                        if (data.success) {
                            that.upDate();
                            utils.alert({
                                text: '删除成功',
                                type: 'success'
                            });
                        } else {
                            utils.alert({
                                text: data.msg,
                                type: 'danger'
                            });
                        }
                    }
                });
            },

            // 打开状态设置框
            setStatus: function(evt) {
        	if (+new Date() >= evt.dataset.endtime) {
                    utils.alert({
                        text: '活动有效期已过期，请修改后发布',
                        type: 'warning'
                    });
                    return;
                }
                if (evt.dataset.time != '') {
                    utils.alert({
                        text: '当前活动已经发布',
                        type: 'warning'
                    });
                } else {
                    this.checkId[0] = evt.dataset.id;
                    this.pulishModal.modal('open');
                }
            },

            // 状态设置
            pulish: function() {
                var that = this;
                utils.ajax({
                    url: config.api.changeActStatus,
                    data: {
                        pk: this.checkId[0]
                    },
                    success: function (data) {
                        if (data.success) {
                            that.upDate();
                            utils.alert({
                                text: '发布成功',
                                type: 'success'
                            });
                        } else {
                            utils.alert({
                                text: data.msg
                            });
                        }
                    }
                });
            },

            // 报名查询
            searchApply: function(evt) {
                window.location.href = this.applyPage + '?id=' + evt.dataset.id;
            },

            check: function(evt) {
                var that = this,
                    _this = $(evt);

                if (_this.hasClass('checked')) {
                    that.checkId.push(_this.data('id'));
                } else {
                    that.checkId.map(function (item, index) {
                        if (_this.data('id') == item) {
                            that.checkId.splice(index, 1);
                        }
                    });
                }
            },

            // 查询
            search: function() {
                var that = this,
                    options = utils.getFormData($('.search-form'));

                $.extend(this.searchAjaxArgs, options);

                this.upDate();
            },

            getInitialState: function() {
                var that = this;

                this.checkId = [];
                this.infoPage = typePk[pageType].pageInfo; // 新增/编辑页
                this.applyPage = 'apply_search.html'; // 报名查询页
                this.addBtn = $('[data-activity="add"]'); // 新增按钮
                this.delBtn = $('[data-activity="del"]'); // 删除按钮
                this.searchBtn = $('[data-activity="query"]'); // 搜索按钮
                this.searchAjaxArgs = {
                    pageNum: 1,
                    pageSize: 50,
                    typePk: typePk[pageType].id // 普通活动
                };

                // 初始化查询日期
                utils.datepick('.date-start', '.date-end', {
                    format: 'yyyy-mm-dd',
                    startView: 3,
                    minView: 2,
                    maxView: 4
                });

                utils.datepick('.pulishTime-start', '.pulishTime-end', {
                    format: 'yyyy-mm-dd',
                    startView: 3,
                    minView: 2,
                    maxView: 4
                });

                this.searchBtn.on('click', function() {
                    that.search();
                });

                this.addBtn.on('click', function() {
                    that.add();
                });

                // 状态设置弹出窗
                this.pulishModal = utils.delModal.init({
                    title: '温馨提示',
                    content: '你确定要发布这条活动？'
                });

                this.pulishModal.modal({
                    onConfirm: function() {
                        that.pulish();
                    }
                }).modal('close');

                // 删除提示框
                this.delModal = utils.delModal.init({ /// 删除提示框
                    title: '温馨提示',
                    content: '你确定要删除吗？'
                });

                this.delModal.modal({
                    onConfirm: function() {
                        that.del();
                    }
                }).modal('close');

                this.delBtn.on('click', function() {
                    if(that.checkId.length < 1) {
                        utils.alert({
                            text: '至少要选择一条数据',
                            type: 'warning'
                        });
                        return;
                    }
                    that.delModal.modal('open');
                });

                this.upDate();
            }
        };

        // 初始化
        (function initialization() {
            var url = "sysMenu/findMenuFunc";
            var data = {
                menuPk: typePk[pageType].power
            };
            utils.ajax({
                url: url,
                data: data,
                success: function (data) {
                    if (data.success) {
                        var power = { // 用户权限
                            edit: data.obj.indexOf("edit") != -1,
                            del: data.obj.indexOf("del") != -1,
                            show: data.obj.indexOf("show") != -1
                        };

                        createBtn(power);

                        var Activity = extendsFn(activityPrototype);
                        var activity = new Activity({
                            id: '#activity-list',
                            data: {
                                power: power
                            }
                        });
                        // var pageRender = new PageRender(power);
                    }
                }
            });
        })();

        function createBtn(power) {

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

        }

    });

});

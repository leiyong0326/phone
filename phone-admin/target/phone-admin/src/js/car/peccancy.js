define(['../require.config'], function(r) {

    require(['jquery','format', 'utils', 'template', 'pagination', 'datetimepicker', 'navbar', 'amazeui'], function($,fmt, utils, Temp, Page, datepicker) {

        function PageRender(options) {
            this.data = null; // 数据
            this.temp = null; // 模板
            this.checkId = []; // 选中id
            this.eventType = null; // 当前操作
            this.options = options;
            this.detailModal = $('[data-detail="detail-modal"]'); // 详情弹窗
            this.dbModal = $('[data-detail="confirm-modal"]'); // 代办确认窗
            this.dbModalMoney = this.dbModal.find('.db-money');
            this.dbPros = {
        	    pk: null,
        	    status: null
            }; // 代办参数
            this.dataApi = {
                search: 'peccancy/find', // 查询接口
                del: 'peccancy/delete', // 删除接口
                updateStaus: 'peccancy/updateStatus', //修改状态
                findDetail: 'peccancyDetail/find' // 违章详情查询
            };

            // 初始化查询日期
            utils.datepick('.illegal-start', '.illegal-end');

            // 默认查询参数
            this.searchOptions = {
                pageNum: 1,
                pageSize: 10
            };

            this.delModal = utils.delModal.init({ // / 删除提示框
                title: '温馨提示',
                content: '你确定要删除这条信息？'
            });
            
            // 按钮组合
            this.elements = {
                search: $('[data-illegal="query"]'),
                del: $('[data-illegal="del"]')
            };

            // 详情数据
            this.addOptions = [];

            // 详情弹出框模板
            this.modalTemp = new Temp({
                tempId: 'detail-content',
                data: this.addOptions
            });

            this.dbModal.modal({
        	onConfirm: function() {
        	    // 代办处理
                    utils.ajax({
                        url: that.dataApi.updateStaus,
                        data: that.dbPros,
                        success: function(data) {
                            if (data.success) {
                                utils.alert({
                                    text: '操作成功',
                                    type: 'success'
                                });
                                that.refreshData();
                            } else {
                                utils.alert({
                                    text: data.msg,
                                    type: 'danger'
                                });
                            }
                        }
                    });
        	}
            }).modal('close');
            
            var that = this;

            this.cyCollapse = new utils.CyCollapse({
        	showDefault: true
            });
            
            // 违章详情查询弹窗
            this.detailModal.modal({
                closeOnConfirm: false,
                onConfirm: function() {
                    console.log("confirm");
                }
            }).modal('close');

            // 第一次更新数据
            this.upData(function(data) {
                that.data = data;

                // 创建模板
                that.temp = new Temp({
                    tempId: 'illegal-list',
                    data: that.dataFormat(that.data.obj)
                });

                // 创建分页
                that.pagination = new Page({
                    pageTotal: that.data.total,
                    pageNo: that.searchOptions.pageNum,
                    events: function(id) {
                        that.searchOptions.pageNum = id;

                        that.upData(function(data) {
                            that.temp.data = that.dataFormat(data.obj);
                            that.temp.render();
                        });
                    }
                });

                that.events();
            });
        }

        PageRender.prototype = {

            // 更新数据
            upData: function(callback) {
                utils.ajax({
                    url: this.dataApi.search,
                    data: this.searchOptions,
                    success: function(data) {
                        callback.call(this, data);
                    }
                });
            },

            // 查询数据
            search: function() {
                var that = this,
                    options = utils
                    .getFormData($('[data-cy-validator]'));

                this.searchOptions = {
                    pageNum: 1,
                    pageSize: 10
                };

                $.extend(this.searchOptions, options);

                this.upData(function(data) {
                    that.data = data; // 第一次更新数据
                    that.temp.data = that.dataFormat(data.obj);
                    that.temp.render();
                });
            },

            // 删除数据
            del: function() {
                var that = this;

                this.delModal.modal({
                    onConfirm: function() {
                        utils.ajax({
                            url: that.dataApi.del,
                            data: {
                                pk: that.checkId.join(',')
                            },
                            success: function(data) {
                                if (data.success) {
                                    utils.alert({
                                        text: '删除成功',
                                        type: 'success'
                                    });
                                    that.refreshData();
                                } else {
                                    utils.alert({
                                        text: data.msg,
                                        type: 'danger'
                                    });
                                }
                            }
                        });
                    }
                });
            },

            // 违章详情
            findDetail: function() {
                var that = this;
                console.log(this.checkId[0]);
                utils.ajax({
                    url: this.dataApi.findDetail,
                    data: {
                	peccancyPk: this.checkId[0],
                        pageNum: 0,
                        pageSize: 0
                    },
                    success: function(data) {
                        if (data.success) {
                            that.addOptions = that.modalDataFormat(data.obj);
                            that.refreshModal();
                        }
                    }
                });
            },

            // 格式化弹窗数据
            modalDataFormat: function(data) {
                return data.map(function(item) {
                    return $.extend(item, {
                        peccancyTime: utils.dateFormat(item.peccancyTime, 'YYYY年MM月DD日 hh:mm'),
                        status: ['未缴费', '缴费中', '已缴费'][item.status],
                        handleStatus: ['未处理', '处理中', '已处理'][item.handleStatus]
                    });
                });
            },

            // 刷新弹框
            refreshModal: function() {
                this.modalTemp.data = this.addOptions;
                this.modalTemp.render();
                this.detailModal.modal('open');
                this.cyCollapse.refresh();
            },

            // 刷新列表数据
            refreshData: function() {
                var that = this;

                this.searchOptions.pageNum = 1;

                this.upData(function(data) {
                    that.temp.data = that.dataFormat(data.obj);
                    that.temp.render();
                    that.pagination.options.pageTotal = data.total;
                    that.pagination.refreshData(1);
                });
            },

            // 选中
            check: function() {
                var that = this;

                this.temp.content.on('click', '.cy-checkbox', function() {
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
                    return {
                        pk: item.pk,
                        edit : fmt.display(that.options.edit),
                        show : fmt.display(that.options.show),
                        no: item.no,
                        name: item.name,
                        phone: item.phone,
                        cardNo: item.cardNo,
                        money: item.money,
                        isPay: ['否','是'][item.isPay],
                        status: item.status,
                        statusText: ['未确认', '代办中', '代办失败', '失败已退款', '代办成功'][item.status],
                        btnClass: ['', 'style="display:none;', 'style="display:none;', 'style="display:none;', 'style="display:none;'][item.status],
                        btnText: ['确认', true, true, true, true][item.status],
                        createTime: utils.dateFormat(item.createTime,
                            'YYYY-MM-DD'),
                        agencyTime: utils.dateFormat(item.agencyTime,
                            'YYYY-MM-DD')
                    };
                });
            },

            // 控制
            events: function() {
                var that = this;
                this.check();

                // 事件组合
                this.elementEvents = {
                    search: this.search.bind(this),
                    findDetail: this.findDetail.bind(that),
                    del: this.del.bind(that)
                };

                ['search', 'del'].map(function(i) {
                    that.elements[i].on('click', function() {
                        this.eventType = null;

                        if ((i == 'edit' && that.checkId.length != 1) ||
                            (i == 'del' && that.checkId.length < 1)) {
                            utils.alert({
                                text: '必须选择一条数据',
                                type: 'warning'
                            });
                            return;
                        }

                        that.elementEvents[i]();
                    });
                });

                // 详情查询
                this.temp.content.on('click', '.findDetail', function() {
                    that.checkId[0] = $(this).data('id');
                    that.elementEvents['findDetail']();
                });

                // 列表操作
                this.temp.content.on('click', '.editB', function() {
                    var _id = $(this).data('status');
                    var _pk = $(this).data('id');
                    that.dbModalMoney.text($(this).data('money'));
                    switch (_id) {
                        case 0:
                            that.dbPros = {
                        		pk: _pk,
                        		status: '1'
                        	}
                            that.dbModal.modal('open');
                            break;
                    }
                });

            }

        };

        // 初始化
        (function initialization() {
            var url = "sysMenu/findMenuFunc";
            var data = {
                menuPk: "MtPeccancy"
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

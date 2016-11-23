define(['../require.config'], function (r) {

    require(['jquery','format','warn', 'utils', 'template', 'pagination', 'datetimepicker', 'format',
            'navbar', 'amazeui'
        ],
        function ($,fmt,w, utils, Temp, Page, datepicker, format) {

            function PageRender(options) {
                this.data = null; // 数据
                this.temp = null; // 模板
                this.checkId = []; // 选中id
                this.options = options;
                this.eventType = null; // 当前操作
                this.dataApi = {
                    search: 'shopCar/find', // 查询接口
                    searchOne: 'shopCar/get', // 查询某条数据
                    del: 'shopCar/delete', // 删除接口
                    updateSeq: 'shopCar/updateSeq' //排序接口
                };

                // 默认查询参数
                this.searchOptions = {
                    pageNum: 1,
                    pageSize: 10
                };

                this.delModal = utils.delModal.init({ // / 删除提示框
                    title: '温馨提示',
                    content: '你确定要删除这条信息？'
                });

                this.updateSeqModal = utils.delModal.init({ // / 排序操作提示框
                    title: '温馨提示',
                    content: '你确定要执行此排序？'
                });

                // 下拉框
                this.selects = {
                    brandPk: $('[name="brandPk"]'),
                    classPk: $('[name="classPk"]')
                };

                // 按钮组合
                this.elements = {
                    search: $('[data-car="query"]'),
                    add: $('[data-car="add"]'),
                    del: $('[data-car="del"]'),
                    edit: $('[data-car="edit"]'),
                };

                var that = this;

                // 第一次更新数据
                this.upData(function (data) {
                    that.data = data;

                    // 创建模板
                    that.temp = new Temp({
                        tempId: 'car-list',
                        data: that.dataFormat(that.data.obj)
                    });

                    // 创建分页
                    that.pagination = new Page({
                        pageTotal: that.data.total,
                        pageNo: that.searchOptions.pageNum,
                        events: function (id) {
                            that.searchOptions.pageNum = id;

                            that.upData(function (data) {
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
                upData: function (callback) {
                    utils.ajax({
                        url: this.dataApi.search,
                        data: this.searchOptions,
                        success: function (data) {
                            callback.call(this, data);
                        }
                    });
                },

                // 删除数据
                del: function () {
                    var that = this;

                    this.delModal.modal({
                        onConfirm: function () {
                            utils.ajax({
                                url: that.dataApi.del,
                                data: {
                                    pk: that.checkId.join(',')
                                },
                                success: function (data) {

                                    that.checkId = [];
                                    utils.alert({
                                        text: '删除成功',
                                        type: 'success'
                                    });

                                    that.refreshData();
                                }
                            });
                        }
                    });
                },

                // 初始化添加数据
                add: function () {
                    window.location.href = 'car_info.html?type=add';
                },

                // 初始化编辑数据
                edit: function (pk) {
                    window.location.href = 'car_edit_info.html?pk=' + pk;
                },

                // 初始化添加数据
                findImg: function (carPk) {
                    window.location.href = 'carImg_info.html?carPk=' + carPk;
                },

                // 查询数据
                search: function () {
                    var that = this;

                    $.extend(that.searchOptions, utils.getFormData($('.search-form')));

                    this.upData(function (data) {
                        that.data = data; // 第一次更新数据

                        that.temp.data = that.dataFormat(data.obj);
                        that.temp.render();
                    });
                },

                //排序
                updateSeq: function (btn) {
                    var that = this;
                    var type = btn.data('type');
                    var pk = btn.data('id');
                    var seq = btn.data('seq');
                    if ('top' == type) {
                        seq = 1;
                    } else if ('up' == type) {
                        seq = seq - 1;
                    } else if ('down' == type) {
                        seq = seq + 1;
                    } else {
                        seq = 99;
                    }
                    if(seq < 1){
                	seq = 1;
                    }
                    if(seq > 98){
                	seq = 99;
                    }
                    this.updateSeqModal.modal({
                        onConfirm: function () {
                            utils.ajax({
                                url: that.dataApi.updateSeq,
                                data: {
                                    pk: pk,
                                    seq: seq
                                },
                                success: function (data) {
                                    if (data.success) {
                                        utils.alert({
                                            text: '排序成功',
                                            type: 'success'
                                        })
                                    } else {
                                        utils.alert({
                                            text: '排序失败',
                                            type: 'danger'
                                        })
                                    }
                                }
                            })
                        }

                    });

                },

                // 刷新列表数据
                refreshData: function () {
                    var that = this;

                    this.searchOptions.pageNum = 1;

                    this.upData(function (data) {
                        that.temp.data = that.dataFormat(data.obj);
                        that.temp.render();
                        that.pagination.options.pageTotal = data.total;
                        that.pagination.refreshData(1);
                    });
                },

                // 选中
                check: function () {
                    var that = this;

                    this.temp.content.on('click', '.cy-checkbox', function () {
                        var _this = $(this);
                        if (!_this.hasClass('checked')) {
                            that.checkId.push(_this.data('id'));
                        } else {
                            that.checkId.map(function (item, index) {
                                if (_this.data('id') == item) {
                                    that.checkId.splice(index, 1);
                                }
                            });
                        }
                    });
                },

                // 获取品牌
                getBrand: function () {
                    var that = this;
                    $.ajax({
                        url: '/shopCarBrand/findByOrg',
                        type: 'post',
                        success: function (data) {
                            var obj = $.parseJSON(data).obj;

                            var optionStr = '<option value=""> 所有</option>';
                            for (var i = 0, len = obj.length; i < len; i++) {
                                optionStr += '<option value="' + obj[i].pk + '">' +
                                    obj[i].name + '</option>';
                            }
                            that.selects.brandPk.html(optionStr);
                        }
                    });
                },

                // 获取车系
                getClass: function () {
                    var that = this;
                    var brandPk = that.selects.brandPk.val();
                    if (brandPk != '') {
                        $.ajax({
                            url: '/shopOrgClass/findByBrand?brandPk=' +
                                brandPk,
                            type: 'get',
                            success: function (data) {
                                var obj = $.parseJSON(data).obj;
                                var optionStr = '<option value=""> 所有</option>';
                                for (var i = 0, len = obj.length; i < len; i++) {
                                    optionStr += '<option value="' + obj[i].pk +
                                        '">' + obj[i].name + '</option>';
                                }
                                that.selects.classPk.html(optionStr);
                            }
                        });
                    }

                },

                // 格式化数据
                dataFormat: function (data) {
                    var listData = null;
                    var that = this;
                    if (!Array.isArray(data)) {
                        return false;
                    }

                    return data.map(function (item) {
                        return {
                            pk: [item.pk],
                            brandPk: [item.brandPk],
                            brandName: [item.brandName],
                            classPk: [item.classPk],
                            className: [item.className],
                            level: format.carLevel(item.level),
                            type: [item.type],
                            srcPrice: [item.srcPrice],
                            price: [item.price],
                            status: ['需预定', '库存告急', '现车充足'][item.status],
                            sale: ['否', '是'][item.sale],
                            startTime: [item.startTime],
                            endTime: [item.endTime],
                            stuck: [item.stuck],
                            hot: ['否', '是'][item.hot],
                            money: [item.money],
                            forcePay: ['否', '是'][item.forcePay],
                            consume: [item.consume],
                            tag1: [item.tag1],
                            msg1: [item.msg1],
                            tag2: [item.tag2],
                            msg2: [item.msg2],
                            tag3: [item.tag3],
                            msg3: [item.msg3],
                            tag4: [item.tag4],
                            msg4: [item.msg4],
                            seq: [item.seq],
                            editShow:fmt.display(that.options.edit)
                        };
                    });
                },

                // 控制
                events: function () {
                    var that = this;
                    this.check();
                    this.getBrand();
                    this.selectsEvents = {
                        brandPk: this.getClass.bind(this)
                    };

                    ['brandPk'].map(function (i) {
                        that.selects[i].on('change', function () {
                            that.selectsEvents[i]();
                        });
                    });

                    // 事件组合
                    this.elementEvents = {
                        search: this.search.bind(that),
                        add: this.add.bind(that),
                        edit: this.edit.bind(that),
                        del: this.del.bind(that),
                        updateSeq: this.updateSeq.bind(that),
                        findImg: this.findImg.bind(that)
                    };

                    ['search', 'add', 'edit', 'del'].map(function (i) {
                        that.elements[i].on('click', function () {
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


                    // 列表排序
                     this.temp.content.on('click', '.updateSeqB', function () {
                         that.updateSeq($(this));
                     });

                    // 列表编辑
                    this.temp.content.on('click', '.editB', function () {
                        that.edit($(this).attr('data-id'));
                    });

                    // 列表查看图片
                    this.temp.content.on('click', '.findImg', function () {
                        that.findImg($(this).attr('data-id'));
                    });
                }

            };

            // 初始化
            (function initialization() {

                var url = "sysMenu/findMenuFunc";
                var data = {
                    menuPk: "ShopCar"
                };
                utils.ajax({
                    url: url,
                    data: data,
                    success: function (data) {
                        if (data.success) {
                            var power = { // 用户权限
                                add: data.obj.indexOf("edit") >= 0,
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

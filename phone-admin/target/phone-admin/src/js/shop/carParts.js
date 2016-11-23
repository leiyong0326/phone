define(['../require.config'], function (r) {

    require(['jquery', 'format', 'warn', 'utils', 'template', 'pagination', 'datetimepicker',
        'navbar', 'amazeui'
    ], function ($, fmt, w, utils, Temp, Page, datepicker) {

        function PageRender(options) {
            this.data = null; // 数据
            this.temp = null; // 模板
            this.options = options;
            this.addModal = $('[data-carParts="add-modal"]'); // 新增编辑的窗口
            this.checkId = []; // 选中id
            this.eventType = null; // 当前操作
            this.dataApi = {
                search: 'shopCarParts/find', // 查询接口
                searchOne: 'shopCarParts/get', // 查询某条数据
                del: 'shopCarParts/delete' // 删除接口
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

            // 下拉框
            this.selects = {
                classPk: $('[name="classPk"]'),
                slavePk: $('[name="slavePk"]')
            };

            // 按钮组合
            this.elements = {
                search: $('[data-carParts="query"]'),
                add: $('[data-carParts="add"]'),
                edit: $('[data-carParts="edit"]'),
                del: $('[data-carParts="del"]')
            };

            this.addOptions = {
                pk: '',
                classPk: '',
                className: '',
                slavePk: '',
                slaveName: '',
                name: '',
                srcPrice: '',
                price: '',
                status: '',
                recommend: '',
                discount: '',
                carType: '',
                count: '',
                scoreBuy: '',
                score: '',
                getScore: '',
                url: '',
                listUrl: '',
            };

            var that = this;

            // 第一次更新数据
            this.upData(function (data) {
                that.data = data;

                // 创建模板
                that.temp = new Temp({
                    tempId: 'carParts-list',
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
                window.location.href = 'carParts_info.html?type=add';
            },

            // 初始化编辑数据
            edit: function () {
                window.location.href = 'carParts_info.html?type=edit' + '&id=' +
                    this.checkId[0];
            },

            // 初始化添加数据
            findImg: function (partsPk) {
                window.location.href = 'partsImg_info.html?partsPk=' + partsPk;
            },


            // 查询数据
            search: function () {
                var that = this;

                $.extend(this.searchOptions, utils.getFormData($('[data-modal="search"]')));

                this.upData(function (data) {
                    that.data = data; // 第一次更新数据

                    that.temp.data = that.dataFormat(data.obj);
                    that.temp.render();
                });
            },

            // 刷新弹框
            refreshModal: function () {
                this.modalTemp.data = this.addOptions;
                this.modalTemp.render();
                this.modalTemp.content.find('select').selected();
                this.modalTemp.content.find(
                    'option[value="' + this.addOptions.type + '"]').attr(
                    'selected', true);
                this.addModal.modal('open');
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

            // 获取配件大类
            getClass: function () {
                var that = this;
                utils.ajax({
                    url: 'shopPartsClass/findAll',
                    success: function (data) {
                        var obj = data.obj;
                        var optionStr = '<option value=""> 所有</option>';
                        for (var i = 0, len = obj.length; i < len; i++) {
                            optionStr += '<option value="' + obj[i].pk + '">' +
                                obj[i].name + '</option>';
                        }
                        that.selects.classPk.html(optionStr);
                    }
                });

            },

            // 获取配件小类
            getSlave: function () {
                var that = this;
                var upPk = that.selects.classPk.val();
                if (upPk.trim().length == 0) {
                    return;
                }
                utils.ajax({
                    url: 'shopPartsClassSlave/findByUp',
                    data: {
                        upPk: upPk
                    },
                    success: function (data) {
                        var optionStr = '';
                        if (data.obj == null) {
                            optionStr = '<option value="">暂无小类</option>';
                        } else {
                            var obj = data.obj;
                            optionStr = '<option value=""> 所有</option>';
                            for (var i = 0, len = obj.length; i < len; i++) {
                                optionStr += '<option value="' + obj[i].pk + '">' +
                                    obj[i].name + '</option>';
                            }

                        }
                        that.selects.slavePk.html(optionStr);
                    }
                });
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
                        pk: item.pk,
                        classPk: item.classPk,
                        className: item.className,
                        slavePk: item.slavePk,
                        slaveName: item.slaveName,
                        name: item.name.substring(0, 8) + '...',
                        srcPrice: item.srcPrice,
                        price: item.price,
                        status: ['下架', '在售'][item.status],
                        recommend: ['否', '是'][item.recommend],
                        discount: item.discount,
                        carType: item.carType,
                        count: item.count,
                        scoreBuy: ['不支持', '支持'][item.scoreBuy],
                        score: item.score,
                        getScore: item.getScore,
                        url: item.url,
                        listUrl: item.listUrl,
                        editShow: fmt.display(that.options.edit)
                    };
                });
            },

            // 控制
            events: function () {
                var that = this;
                this.check();
                this.getClass();
                this.selectsEvents = {
                    classPk: this.getSlave.bind(this)
                };

                ['classPk'].map(function (i) {
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
                };

                ['search', 'add', 'del'].map(function (i) {
                    that.elements[i].on('click', function () {
                        this.eventType = null;
                        if (i == 'del' && that.checkId.length < 1) {
                            utils.alert({
                                text: '必须选择一条数据',
                                type: 'warning'
                            });
                            return;
                        }

                        that.elementEvents[i]();
                    });
                });

                // 列表编辑
                this.temp.content.on('click', '.editB', function () {
                    that.checkId[0] = $(this).data('id');
                    that.elementEvents['edit']();
                });

                // 列表查看图片
                this.temp.content.on('click', '.findImgB', function () {
                    that.findImg($(this).data('id'));
                });

            }

        };

        // 初始化
        (function initialization() {

            var url = "sysMenu/findMenuFunc";
            var data = {
                menuPk: "ShopCarParts"
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

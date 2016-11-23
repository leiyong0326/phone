define(['../require.config'], function (r) {

    require(['jquery','format','warn', 'utils', 'template', 'pagination', 'datetimepicker', 'format', 'navbar', 'amazeui', 'updataFile'],
        function ($,fmt,w, utils, Temp, Page, datepicker, format) {

            function PageRender(options) {
                this.data = null; // 数据
                this.temp = null; // 模板
                this.options = options;
                this.addModal = $('[data-carOld="add-modal"]'); // 新增编辑的窗口
                this.checkId = []; // 选中id
                this.eventType = null; // 当前操作
                this.dataApi = {
                    search: 'shopCarOld/find', // 查询接口
                    add: 'shopCarOld/save', // 新增接口
                    searchOne: 'shopCarOld/get', // 查询某条数据
                    del: 'shopCarOld/delete' // 删除接口
                };

                // 默认查询参数
                this.searchOptions = {
                    pageNum: 1,
                    pageSize: 10
                };

                // 新增列表参数
                this.addOptions = {
                    pk: '',
                    brandPk: '',
                    brandName: '',
                    classPk: '',
                    className: '',
                    level: '',
                    type: '',
                    srcPrice: '',
                    price: '',
                    status: '',
                    hot: '',
                    money: '',
                    forcePay: '',
                    speedType: '',
                    mileage: '',
                    paiLiang: '',
                    color: '',
                    maintain: '',
                    used: '',
                    first: '',
                    buyTime: '',
                    remark: '',
                    seq: '',
                    maxPrice: '',
                    maxMileage: '',
                    minAge: '',
                    maxAge: '',
                    img: '',
                    urls:'',
                    isCollection: ''
                };


                this.delModal = utils.delModal.init({ // / 删除提示框
                    title: '温馨提示',
                    content: '你确定要删除这条信息？'
                });

                // 按钮组合
                this.elements = {
                    search: $('[data-carOld="query"]'),
                    add: $('[data-carOld="add"]'),
                    del: $('[data-carOld="del"]')
                };
                var that = this;

                // 第一次更新数据
                this.upData(function (data) {
                    that.data = data;

                    // 创建模板
                    that.temp = new Temp({
                        tempId: 'carOld-list',
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

            // 列表页
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
                            console.log(that.checkId);
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
                    window.location.href = 'carOld_info.html';
                },

                // 初始化编辑数据
                edit: function () {
                    window.location.href = 'carOld_edit_info.html?pk='+this.checkId[0];
                },

                // 初始化添加数据
                findImg: function (carPk) {
                    window.location.href = 'carOldImg_info.html?carPk=' + carPk;
                },

                // 查询数据
                search: function () {
                    var that = this;

                    $.extend(this.searchOptions, {
                        name: $('.carOld-search-name').val(),
                        type: $('.carOld-search-type').val(),
                        days: $('.carOld-search-date').val()
                    });

                    this.upData(function (data) {
                        that.data = data; // 第一次更新数据

                        that.temp.data = that.dataFormat(data.obj);
                        that.temp.render();
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

                // 格式化数据
                dataFormat: function (data) {
                    var listData = null;
                    var that = this;
                    if (!Array.isArray(data)) {
                        return false;
                    }

                    return data.map(function (item) {
                        item.status = ['已售', '在售'][item.status];
                        item.hot = ['否', '是'][item.hot];
                        item.buyTime = format.date(item.buyTime, 'YYYY-MM-DD');
                        item.used = [item.used];
                        item.editShow = fmt.display(that.options.edit);
                        return item;
                    });
                },

                // 控制
                events: function () {
                    var that = this;
                    this.check();

                    // 事件组合
                    this.elementEvents = {
                        search: this.search.bind(this),
                        add: this.add.bind(this),
                        del: this.del.bind(this),
                        edit : this.edit.bind(this)
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
                    this.temp.content.on('click', '.findImg', function () {
                        that.findImg($(this).attr('data-id'));
                    });

                }

            };

            // 初始化
            (function initialization() {

                var url = "sysMenu/findMenuFunc";
                var data = {
                    menuPk: "ShopCarOld"
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

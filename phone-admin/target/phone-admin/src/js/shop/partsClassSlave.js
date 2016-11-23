define(['../require.config'], function (r) {

    require(['jquery', 'format', 'warn', 'utils', 'template', 'temp', 'pagination', 'navbar', 'amazeui'],
        function ($, fmt, w, utils, Temp, extendsFn, Page) {

            // 弹出窗
            var editModalPrototype = {

                // 获取配件大类
                getClass: function () {
                    var that = this;

                    utils.ajax({
                        url: 'shopPartsClass/findAll',
                        success: function (data) {
                            that.data.classs = data.obj;
                            that.render();
                        }
                    });

                },

                // 获取输入数据
                getFormData: function () {
                    var _formData = utils.getFormData($('[data-temp="addcontent"]'));
                    return _formData;
                },

                getInitialState: function() {
                    this.getClass();
                },

                componentDidMount: function () {
                    var that = this;
                    var _types = $(this.refs.types);
                    var _name = _types.attr('name');

                    _types.on('change', function () {
                        that.data[_name] = $(this).val();
                    });

                    if (this.data.hasOwnProperty(_name)) {
                        _types.val(this.data[_name]);
                    }
                }
            };
            var EditModal = extendsFn(editModalPrototype);
            var editModal = new EditModal({
                id: '#addcontent',
                data: {}
            });

            function PageRender(options) {
                this.data = null; // 数据
                this.temp = null; // 模板
                this.options = options;
                this.addModal = $('[data-partsClassSlave="add-modal"]'); // 新增编辑的窗口
                this.checkId = []; // 选中id
                this.eventType = null; // 当前操作
                this.dataApi = {
                    search: 'shopPartsClassSlave/find', // 查询接口
                    add: 'shopPartsClassSlave/save', // 新增接口
                    searchOne: 'shopPartsClassSlave/get', // 查询某条数据
                    del: 'shopPartsClassSlave/delete' // 删除接口
                };

                // 默认查询参数
                this.searchOptions = {
                    pageNum: 1,
                    pageSize: 10
                };

                // 新增列表参数
                this.addOptions = {
                    name: '',
                    classPk: ''
                };

                this.delModal = utils.delModal.init({ // / 删除提示框
                    title: '温馨提示',
                    content: '你确定要删除这条信息？'
                });

                // 下拉框
                this.selects = {
                    classPk: $('.classPk')
                };

                // 按钮组合
                this.elements = {
                    search: $('[data-partsClassSlave="query"]'),
                    add: $('[data-partsClassSlave="add"]'),
                    del: $('[data-partsClassSlave="del"]')
                };

                var that = this;

                // 创建新增/编辑弹窗
                this.addModal.modal({
                    closeOnConfirm: false,
                    onConfirm: function () {

                        if (that.eventType == 'add') { // 新增
                            that.addData();
                        } else if (that.eventType == 'edit') { // 编辑
                            that.editData();
                        }

                    }
                }).modal('close');

                // 第一次更新数据
                this.upData(function (data) {
                    that.data = data;

                    // 创建模板
                    that.temp = new Temp({
                        tempId: 'partsClassSlave-list',
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
                    var _obj;
                    if(editModal.data.classs) {
                         _obj = editModal.data.classs;
                    }else {
                        _obj = [];
                    }

                    this.eventType = 'add';
                    editModal.data.classs = _obj;
                    editModal.render();
                    this.addModal.modal('open');
                },

                // 添加数据
                addData: function () {
                    var that = this;
                    var _options = editModal.getFormData();

                    utils.ajax({
                        url: this.dataApi.add,
                        data: _options,
                        success: function (data) {
                            utils.alert({
                                text: '新增成功',
                                type: 'success'
                            });

                            that.refreshData();
                            that.addModal.modal('close');
                        }
                    });
                },

                // 初始化编辑数据
                edit: function () {
                    var that = this;
                    this.eventType = 'edit';

                    utils.ajax({
                        url: this.dataApi.searchOne,
                        data: {
                            pk: this.checkId[0]
                        },
                        success: function (data) {
                            editModal.data = $.extend(editModal.data, data.obj);
                            editModal.render();
                            that.addModal.modal('open');
                        }
                    });
                },

                // 编辑数据
                editData: function () {
                    var that = this;
                    var _options = editModal.getFormData();

                    utils.ajax({
                        url: this.dataApi.add,
                        data: _options,
                        success: function (data) {

                            utils.alert({
                                text: '编辑成功',
                                type: 'success'
                            });

                            that.refreshData();
                            that.addModal.modal('close');
                        }
                    });
                },

                // 查询数据
                search: function () {
                    var that = this;

                    $.extend(this.searchOptions, {
                        classPk: $('.partsClassSlave-search-classPk').val()
                    });

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
                            that.selects.classPk.createSelect({
                                data: obj,
                                attr: ['pk', 'name'],
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
                        return {
                            pk: item.pk,
                            name: item.name,
                            classPk: item.classPk,
                            className: item.className,
                            editShow: fmt.display(that.options.edit)
                        };
                    });
                },

                // 控制
                events: function () {
                    var that = this;
                    this.check();
                    this.getClass();
                    // 事件组合
                    this.elementEvents = {
                        search: this.search.bind(that),
                        add: this.add.bind(that),
                        edit: this.edit.bind(that),
                        del: this.del.bind(that)
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

                    // 列表删除
                    this.temp.content.on('click', '.editB', function () {
                        that.checkId[0] = $(this).data('id');
                        that.elementEvents['edit']();
                    });

                }

            };

            // 初始化
            (function initialization() {
                var url = "sysMenu/findMenuFunc";
                var data = {
                    menuPk: "ShopPartsClassSlave"
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

define(['../require.config'], function (r) {

    require(['jquery', 'config', 'utils', 'temp', 'pagination', 'navbar', 'amazeui'],
        function ($, config, utils, extendsFn, Page) {

            // 新增/编辑
            var editModalPrototype = {

                // 获取品牌
                getBrand: function () {
                    var that = this;
                    utils.ajax({
                        url: config.api.searchShopCarBrand,
                        success: function (data) {
                            that.data.brands = [{
                                name: '请选择品牌',
                                pk: '00000000000'
                            }].concat(data.obj);
                            that.render();
                        }
                    });
                },

                //获取车系
                getCarClass: function (evt) {
                    var that = this;

                    utils.ajax({
                        url: config.api.searchShopCarClass,
                        data: {
                            brandPk: evt.value,
                            pageNum: 1,
                            pageSize: 500
                        },
                        success: function (data) {
                            that.data.names = [{
                                name: '请选择车系',
                                pk: 999999
                            }].concat(data.obj);

                            if (that.data.names.length == 1) {
                                that.data.classPk = 999999;
                                that.data.seq = null;
                                that.data.dsc = null;
                            }
                            that.render();
                        }
                    });
                },

                refresh: function() {
                    this.getBrand();
                },

                // 获取输入数据
                getFormData: function () {
                    var _formData = utils.getFormData($('[data-temp="editcontent"]'));
                    _formData.name = this.data.name;
                    return _formData;
                },

                setSketch: function() {
                    var _data = utils.find(this.data.classPk, this.data.names, 'pk');
                    this.data.seq = _data.seq;
                    this.data.dsc = _data.dsc;
                    this.data.pk = _data.pk;
                    this.data.name = _data.name;
                    this.render();
                },

                getInitialState: function () {
                    this.getBrand();
                },

                componentDidMount: function () {
                    var that = this;
                    var selects = $('[data-temp="editcontent"] .cy-select');

                    selects.on('change', function () {
                        that.data[$(this).attr('name')] = $(this).val();
                        that.render();
                    });

                    // 设置下拉菜单选中属性
                    selects.each(function () {
                        var _name = $(this).attr('name');
                        if (that.data.hasOwnProperty(_name)) {
                            $(this).val(that.data[_name]);
                        }
                    });
                }

            };

            var orgClassPrototype = {

                // 查询当前4s店车系列表
                upDate: function () {
                    var that = this;

                    utils.ajax({
                        url: config.api.searchShopOrgClass,
                        data: this.searchAjaxArgs,
                        success: function (data) {
                            that.data.classLists = data.obj.map(function (item) {
                                item.edit = that.data.power.edit;
                                return item;
                            });
                            that.render();
                        }
                    });
                },

                // 删除车系
                del: function () {
                    var that = this;
                    if(this.checkId.length == 0) {
                        utils.alert({
                            text: '请最少选择一个车系',
                            type: 'warning'
                        });
                        return;
                    }
                    utils.ajax({
                        url: config.api.delShopOrgClass,
                        data: {
                            pk: this.checkId.join(',')
                        },
                        success: function (data) {
                            utils.alert({
                                text: '删除成功',
                                type: 'success'
                            });
                            that.upDate();
                        }
                    });
                },

                // 添加数据
                add: function () {
                    var that = this;
                    var _options = this._editModal.getFormData();

                    utils.ajax({
                        url: config.api.addShopOrgClass,
                        data: _options,
                        success: function (data) {
                            utils.alert({
                                text: '新增成功',
                                type: 'success'
                            });
                            that.upDate();
                        }
                    });
                },

                openEditModal: function (evt) {
                    var _data = utils.find(evt.dataset.id, this.data.classLists, 'pk');
                    this._editModal.data = _data;
                    this._editModal.data.classPk = _data.pk;
                    this._editModal.refresh();
                    this.eventType = 'edit';
                    this.editModal.modal('open');
                },

                edit: function () {
                    var _options = this._editModal.getFormData();

                    utils.ajax({
                        url: config.api.addShopOrgClass,
                        data: _options,
                        success: function (data) {
                            utils.alert({
                                text: '编辑成功',
                                type: 'success'
                            });
                        }
                    });
                },

                // 选中
                check: function (evt) {
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

                // 获取品牌
                getBrand: function () {
                    var that = this;

                    utils.ajax({
                        url: config.api.searchShopCarBrand,
                        success: function (data) {
                            that.searchBrand.data = [{
                                name: '所有品牌',
                                pk: ''
                            }].concat(data.obj);
                            that.searchBrand.render();
                        }
                    });
                },

                getInitialState: function () {
                    var that = this;
                    this.eventType = null; // 当前操作
                    this.checkId = []; // 选中id

                    // 默认查询参数
                    this.searchAjaxArgs = {
                        pageNum: 1,
                        pageSize: 20
                    };

                    // 新增按钮
                    this.addBtn = $('[data-carClass="add"]');

                    // 删除按钮
                    this.delBtn = $('[data-carClass="del"]');

                    // 查询按钮
                    this.searchBtn = $('[data-carClass="query"]');

                    this.searchBtn.on('click', function() {
                        var _options = utils.getFormData($('.search-form'));

                        that.searchAjaxArgs = {
                            pageNum: 1,
                            pageSize: 20
                        };
                        that.searchAjaxArgs = $.extend(that.searchAjaxArgs, _options);
                        that.upDate();
                    });

                    // 删除提示框
                    this.delModal = utils.delModal.init({
                        title: '温馨提示',
                        content: '你确定要删除这条信息？'
                    });

                    this.delBtn.on('click', function () {
                        that.delModal.modal({
                            onConfirm: function () {
                                that.del();
                            }
                        });
                    });

                    // 编辑/新增模态窗
                    this.editModal = $('[data-carClass="add-modal"]');
                    this.editModal.modal({
                        onConfirm: function () {
                            switch (that.eventType) {
                            case 'add':
                                that.add();
                                break;
                            case 'edit':
                                that.edit();
                                break;
                            }
                        }
                    }).modal('close');

                    this.addBtn.on('click', function () {
                        that._editModal.refresh();
                        that.eventType = 'add';
                        that.editModal.modal('open');
                    });

                    var EditModal = extendsFn(editModalPrototype);
                    this._editModal = new EditModal({
                        id: '#editcontent',
                        data: {}
                    });

                    // 查询品牌列表
                    var SearchBrand = extendsFn({});
                    this.searchBrand = new SearchBrand({
                        id: '#searchBrand',
                        data: []
                    });

                    this.getBrand();
                    this.upDate();
                }

            };

            // 初始化
            (function initialization() {

                utils.ajax({
                    url: config.api.findMenuFunc,
                    data: {
                        menuPk: 'ShopOrgClass'
                    },
                    success: function (data) {
                        if (data.success) {
                            var power = { // 用户权限
                                add: data.obj.indexOf("edit") != -1,
                                edit: data.obj.indexOf("edit") != -1,
                                del: data.obj.indexOf("del") != -1,
                                show: data.obj.indexOf("show") != -1
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

                            var OrgClass = extendsFn(orgClassPrototype);
                            var OrgClass = new OrgClass({
                                id: '#orgClass-list',
                                data: {
                                    power: power
                                }
                            });

                        }
                    }
                });
            })();
        });

});

define(['../require.config'], function (r) {

    require(['jquery', 'format', 'utils', 'template', 'temp', 'pagination', 'navbar', 'amazeui'],
        function ($, fmt, utils, Temp, extendsFn, Page) {

            // 新增/编辑
            // 弹出窗
            var editModalPrototype = {

                // 获取输入数据
                getFormData: function () {
                    var _formData = utils.getFormData($('[data-temp="editcontent"]'));
                    return _formData;
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
                id: '#editcontent',
                data: {}
            });

            function PageRender(options) {
                this.data = null; // 数据
                this.temp = null; // 模板
                this.options = options;
                this.addModal = $('[data-carClass="add-modal"]'); // 新增编辑的窗口
                this.checkId = []; // 选中id
                this.brandPk = utils.parseUrl().brandPk;
                this.eventType = null; // 当前操作
                this.dataApi = {
                    search: 'shopCarClass/find', // 查询接口
                    add: 'shopCarClass/add', // 新增接口
                    update:'shopCarClass/update',
                    searchOne: 'shopCarClass/get', // 查询某条数据
                    del: 'shopCarClass/delete' // 删除接口
                };

                this.initSearchOption();

                this.delModal = utils.delModal.init({ // / 删除提示框
                    title: '温馨提示',
                    content: '你确定要删除这条信息？'
                });

                // 按钮组合
                this.elements = {
                    search: $('[data-carClass="query"]'),
                    add: $('[data-carClass="add"]'),
                    del: $('[data-carClass="del"]')
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
                        tempId: 'carClass-list',
                        data: that.dataFormat(that.data.obj)
                    });

                    that.paginationFn();

                    that.events();
                });
            }

            PageRender.prototype = {
        	    
        	  //分页
    		paginationFn: function(){
    		   var that = this; 
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
    		},
        	
        	initSearchOption : function(){
        	    this.searchOptions = {
        		pageNum: 1,
                        pageSize: 10,
                        brandPk:this.brandPk
                    };
        	},

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
                    if(editModal.data.brands) {
                         _obj = editModal.data.brands;
                    }else {
                        _obj = [];
                    }

                    this.eventType = 'add';
                    editModal.data = {};
                    editModal.data.brands = _obj;
                    editModal.render();
                    this.addModal.modal('open');
                },

                // 添加数据
                addData: function () {
                    var that = this;
                    var _options = editModal.getFormData();
                    _options.brandPk =  that.brandPk;
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
                edit: function (pk) {
                    var that = this;
                    this.eventType = 'edit';

                    utils.ajax({
                        url: this.dataApi.searchOne,
                        data: {
                            pk: pk
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
                        url: this.dataApi.update,
                        data: _options,
                        success: function (data) {
                            if(data.success){
                        	utils.alert({
                                    text: '修改成功',
                                    type: 'success'
                                });
                                that.refreshData();
                                that.addModal.modal('close');
                            }else{
                        	utils.alert({
                                    text: data.msg,
                                    type: 'danger'
                                });
                            }
                            
                        }
                    });
                },

                // 查询数据
                search: function () {
                    var that = this;
                    that.initSearchOption();
                    $.extend(this.searchOptions, utils.getFormData($('.search-form')));
                    this.upData(function (data) {
                        that.data = data; // 第一次更新数据
                        that.temp.data = that.dataFormat(data.obj);
                        that.temp.render();
                        that.paginationFn();
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
                    var that = this;
                    var listData = null;

                    if (!Array.isArray(data)) {
                        return false;
                    }

                    return data.map(function (item) {
                        return {
                            brandPk: [item.brandPk],
                            name: [item.name],
                            brandName: [item.brandName],
                            dsc: [item.dsc],
                            seq: [item.seq],
                            pk: [item.pk],
                            editShow: fmt.display(that.options.edit)
                        };
                    });
                },


                // 控制
                events: function () {
                    var that = this;
                    this.check();
                    // 事件组合
                    this.elementEvents = {
                        search: this.search.bind(that),
                        add: this.add.bind(that),
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

                    // 列表编辑
                    this.temp.content.on('click', '.editB', function () {
                        that.edit($(this).data('id'));
                    });

                }

            };

            // 初始化
            (function initialization() {

                var url = "sysMenu/findMenuFunc";
                var data = {
                    menuPk: "ShopCarBrand"
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

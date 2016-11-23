/*
 * Date: 2016.06.30
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */

define(['../require.config'], function (r) {

    require(['jquery', 'utils', 'temp', 'pagination', 'navbar', 'amazeui'],
        function ($, utils, extendsFn, Page) {

            // 弹出窗
            var editModalPrototype = {
                // 获取输入数据
                getFormData: function () {
                    var _formData = utils.getFormData($('[data-role="addmodal"]'));
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
                id: '#addmodal',
                data: {}
            });

            // 角色权限
            var rolePrototype = {

                getUserList: function () {
                    var that = this;

                    utils.ajax({
                        url: this.api.search,
                        data: this.ajaxArgs,
                        success: function (data) {
                            that.data.userList = data.obj.map(function (item) {
                                return item;
                            });
                            that.render();
                        }
                    });
                },

                // 新增
                add: function (evt) {
                    this.doType = 'add';
                    editModal.data = {
                        id: Math.random()
                    };
                    editModal.render();
                    this.editModal.modal('open');
                },

                // 编辑
                edit: function (evt) {
                    this.doType = 'edit';
                    editModal.data = utils.find(evt.dataset.id, this.data.userList, 'pk');
                    editModal.render();
                    this.editModal.modal('open');
                },

                // 提交新增
                addUpdate: function () {
                    var that = this;
                    var _formData = editModal.getFormData();

                    utils.ajax({
                        url: this.api.update,
                        data: _formData,
                        success: function (data) {
                            if (data.success) {
                                that.editModal.modal('close');
                                utils.alert({
                                    text: '新增成功',
                                    type: 'success'
                                });
                                that.getUserList();
                            } else {
                                utils.alert({
                                    text: '新增失败',
                                    type: 'wraning'
                                });
                            }
                        }
                    });
                },

                // 提交编辑
                editUpdate: function () {
                    var that = this;
                    var _formData = editModal.getFormData();

                    utils.ajax({
                        url: this.api.update,
                        data: _formData,
                        success: function (data) {
                            if (data.success) {
                                that.editModal.modal('close');
                                utils.alert({
                                    text: '编辑成功',
                                    type: 'success'
                                });
                                that.getUserList();
                            } else {
                                utils.alert({
                                    text: '编辑失败',
                                    type: 'wraning'
                                });
                            }
                        }
                    });
                },

                delUpdate: function () {
                    var that = this;

                    utils.ajax({
                        url: this.api.del,
                        data: {
                            pks: this.checkId
                        },
                        success: function (data) {
                            if (data.success) {
                                utils.alert({
                                    text: '删除成功',
                                    type: 'success'
                                });
                                that.getUserList();
                            } else {
                                utils.alert({
                                    text: '删除失败',
                                    type: 'wraning'
                                });
                            }
                        }
                    });
                },

                // 删除
                del: function (evt) {
                    var that = this;

                    if (this.checkId.length < 1) {
                        utils.alert({
                            text: '请至少选择一条需要删除数据',
                            type: 'warning'
                        });
                        return;
                    }
                    this.delModal.modal('open');
                },

                // 编辑权限
                editPower: function (evt) {
                    window.location.href = 'role_info.html?pk=' + evt.dataset.id;
                },

                // 选择数据
                changeCheckbox: function (evt) {
                    var that = this;
                    if (evt.classList.contains('checked')) {
                        this.checkId.push(evt.dataset.id);
                    } else {
                        this.checkId.map(function (item, index) {
                            if (evt.dataset.id == item) {
                                that.checkId.splice(index, 1);
                            }
                        });
                    }
                },

                getInitialState: function () {
                    var that = this;

                    this.api = {
                        search: 'sysRole/findByPage', // 查询接口
                        update: 'sysRole/insert', // 修改状态
                        searchOne: 'sysRole/get', // 修改状态
                        del: 'sysRole/deleteByBatch' // 删除
                    };

                    // 默认查询参数
                    this.ajaxArgs = {
                        pageNum: 1,
                        pageSize: 10,
                        order: 'enable desc,CREATE_TIME asc'
                    };

                    // 删除提示框
                    this.delModal = utils.delModal.init({
                        content: '确定要删除这条数据吗'
                    });
                    this.delModal.modal({
                        onConfirm: function () {
                            that.delUpdate();
                        }
                    }).modal('close');

                    // 当前操作是新增/编辑
                    this.doType = null;

                    // 新增/编辑提示框
                    this.editModal = $('[data-role="addmodal"]');
                    this.editModal.modal({
                        closeOnConfirm: false,
                        onConfirm: function () {
                            switch (that.doType) {
                            case 'edit':
                                that.editUpdate();
                                break;
                            case 'add':
                                that.addUpdate();
                                break;
                            }
                        }
                    }).modal('close');

                    // 角色权限编辑框
                    this.powerModal = $('[data-role="powermodal"]');
                    this.powerModal.modal({
                        closeOnConfirm: false,
                        onConfirm: function () {

                        }
                    }).modal('close');

                    this.checkId = []; // 选中的id
                    this.getUserList();
                }
            };

            // 初始化
            (function initialization() {
                var url = "sysMenu/findMenuFunc";
                var data = {
                    menuPk: "SysRole"
                };
                utils.ajax({
                    url: url,
                    data: data,
                    success: function (data) {
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

                            var Role = extendsFn(rolePrototype);
                            var role = new Role({
                                id: '#rolelist',
                                data: {}
                            });
                        }
                    }
                });

            })();

        });

});

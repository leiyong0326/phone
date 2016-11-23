define(['../require.config'], function (r) {
    require(['jquery', 'utils', 'template', 'temp', 'datetimepicker', 'umeditor', 'umeditorConfig', 'navbar', 'amazeui', 'updataFile', 'warn', 'format'],
function ($, utils, Temp, extendsFn, datepicker, warn, format) {

        // 标签管理
        function CreateTags() {
            this.data = []; // 数据
            this.tagData = []; // 标签数据
            this.checkData = []; // 选中的数据
            this.tagModal = $('[data-modal="tag"]'); // 弹出框
            this.tagSelect = $('[name="tagType"]'); // 标签选择下拉框
            this.tagTextBox = $('.tag-text'); // 标签输入框容器
            this.tagAddBtn = $('[data-btn="tag-add"]'); // 标签新增按钮
            this.triggerBtn = $('[data-btn="tag"]'); // 标签管理按钮
            this.tagList = $('[data-content="tag-list"]'); // 标签列表
            this.tagActList = $('.caradd-act ul'); // 活动标签列表

            // 标签管理列表模板
            this.temp = new Temp({
                tempId: 'tag-list',
                data: this.data
            });

            // 标签列表模板
            this.actTemp = new Temp({
                tempId: 'tag-act-list',
                data: this.checkData
            });

            var that = this;

            that.tagModal.modal({
                onConfirm: function () {
                    that.renderTagList();
                }
            }).modal('close');

            this.getTag();
            this.events();
        }

        CreateTags.prototype = {

            // 新增
            add: function (options) {
                var data = $.extend({
                    _id: Math.random()
                }, options);

                this.data.push(data);
                this.renderList();
            },

            // 渲染标签下拉菜单
            renderTagSelect: function () {
                this.tagSelect.createSelect({
                    data: this.tagData,
                    attr: ['id', 'sname', 'name'],
                    selected: '1'
                });
            },

            // 渲染保存标签列表
            renderTagList: function () {
                this.actTemp.data = this.checkData;
                this.actTemp.render();
            },

            // 渲染新增标签列表
            renderList: function () {
                this.temp.data = this.data;
                this.temp.render();
            },

            // 输入框内容
            renderInput: function (id) {
                var data = utils.find(id, this.tagData),
                    _input = '<input class="cy-input" type="text" data-id="${id}" data-tag="${sname}" name="text" value="${name}">';

                this.tagTextBox.html(utils.v(_input, data));
            },

            // 获取标签
            getTag: function () {
                var that = this;

                utils.ajax({
                    url: 'shopCarTag/find',
                    data: {},
                    success: function (data) {
                        that.tagData = data.obj;
                        that.renderTagSelect();
                    }
                });
            },

            // 选中
            check: function () {
                var that = this;

                this.tagList.on('click', '.cy-checkbox', function () {
                    var isChecked = $(this).hasClass('checked'),
                        id = $(this).attr('data-id'),
                        i = utils.findIndex(id, that.data, '_id'),
                        option = utils.find(id, that.data, '_id');
                    if (isChecked) {
                        option.checked = '';
                        that.checkData.splice(i, 1);
                    } else {
                        option.checked = 'checked';
                        that.checkData.push(option);
                    }
                });
            },

            // 删除
            del: function (id) {
                var i = utils.findIndex(id, this.data, '_id'),
                    j = utils.findIndex(id, this.checkData, '_id');

                this.data.splice(i, 1);
                this.checkData.splice(j, 1);
                this.renderList();
            },

            // 事件处理
            events: function () {
                var that = this;

                this.check();

                // 触发弹窗
                this.triggerBtn.on('click', function () {
                    that.tagModal.modal('open');
                });

                // 修改tag
                this.tagSelect.on('change', function () {
                    that.renderInput($(this).val());
                });

                // 编辑文本
                this.tagModal.on('input', '[name="text"]', function () {
                    var id = $(this).attr('data-id'),
                        option = utils.find(id, that.tagData);

                    option.name = $(this).val();
                });

                // 新增
                this.tagAddBtn.on('click', function () {
                    var id = $('[name="text"]').attr('data-id'),
                        option = utils.find(id, that.tagData);

                    that.add(option);
                });

                // 删除
                this.tagList.on('click', '[data-btn="tag-del"]', function () {
                    var id = $(this).attr('data-id');
                    that.del(id);
                });

            }
        };

        // 图片和参数配置
        function Photo() {
            this.data = {
                pk: '', // 保存基本信息之后返回的pk
                memo: '', // 参数配置
                list: []
            }; // 数据
            this.photoData = []; // 图片数据
            this.showPhotoData = []; // 当前展示的图片
            this.colorSelect = $('.car-add-color'); // 颜色选择
            this.columnSelect = $('.car-add-column'); // 栏目选择
            this.photoContent = $('.photo-main'); // 图片容器
            this.fileEl = $('.car-add-file');
            this.colorId = null; // 颜色id
            this.columnId = null; // 栏目id

            this.api = {
                save: 'shopCar/addImg' // 保存接口
            }

            this.events();
        };

        Photo.prototype = {

            // 渲染图片及上传组件
            render: function () {
                var i = 0,
                    len = this.photoData.length,
                    view = '<div class="car-add-photo"style="background: url(${url}) no-repeat center center"></div>';
                this.showPhotoData = [];

                if (this.colorId && this.columnId) {
                    this.fileEl.removeAttr('disabled');
                    for (; i < len; i++) {
                        if (this.photoData[i].color == this.colorId &&
                            this.photoData[i].type == this.columnId) {
                            this.showPhotoData
                                .push(this.photoData[i]);
                        }
                    }
                }

                this.photoContent.html(utils.v(view,
                    this.showPhotoData));
            },

            parsePhotoData: function () {
                var data = this.photoData;
                if (data != undefined && data.length > 0) {
                    var photoArr = [];
                    for (var i = 0; i < data.length; i++) {
                        var d = data[i],
                            type = d.type,
                            color = d.color,
                            url = d.url;
                        if (type != undefined && color != undefined && url != undefined) {
                            var pushFlag = false;
                            for (var j = 0; j < photoArr.length; j++) {
                                var po = photoArr[j];
                                if (po.type == d.type && po.color == d.color) {
                                    po.url.push(url);
                                    pushFlag = true;
                                    break;
                                }
                            }
                            if (!pushFlag) {
                                photoArr.push({
                                    type: type,
                                    color: color,
                                    url: [url]
                                });
                            }
                        }
                    }
                    return photoArr;
                }
                return [];
            },

            // 保存数据
            save: function (pk) {
                this.data.memo = this.um.getContent();
                this.data.list = JSON.stringify(this.parsePhotoData());
                this.data.pk = pk;
                var that = this;
                utils.ajax({
                    url: this.api.save,
                    data: this.data,
                    success: function (data) {
                	if(data.success){
                	    history.back();
                	    utils.alert({
                                text: '保存成功',
                                type: 'success'
                            });
                	}else{
                	    utils.alert({
                                text: data.msg,
                                type: 'danger'
                            });
                	}
                    }
                })
            },

            // 事件处理
            events: function () {
                var that = this;

                $('.cy-input-file').upDataFile({
                    style: 'small',
                    imageUrl: 'carImg',
                    size : [750,500],
                    success: function (data) {
                        that.photoData.push({
                            color: that.colorId,
                            type: that.columnId,
                            url: data
                        });
                        that.render();
                    }
                });

                this.colorSelect.on('change', function () {
                    that.colorId = $(this).val();
                    that.render();
                });

                this.columnSelect.on('change', function () {
                    that.columnId = $(this).val();
                    that.render();
                });
            }
        };

        // 车辆基本信息
        var fnPrototype = {

            // 获取品牌
            getCarBrand: function () {
                var that = this;
                utils.ajax({
                    url: 'shopCarBrand/findByOrg',
                    success: function (data) {
                        data.obj.unshift({
                            name: '请选择品牌',
                            pk: ''
                        });
                        that.data.brandPks = data.obj;
                        that.render();
                    }
                });
            },

            // 获取车系
            getLine: function () {
                var that = this;
                utils.ajax({
                    url: 'shopOrgClass/findByBrand',
                    data: {
                        brandPk: that.data.brandPk
                    },
                    success: function (data) {
                        that.data.line = data.obj;
                        that.render();
                    }
                });
            },

            // 保存
            save: function () {
                var that = this;
                var _formData = utils.getFormData($('.car-main'));
                _formData.paramDetail = this.um.getContent();

                utils.ajax({
                    url: 'shopCar/save',
                    data: _formData,
                    success: function (data) {
                        if (data.success) {
                            history.back();
                            // that.photo.save(data.obj.pk);
                        } else {
                            utils.alert({
                                text: data.msg,
                                type: 'danger'
                            });
                        }
                    }
                });
            },

            getInitialState: function () {

                // 创建标签
                this.createTag = new CreateTags();

                // 创建编辑器
                this.um = UM.getEditor('editor');

                this.urlOptions = utils.parseUrl(window.location.search); // 超链接参数
                this.saveBtn = $('[data-btn="save"]');
                this.getCarBrand();

                if (this.data.brandPk) {
                    this.getLine();
                }

                this.saveBtn.on('click', this.save.bind(this));
            },

            componentWillMount: function () {
                var that = this;

                // 选择品牌
                $(this.refs.brandPk).on('change', function () {
                    var _val = $(this).val();
                    if (_val) {
                        that.data.brandPk = _val;
                        that.getLine();
                    }
                });

            },

            componentDidMount: function () {

                var inputs = $('[data-temp="carinfo"] [name]');

                inputs.each(function () {
                    var _name = $(this).attr('name');

                    $(this).on('change', function () {
                        that.data[_name] = $(this).val();
                    });
                });

                // 设置下拉菜单选中属性
                var selects = $('[data-temp="carinfo"] .cy-select');
                var that = this;
                selects.each(function () {
                    var _name = $(this).attr('name');

                    if (that.data.hasOwnProperty(_name)) {
                        $(this).val(that.data[_name]);
                    }
                });

                // 日期插件
                utils.datepick('.start-date', '.end-date', {
                    startView: 3,
                    minView: 2,
                    maxView: 4
                });

                $(this.refs.updatefile).upDataFile({
                    imgs: this.data.imgs ? this.data.imgs : null,
                    size: [258, 168],
                    success: function (data) {
                        that.data.imgs = data;
                    }
                });
            }
        };

        var CarInfo = extendsFn(fnPrototype);

        var carInfo = new CarInfo({
            id: '#carinfo',
            data: {}
        });

    });
});

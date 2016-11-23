define(['../require.config'], function (r) {
    require(['jquery', 'utils', 'template', 'temp', 'datetimepicker', 'umeditor', 'umeditorConfig', 'navbar', 'amazeui', 'updataFile', 'warn', 'format'], function ($, utils, Temp, extendsFn, datepicker, warn, format) {

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

            // 创建编辑器
            this.um = UM.getEditor('editor');

            this.events();
        };

        Photo.prototype = {

            // 渲染图片及上传组件
            render: function () {
                var i = 0,
                    len = this.photoData.length,
                    view = '<div class="car-add-photo"style="background: url(${url}) no-repeat center center"></div>';
                this.showPhotoData = [];

                if (this.columnId) {
                    this.fileEl.removeAttr('disabled');
                    for (; i < len; i++) {
                        if (this.photoData[i].type == this.columnId) {
                            this.showPhotoData.push(this.photoData[i]);
                        }
                    }
                }

                this.photoContent.html(utils.v(view, this.showPhotoData));
            },

            parsePhotoData: function () {
                var data = this.photoData;
                if (data != undefined && data.length > 0) {
                    var photoArr = [];
                    for (var i = 0; i < data.length; i++) {
                        var d = data[i], type = d.type, url = d.url;
                        if (type != undefined && url != undefined) {
                            var pushFlag = false;
                            for (var j = 0; j < photoArr.length; j++) {
                                var po = photoArr[j];
                                if (po.type == d.type) {
                                    po.url.push(url);
                                    pushFlag = true;
                                    break;
                                }
                            }
                            if (!pushFlag) {
                                photoArr.push({
                                    type: type,
                                    url: [url]
                                });
                            }
                        }
                    }
                    return photoArr;
                }
                return [];
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
                            type: that.columnId,
                            url: data
                        });
                        that.render();
                    }
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
                _formData.remark = this.data.memo = this.um.getContent();
                _formData.urls = JSON.stringify(this.photo.parsePhotoData());
                _formData.className = $('[name="classPk"] option:selected').text();
                _formData.brandName = $('[name="brandPk"] option:selected').text();
                utils.ajax({
                    url: 'shopCarOld/save',
                    data: _formData,
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
                });
            },

            getInitialState: function () {

                // 创建相册
                this.photo = new Photo();

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

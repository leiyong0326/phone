define(['../require.config'], function (r) {
    require(['jquery', 'utils', 'template', 'pagination', 'datetimepicker', 'format', 'umeditorConfig', 'umeditor', 'navbar', 'amazeui', 'updataFile'],
        function ($, utils, Temp, Page, datepicker, format) {

            var paramDetail;

            function ShopCarInfo(options) {
                this.data = {
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
                    sale: '',
                    startTime: '',
                    endTime: '',
                    stuck: '',
                    hot: '',
                    money: '',
                    forcePay: '',
                    consume: '',
                    tag1: '',
                    msg1: '',
                    tag2: '',
                    msg2: '',
                    tag3: '',
                    msg3: '',
                    tag4: '',
                    msg4: '',
                    seq: '',
                    url: '',
                    img: '',
                    paramDetail: ''
                }; // 数据
                this.data.pk = utils.parseUrl().pk; // 超链接参数

                this.dataApi = {
                    searchOne: 'shopCar/get', //查询单个
                    update: 'shopCar/update', // 修改接口
                    getBrand: 'shopCarBrand/findByOrg',
                    getClass: 'shopOrgClass/findByBrand',
                    getCarTag: 'shopCarTag/find',
                };

                //创建编辑器
                this.um = UM.getEditor('editor', {
                    autoHeightEnabled: false,
                });

                this.button = {
                    save: $('[data-btn="save"]')
                };

                this.selects = {
                    brandPk: $('[name="brandPk"]'),
                    classPk: $('[name="classPk"]'),
                    shopCarTag: $('.shopCarTag')
                };

                var that = this;

                // 新增/编辑弹出框模板
                this.temp = new Temp({
                    tempId: 'info-content',
                    data: this.data,
                    componentDidMount: function () {
                        // 下拉框
                        that.selects = {
                            brandPk: $('[name="brandPk"]'),
                            classPk: $('[name="classPk"]'),
                            shopCarTag: $('.shopCarTag')
                        };
                        this.content.find('.cy-input-file').upDataFile({
                            imageUrl: 'img',
                            size: [258, 168],
                            imgs: this.data.img
                        });
                        utils.datepick('.start-date', '.end-date');
                        this.content.find('[name="level"] option[value="' + this.data.level + '"]').attr('selected', true);
                        this.content.find('[name="status"] option[value="' + this.data.status + '"]').attr('selected', true);
                        this.content.find('[name="forcePay"] option[value="' + this.data.forcePay + '"]').attr('selected', true);
                        this.content.find('[name="sale"] option[value="' + this.data.sale + '"]').attr('selected', true);
                        this.content.find('[name="hot"] option[value="' + this.data.hot + '"]').attr('selected', true);

                        if (this.data.sale == 0) {
                            $('[name="startTime"]').attr('disabled', 'disabled');
                            $('[name="endTime"]').attr('disabled', 'disabled');
                        }
                        paramDetail = this.data.paramDetail;
                    }
                });

                var that = this;
                this.upData(function (data) {
                    that.data = data.obj;
                    that.refresh();
                    that.events();
                });

            }

            ShopCarInfo.prototype = {

                refresh: function () {
                    var that = this;
                    that.temp.data = that.modalDataFormat(this.data);
                    if (that.temp.data.startTime != undefined) {
                        that.temp.data.startTime = format.date(this.data.startTime, 'YYYY-MM-DD');
                    }
                    if (that.temp.data.endTime != undefined) {
                        that.temp.data.endTime = format.date(this.data.endTime, 'YYYY-MM-DD');
                    }
                    that.temp.render();
                    if (paramDetail != null && paramDetail.length > 0) {
                        that.um.setContent(paramDetail);
                    }
                },
                // 更新数据
                upData: function (callback) {
                    utils.ajax({
                        url: this.dataApi.searchOne,
                        data: {
                            pk: this.data.pk
                        },
                        success: function (data) {
                            callback.call(this, data);
                        }
                    });
                },

                modalDataFormat: function (data) {
                    return $.extend(data, this.data);
                },


                // 获取品牌
                getBrand: function () {
                    var that = this;
                    utils.ajax({
                        url: that.dataApi.getBrand,
                        success: function (data) {
                            var obj = data.obj;
                            that.selects.brandPk.createSelect({
                                data: obj,
                                attr: ['pk', 'name'],
                                selected: that.data.brandPk
                            });
                            if (that.data.brandPk.length > 0) {
                                that.getClass();
                            }
                        }
                    });
                },

                // 获取车系
                getClass: function () {
                    var that = this;
                    var brandPk = that.selects.brandPk.val();
                    if (brandPk != '') {
                        utils.ajax({
                            url: that.dataApi.getClass,
                            data: {
                                'brandPk': brandPk
                            },
                            success: function (data) {
                                that.selects.classPk.createSelect({
                                    data: data.obj,
                                    attr: ['pk', 'name'],
                                    selected: that.data.classPk
                                });
                            }
                        });
                    }

                },

                getCarTag: function () {
                    var that = this;
                    utils.ajax({
                        url: that.dataApi.getCarTag,
                        success: function (data) {
                            that.selects.shopCarTag.createSelect({
                                data: data.obj,
                                attr: ['id', 'sname'],
                            });
                            $('[name="tag1"] option').first().remove();
                            $('[name="tag2"] option').first().remove();
                            $('[name="tag3"] option').first().remove();
                            $('[name="tag4"] option').first().remove();
                            if (that.data.tag1 != undefined) {
                                $('[name="tag1"] option[value="' + that.data.tag1 + '"]').attr('selected', true);
                            }
                            if (that.data.tag2 != undefined) {
                                $('[name="tag2"] option[value="' + that.data.tag2 + '"]').attr('selected', true);
                            }
                            if (that.data.tag3 != undefined) {
                                $('[name="tag3"] option[value="' + that.data.tag3 + '"]').attr('selected', true);
                            }
                            if (that.data.tag4 != undefined) {
                                $('[name="tag4"] option[value="' + that.data.tag4 + '"]').attr('selected', true);
                            }
                        }
                    });
                },

                updateData: function () {
                    var that = this;
                    var umContent = this.um.getContent();
                    if (umContent.trim().length < 1) {
                        utils.alert({
                            text: '商品详情不能为空',
                            type: 'danger'
                        });
                        return;
                    } else {
                        if (paramDetail != null && paramDetail.trim() != umContent.trim()) {
                            this.data.url = this.data.url + "?edit";
                        }
                        this.data.paramDetail = umContent;
                    }
                    utils.ajax({
                        url: this.dataApi.update,
                        data: this.data,
                        success: function (data) {
                            if (data.success == true) {
                                history.back();
                                utils.alert({
                                    text: '修改成功',
                                    type: 'success'
                                });

                            } else {
                                utils.alert({
                                    text: data.msg,
                                    type: 'danger'
                                });
                            }

                        }
                    });
                },



                // 控制
                events: function () {
                    var that = this;
                    that.getBrand();
                    that.getCarTag();
                    that.temp.content.on('change', '[name="brandPk"]', function () {
                        that.getClass();
                    });
                    this.button.save.on('click', function () {
                        that.data = utils.getFormData($('[data-modal="update"]'));
                        that.updateData();
                    });
                    $('[name="sale"]').on('change', function () {
                        switch ($(this).val()) {
                        case '0':
                            $('[name="startTime"]').val('');
                            $('[name="endTime"]').val('');
                            $('[name="startTime"]').attr('disabled', 'disabled');
                            $('[name="endTime"]').attr('disabled', 'disabled');
                            break;
                        case '1':
                            $('[name="startTime"]').removeAttr('disabled');
                            $('[name="endTime"]').removeAttr('disabled');
                        }
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

                            var shopCarInfo = new ShopCarInfo(power);
                        }
                    }
                });
            })();

        });

});

define(['../require.config'], function (r) {
    require(['jquery', 'utils', 'template', 'temp', 'datetimepicker', 'umeditor', 'umeditorConfig', 'navbar', 'amazeui', 'updataFile', 'warn', 'format'], function ($, utils, Temp, extendsFn, datepicker, warn, format) {
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
                _formData.className = $('[name="classPk"] option:selected').text();
                _formData.brandName = $('[name="brandPk"] option:selected').text();
                _formData.pk = utils.parseUrl().pk;
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
            
            search:function(){
        	var that= this;
        	utils.ajax({
        	    url:'shopCarOld/get',
        	    data:{
        		pk:utils.parseUrl().pk
        	    },
        	    success:function(data){
        		that.data = $.extend(that.data, data.obj);
        		that.data.buyTime = utils.dateFormat(that.data.buyTime, 'YYYY-MM-DD');
        		if(that.data.brandPk){
        		    that.getLine();
        		}
        		that.render();
        	    }
        	})
            },

            getInitialState: function () {

                // 创建编辑器
                this.um = UM.getEditor('editor');

                this.urlOptions = utils.parseUrl(window.location.search); // 超链接参数
                this.saveBtn = $('[data-btn="save"]');
                this.search();
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
                    imgs: this.data.img ? this.data.img : null,
                    size: [258, 168],
                    success: function (data) {
                        that.data.img = data;
                    }
                });
                if(this.data.remark){
                    this.um.setContent(this.data.remark); 
                }
            }
        };

        var CarInfo = extendsFn(fnPrototype);

        var carInfo = new CarInfo({
            id: '#carinfo',
            data: {}
        });

    });
});

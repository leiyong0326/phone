define(['../require.config'], function (r) {

    require(['jquery', 'utils', 'temp', 'config', 'pagination', 'datetimepicker', 'navbar', 'amazeui'], function ($, utils, extendsFn, config, Page, datepicker) {

        // 详情
        var prizedrawInfoPrototype = {

            // 更新数据
            upDate: function () {
                var that = this;

                utils.ajax({
                    url: config.api.getMtInsurance,
                    data: {
                        pk: this.urlArgs.id
                    },
                    success: function (data) {
                        that.data = data.obj;
                        that.render();
                    }
                });
            },

            //  保存
            save: function () {
                var that = this;
                var formData = utils.getFormData($('.insurance'));

                if(isNaN(formData.evaluation) || formData.evaluation < 1) {
                    utils.alert({
                        text: '请输入正确的报价',
                        type: 'warning'
                    });
                    return;
                }

                utils.ajax({
                    url: config.api.sendInsurancePrice,
                    data: {
                        pk: this.urlArgs.id,
                        status: 1,
                        money: formData.evaluation
                    },
                    success: function (data) {
                        utils.alert({
                            text: '保存成功',
                            type: 'success'
                        });
                        setTimeout(function() {
                            history.back();
                            window.reload();
                        }, 1500);
                    }
                });
            },

            getInitialState: function () {
                this.urlArgs = utils.parseUrl();
                this.upDate();
            }
        };

        var PrizedrawInfo = extendsFn(prizedrawInfoPrototype);
        var prizedrawInfo = new PrizedrawInfo({
            id: '#detailinfo',
            data: {
                version: '1.0.0'
            }
        });

    });

});

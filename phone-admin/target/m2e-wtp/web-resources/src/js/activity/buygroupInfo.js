/*
 * Date: 2016.07.04
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */


define(['../require.config'], function (r) {

    require(['utils', 'template', 'temp', 'config', 'datetimepicker', 'umeditor', 'umeditorConfig', 'navbar', 'amazeui', 'updataFile'], function (utils, Temp, extendsFn, config, datepicker) {

        var activityInfoPrototype = {

            // 更新数据
            upDate: function () {
                var that = this;

                utils.ajax({
                    url: config.api.searchActInfo,
                    data: {
                        pk: this.pageId
                    },
                    success: function (data) {
                        console.log(data);
                        that.data = $.extend(that.data, that.formatData(data.obj));
                        that.render();
                    }
                });
            },

            formatData: function (data) {
                return $.extend(data, {
                    banner: data.banner == 1 ? 'checked' : '',
                    forcePay: data.forcePay == 1 ? 'checked' : '',
                    startTime: utils.dateFormat(data.startTime, 'YYYY-MM-DD'),
                    endTime: utils.dateFormat(data.endTime, 'YYYY-MM-DD')
                });
            },

            // 获取参与对象
            getUser: function () {
                var that = this;

                utils.ajax({
                    url: config.api.searchUser,
                    data: {
                        pageNum: 0,
                        pageSize: 0
                    },
                    success: function (data) {
                        var _data = [{
                            name: '不限',
                            pk: ''
                        }];
                        that.data.users = _data.concat(data.obj);
                        that.render();
                    }
                });
            },

            // 获取奖品列表
            getGift: function () {
                var that = this;

                utils.ajax({
                    url: config.api.searchGift,
                    data: {
                        pageNum: 0,
                        pageSize: 0
                    },
                    success: function (data) {
                        var _data = [{
                            name: '无',
                            pk: ''
                        }];
                        that.data.gifts = _data.concat(data.obj);
                        that.render();
                    }
                });
            },

            // 保存
            save: function () {
				var that = this,
					options = utils.getFormData($('.cy-form')),
					_content = this.um.getContent();

				if(this.pageId) {
					options.pk = this.data.pk;
				}

				options.typePk = 1;

				if(_content.length > 0) {
					options.content = _content;
				}else {
					utils.alert({
						text: '活动内容不能为空.',
						type: 'warning'
					});
					return;
				}

                utils.ajax({
                    url: config.api.saveActInfo,
                    data: options,
                    success: function (data) {
                        if (data.success) {
                            utils.alert({
                                text: '保存成功',
                                type: 'success'
                            });
							history.back();
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
                var urlArgs = utils.parseUrl();
                this.pageId = urlArgs.id;

                if (this.pageId) {
                    // 编辑状态
                    this.upDate();
                }
                this.getUser();
                this.getGift();
            },

            componentDidMount: function () {
                var that = this;
                var inputs = $('[data-temp="activity-info-content"] [name]');

                inputs.each(function () {
                    var _name = $(this).attr('name');

                    $(this).on('change', function () {
                        that.data[_name] = $(this).val();
                    });
                });

                // 设置下拉菜单选中属性
                var selects = $('[data-temp="activity-info-content"] .cy-select');
                var that = this;
                selects.each(function () {
                    var _name = $(this).attr('name');

                    if (that.data.hasOwnProperty(_name)) {
                        $(this).val(that.data[_name]);
                    }
                });

                // 日期插件, 避免多次创建
                $('.datetimepicker').remove();
                utils.datepick('.activity-start', '.activity-end', {
                    startView: 3,
                    minView: 2,
                    maxView: 4
                });

                $(this.refs.updatefile).upDataFile({
                    imgs: this.data.img ? this.data.img : null,
                    size: [750, 300],
                    success: function (data) {
                        that.data.img = data;
                    }
                });

                // 编辑器
                $(this.refs.editer).html('<script type="text/plain" id="editor" style="width:100%;height:250px;"></script>');
				setTimeout(function() {
					that.um = UM.getEditor('editor');
	                that.um.addListener('ready', function (editor) {
						if(that.data.content && that.data.content.length > 8) {
							that.um.setContent(that.data.content);
						}
	                });
				}, 200);
            }
        };

        // 初始化
        (function initialization() {

            var power = { // 用户权限
                add: true,
                edit: true,
                del: true,
                edits: true
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

            var ActivityInfo = extendsFn(activityInfoPrototype);
            var activityInfo = new ActivityInfo({
                id: '#activity-info-content',
                data: {
                    power: power
                }
            });

            // var pageRender = new PageRender(power);

        })();

    });

});

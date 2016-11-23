define(['../require.config'], function (r) {

    require(['jquery', 'utils', 'temp', 'config', 'pagination', 'datetimepicker', 'navbar', 'amazeui'],
        function ($, utils, extendsFn, config, Page, datepicker) {

            // 新增/编辑窗口
            var addModalPrototype = {

                upDate: function () {
                    var that = this;

                    utils.ajax({
                        url: config.api.searchGiftNameList,
                        data: this.ajaxArgs,
                        success: function (data) {
                            that.data.gifts = data.obj.map(function (item) {
                                item.types = ['实物奖品', '电子代金券'][item.type];
                                return item;
                            });
                            that.render();
                        }
                    });
                },

                // 选取
                check: function (evt) {
                    var options = utils.find(evt.dataset.id, this.data.gifts, 'pk'); // 当前列表选中的对象
					var index = utils.findIndex(evt.dataset.id, this.options.parent.data.giftList, 'pk'); //抽奖列表选中的对象的索引值
                    var _index = utils.findIndex(this.checkId, this.options.parent.data.giftList, 'pk'); // 抽奖模板当前编辑对象的索引值

					if(!options.checked) {
						if(this.eventType == 'edit') {
							this.options.parent.data.giftList[_index] = options;
						}else {
                            if(this.options.parent.data.giftList.length >= 6) {
        						utils.alert({
        							text: '最多只能添加六个奖品',
        							type: 'warning'
        						})
        						return;
        					}
							this.options.parent.data.giftList.push(options);
						}
                        options.checked = 'green';
					}else {
						options.checked = false;
						this.options.parent.data.giftList.splice(index, 1);
					}
                    this.options.parent.render();
					this.render();
                },

                // 查询
                search: function() {
                    this.ajaxArgs = $.extend(this.ajaxArgs, {
                        name: this.refs.searchInput.value
                    });
                    this.upDate();
                },

                getInitialState: function () {
                    this.ajaxArgs = {
                        pageNum: 0,
                        pageSize: 0
                    };
					this.eventType = 'add'; // 操作状态
					this.checkId = null; // 编辑状态下选中的Id
                    this.upDate();
                }
            };

            // 详情
            var prizedrawInfoPrototype = {

                // 更新数据
                upDate: function () {
                    var that = this;

                    utils.ajax({
                        url: config.api.searchGiftInfo,
                        data: {
                            pk: this.pageId
                        },
                        success: function (data) {
                            that.data.model = data.obj.model;
                            that.data.giftList = data.obj.list.map(function(item) {
                                item.name = item.dsc;
                                return item;
                            });
                            that.render();
                        }
                    });
                },

                // 新增
                edit: function (evt) {
                    this.addModal.modal('open');
					this._addModal.eventType = 'edit';
					this._addModal.checkId = evt.dataset.id;
                    this._addModal.ajaxArgs = {
                        pageNum: 0,
                        pageSize: 0
                    };
                    this._addModal.upDate();
                },

				// 删除
				del: function(evt) {
					var index = utils.findIndex(evt.dataset.id, this.data.giftList, 'pk');
					this.data.giftList.splice(index, 1);
					this.render();
				},

                // 打开新增窗口
                openAddModal: function () {
                    this.addModal.modal('open');
					this._addModal.eventType = 'add';
                    this._addModal.ajaxArgs = {
                        pageNum: 0,
                        pageSize: 0
                    };
                    this._addModal.upDate();
                },

                //  保存
                save: function () {
                    var that = this;

                    var options = utils.getFormData($('.prizedraw-dsc'));
                    this.data.model = $.extend(this.data.model, options);

                    this.data.giftList.map(function(item, index) {
                        item.masterPk = that.pageId;
                        item.giftPk = item.pk;
                        item.dsc = item.name;
                        item.way = item.type;
                        item.num = 1;
                        item.level = index + 1;
                        delete item.checked;
                        delete item.pk;
                        return item;
                    });



                    utils.ajax({
                        url: config.api.saveGiftInfo,
                        data: {
                            list: JSON.stringify(this.data.giftList),
                            model: JSON.stringify(this.data.model)
                        },
                        success: function (data) {
                            if (data.success) {
                                utils.alert({
                                    text: '保存成功',
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

                // 修改属性
                changeAttr: function(evt) {
                    var _options = utils.find(evt.dataset.id, this.data.giftList, 'pk'),
                        chanceTotal = 0;

                    if(evt.name == 'count' && evt.value > _options.num) {
                        utils.alert({
                            text: '所选的数量不能超过总数量',
                            type: 'warning'
                        });
                        evt.value = 1;
                        return;
                    }
                    if(evt.name == 'chance') {
                        chanceTotal = 1 * evt.value;
                        // 抽奖概率总数大于1
                        for(var i = 0; i < this.data.giftList.length; i++) {
                            if(this.data.giftList[i].chance && this.data.giftList[i].pk != evt.dataset.id) {
                                chanceTotal += 1 * this.data.giftList[i].chance;
                            }
                        }
                        if(chanceTotal > 1) {
                            utils.alert({
                                text: '所有奖品概率总和不能超过1',
                                type: 'warning'
                            });
                            evt.value = '';
                            return;
                        }
                    }
                    _options[evt.name] = evt.value;
                },

                getInitialState: function () {
                    var that = this;
                    this.urlArgs = utils.parseUrl();

                    this.addBtn = $('[data-prize="add"]'); // 新增按钮
                    this.addModal = $('[data-prize="add-modal"]'); // 新增

                    var AddModal = extendsFn(addModalPrototype);
                    this._addModal = new AddModal({
                        id: '#add-content',
                        data: {},
                        parent: this
                    });

                    this.addModal.modal({
                        width: 800,
                        height: 400
                    }).modal('close');

                    if(this.urlArgs.type == 'edit') {
                        this.pageId = this.urlArgs.id;
                        this.upDate();
                    }
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

                var PrizedrawInfo = extendsFn(prizedrawInfoPrototype);
                var prizedrawInfo = new PrizedrawInfo({
                    id: '#prizedraw-info',
                    data: {
                        power: power,
                        giftList: [],
                        model: {}
                    }
                });

            })();

        });

});

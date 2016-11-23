define(['../require.config'], function (r) {
    require(['jquery', 'utils', 'temp', 'navbar', 'amazeui'],
        function ($, utils, extendsFn) {

            var powerPrototype = {

                // 获取基础列表
                getTableList: function (options) {
                    var that = this;

                    utils.ajax({
                        url: 'sysMenu/findFuncList',
                        data: options,
                        success: function (data) {
							var _data = {};
                            var _obj = 0;
                            if (!options.rolePk) {
                                that.data.list = that.formatInitData(data.obj);
								that.getTableList({
									rolePk: that.rolePk
								});
                            } else {
								_data = data.obj;
								for(var i = 0; i < that.data.list.length; i ++) {
                                    _obj = utils.findIndex(that.data.list[i].pk, _data, 'pk');
                                    if(_data[_obj]) {
                                        _data[_obj].notVisit = true;
                                    }
									delete that.data.list[i].name;
									that.data.list[i] = $.extend(that.data.list[i], _data[_obj]);
								};
								that.data.list = that.formatData(that.data.list);
								that.render();
                            }
                        }
                    });
                },

                // 选中所有
                checkAll: function (evt) {
                    var str = '';
                    if (!evt.checked) {
                        this.data.allchecked = false;
                        str = '';
                    } else {
                        this.data.allchecked = true;
                        str = 'show,edit,del,visit';
                    }
                    for (var i = 0, len = this.data.list.length; i < len; i++) {
                        this.data.list[i].name = str;
                    }
                    this.data.list = this.formatData(this.data.list);
                    this.render();
                },

                // 选中一列
                checkCol: function (evt) {

                },

                // 选中一行
                checkRow: function (evt) {
                    var _obj = utils.find(evt.dataset.id, this.data.list, 'pk');

                    if (!evt.checked) {
						this.data.allchecked = false;
                        _obj.name = '';
                    } else {
                        _obj.name = 'show,edit,del';
                    }
                    this.data.list = this.formatData(this.data.list);
                    this.render();
                },

				// 设置一个
				checkOne: function(evt) {
					var _obj = utils.find(evt.dataset.id, this.data.list, 'pk');

                    if(evt.dataset.type == 'visit') {
                        // 若是设置访问权限
                        if (!evt.checked) {
                            _obj.notVisit = false;
    						_obj.name = '';
                        } else {
                            _obj.notVisit = true;
                            _obj.name = ',';
                        }
                    }else {
                        if (!evt.checked) {
    						this.data.allchecked = false;
                            _obj.name = _obj.name.replace(evt.dataset.type, '');
                        } else {
                            _obj.name = _obj.name + ',' + evt.dataset.type;
                        }
                    }
					this.data.list = this.formatData(this.data.list);
                    this.render();
				},

                /* 格式化初始数据
                 * 根据属性判断是否可编辑这些权限
                 */
                formatInitData: function (data) {
                    return data.map(function (item) {
                        item.box = {
                            show: item.name ? item.name.indexOf('show') != -1 : false,
                            edit: item.name ? item.name.indexOf('edit') != -1 : false,
                            del: item.name ? item.name.indexOf('del') != -1 : false,
                            visit: item.name ? true : false
                        };

                        if (item.name) {
                            item.box.row = true;
                        } else {
                            item.box.row = false;
                        }
                        return item;
                    });
                },

                // 格式化数据
                formatData: function (data) {
                    return data.map(function (item) {
						item.powers = {
							show: item.name ? item.name.indexOf('show') != -1 : false,
                            edit: item.name ? item.name.indexOf('edit') != -1 : false,
                            del: item.name ? item.name.indexOf('del') != -1 : false,
                            visit: item.notVisit ? true : false
						};

                        if(!item.name) {
                            item.name = '';
                        }

                        if (item.powers.show && item.powers.edit && item.powers.del && item.powers.visit) {
                            item.checked = true;
                        } else {
                            item.checked = false;
                        }

                        return item;
                    });
                },

				// 保存
				save: function() {

					var saveData = [];
                    var _i = 0;
					for(var i = 0; i < this.data.list.length; i++) {
                        if(this.data.list[i].notVisit) {
                            saveData[_i] = $.extend({}, this.data.list[i]);
                            _i++;
                        }
					}

					saveData.map(function(item) {
                        delete item.notVisit;
						delete item.box;
						delete item.powers;
						delete item.text;
						delete item.checked;
						return item;
					});

					utils.ajax({
                        url: 'sysRole/grantRole',
                        data: {
                            menuPks: JSON.stringify(saveData),
                            rolePk: this.rolePk
                        },
                        success: function (data) {
                            if(data.success) {
                                utils.alert({
                                    text: '保存成功',
                                    type: 'success'
                                });
                            }
                        }
                    });
				},

                getInitialState: function () {
                    this.rolePk = utils.parseUrl().pk;
                    this.getTableList({"pk":this.rolePk});
                }
            };

            var Power = extendsFn(powerPrototype);
            var power = new Power({
                id: '#powermodal',
                data: {}
            });

        });
});

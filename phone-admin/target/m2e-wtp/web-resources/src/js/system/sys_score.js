/*
 * Date: 2016.07.20
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */

define(['../require.config'],
    function (r) {

        require(['jquery', 'format', 'warn', 'utils', 'template', 'pagination', 'datetimepicker', 'navbar', 'amazeui'],
            function ($, fmt, w, utils, Temp, Page, datepicker) {

                function PageRender(options) {
                    this.data = null; // 数据
                    this.orgScore = null;
                    this.temp = null; // 模板
                    this.options = options;
                    this.loadOver = false;
                    this.dataApi = {
                        search: 'sysOrgScore/find',
                        // 查询接口
                        save: 'sysOrgScore/save',
                        updateScoreRate: 'sysOrgScore/updateScoreRate',
                        selectOrgScore: 'sysOrgScore/selectOrgScore'
                    };
                    // 默认查询参数
                    this.searchOptions = {
                        order: 'type asc'
                    };

                    // 新增列表参数
                    this.addOptions = {};
                    this.pkOptions = {};

                    // 按钮组合
                    this.btns = {
                        save: $('[data-btn="save"]')
                    }

                    var that = this;

                    // 第一次更新数据
                    this.upData(function (data) {
                        that.data = data;
                        if (that.loadOver) {
                            // 创建模板
                            that.temp = new Temp({
                                tempId: 'add-content',
                                data: that.dataFormat(that.data)
                            });
                            that.events();
                        } else {
                            that.loadOver = true;
                        }

                    }, this.dataApi.search);
                    // 第一次更新数据
                    this.upData(function (data) {
                        that.orgScore = data;
                        if (that.loadOver) {
                            // 创建模板
                            that.temp = new Temp({
                                tempId: 'add-content',
                                data: that.dataFormat(that.data)
                            });
                            that.events();
                        } else {
                            that.loadOver = true;
                        }
                    }, this.dataApi.selectOrgScore);
                }

                PageRender.prototype = {
                    // 更新数据
                    upData: function (callback, url) {
                        utils.ajax({
                            url: url,
                            data: this.searchOptions,
                            success: function (data) {
                                if (data.success) {
                                    callback.call(this, data.obj);
                                }
                            }
                        });
                    },

                    save: function () {
                        var that = this;
                        var _data = utils.getFormData($('[data-content="add-content"]'));
                        var datas = [];

                        for(var attr in _data) {
                            if(attr != 'orgScore') {
                                datas.push({
                                    type: fmt.scoreValue(attr),
                                    score: _data[attr],
                                    pk: that.pkOptions[attr]
                                });
                            }
                        }
                        var saveSuccess = false;
                        if (datas.length > 0) {
                            utils.ajax({
                                url: that.dataApi.save,
                                data: {
                                    infos: JSON.stringify(datas)
                                },
                                success: function (data) {
                                    that.dataFormat(datas);
                                    for (var i = 0; i < datas.length; i++) {
                                        for (var j = 0; j < that.data.length; j++) {
                                            if (that.data[j].type == datas[i].type) {
                                                that.data[j].score = datas[i].score;
                                                break;
                                            }
                                        }
                                    }
                                    if (saveSuccess) {
                                        that.temp = new Temp({
                                            tempId: 'add-content',
                                            data: that.dataFormat(that.data)
                                        });
                                        utils.alert({
                                            text: w.editSuccess,
                                            type: 'success'
                                        });
                                    } else {
                                        saveSuccess = true;
                                    }
                                }
                            });
                        } else {
                            saveSuccess = true;
                        }
                        //积分比例配置
                        var score = $('body').find('[name="orgScore"]').val();
                        if (that.addOptions.orgScore != score) {
                            utils.ajax({
                                url: that.dataApi.updateScoreRate,
                                data: {
                                    scoreRate: score
                                },
                                success: function (data) {
                                    that.orgScore = score;
                                    if (saveSuccess) {
                                        that.temp = new Temp({
                                            tempId: 'add-content',
                                            data: that.dataFormat(that.data)
                                        });
                                        utils.alert({
                                            text: w.editSuccess,
                                            type: 'success'
                                        });
                                    } else {
                                        saveSuccess = true;
                                    }
                                }
                            });
                        } else {
                            if (saveSuccess) {
                                utils.alert({
                                    text: w.editSuccess,
                                    type: 'success'
                                });
                            } else {
                                saveSuccess = true;
                            }
                        }
                    },
                    // 格式化数据
                    dataFormat: function (data) {
                        var that = this;
                        var listData = null;

                        if (!Array.isArray(data)) {
                            return false;
                        }
                        data.map(function (item) {
                            var key = fmt.scoreType(item.type);
                            that.addOptions[key] = item.score;
                            that.pkOptions[key] = item.pk;
                            return item;
                        });
                        that.addOptions['orgScore'] = that.orgScore;
                        return [this.addOptions];
                    },

                    // 控制
                    events: function () {
                        var that = this;

                        // 事件组合
                        this.elementEvents = {
                            save: this.save.bind(this)
                        };

                        ['save'].map(function (i) {
                            that.btns[i].on('click',
                                function () {
                                    that.elementEvents[i]();
                                });
                        });
                    }
                };

                // 初始化
                (function initialization() {
                    var url = "sysMenu/findMenuFunc";
                    var data = {
                        menuPk: "SysOrgScore"
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

                                var pageRender = new PageRender(power);
                            }
                        }
                    });
                })();

            });

    });

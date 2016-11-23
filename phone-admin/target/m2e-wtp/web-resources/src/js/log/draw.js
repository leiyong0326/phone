/*
 * Date: 2016.07.20
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */

define(['../require.config'],
function(r) {

    require(['jquery', 'format', 'warn', 'utils', 'template', 'pagination', 'datetimepicker', 'navbar', 'amazeui'],
    function($, fmt, w, utils, Temp, Page, datepicker) {

        function PageRender(options) {
            this.data = null; // 数据
            this.temp = null; // 模板
            this.checkId = []; // 选中id
            this.options = options;
            this.addModal = $('[data-department="add-modal"]'); // 新增编辑的窗口
            this.dataApi = {
                search: 'logDrawMoney/findByPage',
                // 查询接口
                update: 'logDrawMoney/update',
                // 修改状态
                save: 'logDrawMoney/insert',
                // 修改状态
                updateConfim: 'logDrawMoney/updateConfim',
                // 修改状态
                del: 'logDrawMoney/deleteByBatch',
                // 删除接口
                searchOne: 'logDrawMoney/get'
            };
            // 默认查询参数
            this.searchOptions = {
                pageNum: 1,
                pageSize: 10,
                orderBy: 'CREATE_TIME desc'
            };

            // 新增列表参数
            this.addOptions = {
        	pk:null,
        	userPk: '',
        	name: '',
        	phone: '',
        	money: '',
        	to: '',
        	createTime: '',
        	confimTime: '',
        	confimBy: ''
            };

            // 新增/编辑弹出框模板
            this.addTemp = new Temp({
                tempId: 'add-content',
                data: this.addOptions,
                events: function() {
                    this.content.find('select').selected();
                }
            });

            // 创建新增/编辑弹窗
            this.addModal.modal({
                closeOnConfirm: false,
                onConfirm: function() {
                    if(that.eventType!='show'){
                         // 遍历数据
                        console.log(that.addOptions);
                        that.saveData();
                    }else{
                        that.addModal.modal('close');
                    }
                }
            }).modal('close');
            this.delModal = utils.delModal.init({ // / 删除提示框
                title: w.t,
                content: w.d
            });

            // 按钮组合
            this.btns = {
                add: $('[data-btn="add"]'),
                del: $('[data-btn="del"]')
            }

            var that = this;

            // 创建分页
		that.pagination = new Page({
			pageNo: that.searchOptions.pageNum,
			events: function(id) {
				that.searchOptions.pageNum = id;
				that.upData(function(data) {
					that.temp.data = that.dataFormat(data);
					that.temp.render();
				});
			}
		});
            // 第一次更新数据
            this.upData(function(data) {
                that.data = data;

                // 创建模板
                that.temp = new Temp({
                    tempId: 'data-list',
                    data: that.dataFormat(that.data)
                });
                that.events();
            });
        }

        PageRender.prototype = {
            initSelect: function() {
        	var that=this;
		utils.ajax({
		    url : 'sysUser/findAll',
		    data:{
			orderBy:"CREATE_TIME"
		    },
		    success : function(data){
			 var obj = data.obj;
			 for(var i = 0;i<obj.length;i++){
			     obj[i].name = obj[i].name+'-'+obj[i].phone;
			 }
                         that.addModal.find('[name="userPk"]').createSelect({
                             data: obj,
                             attr: ['pk', 'name'],
                             selected: that.addOptions.userPk
                         });
		    }
		});
		utils.ajax({
		    url : 'goodsPhone/findAll',
		    data:{
			orderBy:"SEQ"
		    },
		    success : function(data){
			 var obj = data.obj;
                         that.addModal.find('[name="phonePk"]').createSelect({
                             data: obj,
                             attr: ['pk', 'name'],
                             selected: that.addOptions.phonePk
                         });
		    }
		});
            },
            // 更新数据
            upData: function(callback) {
        	var that = this;
                utils.ajax({
                    url: this.dataApi.search,
                    data: this.searchOptions,
                    success: function(data) {
                        if (data.success) {
    			that.pagination.options.pageTotal = data.total;
    	                that.pagination.refreshData(that.searchOptions.pageNum);
                            callback.call(this, data.obj);
                        }
                    }
                });
            },

            // 删除数据
            del: function() {
                var that = this;

                this.delModal.modal({
                    onConfirm: function() {
                        var url = that.dataApi.del;
                        var text = w.delSuccess;
                        utils.ajax({
                            url: url,
                            data: {pks:that.checkId},
                            success: function(data) {
                                if (data.success) {
                                    utils.alert({
                                        text: text,
                                        type: 'success'
                                    });

                                    that.refreshData();
                                    that.addModal.modal('close');
                                } else {
                                    utils.alert({
                                        text: data.msg,
                                        type: 'danger'
                                    });
                                }
                            }
                        });
                    }
                });
            },

            // 初始化添加数据
            add: function() {
                for (attr in this.addOptions) {
                    this.addOptions[attr] = '';
                }
                this.eventType = 'add';
                this.refreshModal();
            },

            // 初始化编辑数据
            saveData: function() {
                var that = this;
                var url = this.dataApi.updateConfim;
                var text = this.addOptions.pk == undefined ||this.addOptions.pk==''? w.addSuccess: w.editSuccess;
                utils.ajax({
                    url: url,
                    data: this.addOptions,
                    success: function(data) {
                        if (data.success) {
                            utils.alert({
                                text: text,
                                type: 'success'
                            });

                            that.refreshData();
                            that.addModal.modal('close');
                        } else {
                            utils.alert({
                                text: data.msg,
                                type: 'danger'
                            });
                        }
                    }
                });
            },
            changeState: function(btn) {
                var pk = btn.data('id');
                var text = btn.html();
                var url = undefined;
                var prompt = undefined;
                var removeClass,addClass,stateText;
                if (text == '禁用') {
                    text = '启用';
                    removeClass = 'cy-btn-orange';
                    addClass = 'cy-btn-green';
                    url = this.dataApi.disable;
                    prompt = w.dsbs;
                    stateText = fmt.status('0');
                } else {
                    text = '禁用';
                    addClass = 'cy-btn-orange';
                    removeClass = 'cy-btn-green';
                    url = this.dataApi.enable;
                    prompt = w.enbs;
                    stateText = fmt.status('1');
                }
                utils.ajax({
                    url: url,
                    data: {
                        pk: pk
                    },
                    success: function(data) {
                        if (data.success) {
                            btn.removeClass(removeClass).addClass(addClass);
                            btn.html(text);
                            btn.parent().prev().html(stateText);
                            utils.alert({
                                text: prompt,
                                type: 'danger'
                            });
                        }
                    }
                });
            },
            // 刷新弹框
            refreshModal: function() {
                var that = this;
                this.addTemp.data = this.addOptions;
                this.addTemp.render();

                //this.getType(function() {
                //});
                this.initSelect();

                if (that.addOptions.upPk != undefined) {
                    that.addTemp.content.find('.type-name option[value="' + that.addOptions.upPk + '"]').attr('selected', true);
                }
                this.addModal.modal('open');
            },
            // 刷新列表数据
            refreshData: function() {
                var that = this;

                this.searchOptions.pageNum = 1;

                this.upData(function(data) {
                    that.data = data;
                    that.temp.data = that.dataFormat(data);
                    that.temp.render();
                });
            },

            // 选中
            check: function() {
                var that = this;

                this.temp.content.on('click', '.cy-checkbox',
                function() {
                    var _this = $(this);
                    if (!_this.hasClass('checked')) {
                        that.checkId.push(_this.data('id'));
                    } else {
                        that.checkId.map(function(item, index) {
                            if (_this.data('id') == item) {
                                that.checkId.splice(index, 1);
                            }
                        });
                    }
                });
            },

            // 格式化数据
            dataFormat: function(data) {
        	var that = this;
                var listData = null;

                if (!Array.isArray(data)) {
                    return false;
                }

                return data.map(function(item) {
                    item.id = item.pk;
                    item.enableShow = fmt.display(that.options.edit);
                    item.enableStyle = fmt.statusStyle(item.enable);
                    item.enableBtnText = fmt.statusText(item.enable);
                    item.editShow = fmt.display(that.options.edit&&!item.confimTime);
                    item.showShow = fmt.display(that.options.show);
                    item.createTime = fmt.date(item.createTime);
                    item.confimTime = fmt.date(item.confimTime);
                    return item;
                });
            },

            // 控制
            events: function() {
                var that = this;

                this.check();

                // 事件组合
                this.elementEvents = {
                    add: this.add.bind(this),
                    del: this.del.bind(this)
                };

                ['add', 'del'].map(function(i) {
                    that.btns[i].on('click',
                    function() {
                        if (i != 'add' && that.checkId.length == 0) {
                            utils.alert({
                                text: w.ms,
                                type: 'warning'
                            });
                            return;
                        }
                        that.elementEvents[i]();
                    });
                });
                //编辑
                this.temp.content.on('click','.editBtn',function(){
                    that.eventType = 'edit';
                    utils.ajax({
                        url: that.dataApi.searchOne,
                        data: {
                            pk: $(this).data('id')
                        },
                        success: function(data) {
                            var obj = data.obj;
                            for (var attr in that.addOptions) {
                                that.addOptions[attr] = obj[attr];
                            }
                            that.addOptions['createTime'] = fmt.date(obj['createTime']);
                            that.addOptions['confimTime'] = fmt.date(obj['confimTime']);
                            that.refreshModal();
                        }
                    });
                
                })
            }
        };

        // 初始化
        (function initialization() {
            var url = "sysMenu/findMenuFunc";
            var data = {
                menuPk: "LogDrawMoney"
            };
            utils.ajax({
                url: url,
                data: data,
                success: function(data) {
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
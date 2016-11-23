define([ '../require.config' ], function(r) {

    require([ 'jquery','format', 'utils', 'template', 'pagination', 'datetimepicker',
	    'navbar', 'amazeui' ], function($,fmt, utils, Temp, Page, datepicker) {

	function PageRender(options) {
	    this.data = null; // 数据
	    this.temp = null; // 模板
	    this.checkId = []; // 选中id
	    this.eventType = null; // 当前操作
	    this.options = options;
	    this.dataApi = {
		search : 'carOrder/find',// 查询接口
		del : 'carOrder/delete', // 删除接口
		updateStaus: 'carOrder/updateStatusByPk'	//修改状态
	    };

	    // 默认查询参数
	    this.searchOptions = {
		pageNum : 1,
		pageSize : 10
	    };

	    this.delModal = utils.delModal.init({ // / 删除提示框
		title : '温馨提示',
		content : '你确定要删除这条信息？'
	    });

	    // 按钮组合
	    this.elements = {
		search : $('[data-carOrder="query"]'),
		del : $('[data-carOrder="del"]')
	    };

	    var that = this;

	    // 第一次更新数据
	    this.upData(function(data) {
		that.data = data;

		// 创建模板
		that.temp = new Temp({
		    tempId : 'carOrder-list',
		    data : that.dataFormat(that.data.obj)
		});

		// 创建分页
		that.pagination = new Page({
		    pageTotal : that.data.total,
		    pageNo : that.searchOptions.pageNum,
		    events : function(id) {
			that.searchOptions.pageNum = id;

			that.upData(function(data) {
			    that.temp.data = that.dataFormat(data.obj);
			    that.temp.render();
			});
		    }
		});

		that.events();
	    });
	}

	PageRender.prototype = {

	    // 更新数据
	    upData : function(callback) {
		utils.ajax({
		    url : this.dataApi.search,
		    data : this.searchOptions,
		    success : function(data) {
			callback.call(this, data);
		    }
		});
	    },

	    // 查询数据
	    search : function() {
		var that = this, options = utils
			.getFormData($('[data-cy-validator]'));

		this.searchOptions = {
		    pageNum : 1,
		    pageSize : 10
		};

		$.extend(this.searchOptions, options);
		
		this.upData(function(data) {
		    that.data = data; // 第一次更新数据
		    that.temp.data = that.dataFormat(data.obj);
		    that.temp.render();
		});
	    },

	    // 删除数据
	    del : function() {
		var that = this;

		this.delModal.modal({
		    onConfirm : function() {
			utils.ajax({
			    url : that.dataApi.updateStaus,
			    data : {
				pk : that.checkId.join(','),
				status: '2'
			    },
			    success : function(data) {
				if (data.success) {
				    utils.alert({
					text : '删除成功',
					type : 'success'
				    });
				    that.refreshData();
				} else {
				    utils.alert({
					text : data.msg,
					type : 'danger'
				    });
				}
			    }
			});
		    }
		});
	    },


	    // 刷新列表数据
	    refreshData : function() {
		var that = this;

		this.searchOptions.pageNum = 1;

		this.upData(function(data) {
		    that.temp.data = that.dataFormat(data.obj);
		    that.temp.render();
		    that.pagination.options.pageTotal = data.total;
		    that.pagination.refreshData(1);
		});
	    },

	    // 选中
	    check : function() {
		var that = this;

		this.temp.content.on('click', '.cy-checkbox', function() {
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
	    dataFormat : function(data) {
		var that = this;
		var listData = null;

		if (!Array.isArray(data)) {
		    return false;
		}
		
		return data.map(function(item) {
		    return {
			pk : item.pk,
			edit : fmt.display(that.options.edit),
			no : item.orderNo,
			name : item.name,
			phone : item.phone,
			type : ['新车', '特价车','二手车'][item.type],
			carType : item.carType,
			color : ['红色','橙色','黄色','绿色','蓝色','青色','紫色','白色','黑色','银色'][item.color],
			img : item.img,
			money: item.money,
			operName : item.operName,
			payStatus: ['否','是'][item.payStatus],
			status: item.status,
			statusText: ['未确认', '已确认','已取消'][item.status],
			btnClass: ['', 'style="display:none;', 'style="display:none;'][item.status],
			btnText: ['确认', true,true][item.status],
			payTime : utils.dateFormat(item.payTime,
				'YYYY-MM-DD hh:mm')
		    };
		});
	    },

	    // 控制
	    events : function() {
		var that = this;
		this.check();

		// 事件组合
		this.elementEvents = {
		    search : this.search.bind(this),
		    del : this.del.bind(that)
		};

		[ 'search', 'del' ].map(function(i) {
		    that.elements[i].on('click', function() {
			this.eventType = null;

			if ((i == 'edit' && that.checkId.length != 1)
				|| (i == 'del' && that.checkId.length < 1)) {
			    utils.alert({
				text : '必须选择一条数据',
				type : 'warning'
			    });
			    return;
			}

			that.elementEvents[i]();
		    });
		});

		// 列表操作
		this.temp.content.on('click', '.editB', function() {
		    var _id = $(this).data('status');
			var _pk = $(this).data('id'),_status,_isAjax = false;
			switch(_id) {
				case 0: (function() { // 确认
					_status = '1';
					_isAjax = true;
				})();
				break;
//				case 1: (function() { // 完成
//					_status = '2';
//					_isAjax = true;
//				})();
//				break;
			}
			if(_isAjax){
				utils.ajax({
					url: that.dataApi.updateStaus,
					data: {
						pk: _pk,
						status: _status
					},
					success: function(data) {
						if(data.success){
							utils.alert({
								text : '操作成功',
								type : 'success'
							});
							that.refreshData();
						}else{
							utils.alert({
								text:data.msg,
								type:'danger'
							});
						}
					}
				});
			}
		   
		});

	    }

	};

	// 初始化
	(function initialization() {

	    var url = "sysMenu/findMenuFunc";
            var data = {
                menuPk: "ShopCarOrder"
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
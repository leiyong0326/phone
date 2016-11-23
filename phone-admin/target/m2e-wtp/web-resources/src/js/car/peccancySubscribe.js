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
		search : 'peccancySubscribe/find',// 查询接口
		del : 'peccancySubscribe/delete' // 删除接口
	    };

	    // 初始化查询日期
	    utils.datepick('.startTime', '.endTime');

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
		search : $('[data-subscribe="query"]'),
		del : $('[data-subscribe="del"]')
	    };

	    var that = this;
	    
	    // 第一次更新数据
	    this.upData(function(data) {
		that.data = data;

		// 创建模板
		that.temp = new Temp({
		    tempId : 'subscribe-list',
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
		console.log(this.searchOptions);
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
			    url : that.dataApi.del,
			    data : {
				pk : that.checkId.join(',')
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
			no : item.no,
			name : item.name,
			phone : item.phone,
			typeName : item.typeName,
			money : item.money,
			status: ['未支付','已支付'][item.status],
			cardNo: item.cardNo,
			frameNo: item.frameNo,
			engineNo: item.engineNo,
			createTime : utils.dateFormat(item.createTime,
				'YYYY-MM-DD'),
			startTime : utils.dateFormat(item.startTime,
				'YYYY-MM-DD'),
			endTime : utils.dateFormat(item.endTime,
				'YYYY-MM-DD')
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
	    }

	};

	// 初始化
	(function initialization() {
	    var url = "sysMenu/findMenuFunc";
            var data = {
                menuPk: "MtPeccancySubscribe"
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
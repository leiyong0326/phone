define([ '../require.config' ], function(r) {

    require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker',
	    'format', 'navbar', 'amazeui' ], function($, utils, Temp, Page,
	    datepicker, format) {

	function PageRender(options) {
	    this.data = null; // 数据
	    this.temp = null; // 模板
	    this.addModal = $('[data-user="add-modal"]'); // 新增编辑的窗口
	    this.checkId = []; // 选中id
	    this.eventType = null; // 当前操作
	    this.dataApi = {
		search : 'usrUserCar/find', // 查询接口
		add : 'usrUserCar/save', // 新增接口
		searchOne : 'usrUserCar/get', // 查询某条数据
		del : 'usrUserCar/delete' // 删除接口
	    };

	    this.initSearchOption();

	    // 新增列表参数
	    this.addOptions = {
		pk : '',
		type : '',
		cardNo : '',
		frameNo : '',
		engineNo : '',
		buyTime : '',
		lastMileage : '',
		jiangeMileage : '',
		maintainTime : '',
		userPk : '',
		userName : '',
		userPhone : '',
	    };

	    this.delModal = utils.delModal.init({ // / 删除提示框
		title : '温馨提示',
		content : '你确定要删除这条信息？'
	    });

	    // 按钮组合
	    this.elements = {
		search : $('[data-usrCar="query"]'),
	    };

	    // 新增/编辑弹出框模板
	    this.modalTemp = new Temp({
		tempId : 'add-content',
		data : this.addOptions,
		events : function() {
		    this.content.find('select').selected();
		}
	    });

	    var that = this;

	    // 第一次更新数据
	    this.upData(function(data) {
		that.data = data;
		// 创建模板
		that.temp = new Temp({
		    tempId : 'usrCar-list',
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
	    initSearchOption : function() {
		// 默认查询参数
		this.searchOptions = {
		    pageNum : 1,
		    pageSize : 10
		};
	    },
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
		var that = this;
		that.initSearchOption();
		$.extend(this.searchOptions, {
		    userName : $('[name="userName"]').val(),
		    userPhone : $('[name="userPhone"]').val(),
		    type : $('[name="type"]').val(),
		    carNo : $('[name="carNo"]').val(),
		});
		this.upData(function(data) {
		    that.data = data; // 第一次更新数据
		    that.temp.data = that.dataFormat(data.obj);
		    that.temp.render();
		});
	    },

	    // 刷新弹框
	    refreshModal : function() {
		this.modalTemp.data = this.addOptions;
		this.modalTemp.render();
		this.modalTemp.content.find('select').selected();

		this.addModal.modal('open');
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
		var listData = null;

		if (!Array.isArray(data)) {
		    return false;
		}

		return data.map(function(item) {
		    return {
			pk : item.pk,
			name : item.name,
			phone : item.phone,
			carNo : item.carNo,
			type : item.type,
			frameNo : item.frameNo,
			engineNo : item.engineNo,
			buyTime : format.date(item.buyTime, 'YYYY-MM-DD'),
			lastMileage : item.lastMileage,
			jiangeMileage : item.jiangeMileage,
			maintainTime : format.date(item.maintainTime,
				'YYYY-MM-DD'),
			userPk : item.userPk,
			userName : item.userName,
			userPhone : item.userPhone,
			insuranceDays : item.insuranceDays,
			mainTainDays : item.mainTainDays
		    };
		});
	    },

	    // 控制
	    events : function() {
		var that = this;
		this.check();

		// 事件组合
		this.elementEvents = {
		    search : this.search.bind(that),
		};
		that.elements.search.on('click', function() {
		    that.elementEvents.search();
		})
	    }

	};

	// 初始化
	(function initialization() {
	    var url = "sysMenu/findMenuFunc";
	    var data = {
		menuPk : "UsrUserCar"
	    };
	    utils.ajax({
		url : url,
		data : data,
		success : function(data) {
		    if (data.success) {
			var power = { // 用户权限
			    add : data.obj.indexOf("edit") >= 0,
			    edit : data.obj.indexOf("edit") >= 0,
			    del : data.obj.indexOf("del") >= 0,
			    show : data.obj.indexOf("show") >= 0
			};
			// 根据权限判断是否显示
			for (attr in power) {
			    if (power.hasOwnProperty(attr) && power[attr]) {
				$('[data-rel="' + attr + '"]').css({
				    display : 'inline-block'
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
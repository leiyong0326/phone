define([ '../require.config' ], function(r) {

    require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker',
	    'format', 'navbar', 'amazeui' ], function($, utils, Temp, Page,
	    datepicker, format) {

	function PageRender(options) {
	    this.data = null; // 数据
	    this.temp = null; // 模板
	    this.dataApi = {
		search : 'usrRecharge/find', // 查询接口
		searchOne : 'usrRecharge/get' // 查询某条数据
	    };

	    // 日期插件
	    utils.datepick('.date-start', '.date-end', {
		startView : 3,
		minView : 2,
		maxView : 4
	    });

	    var that = this;

	    that.initSearchOption();
	    // 按钮组合
	    this.elements = {
		search : $('[data-recharge="query"]')
	    };

	    // 第一次更新数据
	    this.upData(function(data) {
		that.data = data;
		// 创建模板
		that.temp = new Temp({
		    tempId : 'recharge-list',
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
		$.extend(this.searchOptions, utils.getFormData($('#queryForm')));
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

	    // 格式化数据
	    dataFormat : function(data) {
		var listData = null;

		if (!Array.isArray(data)) {
		    return false;
		}

		return data.map(function(item) {
		    return {
			pk : item.pk,
			face : item.face,
			nickName : item.nickName,
			userName : item.userName,
			userPhone : item.userPhone,
			money : item.money,
			rechargeTime : format.date(item.rechargeTime,
				'YYYY-MM-DD'),
			dsc : item.dsc,
			way : format.payWay(item.way),
			operName : item.operName,
			remark : item.remark
		    };
		});
	    },

	    // 控制
	    events : function() {
		var that = this;
		// 事件组合
		this.elementEvents = {
		    search : this.search.bind(that)

		};

		that.elements.search.on('click', function() {
		    that.search();
		});

	    }

	};

	// 初始化
	(function initialization() {
	    var url = "sysMenu/findMenuFunc";
	    var data = {
		menuPk : "UsrRecharge"
	    };
	    utils.ajax({
		url : url,
		data : data,
		success : function(data) {
		    if (data.success) {
			var power = { // 用户权限
			    show : data.obj.indexOf("show") >= 0
			};
			var pageRender = new PageRender(power);
		    }
		}
	    });
	})();
    });

});
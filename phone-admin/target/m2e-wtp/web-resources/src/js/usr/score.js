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
		search : 'usrScoreDetail/find', // 查询接口
		add : 'usrScoreDetail/save', // 新增接口
		searchOne : 'usrScoreDetail/get', // 查询某条数据
		del : 'usrScoreDetail/delete' // 删除接口
	    };

	    // 新增列表参数
	    this.addOptions = {
		pk : '',
		userPk : '',
		score : '',
		type : '',
		business : '',
		dsc : ''
	    };
	    
	    this.initSearchOption();

	    this.delModal = utils.delModal.init({ // / 删除提示框
		title : '温馨提示',
		content : '你确定要删除这条信息？'
	    });

	    // 按钮组合
	    this.elements = {
		search : $('[data-usrScore="query"]'),
		add : $('[data-usrScore="add"]'),
		del : $('[data-usrScore="del"]')
	    };

	    // 新增/编辑弹出框模板
	    this.modalTemp = new Temp({
		tempId : 'add-content',
		data : this.addOptions,
		componentDidMount : function() {
		    this.content.find('select').selected();

		    // 日期插件
		    utils.datepick('.start-date', '.end-date', {
			startView : 3,
			minView : 2,
			maxView : 4
		    });
		}
	    });

	    var that = this;

	    // 创建新增/编辑弹窗
	    this.addModal.modal({
		closeOnConfirm : false,
		onConfirm : function() {

		    if (that.eventType == 'add') { // 新增
			that.addData();
		    } else if (that.eventType == 'edit') { // 编辑
			that.editData();
		    }

		}
	    }).modal('close');

	    // 第一次更新数据
	    this.upData(function(data) {
		that.data = data;

		// 创建模板
		that.temp = new Temp({
		    tempId : 'usrScore-list',
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

	    // 删除数据
	    del : function() {
		var that = this;

		this.delModal.modal({
		    onConfirm : function() {
			console.log(that.checkId);
			utils.ajax({
			    url : that.dataApi.del,
			    data : {
				pk : that.checkId.join(',')
			    },
			    success : function(data) {

				utils.alert({
				    text : '删除成功',
				    type : 'success'
				});

				that.refreshData();
			    }
			});
		    }
		});
	    },

	    // 初始化添加数据
	    add : function() {

		this.eventType = 'add';
		this.refreshModal();
	    },

	    // 添加数据
	    addData : function() {
		var that = this;

		utils.ajax({
		    url : this.dataApi.add,
		    data : this.addOptions,
		    success : function(data) {
			utils.alert({
			    text : '新增成功',
			    type : 'success'
			});

			that.refreshData();
			that.addModal.modal('close');
		    }
		});
	    },

	    // 初始化编辑数据
	    edit : function() {
		var that = this;

		this.eventType = 'edit';

		utils.ajax({
		    url : this.dataApi.searchOne,
		    data : {
			pk : this.checkId[0]
		    },
		    success : function(data) {
			for ( var attr in that.addOptions) {
			    if (that.addOptions.hasOwnProperty(attr)) {
				that.addOptions[attr] = data.obj[attr];
			    }
			}
			that.addOptions.pk = data.obj.pk;
			that.refreshModal();
		    }
		});
	    },

	    // 编辑数据
	    editData : function() {
		var that = this;

		utils.ajax({
		    url : this.dataApi.add,
		    data : this.addOptions,
		    success : function(data) {

			utils.alert({
			    text : '编辑成功',
			    type : 'success'
			});

			that.refreshData();
			that.addModal.modal('close');
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
		    console.log(data);
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
			userPk : item.userPk,
			nickName : item.nickName,
			face : item.face,
			userPhone : item.userPhone,
			userName : item.userName,
			score : item.score,
			type : [ '增加', '消耗' ][item.type],
			business : item.business,
			createTime : format.date(item.createTime),
			dsc : item.dsc,
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
		    add : this.add.bind(that),
		    del : this.del.bind(that)
		};

		[ 'search', 'add', 'del' ].map(function(i) {
		    that.elements[i].on('click', function() {
			this.eventType = null;

			if (i == 'del' && that.checkId.length < 1) {
			    utils.alert({
				text : '必须选择一条数据',
				type : 'warning'
			    });
			    return;
			}

			that.elementEvents[i]();
		    });
		});

		// 列表删除
		this.temp.content.on('click', '.editB', function() {
		    that.checkId[0] = $(this).data('id');
		    that.elementEvents['edit']();
		});

	    }

	};

	// 初始化
	(function initialization() {

	    var url = "sysMenu/findMenuFunc";
	    var data = {
		menuPk : "UsrScoreDetail"
	    };
	    utils.ajax({
		url : url,
		data : data,
		success : function(data) {
		    if (data.success) {
			var power = { // 用户权限
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
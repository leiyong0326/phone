define([ '../require.config' ], function(r) {

    require([ 'jquery', 'utils', 'template', 'pagination', 'format', 'navbar', 'amazeui', 'updataFile' ], 
	    function($, utils, Temp, Page,format) {

	function PageRender(options) {
	    this.data = null; // 数据
	    this.temp = null; // 模板
	    this.addModal = $('[data-partsImg="add-modal"]'); // 新增编辑的窗口
	    this.checkId = []; // 选中id
	    this.eventType = null; // 当前操作
	    this.partsPk=utils.parseUrl().partsPk;
	    this.dataApi = {
		search : 'shopCarPartsImg/find', // 查询接口
		add : 'shopCarPartsImg/add', // 新增接口
		update : 'shopCarPartsImg/update', // 新增接口
		searchOne : 'shopCarPartsImg/get', // 查询某条数据
		del : 'shopCarPartsImg/delete' // 删除接口
	    };

	    // 默认查询参数
	    this.searchOptions = {
		partsPk:this.partsPk,
		pageNum : 1,
		pageSize : 10
	    };


	    // 新增列表参数
	    this.addOptions = {
		pk : '',
		partsPk : '',
		seq : '',
		url : ''
	    };

	    this.delModal = utils.delModal.init({ // / 删除提示框
		title : '温馨提示',
		content : '你确定要删除这条信息？'
	    });

	    // 按钮组合
	    this.elements = {
		search : $('[data-partsImg="query"]'),
		add : $('[data-partsImg="add"]')
	    };
	    
	    // 新增/编辑弹出框模板
	    this.modalTemp = new Temp({
		tempId : 'add-content',
		data : this.addOptions,
		componentDidMount: function() {
		    //this.content.find('select').selected();
		    //颜色
		    // 图片上传组件.
		    this.content.find('.cy-input-file').upDataFile({
			imageUrl : 'partsImg',
			imgs : this.data.url
		    });
		}
	    
	    });

	    var that = this;

	    // 创建新增/编辑弹窗
	    this.addModal.modal(
		    {
			closeOnConfirm : false,
			onConfirm : function() {
			    that.addOptions = utils .getFormData($('[data-content="add-content"]'));

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
		    tempId : 'partsImg-list',
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

	    // 删除数据
	    del : function(pk) {
		var that = this;

		this.delModal.modal({
		    onConfirm : function() {
			utils.ajax({
			    url : that.dataApi.del,
			    data : {
				partsPk : that.partsPk,
				pk : pk
			    },
			    success : function(data) {
				if(data.success){
				    utils.alert({
					text : '删除成功',
					type : 'success'
				    });
				    that.refreshData();
				}else{
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

	    // 初始化添加数据
	    add : function() {
		this.addOptions = {
		 pk : '',
		 partsPk : '',
		 url : ''
		};
		this.eventType = 'add';
		this.refreshModal();
	    },

	    // 添加数据
	    addData : function() {
		var that = this;
		this.addOptions.partsPk=this.partsPk;
		utils.ajax({
		    url : this.dataApi.add,
		    data : this.addOptions,
		    success : function(data) {
			if(data.success){
			    utils.alert({
				text : '新增成功',
				type : 'success'
			    });
			    that.refreshData();
			    that.addModal.modal('close');
			}else{
			    utils.alert({
				text : data.msg,
				type : 'danger'
			    });
			}
		    }
		});
	    },

	    // 初始化编辑数据
	    edit : function(pk) {
		var that = this;
		this.eventType = 'edit';
		utils.ajax({
		    url : this.dataApi.searchOne,
		    data : {
			pk : pk,
			partsPk : that.partsPk
		    },
		    success : function(data) {
			for ( var attr in that.addOptions) {
			    if (that.addOptions.hasOwnProperty(attr)) {
				that.addOptions[attr] = data.obj[attr];
			    }
			}
			that.addOptions.pk = data.obj.pk;
			that.addOptions.partsPk = data.obj.partsPk;
			that.refreshModal();
		    }
		});
	    },

	    // 编辑数据
	    editData : function() {
		var that = this;

		utils.ajax({
		    url : this.dataApi.update,
		    data : this.addOptions,
		    success : function(data) {
			if(data.success){
			    utils.alert({
				text : '编辑成功',
				type : 'success'
			    });
			    that.refreshData();
			    that.addModal.modal('close');
			}else{
			    utils.alert({
				text : data.msg,
				type : 'danger'
			    });
			}
			
		    }
		});
	    },

	    // 查询数据
	    search : function() {
		var that = this;

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
		this.modalTemp.content.find('[name="type"] option[value="' + this.addOptions.type + '"]').attr('selected', true);
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
			partsPk : item.partsPk,
			seq : item.seq,
			url :item.url,
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
		    add : this.add.bind(that)
		};

		[ 'search', 'add'].map(function(i) {
		    that.elements[i].on('click', function() {
			this.eventType = null;
			that.elementEvents[i]();
		    });
		});

		// 列表编辑
		this.temp.content.on('click', '.editB', function() {
		    that.edit($(this).data('id'));
		});
		
		// 列表删除
		this.temp.content.on('click', '.delB', function() {
		    that.del($(this).data('id'));
		});

	    }

	};

	// 初始化
	(function initialization() {

	    var url = "sysMenu/findMenuFunc";
            var data = {
        	    menuPk: "ShopCarParts"
            };
            utils.ajax({
                url: url,
                data: data,
                success: function(data) {
                    if (data.success) {
                        var power = { // 用户权限
                            add: data.obj.indexOf("edit") >= 0,
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
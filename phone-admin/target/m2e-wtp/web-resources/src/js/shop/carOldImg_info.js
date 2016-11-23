define([ '../require.config' ], function(r) {

    require([ 'jquery', 'utils', 'template', 'pagination', 'format', 'navbar', 'amazeui', 'updataFile' ], 
	    function($, utils, Temp, Page,format) {

	function PageRender(options) {
	    this.data = null; // 数据
	    this.temp = null; // 模板
	    this.addModal = $('[data-shopCarOldImg="add-modal"]'); // 新增编辑的窗口
	    this.checkId = []; // 选中id
	    this.eventType = null; // 当前操作
	    this.carPk=utils.parseUrl().carPk;
	    this.dataApi = {
		search : 'shopCarOldImg/find', // 查询接口
		add : 'shopCarOldImg/save', // 新增接口
		update : 'shopCarOldImg/update', // 新增接口
		searchOne : 'shopCarOldImg/get', // 查询某条数据
		del : 'shopCarOldImg/delete' // 删除接口
	    };

	    // 默认查询参数
	    this.searchOptions = {
		carPk:this.carPk,
		pageNum : 1,
		pageSize : 10
	    };


	    // 新增列表参数
	    this.addOptions = {
		pk : '',
		type : '',
		color : '',
		carPk : '',
		url : ''
	    };

	    this.delModal = utils.delModal.init({ // / 删除提示框
		title : '温馨提示',
		content : '你确定要删除这条信息？'
	    });

	    // 按钮组合
	    this.elements = {
		search : $('[data-shopCarOldImg="query"]'),
		add : $('[data-shopCarOldImg="add"]'),
		del : $('[data-shopCarOldImg="del"]'),
		edit : $('[data-shopCarOldImg="edit"]')
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
			imageUrl : 'carOldImg',
			size : [750,500],
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
		    tempId : 'shopCarOldImg-list',
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
	    del : function() {
		var that = this;

		this.delModal.modal({
		    onConfirm : function() {
			utils.ajax({
			    url : that.dataApi.del,
			    data : {
				carPk : that.carPk,
				pk : that.checkId.join(',')
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
		 type : '',
		 color : '',
		 carPk : '',
		 url : ''
		};
		this.eventType = 'add';
		this.refreshModal();
	    },

	    // 添加数据
	    addData : function() {
		var that = this;
		this.addOptions.carPk=this.carPk;
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
				text:data.msg,
				type:'danger'
			    });
			}
			
		    }
		});
	    },

	    // 查询数据
	    search : function() {
		var that = this;

		$.extend(this.searchOptions, {
		    type : $('[name="type"]').val(),
		});

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
			type : format.carImgType(item.type),
			carPk : item.carPk,
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
		    add : this.add.bind(that),
		    edit : this.edit.bind(that),
		    del : this.del.bind(that)
		};

		[ 'search', 'add', 'edit', 'del' ].map(function(i) {
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

		// 列表编辑
		this.temp.content.on('click', '.editB', function() {
		    that.checkId[0] = $(this).data('id');
		    that.elementEvents['edit']();
		});
		
		// 列表删除
		this.temp.content.on('click', '.delB', function() {
		    that.checkId[0] = $(this).data('id');
		    that.elementEvents['del']();
		});

	    }

	};

	// 初始化
	(function initialization() {

	    var url = "sysMenu/findMenuFunc";
            var data = {
        	    menuPk: "ShopCarOld"
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
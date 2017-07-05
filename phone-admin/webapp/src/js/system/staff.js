define([ '../require.config' ], function(r) {
    require([ 'jquery', 'utils', 'template', 'pagination','format', 'warn','datetimepicker', 'navbar', 'amazeui' ], 
	    function($, utils, Temp, Page,format, w,datepicker) {
	function PageRender(options) {
	    this.data = null; // 数据
	    this.temp = null; // 模板
	    this.options = options;
	    this.addModal = $('[data-modal="add-modal"]'); // 新增编辑的窗口
	    this.checkId = []; // 选中id
	    this.eventType = null; // 当前操作
	    this.dataApi = {
		search : 'sysUser/findByPage', // 查询接口
		export : 'sysUser/export', // 导出接口
		update : 'sysUser/insert', // 新增接口
		searchOne : 'sysUser/get', // 查询某条数据
		del : 'sysUser/deleteByBatch', // 删除接口
		enables : 'sysUser/enable', // 启用/禁用接口
		disables : 'sysUser/disable', // 启用/禁用接口
		resetPwd : 'sysUser/resetPwd'
	    };
	    this.initSearchOption();
	    // 新增列表参数
	    this.initEditOptions = function(){
		return {
			loginName:'',
			phone:'',
			upPk:'',
			password:'',
			name:'',
			cardId:'',
			cardAddress:'',
			address:'',
			ratio:'0',
			birthday:'',
			email:'',
			qq:'',
			rolePk:'',
			accountPk:'',
			createTime:'',
			updateTime:'',
			updatePassword:'',
			loginTime:'',
			enable:'1',
			face:'',
			sex:'1',
			referrer:'',
			alipay:'',
			wechat:''
		};
	    };
	    this.editOptions = this.initEditOptions();
	    this.delModal = utils.delModal.init({ // / 删除提示框
		title : '温馨提示',
		content : '你确定要删除这条信息？'
	    });
	    
	    this.resetPwdModal = utils.delModal.init({
		title : '温馨提示',
		content : '确定要重置该员工账号登录密码为123456?'
	    });
	    
	    this.disableModal = utils.delModal.init({
		title : '温馨提示',
		content : '确定要禁用该员工账号?'
	    });
	    
	    this.enableModal = utils.delModal.init({
		title : '温馨提示',
		content : '确定要启用该员工账号?'
	    });

	    // 按钮组合
	    this.elements = {
		search : $('[data-sysUser="query"]'),
		export : $('[data-sysUser="export"]'),
		add : $('[data-sysUser="add"]')
	    };
	    var that = this;
	    
	    // 新增/编辑弹出框模板
	    this.modalTemp = new Temp({
		tempId : 'add-content',
		data : that.editOptions,
		componentDidMount: function() {
		    this.content.find('select').selected();
		    that.selects = {
			 upPk : $('[name="upPk"]'),
			 referrer : $('[name="referrer"]'),
			 role : $('[name="rolePk"]'),
			 account : $('[name="accountPk"]')
		    };
		    // 日期插件
	            utils.datepick('.birthday',null,{
	        	startView : 3,
		        minView : 2,
		        maxView : 4,
	        	endDate :format.date(new Date(),'YYYY-MM-DD'),
	        	startDate : '1936-12-31'
	            });
	            if(that.editOptions.sex){
	        	$('[name="sex"]').find('option[value="' + that.editOptions.sex + '"]').attr('selected', true);
	            }
	            if(that.editOptions.birthday){
	        	$('[name="birthday"]').val(format.date(that.editOptions.birthday,'YYYY-MM-DD'));
	            }
	            if(that.editOptions.createTime){
	        	$('[name="createTime"]').val(format.date(that.editOptions.createTime));
	            }
	            if(that.editOptions.updateTime){
	        	$('[name="updateTime"]').val(format.date(that.editOptions.updateTime));
	            }
	            if(that.editOptions.loginTime){
	        	$('[name="loginTime"]').val(format.date(that.editOptions.loginTime));
	            }
	            if(that.editOptions.updatePassword){
	        	$('[name="updatePassword"]').val(format.date(that.editOptions.updatePassword));
	            }
	             
		}
	    
	    });
	    
	    // 编辑弹窗
	    this.addModal.modal({
		closeOnConfirm : false,
		onConfirm : function() {
		    that.editOptions = $.extend(that.editOptions,utils.getFormData($('[data-content="add-content"]')));
		    that.editOptions['createTime'] = undefined;
		    that.editOptions['updateTime'] = undefined;
		    that.editOptions['loginTime'] = undefined;
		    that.editOptions['updatePassword'] = undefined;
		    that.editOptions['face'] = undefined;
		    if(!that.editOptions['birthday']){
			that.editOptions['birthday'] = undefined;
		    }
		    that.editData();
		}
	    }).modal('close');

	    // 第一次更新数据
	    this.upData(function(data) {
		that.data = data;
		// 创建模板
		that.temp = new Temp({
		    tempId : 'sysUser-list',
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
	    
	 // 初始化添加数据
            add: function () {
        	this.editOptions = this.initEditOptions();
                this.eventType = 'add';
                this.refreshModal();
            },

	    // 删除数据
	    del : function(pk) {
		var that = this;
		if(pk == undefined || pk.length < 1){
		    utils.alert({
			text : '请选择数据',
			type : 'danger'
		    });
		}
		this.delModal.modal({
		    onConfirm : function() {
			utils.ajax({
			    url : that.dataApi.del,
			    data : {
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

	    // 初始化编辑数据
	    edit : function(pk) {
		var that = this;
        	this.editOptions = this.initEditOptions();
		utils.ajax({
		    url : this.dataApi.searchOne,
		    data : {
                        pk: pk
		    },
		    success : function(data) {
			for ( var attr in data.obj) {
			    if (that.editOptions.hasOwnProperty(attr)) {
				that.editOptions[attr] = data.obj[attr];
			    }
			}
			that.editOptions.pk = data.obj.pk;
			that.refreshModal();
		    }
		});
	    },

	    // 编辑数据
	    editData : function() {
		var that = this;
		utils.ajax({
		    url : this.dataApi.update,
		    data : that.editOptions,
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
		this.initSearchOption();
		$.extend(this.searchOptions,utils.getFormData($('#queryFrom')));

		this.upData(function(data) {
		    that.data = data; // 第一次更新数据
		    that.temp.data = that.dataFormat(data.obj);
		    that.temp.render();
		});
	    },
	    export:function(){
		var that = this;
		this.initSearchOption();
		$.extend(this.searchOptions,utils.getFormData($('#queryFrom')));
		var url = window.location.origin +"/"+ this.dataApi.export;
		for(var p in this.searchOptions){
		    var value = this.searchOptions[p];
		    if(value!=undefined&&value!==""){
			if(url.indexOf("?")==-1){
			    url += "?";
			}else{
			    url += "&";
			}
			url += p+"="+value;
		    }
		}
		window.location.href = url;
	    },
	    initSearchOption : function(){
		this.searchOptions = {
			pageNum : 1,
			pageSize : 10,
			orderBy:'RATIO_TOTAL DESC,SALE_TOTAL DESC,CREATE_TIME'
		}
	    },
	    
	    getUser : function(){
		var that=this;
		utils.ajax({
		    url : 'sysUser/findAll',
		    data:{
		    },
		    success : function(data){
			 var obj = data.obj;
			 if(obj instanceof Array){
			     for(var i =0;i<obj.length;i++){
				 var o = obj[i];
				 o.name = o.name+'-'+o.phone;
			     }
			 }
                         that.selects.upPk.createSelect({
                             data: obj,
                             attr: ['pk', 'name'],
                             removed:that.editOptions.pk,
                             selected: that.editOptions.upPk
                         });
                         that.selects.referrer.createSelect({
                             data: obj,
                             attr: ['pk', 'name'],
                             removed:that.editOptions.pk,
                             selected: that.editOptions.referrer
                         });
		    }
		});
	    },
	    
	    getRole : function(){
		var that=this;
		utils.ajax({
		    url : 'sysRole/findAll',
		    data:{
		    },
		    success : function(data){
			 var obj = data.obj;
                         that.selects.role.createSelect({
                             data: obj,
                             attr: ['pk', 'name'],
                             selected: that.editOptions.rolePk
                         });
		    }
		});
	    },
	    getAccount : function(){
		var that=this;
		utils.ajax({
		    url : 'sysAccount/findAll',
		    data:{
		    },
		    success : function(data){
			 var obj = data.obj;
                         that.selects.account.createSelect({
                             data: obj,
                             attr: ['pk', 'name'],
                             selected: that.editOptions.accountPk
                         });
		    }
		});
	    },
	    resetPwd : function(pk){
		var that = this;
		this.resetPwdModal.modal({
		    onConfirm : function() {
			utils.ajax({
			    url : that.dataApi.resetPwd,
			    data : {
				pk : pk
			    },
			    success : function(data){
				if(data.success){
				    utils.alert({
					text : '重置密码成功',
					type : 'success'
				    });
				}else{
				    utils.alert({
					text : data.msg,
					type : 'danger'
				    });
				}
			    }
			});
		    
		    }
		})
	    },
	    
	    changeState: function(btn) {
                var pk = btn.data('id');
                var text = btn.html();
                var prompt = undefined;
                var removeClass, addClass;
                var enable = '0';
                var url = null;
                if (text == '禁用') {
                    text = '启用';
                    removeClass = 'cy-btn-orange';
                    addClass = 'cy-btn-green';
                    prompt = w.dsbs;
                    url = this.dataApi.disables;
                } else {
                    text = '禁用';
                    enable = '1';
                    addClass = 'cy-btn-orange';
                    removeClass = 'cy-btn-green';
                    prompt = w.enbs;
                    url = this.dataApi.enables;
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
                            utils.alert({
                                text: prompt,
                                type: 'danger'
                            });
                        }
                    }
                });
            },
	  
	    // 刷新弹框
	    refreshModal : function() {
		this.modalTemp.data = this.editOptions;
		this.modalTemp.render();
		this.modalTemp.content.find('select').selected();
		this.getUser();
		this.getRole();
		this.getAccount();
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
		var that = this;
		var listData = null;

		if (!Array.isArray(data)) {
		    return false;
		}

		return data.map(function(item) {
		    return {
			pk : [ item.pk ],
			name : [ item.name ],
			loginName : [ item.loginName ],
			phone : [ item.phone ],
			face : [ item.face],
			sex : format.sex([ item.sex ]),
			upName : [item.upName],
			roleName : [item.roleName],
			ratio : [ item.ratio],
			ratioTotal : [ item.ratioTotal],
			saleTotal : [ item.saleTotal],
		    	enable : [item.enable],
		    	enableText : format.status(item.enable),
	                enableStyle : format.statusStyle(item.enable),
	                enableShow : format.display(that.options.edit),
	                enableBtnText : format.statusText(item.enable),
	                editShow : format.display(that.options.edit),
	                createTime : format.date(item.createTime),
	                loginTime : format.date(item.loginTime)
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
		    export : this.export.bind(that),
		    add : this.add.bind(that)
		};

		[ 'search','export','add' ].map(function(i) {
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
		
		// 列表重置密码
		this.temp.content.on('click', '.enableBtn', function() {
		    that.changeState($(this));
		});
		
	    }
	};

	// 初始化
	(function initialization() {
	    var url = "sysMenu/findMenuFunc";
            var data = {
        	    menuPk: "SysUser"
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
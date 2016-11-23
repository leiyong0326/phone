define([ '../require.config' ], function(r) {

    require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker','format', 'navbar', 'amazeui' ], 
	function($, utils, Temp, Page, datepicker,format) {

	function PageRender(options) {
	    this.data=null,
	    this.userOptions = {
		pk : '',
		face : '',
		nickName : '',
		name : '',
		phone : '',
		sex : '',
		email : '',
		cardId:'',
		birthday : '',
		openid:''
	    }; // 数据
	    
	    this.initEmpOptions();
	    
	    
	    this.temp = null; // 模板
	    this.addModal = $('[data-emp="add-modal"]'); // 新增编辑的窗口
	    this.eventType = null; // 当前操作
	    this.dataApi = {
		search : 'usrUserSlave/find', // 查询接口
		save:'sysUser/save'//添加员工接口
	    };
	 
	    this.initSearchOptions();
	    this.delModal = utils.delModal.init({ // / 删除提示框
		title : '温馨提示',
		content : '你确定要删除这条信息？'
	    });

	    var that =  this;
	    // 按钮组合
	    this.elements = {
		search : $('[data-user="query"]')
	    };
	    
	    this.modalTemp=new Temp({
		tempId : 'add-content',
		data : that.empOptions,
		componentDidMount: function() {
		    this.content.find('select').selected();
		    that.selects = {
			department : $('[name="deptPk"]'),
			role : $('[name="rolePk"]')
		    };
			    // 日期插件
		    utils.datepick('.birthday',null,{
		        startView : 3,
			minView : 2,
			maxView : 4,
		        endDate :'2015-12-31',
		        startDate : '1936-12-31'
		     });
		     if(that.empOptions.sex){
		        $('[name="sex"]').find('option[value="' + that.empOptions.sex + '"]').attr('selected', true);
		     }
		}
	    });
	    
	    // 创建新增/编辑弹窗
	    this.addModal.modal({
		closeOnConfirm : false,
		onConfirm : function() {
		    that.empOptions = utils .getFormData($('[data-content="add-content"]'));
		    that.saveData();
		}
	    }).modal('close');


	    var that = this;

	    // 第一次更新数据
	    this.upData(function(data) {
		that.data = data;
		// 创建模板
		that.temp = new Temp({
		    tempId : 'user-list',
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
		
		
	    setEmp : function(){
		this.refreshModal();
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
	    
	    initSearchOptions : function(){
		 // 默认查询参数
		this.searchOptions = {
		    pageNum : 1,
		    pageSize : 10
		};
	    },
	    
	    initEmpOptions : function(){
		this.empOptions = {
			 pk : '',
			 face : '',
			 name : '',
			 phone : '',
			 sex : '',
			 birthday : '',
			 openid:'',
			 deptPk:'',
			 rolePk:'',
			 email : '',
			 cardId:'',
			 loginName:'',
			 password:'',
			 scdCount:''
		    }; // 数据
	    },

	    // 查询数据
	    search : function() {
		this.initSearchOptions();
		var that = this;
		$.extend(this.searchOptions,utils.getFormData($('[data-modal="search"]')));
		this.upData(function(data) {
		    that.data = data; // 第一次更新数据
		    that.temp.data = that.dataFormat(data.obj);
		    that.temp.render();
		});
	    },
	    
	    getDept : function(){
		var that=this;
		utils.ajax({
		    url : 'sysDepartment/find',
		    data:{
			pageNum:1,
			pageSize:100
		    },
		    success : function(data){
			 var obj = data.obj;
                         that.selects.department.createSelect({
                             data: obj,
                             attr: ['pk', 'name'],
                         });
		    }
		});
	    },
	    
	    getRole : function(){
		var that=this;
		utils.ajax({
		    url : 'sysRole/find',
		    data:{
			pageNum:1,
			pageSize:100
		    },
		    success : function(data){
			 var obj = data.obj;
                         that.selects.role.createSelect({
                             data: obj,
                             attr: ['pk', 'name'],
                         });
		    }
		});
	    },
	    
	    saveData : function(){
		var that = this;
		utils.ajax({
		    url : that.dataApi.save,
		    data: that.empOptions,
		    success : function(data){
			if(data.success){
			    history.back();
			    utils.alert({
				text : '新增成功',
				type : 'success'
			    })
			}else{
			    utils.alert({
				text : data.msg,
				type : 'danger'
			    })
			}
			
		    }
		});
		
	    },
	    
	    

	    // 刷新弹框
	    refreshModal : function() {
		this.modalTemp.data = this.empOptions;
		this.modalTemp.render();
		this.modalTemp.content.find('select').selected();
		this.getDept();
		this.getRole();
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
			name : item.name,
			phone : item.phone,
			birthday : item.birthday==undefined?'':format.date(item.birthday,'YYYY-MM-DD'),
			sex : format.sex(item.sex),
			openid : item.opendid,
			email : item.email,
			cardId : item.cardId 
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
		
		that.elements.search.on('click',function(){
		    that.search();
		});
		
		$('body').on('click', '.setEmp', function(){
		    that.initEmpOptions();
		    $.extend(that.empOptions,utils.getFormData($(this).next()));
		    that.setEmp();
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
                            save: data.obj.indexOf("edit") >= 0,
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
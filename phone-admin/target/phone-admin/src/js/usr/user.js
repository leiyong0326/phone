define([ '../require.config' ], function(r) {

    require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker','format',
	    'navbar', 'amazeui' ], function($, utils, Temp, Page, datepicker,format) {

	function PageRender(options) {
	    this.data = null; // 数据
	    this.temp = null; // 模板
	    this.selectSalerModal = $('[data-user="selectSaler-modal"]'); // 新增编辑的窗口
	    this.checkId = []; // 选中id
	    this.checkNickName = []; // 选中昵称
	    this.checkFace = []; // 选中face
	    this.eventType = null; // 当前操作
	    this.dataApi = {
		search : 'usrUserSlave/find', // 查询接口
		searchSaler:'sysUser/findSaler',
		updateSaler : 'usrUserSlave/updateSaler', // 新增接口
		searchOne : 'usrUserSlave/get' // 查询某条数据
	    };
	    this.userDataArr=[];
	    this.pks=[];
	    
	    this.salerOptions={
		pk:'',    
		face:'',
		nickName:''
	    };
	    
	    this.selects={
		salerSlt:$('[name="salerPk"]')    
	    }
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
		search : $('[data-user="query"]'),
		beforeSaler : $('[data-user="beforeSaler"]'),
		afterSaler : $('[data-user="afterSaler"]'),
		renewaler : $('[data-user="renewaler"]'),
		insurance : $('[data-user="insurance"]'),
	    };

	    // 新增/编辑弹出框模板
	    this.modalTemp = new Temp({
		tempId : 'select-saler-content',
		data : this.salerOptions,
		componentDidMount: function() {
		    this.content.find('select').selected();
			// 弹框列表删除
			this.content.find('.remove').on('click', function() {
			    var pk=$(this).attr('data-id');
			    that.pks.map(function(item, index) {
				if (pk == item) {
				    that.pks.splice(index, 1);
				}
			    });
			    $(this).parents('.cy-tr').remove();
			});
		}
	    });

	    var that = this;

	    // 创建分配顾问弹窗
	    this.selectSalerModal.modal(
		    {
			onConfirm : function() {
			    $('[name="pks"]').val(that.pks.join(","));
			    utils.ajax({
				url:that.dataApi.updateSaler,
				data: utils.getFormData($('[data-user="selectSaler-modal"]')),
				success:function(data){
				    if(data.success){
					utils.alert({
					    text:"分配成功",
					    type:'success'
					});
					that.refreshData();
				    }else{
					utils.alert({
					    text:"分配失败",
					    type:'success'
					})
				    }
				}
			    });
			}
		    }).modal('close');

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
		
            getSaler:function(type){
        	var that=this;
        	utils.ajax({
        	    url:this.dataApi.searchSaler,
        	    data:{'type':type},
        	    success:function(data){
        		that.selects.salerSlt.createSelect({
			    data : data.obj
			});
        	    }
        	})
        	
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
		$.extend(this.searchOptions,utils.getFormData($('[data-modal="search"]')));
		this.upData(function(data) {
		    that.data = data; // 第一次更新数据
		    that.temp.data = that.dataFormat(data.obj);
		    that.temp.render();
		});
	    },
	    
	    
	    beforeSaler : function(){
		this.getSaler('1');
		$('[name="type"]').val('1');
		$('.div-title').text('分配销售顾问');
		this.eventType = 'beforeSaler';
		this.refreshModal();
	    },
	    afterSaler : function(){
		this.getSaler('2');
		$('[name="type"]').val('2');
		$('.div-title').text('分配保养顾问');
		this.eventType = 'afterSaler';
		this.refreshModal();
	    },
	    renewaler : function(){
		this.getSaler('3');
		$('[name="type"]').val('3');
		$('.div-title').text('分配续保顾问');
		this.eventType = 'renewaler';
		this.refreshModal();
	    },
	    
	    insurance : function(){
		this.getSaler('4');
		$('[name="type"]').val('4');
		$('.div-title').text('分配保险顾问');
		this.eventType = 'renewaler';
		this.refreshModal();
	    },

	    // 刷新弹框
	    refreshModal : function() {
		this.userDataArr=[];
		this.pks=[];
		for(var i=0;i<this.checkId.length;i++){
		    var user=new Object();
		    user.pk=this.checkId[i];
		    user.nickName=this.checkNickName[i];
		    user.face=this.checkFace[i];
		    this.pks.push(this.checkId[i]);
		    this.userDataArr.push(user);
		}
		this.modalTemp.data = this.formatUser(this.userDataArr);
		this.modalTemp.render();
		this.selectSalerModal.modal('open');
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
			that.checkNickName.push(_this.data('nickname'));
			that.checkFace.push(_this.data('face'));
		    } else {
			that.checkId.map(function(item, index) {
			    if (_this.data('id') == item) {
				that.checkId.splice(index, 1);
				that.checkNickName.splice(index,1);
				that.checkFace.splice(index,1);
			    }
			});
		    }
		});
	    },

	    formatUser:function(data){
		
		var listData = null;
		if (!Array.isArray(data)) {
		    return false;
		}
		return data.map(function(item) {
		    return {
			pk : item.pk,
			face : item.face,
			nickName : item.nickName,
		    };
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
			status : ['未关注','已关注'][item.status],
			name : item.name,
			phone : item.phone,
			sex : format.sex(item.sex),
			carNo:item.carNo,
			carType:item.carType,
			levelName:item.levelName,
			score:item.score,
			saleBeforeUserPk:item.saleBeforeUserPk,
			saleAfterUserPk:item.saleAfterUserPk,
			renewalUserPk:item.renewalUserPk,
			insuranceUserPk:item.insuranceUserPk,
			//购车日期
			createTime : format.date(item.createTime,'YYYY-MM-DD'),
			mainTainDays : item.mainTainDays,
			insuranceDays : item.insuranceDays
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
		    beforeSaler : this.beforeSaler.bind(that),
		    afterSaler : this.afterSaler.bind(that),
		    renewaler : this.renewaler.bind(that),
		    insurance : this.insurance.bind(that)
		};

		[ 'search', 'beforeSaler', 'afterSaler', 'renewaler' ,'insurance'].map(function(i) {
		    that.elements[i].on('click', function() {
			this.eventType = null;

			if ((i == 'edit' && that.checkId.length != 1)
				|| ((i == 'beforeSaler'||i ==  'afterSaler'||i ==  'renewaler' ||i == 'insurance') 
					&& that.checkId.length < 1)
					) {
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
                menuPk: "UsrUserSlave"
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
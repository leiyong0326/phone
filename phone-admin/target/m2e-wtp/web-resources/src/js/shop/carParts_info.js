define([ '../require.config' ], function(r) {
    require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker',
	    'navbar', 'amazeui', 'umeditorConfig', 'umeditor','updataFile' ],
function($, utils, Temp, Page, datepicker) {
	var content;
	function AddInfo(options) {
	    this.data = {
		pk:'',
		classPk : '',
		className : '',
		slavePk : '',
		slaveName : '',
		name : '',
		srcPrice : '',
		price : '',
		status : '',
		recommend : '',
		discount : '',
		carType : '',
		count : '',
		scoreBuy : '',
		score : '',
		getScore : '',
		url : ''  ,
		listUrl:'',
		content:'',
		imgs:''
	    }; // 数据
	    this.options = utils.parseUrl(); // 超链接参数
	    
	    // 创建编辑器
	    this.um = UM.getEditor('editor');
	    
	    this.saveBtns={
		  save:$('[data-btn="saveData"]')  
	    };
	    this.temp = null; // 模板
	    this.checkId = []; // 选中id
	    
	    this.dataApi = {
		searchOne : 'shopCarParts/get',//查询单个
		save : 'shopCarParts/save',//保存接口
		getClass : 'shopPartsClass/findAll',//获取精品大类  
		getSlave : 'shopPartsClassSlave/findByUp',//获取精品小类
	    };
	   
	    var that = this;
	   
	    // 创建模板
	    this.temp = new Temp({
		tempId : 'add-content',
		data : this.data,
		componentDidMount : function() {
		    
		    // 下拉框
		    that.selects = {
			    classPk : $('[name="classPk"]'),
			    slavePk : $('[name="slavePk"]')
		    };
		    this.content.find('select').selected();
		    //parts_list_url
		    $('.parts_list_url').upDataFile({
			imageUrl:'partsImg',
			imgs:this.data.listUrl,
			size:[258,168]
		    });
		    // 图片上传组件.
		    $('.parts_imgs').upDataFile({
			imageUrl:'partsImg',
			imgs:this.data.imgs,
			success:function(data){
			    if(data != null){
				 var urls= $('[name="imgs"]').val();
				 if(urls .length > 0){
				     $('[name="imgs"]').val(urls+","+data);
				 }else{
				     $('[name="imgs"]').val(data);
				 }
			    }
			}
		    });
		   console.log(this.data.listUrl);
		    this.content.find('[name="status"] option[value="'+this.data.status+'"]').attr('selected',true);
		    this.content.find('[name="scoreBuy"] option[value="'+this.data.scoreBuy+'"]').attr('selected',true);
		    this.content.find('[name="discount"] option[value="'+this.data.discount+'"]').attr('selected',true);
		    this.content.find('[name="recommend"] option[value="'+this.data.recommend+'"]').attr('selected',true);
		    
		    content=this.data.content;
		}
	    });
	    
	 // 新增/编辑
	    if(this.options.type == 'add') {
		this.refresh();
		that.events();
		
	    }else if(this.options.type == 'edit') {
		this.upData(function(data) {
		    that.data= data.obj;
		    that.refresh();
		    that.events();
		});
	    }else {
		window.location.href = '../login.html';
	    }
	    
	}

	AddInfo.prototype = {
	    	
	    // 获取配件大类
	    getClass : function() {
		var that = this;
		utils.ajax({
		    url : that.dataApi.getClass,
		    success : function(data) {
			var obj = data.obj;
			that.selects.classPk.createSelect({
			    data: obj,
			    attr: ['pk', 'name'],
			    selected: that.data.classPk
			});
			if(that.data.classPk.length > 0){
			    that.getSlave();	
			}
			
		    }
		});

	    },

	    // 获取配件小类
	    getSlave : function() {
		var that = this;
		var upPk = that.selects.classPk.val();
		if(upPk.trim().length==0){
		    return;
		}
		utils.ajax({
		    url : that.dataApi.getSlave,
		    data:{
			upPk:upPk
		    },
		    success : function(data) {
			that.selects.slavePk.createSelect({
			    data: data.obj,
			    attr: ['pk', 'name'],
			    selected: that.data.slavePk
			});
		    }
		});
	    },
	    // 更新数据
	    upData: function(callback) {
		utils.ajax({
    			url: this.dataApi.searchOne,
    			data: {
    			    pk: this.options.id
    			},
    			success: function(data) {
    			    callback.call(this, data);
    			}
		});
	    },
	    
	    //保存
	    saveData:function(){
		var that = this;
		this.data.className=that.selects.classPk.find('option:selected').text();
		this.data.slaveName=that.selects.slavePk.find('option:selected').text();
		var umContent=this.um.getContent();
		if(umContent.trim().length < 1){
		    utils.alert({
			text:'商品详情不能为空',
			type:'danger'
		    });
		    return;
		}else{
		    if(content != null &&content.trim() != umContent.trim()){
			this.data.url=this.data.url+"?edit";
		    }
		    this.data.content=umContent;
		}
		utils.ajax({
		    url : this.dataApi.save,
		    data : this.data,
		    success : function(data) {
			if(data.success==true){
			    history.back();
			    utils.alert({
				text : '保存成功',
				type : 'success'
			    });
			    
			}else{
			    utils.alert({
				text :data.msg ,
				type : 'danger'
			    });
			}
			
		    }
		});
		
	    },
	    
	    refresh:function(){
		var that=this;
		that.temp.data =  that.modalDataFormat(this.data);
		that.temp.render();
		if(content != null && content.length > 0)
		that.um.setContent(content);
		
	    },
	    
	    modalDataFormat:function(data){
		return $.extend(data, this.data);
	    },
	    
	    // 控制
	    events : function() {
		var that = this;
		this.getClass();
		this.selectsEvents = {
		    classPk : this.getSlave.bind(this)
		};

		[ 'classPk' ].map(function(i) {
		    that.selects[i].on('change', function() {
			that.selectsEvents[i]();
		    });
		});
		
		    this.saveBtns.save.on('click',function(){
			    that.data = utils.getFormData($('[data-modal="save"]'));
			    that.saveData();
		    	});
		
	    }

	};

	// 初始化
	(function initialization() {
	    var power = { // 用户权限
		save : true
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
	    var addInfo = new AddInfo(power);
	})();

    });

});
define([ '../require.config' ], function(r) {

	require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker', 'json', 'navbar', 'amazeui','umeditor','umeditorConfig'], function($, utils, Temp, Page, datepicker, JSON) {

		function PageRender(options) {
			this.data = {
				list: []
			}; // 数据
			this.temp = null; // 模板
			this.addModal = $('[data-config="add-modal"]'); // 新增编辑的窗口
			this.checkId = []; // 选中id
			this.eventType = null; // 当前操作
			this.options = utils.parseUrl(); // 超链接参数
			this.dataApi = {
				search: 'recommendConfig/find', // 查询接口
				save: 'recommendConfig/saveOrUpdate', // 保存接口
				giftName: 'actGift/findGift', // 奖品名称
				getUser: 'actAttend/findAttend' // 查询参与对象条件
			};
			
			// 默认查询参数
			this.searchOptions = {
				pageNum: 1,
				pageSize: 10
			};
			
			// 创建编辑器
			this.um = UM.getEditor('editor');
			
			// 新增列表参数
			this.addOptions = {
				typePk: '',
				attendPk:'',
				attendPkMName: '',
				startTime: '',
				endTime: '',
				giftPkMName:'',
				giftPkSName: '',
				isDel: false,
				rule: '',
				pk: 'pk_' + Math.random()
			};

			// 弹出框表单验证
//			this.validator0 = new utils.Validator({
//				content: $('[data-cy-validators]')
//			});
			
//			this.validator1 = new utils.Validator();

//			this.delModal = utils.delModal.init({ /// 删除提示框
//				title : '温馨提示',
//				content : '你确定要删除这条信息？'
//			});

			// 按钮组合
			this.elements = {
				add: $('[data-config="add"]'),
				save: $('.config-info-save')
			};

			// 新增/编辑弹出框模板
			this.modalTemp = new Temp({
				tempId: 'add-content',
				data: this.addOptions,
				events: function() {
					this.content.find('select').selected();
				}
			});

			var that = this;

			// 创建新增/编辑弹窗
			this.addModal.modal({
				closeOnConfirm : false,
				onConfirm: function() {
					
//					if(that.addModal.find('[data-type="submit"]').data('validator') != 1) {
//						return;
//					}
					
					// 遍历数据
					['typePk', 'attendPkM','attendPkMName','startTime','endTime','giftPkM','giftPkMName','giftPkS','giftPkSName','rule'].map(function(i) {
						that.addOptions[i] = that.addModal.find('[name="' + i + '"]').val();
						if(i == 'attendPkMName') {
							that.addOptions[i] = that.addModal.find('[name="attendPkM"] option:selected').text();
						}
						if(i == 'giftPkMName') {
							that.addOptions[i] = that.addModal.find('[name="giftPkM"] option:selected').text();
						}
						if(i == 'giftPkSName') {
							that.addOptions[i] = that.addModal.find('[name="giftPkS"] option:selected').text();
						}
					});
					
					if(that.eventType == 'add') { // 新增
						that.addData();
					}else if(that.eventType == 'edit') { // 编辑
						that.editData();
					}

				}
			}).modal('close');

			 // 第一次更新数据
			this.upData(function(data) {
				that.data = data.obj;
				
				that.data = that.data.map(function(i) {
				    that.um.setContent(i.content);
					return $.extend(i, {
						isDel: false
					});
				});

				// 创建列表模板
				that.temp = new Temp({
					tempId : 'config-info-list',
					data : that.dataFormat(that.data)
				});

				that.events();
			});

		}

		PageRender.prototype = {

			// 更新数据
			upData: function( callback) {
				utils.ajax({
					url: this.dataApi.search,
					data: this.searchOptions,
					success: function(data) {
						callback.call(this, data);
					}
				});
			},

			// 删除数据
			del: function() {
				for(var i = 0, len = this.data.length; i < len; i++) {
					if(this.data[i].pk == this.checkId[0]) {
						if(this.data[i].pk.indexOf('pk_') != -1) {
							this.data.splice(i, 1);
						}else {
							this.data[i].isDel = true;
						}
					}
				}
				
				this.refreshData();
				this.temp.render();
			},

			// 初始化添加数据
			add: function() {
				this.addOptions = {
						typePk: '',
						attendPkMName: '',
						startTime: '',
						endTime: '',
						giftPkMName:'',
						giftPkSName: '',
						isDel: false,
						rule: '',
						pk: 'pk_' + Math.random()
					};
				this.eventType = 'add';
				this.refreshModal();
			},

			// 添加数据
			addData: function() {
				this.data.push(this.addOptions);
				this.refreshData();
				this.addModal.modal('close');
			},

			// 初始化编辑数据
			edit: function() {
				this.eventType = 'edit';

				for(var i = 0, len = this.data.length; i < len; i++) {
					if(this.data[i].pk == this.checkId[0]) {
						this.addOptions = this.data[i];
						this.refreshModal();
					}
				}
			},

			// 编辑数据
			editData: function() {
				var that = this;
				
				for(var i = 0, len = this.data.length;  i < len; i++) {
					if(this.data[i].pk == this.checkId[0]) {
						 this.data[i] = this.addOptions;
						 this.refreshData();
						 this.addModal.modal('close');
					}
				}
			},

			// 保存
			save: function() {

				for(var i = 0, len = this.data.length; i < len; i++) {
					if(this.data[i].pk && this.data[i].pk.indexOf('pk_') != -1) {
						delete this.data[i].pk;
					}
					this.data[i].content='';
				}
				
				console.log(this.data);
				utils.ajax({
					url: this.dataApi.save,
					data: {
						list: JSON.stringify(this.data),
						content:this.um.getContent()
					},
					success: function(data) {
						if(data.success){
						    	localStorage.name = 'success';
							history.back();
//							utils.alert({
//								text:'保存成功',
//								type:'success'
//							});
						}else{
							utils.alert({
								text:data.msg,
								type:'danger'
							});
						}
					}
				});
			},

			// 获取奖品名称列表
			getGift: function(callback) {
				var that = this;

				utils.ajax({
					url: this.dataApi.giftName,
					data: {
						pageNum: 0,
						pageSize: 0
					},
					success: function(data) {
						data.obj.map(function(i) {
							that.modalTemp.content.find('.giftPkM-name').append('<option value="' + i.pk + '">' + i.name + '</option>');
							that.modalTemp.content.find('.giftPkS-name').append('<option value="' + i.pk + '">' + i.name + '</option>');
						});
						callback();
					}
				});
			},
			
			// 获取参与对象列表
			getUser: function(callback) {
				var that = this;

				utils.ajax({
					url: this.dataApi.getUser,
					data: {
						pageNum: 0,
						pageSize: 0
					},
					success: function(data) {
						data.obj.map(function(i) {
							that.modalTemp.content.find('.attendPkM-name').append('<option value="' + i.pk + '">' + i.days + ['天未消费', '天未到店'][i.type] + '</option>');
						});
						callback();
					}
				});
			},

			// 刷新弹框
			refreshModal: function() {
				var that = this;

				this.modalTemp.data = this.modalDataFormat(this.addOptions);
				this.modalTemp.render();
				
//				this.validator0.init();

				this.getGift(function() {
					that.modalTemp.content.find('select').selected({
						maxHeight: '148px'
					});
					
					that.modalTemp.content.find('.giftPkM-name option[value="' + that.addOptions.giftPkM + '"]').attr('selected', true);
					that.modalTemp.content.find('.giftPkS-name option[value="' + that.addOptions.giftPkS + '"]').attr('selected', true);
				});
				
				this.getUser(function() {
					that.modalTemp.content.find('select').selected({
						maxHeight: '148px'
					});
					that.modalTemp.content.find('.attendPkM-name option[value="' + that.addOptions.attendPkM + '"]').attr('selected', true);
				});
				
				that.modalTemp.content.find('[name="typePk"] option[value="' + that.addOptions.typePk + '"]').attr('selected', true);
				
				// 初始化查询日期
				utils.datepick('.startTime', '.endTime');
				
				this.addModal.modal('open');
			},

			// 格式化弹窗数据
			modalDataFormat: function(data) {
			    return $.extend(data, {
				startTime: utils.dateFormat(data.startTime, 'YYYY-MM-DD'),
				endTime: utils.dateFormat(data.endTime, 'YYYY-MM-DD')
			    });
			},
			
			// 刷新列表数据
			refreshData: function() {
				var that = this;

				that.temp.data = that.dataFormat(this.data);
				that.temp.render();
			},

			// 选中
			check: function() {
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
			dataFormat: function(data) {
				if(!Array.isArray(data)) {
					return false;
				}
				
				return data.map(function(item) {
					return {
						isDel: item.isDel ? 'style="display:none;"' : '',
						pk: item.pk,
						typePk: ['购车', '保养维修','保险','其他'][item.typePk],
						attendPkMName: item.attendPkMName,
						startTime: utils.dateFormat(item.startTime, 'YYYY-MM-DD'),
						endTime: utils.dateFormat(item.endTime, 'YYYY-MM-DD'),
						giftPkMName: item.giftPkMName,
						giftPkSName: item.giftPkSName,
						rule:item.rule
					};
				});
			},

			// 控制
			events: function() {
				var that = this;
				this.check();

				// 事件组合
				this.elementEvents = {
					add: this.add.bind(this),
					edit: this.edit.bind(this),
					del: this.del.bind(this),
					save: this.save.bind(this)
				};

				['add', 'save'].map(function(i) {
					that.elements[i].on('click', function() {
						this.eventType = null;
						that.elementEvents[i]();
					});
				});

				// 列表编辑
				this.temp.content.on('click', '.config-info-edit', function() {
					that.checkId[0] = $(this).data('id');
					that.elementEvents['edit']();
				});

				// 列表删除
				this.temp.content.on('click', '.config-info-del', function() {
					that.checkId[0] = $(this).data('id');
					that.elementEvents['del']();
				});

			}

		};

		// 初始化
		(function initialization() {

			var power = { // 用户权限
				add : true,
				edit : true,
				del : true,
				edits : true
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

		})();

	});

});
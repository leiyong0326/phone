define([ '../require.config' ], function(r) {

	require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker', 'navbar', 'amazeui' ], function($, utils, Temp, Page, datepicker) {

		function PageRender(options) {
			this.data = null; // 数据
			this.temp = null; // 模板
			this.addModal = $('[data-attend="add-modal"]'); // 新增编辑的窗口
			this.checkId = []; // 选中id
			this.eventType = null; // 当前操作
			this.dataApi = {
				search: 'sysServerFee/find', // 查询接口
				add: 'sysServerFee/saveOrUpdate', // 新增接口
				searchOne: 'sysServerFee/getSysServerFee', // 查询某条数据
				del: 'sysServerFee/delete' // 删除接口
			};

			// 默认查询参数
			this.searchOptions = {
				pageNum: 1,
				pageSize: 10,
				type: '0'
			};

			// 新增列表参数
			this.addOptions = {
				name: '',
				fee: '',
				type:'',
				way:'',
				months:''
			};

			this.delModal = utils.delModal.init({ /// 删除提示框
				title : '温馨提示',
				content : '你确定要删除这条信息？'
			});

			// 按钮组合
			this.elements = {
				add: $('[data-config="add"]'),
				del: $('[data-config="del"]')
			};

			// 新增/编辑弹出框模板
			this.modalTemp = new Temp({
				tempId: 'add-content',
				data: this.addOptions
			});

			var that = this;

			// 创建新增/编辑弹窗
			this.addModal.modal({
				closeOnConfirm : false,
				onConfirm: function() {

					// 遍历数据
					['name', 'fee','type','way','months'].map(function(i) {
						that.addOptions[i] = that.addModal.find('[name="' + i + '"]').val();
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
				that.data = data;

				// 创建模板
				that.temp = new Temp({
					tempId : 'config-list',
					data : that.dataFormat(that.data.obj)
				});

				// 创建分页
				that.pagination = new Page({
					pageTotal: that.data.total,
					pageNo: that.searchOptions.pageNum,
					events: function(id) {
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
				var that = this;

				this.delModal.modal({
					onConfirm: function() {
						utils.ajax({
							url: that.dataApi.del,
							data: {
								pk: that.checkId.join(',')
							},
							success: function(data) {
								if(data.success){
									utils.alert({
										text : '删除成功',
										type : 'success'
									});
									that.refreshData();
								}else{
									utils.alert({
										text:data.msg,
										type:'danger'
									});
								}
							}
						});
					}
				});
			},

			// 初始化添加数据
			add: function() {
				this.addOptions = {
					name: '',
					fee: '',
					type: '0',
					months:''
				};
				this.eventType = 'add';
				this.refreshModal();
			},

			// 添加数据
			addData: function() {
				var that = this;

				utils.ajax({
					url: this.dataApi.add,
					data: this.addOptions,
					success: function(data) {
						if(data.success){
							utils.alert({
								text : '新增成功',
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

			// 初始化编辑数据
			edit: function() {
				var that = this;

				this.eventType = 'edit';

				utils.ajax({
					url: this.dataApi.searchOne,
					data: {
						pk: this.checkId[0]
					},
					success: function(data) {
						for(var attr in that.addOptions) {
							if(that.addOptions.hasOwnProperty(attr)) {
								that.addOptions[attr] = data.obj[attr];
							}
						}
						that.addOptions.pk = data.obj.pk;
						that.refreshModal();
					}
				});
			},

			// 编辑数据
			editData: function() {
				var that = this;

				utils.ajax({
					url: this.dataApi.add,
					data: this.addOptions,
					success: function(data) {
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

			// 刷新弹框
			refreshModal: function() {
				this.modalTemp.data = this.addOptions;
				this.modalTemp.render();
				this.addModal.modal('open');
			},

			// 刷新列表数据
			refreshData: function() {
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
				var listData = null;

				if(!Array.isArray(data)) {
					return false;
				}

				return data.map(function(item) {
					return {
						name: item.name,
						fee: item.fee,
						pk: item.pk,
						type: item.type,
						way: item.way,
						months: item.months
					};
				});
			},

			// 控制
			events: function() {
				var that = this;
				this.check();

				// 事件组合
				this.elementEvents = {
					add: this.add.bind(that),
					edit: this.edit.bind(that),
					del: this.del.bind(that)
				};

				['add', 'del'].map(function(i) {
					that.elements[i].on('click', function() {
						this.eventType = null;

						if((i == 'edit' && that.checkId.length != 1) || (i == 'del' && that.checkId.length < 1)) {
							utils.alert({
								text: '必须选择一条数据',
								type: 'warning'
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
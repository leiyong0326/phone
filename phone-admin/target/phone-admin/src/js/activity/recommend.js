/*
 * Date: 2016.07.04
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */


define([ '../require.config' ], function(r) {

	require([ 'jquery','format', 'utils', 'template', 'pagination', 'datetimepicker', 'navbar', 'amazeui' ], function($,fmt, utils, Temp, Page, datepicker) {

		function PageRender(options) {
			this.data = []; // 数据
			this.temp = null; // 模板
			this.checkId = []; // 选中id
			this.eventType = null; // 当前操作
			this.options = options;
			this.dataApi = {
				search: 'recommendDetail/find', // 查询接口
				del: 'recommendDetail/delete', // 删除接口
				updateStaus: 'recommendDetail/updateStatus'	//修改状态
			};

			// 默认查询参数
			this.searchOptions = {
				pageNum: 1,
				pageSize: 10
			};

			this.delModal = utils.delModal.init({ /// 删除提示框
				title : '温馨提示',
				content : '你确定要删除这条信息？'
			});

			// 按钮组合
			this.elements = {
				search: $('[data-recommend="query"]'),
				del: $('[data-recommend="del"]')
			};

			// 初始化查询日期
			utils.datepick('.recommend-start', '.recommend-end');

			var that = this;

			 // 第一次更新数据
			this.upData(function(data) {
				that.data = data;

				// 创建模板
				that.temp = new Temp({
					tempId : 'activity-list',
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
			
			var name = localStorage.name;
			if(name == 'success'){
			    utils.alert({
				text:'保存成功',
				type:'success'
			    });
			    localStorage.removeItem('name');
			}
		}

		PageRender.prototype = {

			// 更新数据
			upData: function(callback) {
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

			// 查询数据
			search: function() {
				var that = this,
				options = utils.getFormData($('[data-cy-validator]'));
				
				this.searchOptions = {
						pageNum: 1,
						pageSize: 10
					};

				$.extend(this.searchOptions, options);
				
				this.upData(function(data) {
					that.data = data; // 第一次更新数据
					
					that.temp.data = that.dataFormat(data.obj);
					that.temp.render();
				});
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

			// 格式化数据
			dataFormat: function(data) {
			    	var that = this;
				var listData = null;

				if(!Array.isArray(data)) {
					return false;
				}
				
				return data.map(function(item) {
					return {
						pk: item.pk,
						edit : fmt.display(that.options.edit),
						show : fmt.display(that.options.show),
						typePk: ['购车', '保养维修','保险','其他'][item.typePk],
						userPkMName: item.userPkMName,
						giftMName: item.giftMName,
						phoneM: item.phoneM,
						userPkSName: item.userPkS,
						giftSName: item.giftSName,
						phoneS: item.phoneS,
						createTime: utils.dateFormat(item.createTime, 'YYYY-MM-DD'),
						joinTime: utils.dateFormat(item.joinTime, 'YYYY-MM-DD'),
						confirmTime: utils.dateFormat(item.confirmTime, 'YYYY-MM-DD'),
						code: item.code,
						status: item.status,
						statusText: ['待确认', '已确认','已领取'][item.status],
						btnClass: ['', '', 'style="display:none;'][item.status],
						btnText: ['确认', '领取', true][item.status]
					};
				});
			},

			// 控制
			events: function() {
				var that = this;
				this.check();

				// 事件组合
				this.elementEvents = {
					search: this.search.bind(this),
					del: this.del.bind(this)
				};

				['search', 'del'].map(function(i) {
					that.elements[i].on('click', function() {
						this.eventType = null;

						if(i == 'del' && that.checkId.length < 1) {
							utils.alert({
								text: '必须选择一条数据',
								type: 'warning'
							});
							return;
						}

						that.elementEvents[i]();
					});
				});

				// 行操作按钮
				this.temp.content.on('click', '.editBtn', function() {
					var _id = $(this).data('status');
					var _pk = $(this).data('id'),_status,_isAjax = false;
					switch(_id) {
						case 0: (function() { // 确认
							_status = '1';
							_isAjax = true;
						})();
						break;
						case 1: (function() { // 领取
							_status = '2';
							_isAjax = true;
						})();
						break;
					}
					if(_isAjax){
						utils.ajax({
							url: that.dataApi.updateStaus,
							data: {
								pk: _pk,
								status: _status
							},
							success: function(data) {
								if(data.success){
									utils.alert({
										text : '操作成功',
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
				
			}

		};

		// 初始化
		(function initialization() {
		    var url = "sysMenu/findMenuFunc";
	            var data = {
	                menuPk: "ActRecommendDetail"
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
define([ '../require.config' ], function(r) {

	require([ 'jquery','format', 'utils', 'temp', 'config', 'pagination', 'datetimepicker', 'navbar', 'amazeui' ], function($,fmt, utils, extendsFn, config, Page, datepicker) {

		var prizedrawPrototype = {

			upDate: function() {
				var that = this;

				utils.ajax({
					url: config.api.searchGiftMasterList,
					data: this.ajaxArgs,
					success: function(data) {
						that.data.lists = data.obj;
						that.render();
					}
				});
			},

			// 新增
			add: function() {
				window.location.href = this.infoPageUrl + '?type=add';
			},

			// 编辑
			edit: function(evt) {
				window.location.href = this.infoPageUrl + '?type=edit&id=' + evt.dataset.id;
			},

			// 删除
			del: function() {
				var that = this;
				utils.ajax({
					url: config.api.delGiftMaster,
					data: {
						pk: that.checkId.join(',')
					},
					success: function(data) {
						if(data.success){
							utils.alert({
								text : '删除成功',
								type : 'success'
							});
							that.upDate();
						}else{
							utils.alert({
								text:data.msg,
								type:'danger'
							});
						}
					}
				});
			},

			// 查询
			search: function() {
				var that = this;
				this.ajaxArgs = $.extend(this.ajaxArgs, {
					name: $('.prize-search-name').val()
				});
				that.upDate();
			},

			check: function(evt) {
				var that = this,
                    _this = $(evt);

                if (_this.hasClass('checked')) {
                    that.checkId.push(_this.data('id'));
                } else {
                    that.checkId.map(function (item, index) {
                        if (_this.data('id') == item) {
                            that.checkId.splice(index, 1);
                        }
                    });
                }
			},

			getInitialState: function() {
				var that = this;

				this.ajaxArgs = {
					pageNum: 1,
					pageSize: 50
				};
                this.checkId = [];
				this.infoPageUrl = 'prizedraw_info.html'; // 详细页

				this.addBtn = $('[data-prizedraw="add"]'); // 新增按钮
				this.delBtn = $('[data-prizedraw="del"]'); // 删除按钮
				this.searchBtn = $('[data-prizedraw="query"]'); // 查询按钮

				this.delModal = utils.delModal.init({ // 删除提示框
					title : '温馨提示',
					content : '你确定要删除这条信息？'
				});

				this.delModal.modal({
					onConfirm: function() {
						that.del();
					}
				}).modal('close');

				this.delBtn.on('click', function() {
					that.delModal.modal('open');
				});

				this.searchBtn.on('click', function() {
					that.search();
				});

				this.addBtn.on('click', this.add.bind(this));
				this.upDate();
			}
		};

		// 初始化
		(function initialization() {
		    var url = "sysMenu/findMenuFunc";
	            var data = {
	                menuPk: "ActGiftMaster"
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

							var Prizedraw = extendsFn(prizedrawPrototype);
	                        var prizedraw = new Prizedraw({
	                            id: '#prizedraw-list',
	                            data: {
	                                power: power
	                            }
	                        });

	                        // var pageRender = new PageRender(power);
	                    }
	                }
	            });

		})();

	});

});

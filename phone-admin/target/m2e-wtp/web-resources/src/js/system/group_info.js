define([ '../require.config' ], function(r) {

	require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker', 'navbar', 'amazeui', 'updataFile' ], function($, utils, Temp, Page, datepicker) {

		// 新车信息管理
		function GroupInfo() {}

		GroupInfo.prototype = {

			// 初始化
			init: function() {
				this.data = [];

				// 图片上传组件.
				$('.cy-input-file').upDataFile();

				// 基本信息模板
				this.actTemp = new Temp({
					tempId: 'group-form',
					data: this.data
				});

			},

			// 获取数据
			upData: function() {

			},

			// 渲染信息模块
			renderInfo: function() {

			},

			// 保存
			save: function() {

			},

			// 事件处理
			events: function() {

			}

		};

		var groupInfo = new GroupInfo();
		groupInfo.init();
	});

});
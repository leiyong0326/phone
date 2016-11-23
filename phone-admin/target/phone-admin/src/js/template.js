/*
 * author: luoxu-xu.github.io;
 * date: 2016.07.04;
 * about: 简单的html模板
 * version: 1.0.0
 */

define(['jquery', 'utils'], function($, utils) {

	function Template(options) {
        this.options = $.extend({
            tempId: null, //模板id
            data: null, // 数据模型
            events: function() {}, // 事件处理
            componentDidMount: function() {} // DOM渲染完毕
        }, options);

        if(!this.options.tempId || !this.options.data) {
        	throw new Error("arguments is not defined.");
        }

        this.content = $('[data-content="' + this.options.tempId + '"]'); // 列表容器
        this.view = $('#' + this.options.tempId).html(); // 模板

        this.data = this.options.data;
		this.render();
		this.events();
	}

	Template.prototype = {

		// 渲染
		render: function() {
            if(!this.data || this.data.length === 0) {
                this.content.html(this.setPrompt());
                return;
            }
			this.el = $(utils.v(this.view, this.data));
			this.content.html(this.el);
			this.options.events.call(this); // v2.0 这个回调函数会取消
			this.options.componentDidMount.call(this);
		},

		// 创建提示信息
		setPrompt: function(text) {
			var text = text || '暂无数据';

			return $('<p class="cy-text-c cy-m24">' + text + '</p>');
		},

		// 新增数据
		addData: function(obj) {
			this.data.push(obj);
			this.render();
		},

		//删除数据
		delData: function(id, type) {
			var _ = this;

			this.data.map(function(item, index) {
				if(item[type] == id) {
					_.data.splice(index, 1);
				}
			});

			this.render();
			return _.data;
		},

		// 控制器
		events:  function() {
		    utils.checkbox.refresh();
		}

	};

	return Template;

});
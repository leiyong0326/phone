/*
 * author: luoxu-xu.github.io;
 * date: 2016.07.05;
 * about: 简单的分页
 * version: 1.0.0
 */

define(['jquery', 'utils'], function ($, utils) {

    function Pagination(options) {
        this.options = $.extend({
            pageContentId: '.cy-pagination', // 分页容器Id
            pageTotal: null, // 页码总数
            pageNo: null, // 当前页码
            events: function () {} // 事件处理
        }, options);

        this.view = '<a class="${active}" href="javascript:;" data-id="${pageId}">${text}</a>';
        this.content = $(this.options.pageContentId);
        this.id = 1;
        this.events();
        if (!this.options.pageTotal || this.options.pageTotal < 2) {
            // 2页以上才有必要分页
            return;
        }
        this.refreshData(1);
        this.render();
    }

    Pagination.prototype = {

        // 渲染
        render: function () {
            this.el = $(utils.v(this.view, this.data));
            this.content.html(this.el);
        },

        // 更新data
        refreshData: function (id) {
            var pageObj = null,
                i = 1,
                len = this.options.pageTotal;

            this.data = [];
            this.id = id;

            if (id > 1) {
                this.data.push({
                    text: '上一页',
                    pageId: 'prev'
                });
            }
            var end = id + 4;
            var start = id - 2;
            if (end > len) {
                end = len;
                start = len - 6;
            } else if (start < 1) {
                end = 7;
            }
            for (; i <= len; i++) {
                if (id === i) {
                    // 当前页码
                    this.data.push({
                        text: i,
                        pageId: i,
                        active: 'active'
                    });
                } else if (i === 1 || (i > start && i < end) || i === len) {
                    // 正常页码
                    this.data.push({
                        text: i,
                        pageId: i
                    });
                }
            }

            if (id < len) {
                this.data.push({
                    text: '下一页',
                    pageId: 'next'
                });
            }

            this.render();
        },

        // 控制器
        events: function () {
            var _ = this;
            this.content.on('click', 'a', function () {
                var id = $(this).data('id');

                if (id === 'prev' && _.id > 1) {
                    _.id--;
                } else if (id === 'next' && _.id < _.options.pageTotal) {
                    _.id++;
                } else if (id !== '') {
                    _.id = id;
                } else {
                    return;
                }

                _.refreshData(_.id);
                _.options.events.call(_, _.id);
            });

        }

    };

    return Pagination;

});

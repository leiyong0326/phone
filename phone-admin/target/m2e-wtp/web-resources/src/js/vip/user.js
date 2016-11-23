/*
 * Date: 2016.06.30
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */


 // 组织架构
 define(['../require.config'], function(r) {

    require(['jquery', 'utils', 'template', 'pagination', 'navbar', 'amazeui'], function($, utils, Temp, Page) {

        // 模拟数据
        var pageData = {
                pageTotal: 10,
                pageNo: 100,
                pageId: 1,
                lists: []
        }, i = 0, names = ['王李张刘陈智杨黄赵周', '忽如一夜春风来千树万树', '空山新雨后天气晚来秋'];

        for(; i < pageData.pageTotal; i++) {
            pageData.lists.push(createList(i));
        }

        function createList(id) {
            var arr = [];

            for(var k = 0; k < pageData.pageNo; k++) {
                arr.push({
                    id: Math.random(),
                    name: names[2].charAt(Math.floor(Math.random() * 10)) + names[1].charAt(Math.floor(Math.random() * 10)) + '科技',
                    store: Math.random() < .5 ? ' 是' : ' 否',
                    power: Math.random() < .5,
                    mobile: 135 + '' + (Math.floor(Math.random() * 10000000000) + '').substring(0, 8),
                    address: names[1].charAt(Math.floor(Math.random() * 10)) + names[2].charAt(Math.floor(Math.random() * 10)) + '屯',
                    user: names[0].charAt(Math.floor(Math.random() * 10)) + names[1].charAt(Math.floor(Math.random() * 10)) + names[2].charAt(Math.floor(Math.random() * 10)),
                    prompt: '2016年高考成绩是：' + (200 + Math.floor(Math.random() * 500)) + '分' + ' ' + (Math.random() < .5 ? ' 文科' : ' 理科'),
                	date: +new Date()
                });
            }

            return arr;
        }


        var departmentTemp = new Temp({
            tempId: 'group-list', //模板id
            data: pageData.lists[0],
            events: function() {

                var _ = this,
                	checkId = [],
                    delBtn = $('[data-group="del"]'), // 删除按钮
                    delModal = utils.delModal.init({
                        title: '温馨提示',
                        content: '你确定要删除这条信息？'
                    });

                // 选中某条数据
                _.content.on('click', '.cy-checkbox', function() {
                    var _this = $(this);
                    if(!$(this).hasClass('checked')) {
                        checkId.push(_this.data('id'));
                    }else {
                        checkId.map(function(item, index) {
                            if(_this.data('id') == item) {
                                checkId.splice(index, 1);
                            }
                        });
                    }
                });

                // 删除数据
                delBtn.bind('click', function() {
                    delModal.modal({
                        onConfirm: function() {
                            if(checkId.length < 1) {
                                $('.cy-wrapper').prepend(alert.init({
                                    text: '请选择至少一个组织',
                                    type: 'warning'
                                }));
                                return;
                            }
                            checkId.map(function(item) {
                                _.delData(item);
                                $('.cy-wrapper').prepend(alert.init({
                                    text: '成功删除所选组织',
                                    type: 'success'
                                }));
                            });

                            checkId = [];
                        }
                    });
                });

            }
        });

    });

});
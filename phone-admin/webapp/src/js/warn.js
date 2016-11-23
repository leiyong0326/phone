/*
 * author: luoxu-xu.github.io;
 * date: 2016.06.30;
 * about: 一些jquery插件
 * version: 1.0.0
 */

define([ ], function(utils) {
    // 工具集
    var warn = {
	success:'成功',
	addSuccess:'新增成功',
	editSuccess:'修改成功',
	delSuccess:'删除成功',
	t:'温馨提示',
	ss:'仅支持单笔操作',
	ms:'至少选择一条数据',
	d:'你确定要删除这条记录吗?',
	dsb:'确定要禁用吗?禁用后相关人员将无法浏览且不允许登录本系统.',
	enb:'确定要启用吗?启用后相关人员将允许登录本系统.',
	dsbs:'禁用成功,相关人员将无法访问本系统',
	enbs:'启用成功,相关人员将允许访问本系统'
    }
    return warn;
});

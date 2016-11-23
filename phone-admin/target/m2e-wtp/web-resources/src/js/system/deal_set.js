/*
 * Date: 2016.06.30
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */


 // 组织架构
 define(['../require.config'], function(r) {

    require(['amazeui', 'jquery', 'navbar'], function(amazeui, $) {

		$('.vip-set-del-btn').on('click', function() {
			$('#del-vip-set').modal({
				relatedTarget: this,
				onConfirm: function(options) {
					$('.cy-table-tr').last().remove();
				},
				// closeOnConfirm: false,
				onCancel: function() {
					
				}
			});
		});

    });

});
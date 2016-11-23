define([ '../require.config' ], function(r) {

	require([ 'jquery', 'utils', 'template', 'pagination', 'datetimepicker', 'navbar', 'amazeui' ], function($, utils, Temp, Page, datepicker) {

		$('.cy-input-file').upDataFile(
			{
			    url:'http://v0.api.upyun.com/cy-carimg-files/img',
			    beforeSend:function(request) {
	                        request.setRequestHeader("Date", "Tue, 12 Jul 2016 02:01:16 GMT");
	                        request.setRequestHeader("Authorization", "UpYun qhcykjadmin:fd65b1d906d9615feae34ea611760f40");
	                    },
			    success:function(data){
				alert(data);
			    },
			    error:function(data){
				
			    }
			}
		);

		var validator = utils.validator;

		$('.addgroup-btn a').on('click', function() {
			
		});

	});

});
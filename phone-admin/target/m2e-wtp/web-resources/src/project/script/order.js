$(document).ready(function() {
    addEvent();
    loadHistory();
});

function addEvent() {
    $('.cy-cancel').bind('click', function() {
	window.parent.lastForward();
    })
}

function loadHistory() {
    sendAjax({
	url : "/logPhone/myHistory",
	data : {},
	success : function(data) {
	    var obj = data.obj;
	    if (obj instanceof Array) {
		if (obj.length == 0) {
		    $('#history').html("暂时还没有订单哦,你可以的.");
		    return;
		}
		var historyHtml = "";
		for (var i = 0; i < obj.length; i++) {
		    var o = obj[i];
		    var createTime = dateFormat(o.createTime);
		    var dsc = o.dsc;
		    var html = '<div class="order"><div class="type">产品:<span>'
			    + o.name
			    + '</span></div><div class="order-price"><p class="order-price-price">价格:<span>'
			    + o.salePrice
			    + '</span></p><p class="order-price-gain">我赚了:<span>'
			    + o.ratio
			    + '</span></p></div><div class="type">时间：<span>'
			    + createTime + '</span></div></p><div class="type">备注：<span>'
			    + dsc + '</span></div></div>';
		    historyHtml += html;
		}
		$('#history').html(historyHtml);
	    }
	}
    });
}

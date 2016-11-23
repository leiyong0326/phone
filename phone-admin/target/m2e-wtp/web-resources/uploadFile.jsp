<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width">
<title>uploadFile</title>
<script type="text/javascript" src="resources/js/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/uploadPreview.min.js"></script>
<script type="text/javascript">
window.onload = function () { 
    new uploadPreview({ UpBtn: "up_img", DivShow: "imgdiv", ImgShow: "imgshow",callback:function() {
    	$("#bt").css("display","none");
    	$("#imgdiv").css("display","block");
    } });
}
</script>  
<style type="text/css">
.add-icon{
	height: 30%;
	width: 100%;
	text-align: center;
	padding: 20px 0% 20px 30%
}

.file-area{
	height: 150px;
	width: 40%;
	border: 3px dotted #eee;
	background: url('./resources/images/add_icon.png') center no-repeat;
    background-size:80px 80px;  
}
.file-area span {
	font-size: 18px;
	font-style: italic;
	color: #aaa
}
.file-input{
	height:125px;
	width: 40%;
    overflow: hidden;
    right:0;
    top:0;
    opacity: 0;
    filter:alpha(opacity=0);
    cursor:pointer;
}

.file-upload{
	width:400px;
	text-align: center;
}
.submit {
	color:#fff;
	font-size:20px;
	border: 0px;
	background-color: #888;
	height: 35px;
	border-radius: 5px;
	width: 50%;
	margin: 10px 0 0 0%;
}
</style>
</head>
<body>
<div class="file-upload">
	<form  accept-charset="utf-8" id="upload" action="<%=request.getContextPath() %>/upload/uploadImg" method="post" enctype="multipart/form-data">
		<div class="add-icon">
			<div class="file-area" id="bt"  style="display: block">
				<input type="file" class="file-input" name="file" id="up_img" accept="image/*" /><br> <span>选择文件</span>
				<input  style="display: none" type="text" name="bookicon" value="">
			</div>
			
	       <div class="file-area" id="imgdiv" style="display: none">
	      	 <img id="imgshow" width="128px" height="150px" />
	       </div>
		</div>
		<input type="submit" class="submit" name="submit" value="上传">
	</form>
</div>
</body>
</html>
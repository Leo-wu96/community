$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	//获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();

//  异步请求
	$.post(
		//发送路径
		CONTEXT_PATH+"/discuss/add",
		{"title":title,"content":content},
		function (data){
			data = $.parseJSON(data);
			//在提示框中显示返回消息
			$("#hintBody").text(data.msg);
			//显示消息框
			$("#hintModal").modal("show");
			//2秒后，自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				if(data.code==0){
					window.location.reload();
				}
			}, 2000);

		}
	)


}
// 我的信息页面初始化
function initMinePage(error, message) {
	showErrorOrMessage(error, message);
	var url = "/ajax/system/user/mine/update";
	var FORM_ID = "form_detail";
	$("#" + FORM_ID).validate();
	// 保存
	$("#button_save").click(function() {
		if ($("#" + FORM_ID).valid()) {
			$.loading();
			var data = $("#" + FORM_ID).serialize();
			$.ajax({
				type : "PUT",
				url : url,
				data : data,
				dataType : "json",
				success : function(json) {
					$.loaded();
					if (json.code == OK) {
						$.successTips($.i18n.prop("update.success"));
					} else {
						$.errorTips(json.message);
					}
				},
				error : function() {
					$.loaded();
					$.errorTips($.i18n.prop("http.request.failed"));
				}
			});
		}
	});
}
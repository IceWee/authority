$(function() {
	var DIALOG_ID = "dialog_password";
	var FORM_ID = "form_password";
	var url = "/ajax/system/user/password";
	
	// 修改密码
	$("#link_changePassword").on("click", function() {
		$("#" + FORM_ID)[0].reset();
		$("#" + FORM_ID).validate();
		$("#" + DIALOG_ID).modal({keyboard:false});
	});
	// 取消
	$("#button_password_cancel").on("click", function() {
		$("#" + DIALOG_ID).modal("hide");
	});
	// 确定
	$("#button_password_confirm").on("click", function() {
		if ($("#" + FORM_ID).valid()) {
			var data = $("#" + FORM_ID).serialize();
			$.ajax({
				type : "PUT",
				url : url,
				data : data,
				dataType : "json",
				success : function(json) {
					if (json.code == OK) {
						$.successTips($.i18n.prop("save.success"));
					} else {
						$.errorTips(json.message);
					}
				},
				error : function() {
					$.errorTips($.i18n.prop("http.request.failed"));
				}
			});
		}
	});
});
// 绑定登陆页面事件
function initPage() {
	$("#username").focus();
	
	$("#button_login").click(function() {
		if ($.trim($("#username").val()) == "") {
			$("#tips_login").text($.i18n.prop("login.required.username"));
			$("#username").focus();
			return;
		}
		if ($.trim($("#password").val()) == "") {
			$("#tips_login").text($.i18n.prop("login.required.password"));
			$("#password").focus();
			return;
		}
		if ($.trim($("#captcha").val()) == "") {
			$("#tips_login").text($.i18n.prop("login.required.captcha"));
			$("#captcha").focus();
			return;
		}
		if ($.trim($("#captcha").val()).toLowerCase() != $("#raw_captcha").text().toLowerCase()) {
			$("#tips_login").text($.i18n.prop("login.error.captcha"));
			$("#captcha").select();
			return;
		}
		$("#button_submit").click();
	});
}
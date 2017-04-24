// 绑定登陆页面事件
function initPage() {
	i18n();
	
	$("#login-button").click(function() {
		if ($.trim($("#username").val()) == "") {
			$("#login-tips").text($.i18n.prop("login.required.username"));
			$("#username").focus();
			return;
		}
		if ($.trim($("#password").val()) == "") {
			$("#login-tips").text($.i18n.prop("login.required.password"));
			$("#password").focus();
			return;
		}
		if ($.trim($("#captcha").val()) == "") {
			$("#login-tips").text($.i18n.prop("login.required.captcha"));
			$("#captcha").focus();
			return;
		}
		if ($.trim($("#captcha").val()).toLowerCase() != $("#raw-captcha").text().toLowerCase()) {
			$("#login-tips").text($.i18n.prop("login.error.captcha"));
			$("#captcha").select();
			return;
		}
		$("#submit-button").click();
	});
}
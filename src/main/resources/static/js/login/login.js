// 绑定登陆页面事件
function initEvents() {
	i18n();
	
	$("#login-button").click(function() {
		if ($.trim($("#username").val()) == "") {
			$("#login-tips").text($.i18n.prop("login.required.username"));
			return;
		}
		if ($.trim($("#password").val()) == "") {
			$("#login-tips").text($.i18n.prop("login.required.password"));
			return;
		}
		if ($.trim($("#captcha").val()) == "") {
			$("#login-tips").text($.i18n.prop("login.required.captcha"));
			return;
		}
		if ($.trim($("#captcha").val()) != $("#raw-captcha").text()) {
			$("#login-tips").text($.i18n.prop("login.error.captcha"));
			return;
		}
		$("#submit-button").click();
	});
}
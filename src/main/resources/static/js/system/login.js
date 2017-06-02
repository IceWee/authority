// 绑定登陆页面事件
function initLoginPage() {
	$("#username").focus();
	// 刷新验证码
	$("#captchaImage").click(function() {
		$(this).attr("src", "/captchaImage?time=" + new Date().getTime());
	});
	// 回车提交
	$("#form_login input").keyup(function(event) {
		if (event.keyCode == 13) {
			$("#button_login").click();
		}
	});
	// 提交校验
	$("#button_login").click(function() {
		if ($.trim($("#username").val()) == "") {
			$("#tips_login").text($.i18n.prop("login.required.username"));
			$.shake("#container_login");
			$("#username").focus();
			return;
		}
		if ($.trim($("#password").val()) == "") {
			$("#tips_login").text($.i18n.prop("login.required.password"));
			$.shake("#container_login");
			$("#password").focus();
			return;
		}
		var captcha = $.trim($("#captcha").val());
		if (captcha == "") {
			$("#tips_login").text($.i18n.prop("login.required.captcha"));
			$.shake("#container_login");
			$("#captcha").focus();
			return;
		}
		$.ajax({
			type : "GET",
			url : "/captchaCode?time=" + new Date().getTime(),
			dataType : "text",
			success : function(code) {
				if (captcha.toLowerCase() == code.toLowerCase()) {
					$.loading();
					$("#form_login").submit();
				} else {
					$("#tips_login").text($.i18n.prop("login.error.captcha"));
					$.shake("#container_login");
					$("#captcha").select();
					return;
				}
			}
		});
	});
}
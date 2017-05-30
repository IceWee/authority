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
			shake();
			$("#username").focus();
			return;
		}
		if ($.trim($("#password").val()) == "") {
			$("#tips_login").text($.i18n.prop("login.required.password"));
			shake();
			$("#password").focus();
			return;
		}
		var captcha = $.trim($("#captcha").val());
		if (captcha == "") {
			$("#tips_login").text($.i18n.prop("login.required.captcha"));
			shake();
			$("#captcha").focus();
			return;
		}
		$.ajax({
			type : "GET",
			url : "/captchaCode?time=" + new Date().getTime(),
			dataType : "text",
			success : function(code) {
				if (captcha.toLowerCase() == code.toLowerCase()) {
					$("#form_login").submit();
				} else {
					$("#tips_login").text($.i18n.prop("login.error.captcha"));
					shake();
					$("#captcha").select();
					return;
				}
			}
		});
	});
}

// 校验失败窗口抖动
function shake() {
    $("#container_login").addClass("animated");
    $("#container_login").addClass("shake");
    // 设置超时清除动画，否则无法进行下一次动画显示
    setTimeout(function() {
	    $("#container_login").removeClass("shake");
    }, 3000);
}
// 绑定登陆页面事件
function initLoginPage() {
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
		
//		ajaxLogin();
	});
}

function ajaxLogin() {
	var data = $("#form_login").serializeJson();
	$.ajax({
		type : "GET",
		contentType: "application/json",
		url : "/login",
		data : data,
		success : function(json) {
			document.write(json);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("status: " + textStatus);
			alert(errorThrown);
		}
	});
}
// JQuery validator 扩展

$.validator.setDefaults({
	highlight: function (element) {
		$(element).closest('.form-group').removeClass('has-success').addClass('has-error');
	},
	success: function (element) {
		element.closest('.form-group').removeClass('has-error').addClass('has-success');
	},
	errorElement: "span",
	errorPlacement: function (error, element) {
		if (element.is(":radio") || element.is(":checkbox")) {
			error.appendTo(element.parent().parent().parent());
	    } else {
	        error.appendTo(element.parent());
	    }
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"
});

//资源地址
$.validator.methods.uri = function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9\/\?]+$/i.test(value);
};
// 账号
$.validator.methods.username = function(value, element) {
	return this.optional(element) || /^[a-zA-Z][a-zA-Z0-9]+$/i.test(value);
};
// 密码
$.validator.methods.password = function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9]{6,}$/i.test(value);
};
// 姓名
$.validator.methods.surname = function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9\u4E00-\u9FA5]+$/i.test(value);
};
// 手机
$.validator.methods.mobile = function(value, element) {
	return this.optional(element) || /^(13|14|15|17|18)\d{9}$/i.test(value);
};
// 编码
$.validator.methods.code = function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9]+$/i.test(value);
};
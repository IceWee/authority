// JQuery validator 扩展
// 账号
$.validator.methods.username = function(value, element) {
	return this.optional(element) || /^[a-zA-Z][a-zA-Z0-9]+$/i.test(value);
};
// 密码
$.validator.methods.password = function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9]{6,}$/i.test(value);
};

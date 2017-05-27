var OK = "200";

// 将表单序列化成JSON对象
// 依赖JQuery的serializeArray方法
(function($) {
	$.fn.serializeJson = function() {
		var serializeObj = {};
		var array = this.serializeArray();
		var str = this.serialize();
		$(array).each(
				function() {
					if (serializeObj[this.name]) {
						if ($.isArray(serializeObj[this.name])) {
							serializeObj[this.name].push(this.value);
						} else {
							serializeObj[this.name] = [serializeObj[this.name], this.value ];
						}
					} else {
						serializeObj[this.name] = this.value;
					}
				});
		return serializeObj;
	};
})(jQuery);

// toastr全局配置
var toastrOptions = {
	"closeButton": false,
	"debug": false,
	"progressBar": false,
	"positionClass": "toast-top-left",
	"onclick": null,
	"showDuration": "400",
	"hideDuration": "1000",
	"timeOut": "3000",
	"extendedTimeOut": "1000",
	"showEasing": "swing",
	"hideEasing": "linear",
	"showMethod": "fadeIn",
	"hideMethod": "fadeOut"
}

// toastr.js扩展
$.extend({
	// 成功提示
	successTips: function(msg, options) {
		toastr.options = $.extend(toastrOptions, options);
		toastr.success(msg);
	},
	// 一般提示
	infoTips: function(msg, options) {
		toastr.options = $.extend(toastrOptions, options);
		toastr.info(msg);
	},
	// 警告提示，不自动消失，包含关闭按钮
	warnTips: function(msg, options) {
		toastr.options = $.extend(toastrOptions, options);
		toastr.options.timeOut = 0;
		toastr.options.closeButton = true;
		toastr.warning(msg);
	},
	// 错误提示，不自动消失，包含关闭按钮
	errorTips: function(msg, options) {
		toastr.options = $.extend(toastrOptions, options);
		toastr.options.timeOut = 0;
		toastr.options.closeButton = true;
		toastr.error(msg);
	},
	// 清空提示
	clearTips: function() {
		toastr.clear();
	}
});
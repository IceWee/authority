var OK = "200";

$(function() {
	$(document).ajaxStart(function() {
		$.loading()
	});
	$(document).ajaxStop(function() {
		$.loaded();
	});
	
	// session超时统一处理
	$(document).ajaxError(function(event, jqxhr, settings, exception) {
		 if (jqxhr.status == 401) {
			 if (window != top) {  
				 top.location.href = "/login?expired";  
			 } else {
				 window.location.href = "/login?expired";
			 }
		 }
	});
});

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
	"positionClass": "toast-top-center",
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

// blockUI封装
$.extend({
	// 加载中
	loading: function() {
		var message = "<div class='sk-spinner sk-spinner-fading-circle' style='float:left;display:inline;margin:20px'>";
		message += "<div class='sk-circle1 sk-circle'></div>";
		message += "<div class='sk-circle2 sk-circle'></div>";
		message += "<div class='sk-circle3 sk-circle'></div>";
		message += "<div class='sk-circle4 sk-circle'></div>";
		message += "<div class='sk-circle5 sk-circle'></div>";
		message += "<div class='sk-circle6 sk-circle'></div>";
		message += "<div class='sk-circle7 sk-circle'></div>";
		message += "<div class='sk-circle8 sk-circle'></div>";
		message += "<div class='sk-circle9 sk-circle'></div>";
		message += "<div class='sk-circle10 sk-circle'></div>";
		message += "<div class='sk-circle11 sk-circle'></div>";
		message += "<div class='sk-circle12 sk-circle'></div>";
		message += "</div>";
		message += "<div style='float:left;display:inline'><span style='line-height:60px'>" + $.i18n.prop("wait.waitting") + "</span></div>";
		$.blockUI({
			message: message,
			css: {
				width: "250px",
				left: "40%",
				textAlign: "center",
				color: "#000",
				border: "1px solid #aaa",
				backgroundColor: "#FFF",
				cursor: "wait"
			}
		});
	},
	// 加载完毕
	loaded: function() {
		$.unblockUI();
	}
});

// shake动画
$.extend({
	shake: function(selector) {
	    $(selector).addClass("animated");
	    $(selector).addClass("shake");
	    // 设置超时清除动画，否则无法进行下一次动画显示
	    setTimeout(function() {
		    $(selector).removeClass("shake");
	    }, 3000);
	}
});
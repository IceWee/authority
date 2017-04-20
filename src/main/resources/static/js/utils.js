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

// 本方法依赖jQuery.i18n，因此需要事先引入如：jquery.i18n.properties-min-1.0.9.js
// 解析浏览器locale进行返回
// 浏览器locale格式为：zh-CN，有些插件需要使用zh_CN，通过参数seprator自动替换
function getBrowserLocale(seprator) {
	var locale = jQuery.i18n.browserLang();
	if (seprator) {
		locale = locale = locale.replace("-", seprator);
	}
	return locale;
}

// 采用jquery easyui loading css效果
// 记载中
function ajaxLoading(msg) {   
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");   
    $("<div class=\"datagrid-mask-msg\" style=\"border:1px solid #8E8E8E;height:44px\"></div>").html(msg).appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});   
}

// 加载完毕
function ajaxLoaded() {   
     $(".datagrid-mask").remove();   
     $(".datagrid-mask-msg").remove();               
}

// 获取所给元素的HTML文本
function cloneSelf(id) {
	return $("<p>").append($("#" + id).clone()).html()
}

// 使用方法，在html中定义一个提示div，样式采用Bootstrap
// 显示提示信息，除msg外其他参数都为可选，但必须按顺序传递
// 举例：提示框id是"myTips"，那么type必须指定
// msg required
// type: success/info/warning/danger
function showTips(msg, type, timeout, id) {
	type = arguments[1] ? arguments[1] : "success";
	timeout = arguments[2] ? arguments[2] : 0;
	id = arguments[3] ? "#" + arguments[3] : "#tips";
	$this = $(id);
	$this.removeClass();
	$this.addClass("alert alert-" + type);
	$(id + " span:last-child").text(msg);
	$this.slideDown();
	if (timeout > 0) {
		$this.delay(timeout * 1000).slideUp();
	}
}

// 显示失败提示，提示用不消失
// msg required
// id optional
function showErrorTips(msg, id) {
	showTips(msg, "danger", 0, id);
}

// 显示成功提示，一般信息显示后消失
// msg required
// timeout optional，默认值：3秒
// id optional
function showSuccessTips(msg, timeout, id) {
	timeout = arguments[1] ? arguments[1] : 3;
	showTips(msg, "success", timeout, id);
}

// id optional
function hideTips(id) {
	id = arguments[0] ? "#" + arguments[0] : "#tips";
	$(id).slideUp();
}

// 清空输入条件
function resetForm(formId) {
	$("#" + formId).form("reset");
}
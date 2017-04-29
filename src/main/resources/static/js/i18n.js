// 国际化，如果未制定locale则采用浏览器报告的语言编码
function i18n(locale, name) {
	var browserLocale = getBrowserLocale("_");
	locale = arguments[0] ? arguments[0] : browserLocale;
	name = arguments[1] ? arguments[1] : "i18n";
	jQuery.i18n.properties({
		path: "/i18n/",
	    name: name,
	    mode: "map",
	    language: locale,
		encoding : "UTF-8"
	});
}

//本方法依赖jQuery.i18n，因此需要事先引入如：jquery.i18n.properties-min-1.0.9.js
//解析浏览器locale进行返回
//浏览器locale格式为：zh-CN，有些插件需要使用zh_CN，通过参数seprator自动替换
function getBrowserLocale(seprator) {
	var locale = jQuery.i18n.browserLang();
	if (seprator) {
		locale = locale = locale.replace("-", seprator);
	}
	return locale;
}
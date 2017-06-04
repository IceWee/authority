/*************************固定常量 begin***********************/
// 固定常量，必须这样命名，否则curd.js无法正常运行
var URI_AJAX_LIST = "/ajax/system/log/list";
var URI_LIST = "/system/log/list"; // 列表页面URI
var FORM_ID_LIST = "form_search"; // 列表页表单ID
/*************************固定常量 end*************************/
function initListPage() {
	$("#container").btable({url: URI_AJAX_LIST});
}

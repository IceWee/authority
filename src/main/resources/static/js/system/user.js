/*************************固定常量 begin***********************/
// 固定常量，必须这样命名，否则curd.js无法正常运行
var URI_AJAX_LIST = "/ajax/system/user/list";
var URI_LIST = "/system/user/list"; // 列表页面URI
var URI_ADD = "/system/user/add"; // 新增页面URI
var URI_SAVE = "/system/user/save"; // 保存URI
var URI_EDIT = "/system/user/edit"; // 编辑页面URI
var URI_UPDATE = "/system/user/update"; // 更新URI
var URI_DELETE = "/system/user/delete"; // 删除URI
var FORM_ID_DETAIL = "form_detail"; // 详情页（新增或编辑）表单ID
var FORM_ID_LIST = "form_search"; // 列表页表单ID
var BTN_ADD_ID = "button_add"; // 增加按钮ID
var BTN_SAVE_ID = "button_save"; // 保存按钮ID
var BTN_BACK_ID = "button_back"; // 返回按钮ID
var HIDDEN_ID = "id"; // 隐藏域主键ID属性
/*************************固定常量 end*************************/

// 扩展操作按钮
function operationFormatterExt(value, row, index) {
	var html = operationFormatter(value, row, index);
	var id = row.id;
	var label_auth = $.i18n.prop("operate.authorize");
	var label_lock = $.i18n.prop("operate.lock");
	var label_unlock = $.i18n.prop("operate.unlock");
	if (id) {
		html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openUserRoleAuth('" + row.id + "', '" + row.name + "')\"><span class=\"label label-warning\">" + label_auth + "</span></a>";
		if (row.status == 1) {
			html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"unlockUser('" + row.id + "')\"><span class=\"label label-success\">" + label_unlock + "</span></a>";
		} else {
			html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"lockUser('" + row.id + "')\"><span class=\"label label-default\">" + label_lock + "</span></a>";
		}
	}
	return html;
}

// 新增页面初始化
function initAddPageExt(error, message) {
	initAddPage(error, message);
	
	$("#roleIds").chosen({
		width: "100%",
		disable_search_threshold: true, 
		allow_single_deselect: true, 
		no_results_text: $.i18n.prop("result.nomatch")
	});
}

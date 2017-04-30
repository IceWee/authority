var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_hidden"; // 隐藏表单ID
var URI_AJAX_LIST = "/ajax/system/users"; // 列表页获取列表数据异步URI
var URI_LIST = "/system/user/list"; // 列表页面URI
var URI_ADD = "/system/user/add"; // 新增页面URI
var URI_SAVE = "/system/user/save"; // 保存URI
var URI_EDIT = "/system/user/edit"; // 编辑页面URI
var URI_UPDATE = "/system/user/update"; // 更新URI
var URI_DELETE = "/system/user/delete"; // 删除URI
var BTN_ADD_ID = "#button_add"; // 增加按钮ID
var BTN_SAVE_ID = "#button_save"; // 保存按钮ID
var BTN_UPDATE_ID = "#button_update"; // 保存按钮ID
var BTN_BACK_ID = "#button_back"; // 返回按钮ID
var PARAM_ID = "#id"; // ID属性名

function initEditPageExt() {
	readonlyColor("username");
	readonlyColor("createUser");
	readonlyColor("createDate");
}
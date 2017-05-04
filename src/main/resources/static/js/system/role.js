// 固定常量，必须这样命名，否则crud.js无法正常运行
var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_hidden"; // 隐藏表单ID
var URI_AJAX_LIST = "/ajax/system/roles"; // 列表页获取列表数据异步URI
var URI_LIST = "/system/role/list"; // 列表页面URI
var URI_ADD = "/system/role/add"; // 新增页面URI
var URI_SAVE = "/system/role/save"; // 保存URI
var URI_EDIT = "/system/role/edit"; // 编辑页面URI
var URI_UPDATE = "/system/role/update"; // 更新URI
var URI_DELETE = "/system/role/delete"; // 删除URI
var BTN_ADD_ID = "#button_add"; // 增加按钮ID
var BTN_SAVE_ID = "#button_save"; // 保存按钮ID
var BTN_UPDATE_ID = "#button_update"; // 保存按钮ID
var BTN_BACK_ID = "#button_back"; // 返回按钮ID
var PARAM_ID = "#id"; // ID属性名
//自定义常量
var URI_AJAX_CATEGORY_TREE = "/ajax/system/roles"; // 列表页获取列表数据异步URI

//列表页面初始化
function initListPageExt(error, message) {
	initListPage(error, message);
}

//编辑页面初始化扩展
function initEditPageExt(error, message) {
	initEditPage(error, message)
	readonlyColor("code");
}
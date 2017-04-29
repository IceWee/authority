var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_hidden"; // 隐藏表单ID
var URI_LIST = "/system/user/list"; // 列表页面URI
var URI_AJAX_LIST = "/ajax/system/users"; // 列表页获取列表数据异步URI
var URI_AJAX_SAVE = "/ajax/system/users"; // ajax保存
var ADD_URI = "/system/user/add"; // 新增页面URI
var EDIT_URI = "/system/user/edit"; // 编辑页面URI
var DELETE_URI = "/system/user/delete"; // 删除URI
var BTN_ADD_ID = "#button_add"; // 增加按钮ID
var BTN_SAVE_ID = "#button_save"; // 保存按钮ID
var BTN_BACK_ID = "#button_back"; // 返回按钮ID
var PARAM_ID = "#id"; // ID属性名

//返回列表页
function back2list() {
	$(FORM_ID_DETAIL).attr("action", URI_LIST);
	$(FORM_ID_DETAIL).submit();
}

// 显示错误或消息提示
function showErrorOrMessage(error, message) {
	// 优先显示错误信息
	if (error) {
		showErrorTips(error);
	} else {
		if (message) {
			showSuccessTips(message);
		}
	}
}

/*************************** list begin *****************************/
// 初始化列表页面
function initListPage(error, message) {
	i18n();
	
	initDatagrid({url:URI_AJAX_LIST,autoLoad:true});
	
	showErrorOrMessage(error, message);
	
	// 跳转到新增页面
	$(BTN_ADD_ID).click(function() {
		add();
	});
}

// 进入新增页
function add() {
	$(FORM_ID_HIDDEN).attr("action", ADD_URI);
	$(FORM_ID_HIDDEN).submit();
}

// 进入编辑页
function edit(id) {
	$(PARAM_ID).val(id);
	$(FORM_ID_HIDDEN).attr("action", EDIT_URI);
	$(FORM_ID_HIDDEN).submit();
}

// 在datagrid的操作列中生成操作按钮
function operateBtnHtml(value, row, index) {
	var id = row.id;
	var html = "";
	if (id) {
		html = html + "<a href=\"#\" onclick=\"edit('" + id + "')\"><span class=\"label label-primary\">编辑</span></a>";  
		html += "&nbsp;&nbsp;";
		html = html + "<a href=\"#\" onclick=\"_delete('" + id + "')\"><span class=\"label label-danger\">删除</span></a>";  
	}
	return html;
}
/*************************** list end *****************************/

/*************************** add begin *****************************/
// 初始化新增页面
function initAddPage(error, message) {
	i18n();
	
	showErrorOrMessage(error, message);
	
	// 保存
	$(BTN_SAVE_ID).click(function() {
		if ($(FORM_ID_DETAIL).form("validate")) {
			ajaxLoading($.i18n.prop("save.saving"));
			$(FORM_ID_DETAIL).submit();
		}
	});
	
	// 返回
	$(BTN_BACK_ID).click(function() {
		$(FORM_ID_DETAIL).attr("action", URI_LIST);
		$(FORM_ID_DETAIL).submit();
	});
}
/*************************** add end *****************************/

/*************************** edit begin *****************************/
// 初始化编辑页面
function initEditPage(error, message) {
	i18n();
	
	readonlyColor("username");
	readonlyColor("createUser");
	readonlyColor("createDate");
	
	showErrorOrMessage(error, message);
	
	// 保存
	$(BTN_SAVE_ID).click(function() {
		if ($(FORM_ID_DETAIL).form("validate")) {
			ajaxLoading($.i18n.prop("update.updating"));
			$(FORM_ID_DETAIL).submit();
		}
	});
	
	// 返回
	$(BTN_BACK_ID).click(function() {
		$(FORM_ID_DETAIL).attr("action", URI_LIST);
		$(FORM_ID_DETAIL).submit();
	});
}
/*************************** edit end *****************************/

/*************************** remove start *****************************/
// 删除
function _delete(id) {
	$.messager.confirm($.i18n.prop("tip.info"), $.i18n.prop("delete.prompt"), function(go){
		if (go){
			$(PARAM_ID).val(id);
			$(FORM_ID_HIDDEN).attr("action", DELETE_URI);
			$(FORM_ID_HIDDEN).submit();
		}
	});
	
}
/*************************** remove end *****************************/


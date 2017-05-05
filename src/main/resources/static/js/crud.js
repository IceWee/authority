// 在datagrid的操作列中生成操作按钮
function operateBtnHtml(value, row, index) {
	var id = row.id;
	var html = "";
	var label_edit = $.i18n .prop("operate.edit");
	var label_delete = $.i18n .prop("operate.delete");
	if (id) {
		html = html + "<a href=\"#\" onclick=\"_edit('" + id + "')\"><span class=\"label label-primary\">" + label_edit + "</span></a>";  
		html += "&nbsp;&nbsp;";
		html = html + "<a href=\"#\" onclick=\"_delete('" + id + "')\"><span class=\"label label-danger\">" + label_delete + "</span></a>";  
	}
	return html;
}

// 自定义行样式
function statusRowStyler(value, row, index) {
	var html = "";
	if (row.status == 1) { // 锁定
		html = "background-color:#FEE38E;color:red";
	}
	return html;
}

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
function initListPage(error, message, autoLoad, autoResize) {
	autoLoad = arguments[2] != undefined ? arguments[2] : true;
	autoResize = arguments[3] != undefined ? arguments[3] : true;
	initDatagrid({url:URI_AJAX_LIST,autoLoad:autoLoad,rowStyler:statusRowStyler,autoResize:autoResize});
	
	showErrorOrMessage(error, message);
	
	// 跳转到新增页面
	$(BTN_ADD_ID).click(function() {
		_add();
	});
}

// 进入新增页
function _add() {
	$(FORM_ID_HIDDEN).attr("action", URI_ADD);
	$(FORM_ID_HIDDEN).submit();
}

// 进入编辑页
function _edit(id) {
	$(PARAM_ID).val(id);
	$(FORM_ID_HIDDEN).attr("action", URI_EDIT);
	$(FORM_ID_HIDDEN).submit();
}
/*************************** list end *****************************/

/*************************** add begin *****************************/
// 初始化新增页面
function initAddPage(error, message) {
	showErrorOrMessage(error, message);
	
	// 保存
	$(BTN_SAVE_ID).click(function() {
		if ($(FORM_ID_DETAIL).form("validate")) {
			ajaxLoading($.i18n.prop("save.saving"));
			$(FORM_ID_DETAIL).attr("action", URI_SAVE);
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
	showErrorOrMessage(error, message);
	
	// 保存
	$(BTN_UPDATE_ID).click(function() {
		if ($(FORM_ID_DETAIL).form("validate")) {
			ajaxLoading($.i18n.prop("update.updating"));
			$(FORM_ID_DETAIL).attr("action", URI_UPDATE);
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
			$(FORM_ID_HIDDEN).attr("action", URI_DELETE);
			$(FORM_ID_HIDDEN).submit();
		}
	});
	
}
/*************************** remove end *****************************/


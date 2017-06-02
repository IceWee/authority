// 通用增删查改操作封装

// 显示错误或消息提示
function showErrorOrMessage(error, message) {
	// 优先显示错误信息
	if (error) {
		$.errorTips(error);
	} else {
		if (message) {
			$.successTips(message);
		}
	}
}

// 默认行样式处理
function rowStyle(row, index) {
	if (row.status && row.status == 1) { // 锁定
		return {classes: "info"};
	}
	return {};
}

/*************************** list begin *****************************/
// 初始化列表页面
function initListPage(error, message) {
	$("#container").btable({url: URI_AJAX_LIST});
	showErrorOrMessage(error, message);
	// 跳转到新增页面
	$("#" + BTN_ADD_ID).click(function() {
		_add();
	});
}

// 操作按钮
function operationFormatter(value, row, index) {
	var id = row.id;
	var html = "";
	var label_edit = $.i18n.prop("operate.edit");
	var label_delete = $.i18n.prop("operate.delete");
	if (id) {
		html += "<a href=\"#\" onclick=\"_edit('" + id + "')\"><span class=\"label label-primary\">" + label_edit + "</span></a>";  
		html += "&nbsp;&nbsp;";
		html += "<a href=\"#\" onclick=\"_delete('" + id + "')\"><span class=\"label label-danger\">" + label_delete + "</span></a>";  
	}
	return html;
}

// 进入新增页
function _add() {
	$("#" + FORM_ID_LIST).attr("action", URI_ADD);
	$("#" + FORM_ID_LIST).submit();
}

// 进入编辑页
function _edit(id) {
	$("#" + HIDDEN_ID).val(id);
	$("#" + FORM_ID_LIST).attr("action", URI_EDIT);
	$("#" + FORM_ID_LIST).submit();
}

// 删除
function _delete(id) {
	parent.layer.confirm($.i18n.prop("delete.prompt"), {
		title: $.i18n.prop("tip.info"),
		closeBtn: 0, // 不显示关闭按钮
		shadeClose: true, // 开启遮罩关闭
		skin: "layui-layer-molv", // 样式类名
	    btn: ["确定", "取消"] //按钮
	}, function(){
		parent.layer.closeAll();
		$.loading();
		$("#" + HIDDEN_ID).val(id);
		$("#" + FORM_ID_LIST).attr("action", URI_DELETE);
		$("#" + FORM_ID_LIST).submit();
	}, function(){
		parent.layer.closeAll();
	});
}
/*************************** list end *****************************/

/*************************** add begin *****************************/
//初始化新增页面
function initAddPage(error, message) {
	showErrorOrMessage(error, message);
	
	// 保存
	$("#" + BTN_SAVE_ID).click(function() {
		if ($("#" + FORM_ID_DETAIL).valid()) {
			$.loading();
			$("#" + FORM_ID_DETAIL).attr("action", URI_SAVE);
			$("#" + FORM_ID_DETAIL).submit();
		}
	});
	
	// 返回
	$("#" + BTN_BACK_ID).click(function() {
		$("#" + FORM_ID_DETAIL)[0].reset();
		$("#" + FORM_ID_DETAIL).validate().settings.ignore = "*"; // 取消时需要把验证清空，否则无法提交表单
		$("#" + FORM_ID_DETAIL).attr("action", URI_LIST);
		$("#" + FORM_ID_DETAIL).submit();
	});
}
/*************************** add end *****************************/

/*************************** edit begin *****************************/
//初始化编辑页面
function initEditPage(error, message) {
	showErrorOrMessage(error, message);
	
	// 保存
	$("#" + BTN_SAVE_ID).click(function() {
		if ($("#" + FORM_ID_DETAIL).valid()) {
			$.loading();
			$("#" + FORM_ID_DETAIL).attr("action", URI_UPDATE);
			$("#" + FORM_ID_DETAIL).submit();
		}
	});
	
	// 返回
	$("#" + BTN_BACK_ID).click(function() {
		$("#" + FORM_ID_DETAIL)[0].reset();
		$("#" + FORM_ID_DETAIL).validate().settings.ignore = "*"; // 取消时需要把验证清空，否则无法提交表单
		$("#" + FORM_ID_DETAIL).attr("action", URI_LIST);
		$("#" + FORM_ID_DETAIL).submit();
	});
}
/*************************** edit end *****************************/


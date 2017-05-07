// 固定常量，必须这样命名，否则crud.js无法正常运行
var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_LIST = "#form_list"; // 列表页表单ID
var URI_AJAX_LIST = "/ajax/system/user/list";
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
// 自定义常量
var URI_LOCK = "/system/user/lock"; // 锁定用户
var URI_UNLOCK = "/system/user/unlock"; // 解除锁定用户
var URI_AJAX_ROLE_LIST = "/ajax/system/role/list";
var URI_AJAX_ROLE_SAVE = "/ajax/system/role/save";
var URI_AJAX_USER_PASSWORD = "/ajax/system/user/password"
var URI_AJAX_USER_UPDATE = "/ajax/system/user/update"

// 列表页面初始化
function initListPageExt(error, message) {
	initListPage(error, message);
}

// 操作按钮扩展
function operateBtnHtmlExt(value, row, index) {
	var html = operateBtnHtml(value, row, index);
	var label_auth = $.i18n.prop("operate.authorize");
	var label_lock = $.i18n.prop("operate.lock");
	var label_unlock = $.i18n.prop("operate.unlock");
	html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openUserRoleAuth('" + row.id + "', '" + row.name + "')\"><span class=\"label label-warning\">" + label_auth + "</span></a>";
	if (row.status == 1) {
		html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"unlockUser('" + row.id + "')\"><span class=\"label label-success\">" + label_unlock + "</span></a>";
	} else {
		html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"lockUser('" + row.id + "')\"><span class=\"label label-default\">" + label_lock + "</span></a>";
	}
	return html;
}

// 用户角色授权
function openUserRoleAuth(userId, name) {
	var tipsId = "_tips_lr_list_box";
	$.ajax({
		type : "GET",
		url : URI_AJAX_ROLE_LIST + "/" + userId,
		dataType : "json",
		success : function(json) {
			if (json.code == CODE_OK) {
				if (json.data) {
					openLRListBoxDialog({
						title: name,
						leftTitle: $.i18n .prop("role.unselect"),
						rightTitle: $.i18n .prop("role.selected"),
						valueField: "id",
					    textField: "name",
					    leftList: json.data.unselectRoles,
					    rightList: json.data.selectedRoles,
					    saveCallback: function(checkedRows) {
					    	saveUserRoleAuth(userId, checkedRows);
					    }
					});
				}
			} else {
				showErrorTips(json.message, tipsId);
			}
		},
		error : function() {
			showErrorTips($.i18n.prop("http.request.failed"), tipsId);
		}
	});
}

// 保存用户角色授权
function saveUserRoleAuth(userId, checkedRows) {
	var roleIdArray = [];
	for (var i = 0; i < checkedRows.length; i++) {
		roleIdArray.push(checkedRows[i].id);
	}
	var data = {userId: userId, roleIds: roleIdArray};
	var tipsId = "_tips_lr_list_box";
	$.ajax({
		type : "POST",
		url : URI_AJAX_ROLE_SAVE,
		dataType : "json",
		data : data,
		traditional: true,
		success : function(json) {
			var code = json.code;
			if (code == CODE_OK) {
				showSuccessTips($.i18n.prop("save.success"), 3, tipsId);
			} else {
				var msg = json.message;
				showErrorTips(msg, tipsId);
			}
		},
		error : function() {
			showErrorTips($.i18n.prop("http.request.failed"), tipsId);
		}
	});
}

// 锁定用户
function lockUser(userId) {
	$.messager.confirm($.i18n.prop("tip.info"), $.i18n.prop("lock.prompt"), function(go){
		if (go){
			$(PARAM_ID).val(userId);
			$(FORM_ID_LIST).attr("action", URI_LOCK);
			$(FORM_ID_LIST).submit();
		}
	});
}

// 解除锁定用户
function unlockUser(userId) {
	$.messager.confirm($.i18n.prop("tip.info"), $.i18n.prop("unlock.prompt"), function(go){
		if (go){
			$(PARAM_ID).val(userId);
			$(FORM_ID_LIST).attr("action", URI_UNLOCK);
			$(FORM_ID_LIST).submit();
		}
	});
}

// 新增页面初始化
function initAddPageExt(error, message) {
	initAddPage(error, message);
	
	$("#roleIds").chosen({
		disable_search_threshold: true, 
		allow_single_deselect: true, 
		no_results_text: $.i18n.prop("result.nomatch"),
		width: "200px"
	});
}

// 编辑页面初始化
function initEditPageExt(error, message) {
	initEditPage(error, message)
	readonlyColor("username");
	readonlyColor("createUser");
	readonlyColor("createDate");
	
	$("#roleIds").chosen({
		disable_search_threshold: true, 
		allow_single_deselect: true, 
		no_results_text: $.i18n.prop("result.nomatch"),
		width: "200px"
	});
}

// 修改密码页面初始化
function initPasswordPage(error, message) {
	showErrorOrMessage(error, message);
	
	// 保存
	$(BTN_SAVE_ID).click(function() {
		var userId = $("#userId").val();
		if ($(FORM_ID_DETAIL).form("validate")) {
			ajaxLoading($.i18n.prop("save.saving"));
			var data = $(FORM_ID_DETAIL).serialize();
			$.ajax({
				type : "PUT",
				url : URI_AJAX_USER_PASSWORD + "/" + userId,
				data : data,
				dataType : "json",
				success : function(json) {
					ajaxLoaded();
					if (json.code == CODE_OK) {
						showSuccessTips($.i18n.prop("save.success"));
					} else {
						showErrorTips(json.message);
					}
				},
				error : function() {
					ajaxLoaded();
					showErrorTips($.i18n.prop("http.request.failed"));
				}
			});
		}
	});
}

// 我的信息页面初始化
function initMinePage(error, message) {
	showErrorOrMessage(error, message);
	
	readonlyColor("username");
	
	// 保存
	$(BTN_SAVE_ID).click(function() {
		var userId = $("#userId").val();
		if ($(FORM_ID_DETAIL).form("validate")) {
			ajaxLoading($.i18n.prop("update.updating"));
			var data = $(FORM_ID_DETAIL).serialize();
			$.ajax({
				type : "PUT",
				url : URI_AJAX_USER_UPDATE + "/" + userId,
				data : data,
				dataType : "json",
				success : function(json) {
					ajaxLoaded();
					if (json.code == CODE_OK) {
						showSuccessTips($.i18n.prop("update.success"));
					} else {
						showErrorTips(json.message);
					}
				},
				error : function() {
					ajaxLoaded();
					showErrorTips($.i18n.prop("http.request.failed"));
				}
			});
		}
	});
}
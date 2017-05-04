// 固定常量，必须这样命名，否则crud.js无法正常运行
var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_hidden"; // 隐藏表单ID
var URI_AJAX_LIST = "/ajax/system/users";
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
var URI_AJAX_ROLE_LIST = "/ajax/system/roles";
var URI_AJAX_USER_ROLE = "/ajax/system/users/roles";

// 列表页面初始化
function initListPageExt(error, message) {
	initListPage(error, message);
}

// 操作按钮扩展
function operateBtnHtmlExt(value, row, index) {
	var html = operateBtnHtml(value, row, index);
	var label_auth = $.i18n .prop("operate.authorize");
	html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openUserRoleAuth('" + row.id + "')\"><span class=\"label label-warning\">" + label_auth + "</span></a>";
	return html;
}

// 用户角色授权
function openUserRoleAuth(userId) {
	$("#userId").val(userId);
	var tipsId = "_tips_lr_list_box";
	$.ajax({
		type : "GET",
		url : URI_AJAX_ROLE_LIST + "/" + userId,
		dataType : "json",
		success : function(json) {
			if (json.code === "200") {
				if (json.data) {
					openLRListBoxDialog({
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
			showErrorTips($.i18n .prop("http.request.failed"), tipsId);
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
		url : URI_AJAX_USER_ROLE,
		dataType : "json",
		data : data,
		traditional: true,
		success : function(json) {
			var code = json.code;
			if (code === "200") {
				showSuccessTips($.i18n.prop("save.success"), 3, tipsId);
			} else {
				var msg = json.message;
				showErrorTips(msg, tipsId);
			}
		},
		error : function() {
			showErrorTips($.i18n .prop("http.request.failed"), tipsId);
		}
	});
}

// 编辑页面初始化
function initEditPageExt(error, message) {
	initEditPage(error, message)
	readonlyColor("username");
	readonlyColor("createUser");
	readonlyColor("createDate");
}
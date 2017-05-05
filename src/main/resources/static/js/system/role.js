// 固定常量，必须这样命名，否则crud.js无法正常运行
var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_LIST = "#form_list"; // 列表页表单ID
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
var URI_AJAX_USER_LIST = "/ajax/system/users"; // 获取用户列表
var URI_AJAX_ROLE_USER = "/ajax/system/roles/users"; // 保存角色用户关系

//列表页面初始化
function initListPageExt(error, message) {
	initListPage(error, message);
}

//操作按钮扩展
function operateBtnHtmlExt(value, row, index) {
	var html = operateBtnHtml(value, row, index);
	var label_user = $.i18n.prop("operate.authorize.user");
	var label_res = $.i18n.prop("operate.authorize.resource");
	html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openConfigUser('" + row.id + "', '" + row.name + "')\"><span class=\"label label-warning\">" + label_user + "</span></a>";
	html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openConfigRes('" + row.id + "', '" + row.name + "')\"><span class=\"label label-success\">" + label_res + "</span></a>";
	return html;
}

// 配置资源
function openConfigRes(roleId, roleName) {
	
}

// 配置用户
function openConfigUser(roleId, roleName) {
	$("#roleId").val(roleId);
	var tipsId = "_tips_lr_list_box";
	$.ajax({
		type : "GET",
		url : URI_AJAX_USER_LIST + "/" + roleId,
		dataType : "json",
		success : function(json) {
			if (json.code === "200") {
				if (json.data) {
					openLRListBoxDialog({
						title: roleName,
						leftTitle: $.i18n .prop("user.unselect"),
						rightTitle: $.i18n .prop("user.selected"),
						valueField: "id",
					    textField: "name",
					    leftList: json.data.unselectUsers,
					    rightList: json.data.selectedUsers,
					    saveCallback: function(checkedRows) {
					    	saveRoleUsers(roleId, checkedRows);
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

// 保存角色用户授权
function saveRoleUsers(roleId, checkedRows) {
	var userIdArray = [];
	for (var i = 0; i < checkedRows.length; i++) {
		userIdArray.push(checkedRows[i].id);
	}
	var data = {roleId: roleId, userIds: userIdArray};
	var tipsId = "_tips_lr_list_box";
	$.ajax({
		type : "POST",
		url : URI_AJAX_ROLE_USER,
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
			showErrorTips($.i18n.prop("http.request.failed"), tipsId);
		}
	});
}

//编辑页面初始化扩展
function initEditPageExt(error, message) {
	initEditPage(error, message)
	readonlyColor("code");
}
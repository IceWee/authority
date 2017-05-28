/*************************固定常量 begin***********************/
//固定常量，必须这样命名，否则curd.js无法正常运行
var URI_AJAX_LIST = "/ajax/system/role/list";
var URI_LIST = "/system/role/list"; // 列表页面URI
var URI_ADD = "/system/role/add"; // 新增页面URI
var URI_SAVE = "/system/role/save"; // 保存URI
var URI_EDIT = "/system/role/edit"; // 编辑页面URI
var URI_UPDATE = "/system/role/update"; // 更新URI
var URI_DELETE = "/system/role/delete"; // 删除URI
var FORM_ID_DETAIL = "form_detail"; // 详情页（新增或编辑）表单ID
var FORM_ID_LIST = "form_search"; // 列表页表单ID
var BTN_ADD_ID = "button_add"; // 增加按钮ID
var BTN_SAVE_ID = "button_save"; // 保存按钮ID
var BTN_BACK_ID = "button_back"; // 返回按钮ID
var HIDDEN_ID = "id"; // 隐藏域主键ID属性
/*************************固定常量 end*************************/
//自定义常量
var URI_AJAX_USER_LIST = "/ajax/system/user/list"; // 获取用户列表
var URI_AJAX_USER_SAVE = "/ajax/system/user/save"; // 保存角色用户关系
var URI_AJAX_RESOURCE_TREE = "/ajax/system/resource/tree"; // 资源树
var URI_AJAX_RESOURCE_SAVE = "/ajax/system/resource/save"; // 保存角色资源关系

//操作按钮扩展
function operationFormatterExt(value, row, index) {
	var html = operationFormatter(value, row, index);
	var id = row.id;
	var label_user = $.i18n.prop("operate.authorize.user");
	var label_res = $.i18n.prop("operate.authorize.resource");
	if (id) {
		html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openConfigUser('" + id + "', '" + row.name + "')\"><span class=\"label label-warning\">" + label_user + "</span></a>";
		html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openConfigRes('" + id + "', '" + row.name + "')\"><span class=\"label label-success\">" + label_res + "</span></a>";
	}
	return html;
}

// 配置资源
function openConfigRes(roleId, roleName) {
	// 资源树
	$("#_dialog_ztree").dialogzTree({
		title: roleName,
		showAtonce: true,
		url: URI_AJAX_RESOURCE_TREE + "/" + roleId,
		confirmCallback: function(checkedNodes) {
			saveRoleResources(roleId, checkedNodes);
		}
	});
}

// 保存角色资源授权
// 注意：所选节点中需要区分资源分类和资源
function saveRoleResources(roleId, checkedNodes) {
	var RESOURCE_TYPE = 1;
	var resourceIdArray = [];
	var resourceId, type;
	$.each(checkedNodes, function(i, node){
		type = node.nodeType;
		resourceId = node.rid;
		if (RESOURCE_TYPE == type) {
			resourceIdArray.push(resourceId);
		}
	});
	var data = {roleId: roleId, resourceIds: resourceIdArray};
	$.ajax({
		type : "POST",
		url : URI_AJAX_RESOURCE_SAVE,
		dataType : "json",
		data : data,
		traditional: true,
		success : function(json) {
			var code = json.code;
			if (code == OK) {
				$.successTips($.i18n.prop("save.success"));
			} else {
				$.erorTips(json.message);
			}
		},
		error : function() {
			$.erorTips($.i18n.prop("http.request.failed"));
		}
	});
}

// 配置用户
function openConfigUser(roleId, roleName) {
	$.ajax({
		type : "GET",
		url : URI_AJAX_USER_LIST + "/" + roleId,
		dataType : "json",
		success : function(json) {
			if (json.code == OK) {
				$("#dialog_select_box").selectBox({
					title: roleName,
					leftTitle: $.i18n.prop("user.unselect"),
					rightTitle: $.i18n.prop("user.selected"),
					idField: "id",
				    textField: "name",
				    showText: $.i18n.prop("user.name"),
				    leftList: json.data.unselectUsers,
				    rightList: json.data.selectedUsers,
				    saveCallback: function(checkedRows) {
				    	saveRoleUsers(roleId, checkedRows);
				    }
				});
			} else {
				$.errorTips(json.message);
			}
		},
		error : function() {
			$.errorTips($.i18n.prop("http.request.failed"));
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
	$.ajax({
		type : "POST",
		url : URI_AJAX_USER_SAVE,
		dataType : "json",
		data : data,
		traditional: true,
		success : function(json) {
			if (json.code == OK) {
				$.successTips($.i18n.prop("save.success"));
			} else {
				$.errorTips(json.message);
			}
		},
		error : function() {
			$.errorTips($.i18n.prop("http.request.failed"));
		}
	});
}
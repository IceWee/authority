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
//自定义常量
var URI_LOCK = "/system/user/lock"; // 锁定用户
var URI_UNLOCK = "/system/user/unlock"; // 解除锁定用户
var URI_AJAX_ROLE_LIST = "/ajax/system/role/list"; // 获取用户已选/未选角色列表
var URI_AJAX_ROLE_SAVE = "/ajax/system/role/save"; // 保存用户角色授权
var URI_EXPORT = "/system/user/export"; // 导出URI

// 初始化列表页面
function initListPageExt(error, message) {
	initListPage(error, message);
	// 导出
	$("#button_export").click(function() {
		$("#" + FORM_ID_LIST).attr("action", URI_EXPORT);
		$("#" + FORM_ID_LIST).submit();
	});
}

// 扩展操作按钮
function operationFormatterExt(value, row, index) {
	var html = operationFormatter(value, row, index);
	var id = row.id;
	var label_auth = $.i18n.prop("operate.authorize");
	var label_lock = $.i18n.prop("operate.lock");
	var label_unlock = $.i18n.prop("operate.unlock");
	var label_resetPasswd = $.i18n.prop("password.reset");
	if (id) {
		html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openUserRoleAuth('" + id + "', '" + row.name + "')\"><span class=\"label label-warning\">" + label_auth + "</span></a>";
		if (row.status == 1) {
			html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"unlockUser('" + id + "')\"><span class=\"label label-success\">" + label_unlock + "</span></a>";
		} else {
			html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"lockUser('" + id + "')\"><span class=\"label label-default\">" + label_lock + "</span></a>";
		}
		html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"resetPasswd('" + id + "')\"><span class=\"label label-info\">" + label_resetPasswd + "</span></a>";
	}
	return html;
}

// 添加页面初始化
function initAddPageExt(error, message) {
	initAddPage(error, message);
	// 角色多选
	$("#roleIds").chosen({
		width: "100%",
		disable_search_threshold: true, 
		allow_single_deselect: true, 
		no_results_text: $.i18n.prop("result.nomatch")
	});
}

// 编辑页面初始化
function initEditPageExt(error, message) {
	initEditPage(error, message);
	// 角色多选
	$("#roleIds").chosen({
		width: "100%",
		disable_search_threshold: true, 
		allow_single_deselect: true, 
		no_results_text: $.i18n.prop("result.nomatch")
	});
}

// 锁定用户
function lockUser(userId) {
	var label_confirm = $.i18n.prop("operate.confirm");
	var label_cancel = $.i18n.prop("operate.cancel");
	parent.layer.confirm($.i18n.prop("lock.prompt"), {
		title: $.i18n.prop("tip.info"),
		closeBtn: 0, // 不显示关闭按钮
		shadeClose: true, // 开启遮罩关闭
		skin: "layui-layer-molv", // 样式类名
	    btn: [label_confirm, label_cancel] //按钮
	}, function(){
		parent.layer.closeAll();
		$.loading();
		$("#" + HIDDEN_ID).val(userId);
		$("#" + FORM_ID_LIST).attr("action", URI_LOCK);
		$("#" + FORM_ID_LIST).submit();
	}, function(){
		parent.layer.closeAll();
	});
}

// 解除锁定用户
function unlockUser(userId) {
	var label_confirm = $.i18n.prop("operate.confirm");
	var label_cancel = $.i18n.prop("operate.cancel");
	parent.layer.confirm($.i18n.prop("unlock.prompt"), {
		title: $.i18n.prop("tip.info"),
		closeBtn: 0, // 不显示关闭按钮
		shadeClose: true, // 开启遮罩关闭
		skin: "layui-layer-molv", // 样式类名
	    btn: [label_confirm, label_cancel] //按钮
	}, function(){
		parent.layer.closeAll();
		$.loading();
		$("#" + HIDDEN_ID).val(userId);
		$("#" + FORM_ID_LIST).attr("action", URI_UNLOCK);
		$("#" + FORM_ID_LIST).submit();
	}, function(){
		parent.layer.closeAll();
	});
}

//用户角色授权
function openUserRoleAuth(userId, name) {
	$.ajax({
		type : "GET",
		url : URI_AJAX_ROLE_LIST + "/" + userId,
		dataType : "json",
		success : function(json) {
			if (json.code == OK) {
				$("#dialog_select_box").selectBox({
					title: name,
					leftTitle: $.i18n.prop("role.unselect"),
					rightTitle: $.i18n.prop("role.selected"),
					idField: "id",
				    textField: "name",
				    showText: $.i18n.prop("role.name"),
				    leftList: json.data.unselectRoles,
				    rightList: json.data.selectedRoles,
				    saveCallback: function(checkedRows) {
				    	saveUserRoleAuth(userId, checkedRows);
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

// 保存用户角色授权
function saveUserRoleAuth(userId, checkedRows) {
	var roleIdArray = [];
	for (var i = 0; i < checkedRows.length; i++) {
		roleIdArray.push(checkedRows[i].id);
	}
	var data = {userId: userId, roleIds: roleIdArray};
	$.ajax({
		type : "POST",
		url : URI_AJAX_ROLE_SAVE,
		dataType : "json",
		data : data,
		traditional: true,
		success : function(json) {
			if (json.code == OK) {
				$.successTips($.i18n.prop("save.success"));
			} else {
				var msg = json.message;
				$.errorTips(msg);
			}
		},
		error : function() {
			$.errorTips($.i18n.prop("http.request.failed"));
		}
	});
}

// 重置密码
function resetPasswd(userId) {
	var url = "/ajax/system/user/resetPassword";
	$("#form_resetPassword")[0].reset();
	$("#dialog_resetPassword").modal({keyboard:false});
	$("#button_rstpassword_cancel").off();
	$("#button_rstpassword_confirm").off();
	// 取消
	$("#button_rstpassword_cancel").on("click", function() {
		$("#dialog_resetPassword").modal("hide");
	});
	// 确定
	var label_confirm = $.i18n.prop("operate.confirm");
	var label_cancel = $.i18n.prop("operate.cancel");
	$("#button_rstpassword_confirm").on("click", function() {
		if ($("#form_resetPassword").valid()) {
			parent.layer.confirm($.i18n.prop("password.reset.prompt"), {
				title: $.i18n.prop("tip.info"),
				closeBtn: 0, // 不显示关闭按钮
				shadeClose: true, // 开启遮罩关闭
				skin: "layui-layer-molv", // 样式类名
			    btn: [label_confirm, label_cancel] //按钮
			}, function() {
				parent.layer.closeAll();
				$.loading();
				$("#form_resetPassword #userId").val(userId);
				var data = $("#form_resetPassword").serialize();
				$.ajax({
					type : "PUT",
					url : url,
					data : data,
					dataType : "json",
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
			}, function(){
				parent.layer.closeAll();
			});
		}
	});
}
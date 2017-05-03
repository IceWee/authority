var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_hidden"; // 隐藏表单ID
var URI_AJAX_LIST = "/ajax/system/users"; // 列表页获取列表数据异步URI
var URI_AJAX_ROLE_LIST = "/ajax/system/roles/"; // 列表页获取列表数据异步URI
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

// 列表页面初始化
function initListPageExt(error, message) {
	initListPage(error, message);
	
	var data = [
		{"text":"Epson WorkForce 845","group":"Printer"},
		{"text":"Canon PIXMA MG5320","group":"Printer"},
		{"text":"HP Deskjet 1000 Printer","group":"Printer"},
		{"text":"Cisco RV110W-A-NA-K9","group":"Firewall"},
		{"text":"ZyXEL ZyWALL USG50","group":"Firewall"},
		{"text":"NETGEAR FVS318","group":"Firewall"},
		{"text":"Logitech Keyboard K120","group":"Keyboard"},
		{"text":"Microsoft Natural Ergonomic Keyboard 4000","group":"Keyboard"},
		{"text":"Logitech Wireless Touch Keyboard K400","group":"Keyboard"},
		{"text":"Logitech Gaming Keyboard G110","group":"Keyboard"},
		{"text":"Nikon COOLPIX L26 16.1 MP","group":"Camera"},
		{"text":"Canon PowerShot A1300","group":"Camera"},
		{"text":"Canon PowerShot A2300","group":"Camera"}
		]
	
	$("#datalist").datalist({
		singleSelect: false,
	    checkbox: true,
	    data: data,
	    lines: true
	});
	openLRListBoxDialog({});
}

// 操作按钮扩展
function operateBtnHtmlExt(value, row, index) {
	var html = operateBtnHtml(value, row, index);
	var label_auth = $.i18n .prop("operate.authorize");
	html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"userRoleAuth('" + row.id + "')\"><span class=\"label label-warning\">" + label_auth + "</span></a>";
	return html;
}

// 用户角色授权
function userRoleAuth(userId) {
	$("#userId").val(userId);
	var tipsId = "_tips_lr_list_box";
	$.ajax({
		type : "GET",
		url : URI_AJAX_ROLE_LIST + userId,
		dataType : "json",
		success : function(json) {
			if (json.code === "200") {
				if (json.data) {
//					openLRListBoxDialog({
//						valueField: "id",
//					    textField: "name",
//					    leftList: json.data.unselectRoles,
//					    rightList: json.data.selectedRoles,
//					    saveCallback: function(checkedRows) {
//					    	alert(checkedRows.length);
//					    }
//					});
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

function openLRListBoxDialog(options) {
	var leftList = $.isArray(options.leftList) ? options.leftList : [];
	var rightList = $.isArray(options.rightList) ? options.rightList : [];
	var saveCallback = options.saveCallback;
	var singleSelect = options.singleSelect == undefined ? false : options.singleSelect;
	var checkbox = options.checkbox == undefined ? true : options.checkbox;
	var valueField = options.valueField ? options.valueField : "id";
	var textField = options.textField ? options.textField : "name";
	$("#_lr_list_box_left").datalist({
		singleSelect: false,
	    checkbox: true,
	    valueField: "id",
	    textField: "name",
	    data: [{"id":1,"name":"nihao"},{"id":1,"name":"nihao"}],
	    lines: true
	});
	$("#_lr_list_box_right").datalist({
		singleSelect: false,
	    checkbox: true,
	    valueField: "id",
	    textField: "name",
	    data: [{id:1,name:"nihao"}],
	    lines: true
	});
	
	// 添加
	$("#_button_lr_list_box_left").one("click", function() {
		var checkedRows = $("#_lr_list_box_left").datalist("getChecked");
		var rowIndex;
		for (var i = 0; i < checkedRows.length; i++) {
			rowIndex = $("#_lr_list_box_left").datalist("getRowIndex", checkedRows[i]);
			$("#_lr_list_box_left").datalist("deleteRow", rowIndex);
			$("#_lr_list_box_right").datalist("appendRow", checkedRows[i]);
		}
	});
		
	// 删除
	$("#_button_lr_list_box_right").one("click", function() {
		var checkedRows = $("#_lr_list_box_right").datalist("getChecked");
		var rowIndex;
		for (var i = 0; i < checkedRows.length; i++) {
			rowIndex = $("#_lr_list_box_right").datalist("getRowIndex", checkedRows[i]);
			$("#_lr_list_box_right").datalist("deleteRow", rowIndex);
			$("#_lr_list_box_left").datalist("appendRow", checkedRows[i]);
		}
	});
	
	// 取消
	$("#_button_lr_list_box_cancel").one("click", function() {
		$("#_dialog_lr_list_box").modal("hide");
		hideTips("_tips_lr_list_box");
	});
	
	// 保存
	$("#_button_lr_list_box_save").one("click", function() {
		if ($.isFunction(saveCallback)) {
			var checkedRows = $("#_lr_list_box_right").datalist("getRows");
			saveCallback(checkedRows);
		}
	});
	
	$("#_dialog_lr_list_box").modal({
		backdrop: false, // 点击空白区域不关闭弹出框
		keyboard: false	 // 按ESC键不关闭弹出框
	});
}

// 编辑页面初始化
function initEditPageExt() {
	readonlyColor("username");
	readonlyColor("createUser");
	readonlyColor("createDate");
}
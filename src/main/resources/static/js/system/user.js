var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_hidden"; // 隐藏表单ID
var URI_LIST = "/system/user/list"; // 列表页面URI
var URI_AJAX_LIST = "/ajax/system/users"; // 列表页获取列表数据异步URI
var URI_AJAX_SAVE = "/ajax/system/users"; // ajax保存
var ADD_URI = "/system/user/add"; // 新增页面URI
var EDIT_URI = "/system/user/edit"; // 编辑页面URI
var BTN_ADD_ID = "#button_add"; // 增加按钮ID
var BTN_SAVE_ID = "#button_save"; // 保存按钮ID
var BTN_BACK_ID = "#button_back"; // 返回按钮ID

//返回列表页
function back2list() {
	$(FORM_ID_DETAIL).attr("action", URI_LIST);
	$(FORM_ID_DETAIL).submit();
}

/*************************** list begin *****************************/
// 初始化列表页面
function initListPage() {
	i18n();
	
	initDatagrid({url:URI_AJAX_LIST});
	
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
	$("id").val(id);
	$(FORM_ID_HIDDEN).attr("action", EDIT_URI);
	$(FORM_ID_HIDDEN).submit();
}

// 在datagrid的操作列中生成操作按钮
function operateBtnHtml(value, row, index) {
	var id = row.id;
	if (!id) {
		return "";
	} else {
		return "<a href=\"#\" onclick=\"edit('" + id + "')\"><span class=\"label label-primary\">编辑</span></a>";  
	}
}
/*************************** list end *****************************/

/*************************** detail begin *****************************/
// 初始化新增页面
function initAddPage() {
	i18n();
	
	readonlyColor("updateUser");
	readonlyColor("updateDate");
	
	// 保存
	$(BTN_SAVE_ID).click(function() {
		if ($(FORM_ID_DETAIL).form("validate")) {
			ajaxLoading($.i18n.prop("saving"));
			var data = $(FORM_ID_DETAIL).serializeJson();
			$.ajax({
				type : "POST",
				url : URI_AJAX_SAVE,
				data : JSON.stringify(data),
				dataType : "json",
				contentType : "application/json",
				success : function(json) {
					ajaxLoaded();
					var code = json.code;
					if (code == CODE_OK) {
						showSuccessTips($.i18n.prop("save.success"));
					} else {
						showErrorTips(json.message);
					}
				},
				error : function() {
					ajaxLoaded();
					showErrorTips($.i18n .prop("http.request.failed"));
				}
			});
		}
	});
	
	// 返回
	$(BTN_BACK_ID).click(function() {
		$(FORM_ID_DETAIL).attr("action", URI_LIST);
		$(FORM_ID_DETAIL).submit();
	});
}
/*************************** add end *****************************/


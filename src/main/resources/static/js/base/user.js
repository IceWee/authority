var FORM_ID_DETAIL = "#pageForm"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#hiddenForm"; // 隐藏表单ID
var URI_LIST = "/user/list"; // 列表页面URI
var URI_AJAX_LIST = "/users"; // 列表页获取列表数据异步URI
var URI_AJAX_SAVE = "/users"; // ajax保存
var DETAIL_URI = "/user/detail"; // 新增页面URI
var BTN_ADD_ID = "#btn_add"; // 增加按钮ID
var BTN_SAVE_ID = "#btn_save"; // 保存按钮ID
var BTN_BACK_ID = "#btn_back"; // 返回按钮ID

//返回列表页
function back2list() {
	$(FORM_ID_DETAIL).attr("action", URI_LIST);
	$(FORM_ID_DETAIL).submit();
}

/*************************** list begin *****************************/
// 初始化列表页面
function initListPage() {
	i18n();
	
	_initDatagrid({url:URI_AJAX_LIST});
	
	// 跳转到新增页面
	$(BTN_ADD_ID).click(function() {
		detail(null);
	});
}

// 进入详情页
function detail(id) {
	$("#id").val(id);
	$(FORM_ID_HIDDEN).attr("action", DETAIL_URI);
	$(FORM_ID_HIDDEN).submit();
}

// 在datagrid的操作列中生成操作按钮
function operateBtnHtml(value, row, index) {
	var id = row.id;
	if (!id || id == null || id == "null") {
		return "";
	} else {
		return "<a href=\"#\" onclick=\"detail('" + id + "')\"><span class=\"label label-primary\">编辑</span></a>";  
	}
}
/*************************** list end *****************************/

/*************************** detail begin *****************************/
// 初始化新增页面
function initDetailPage() {
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
						showSuccessTips($.i18n.prop("saveSuccessful"));
					} else {
						showErrorTips(json.message);
					}
				},
				error : function() {
					ajaxLoaded();
					showErrorTips($.i18n .prop("requestFailed"));
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


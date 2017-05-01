var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_search"; // 隐藏表单ID
var URI_AJAX_LIST = "/ajax/system/resources"; // 列表页获取列表数据异步URI
var URI_AJAX_CATEGORY_TREE = "/ajax/system/category/tree"; // 列表页获取列表数据异步URI
var URI_LIST = "/system/resource/list"; // 列表页面URI
var URI_ADD = "/system/resource/add"; // 新增页面URI
var URI_SAVE = "/system/resource/save"; // 保存URI
var URI_EDIT = "/system/resource/edit"; // 编辑页面URI
var URI_UPDATE = "/system/resource/update"; // 更新URI
var URI_DELETE = "/system/resource/delete"; // 删除URI
var BTN_ADD_ID = "#button_add"; // 增加按钮ID
var BTN_SAVE_ID = "#button_save"; // 保存按钮ID
var BTN_UPDATE_ID = "#button_update"; // 保存按钮ID
var BTN_BACK_ID = "#button_back"; // 返回按钮ID
var PARAM_ID = "#id"; // ID属性名

// 初始化资源分类树
function initResourceListPage() {
	var RESPONSE_OK = "200";
	$.ajax({
		type : "GET",
		url : URI_AJAX_CATEGORY_TREE,
		dataType : "json",
		success : function(json) {
			if (json.code === RESPONSE_OK) {
				if (json.data) {
					var array = json.data;
					$("#tree_category").tree({
						data: array,
						animate: true,
						onClick: function(node){
							$("#categoryId").val(node.id);
						}
					});
					var categoryId = $("#categoryId").val();
					if (categoryId != -1) {
						var node = $("#tree_category").tree("find", categoryId);
						$("#tree_category").tree("select", node.target);
					}
					initListPage(error, message);
				}
			} else {
				showErrorTips(json.message);
			}
		},
		error : function() {
			showErrorTips($.i18n .prop("http.request.failed"));
		}
	});
}
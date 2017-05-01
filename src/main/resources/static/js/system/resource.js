var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_search"; // 隐藏表单ID
var URI_AJAX_LIST = "/ajax/system/resources"; // 列表页获取列表数据异步URI
var URI_AJAX_CATEGORY_TREE = "/ajax/system/category/tree"; // 列表页获取列表数据异步URI
var URI_AJAX_CATEGORY_SAVE = "/ajax/system/category/save"; // 列表页获取列表数据异步URI
var URI_AJAX_CATEGORY_UPDATE = "/ajax/system/category/update"; // 列表页获取列表数据异步URI
var URI_AJAX_CATEGORY_DELETE = "/ajax/system/category/delete"; // 列表页获取列表数据异步URI
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
					initCategoryTree(array);
				}
			} else {
				showErrorTips(json.message);
			}
		},
		error : function() {
			showErrorTips($.i18n .prop("http.request.failed"));
		}
	});
	
	// 关闭分类弹出框
	$("#button_dialog_category_cancel").click(function() {
		$("#dialog_category").modal("hide");
	});
	
	readonlyColor("parentCategoryName");
	
	// 保存分类
	$("#button_dialog_category_save").click(function(){
		var url = $("#form_category").attr("action");
		var data = JSON.stringify($("#form_category").serializeJson());
		var node = getSelectedCategory();
		if ($("#form_category").form("validate")) {
			ajaxLoading($.i18n.prop("save.saving"));
			$.ajax({
				type : "POST",
				contentType: "application/json",
				url : url,
				data : data,
				dataType : "json",
				success : function(json) {
					ajaxLoaded();
					if (json.code === "200") {
						$("#dialog_category").modal("hide");
						freshCategoryTree(node);
						showSuccessTips($.i18n .prop("save.success"));
					} else {
						showErrorTips(json.message, "tips_category");
					}
				},
				error : function() {
					ajaxLoaded();
					showErrorTips($.i18n .prop("http.request.failed"), "tips_category");
				}
			});
		}
	});
}

// 初始化资源分类树
function initCategoryTree(data) {
	$("#tree_category").tree({
		data: data,
		animate: true,
		onClick: function(node){
			selectCategory(node);
		},
		onContextMenu: function(e, node){
			e.preventDefault();
			selectCategory(node);
			$("#menu_category").menu("show", {left: e.pageX, top: e.pageY});
		}
	});
	// 选中第一个节点
	selectFirstCategory();
}

// 新增资源分类
function addCategory() {
	$("#form_category").attr("action", URI_AJAX_CATEGORY_SAVE);
	$("#dialog_category").modal({keyboard:false});
	var node = getSelectedCategory();
	$("#form_category").form("load", {
		parentId: node.id,
		parentCategoryName: node.text,
		name: null,
		categoryId: null
	});
}

// 编辑资源分类
function editCategory() {
	$("#form_category").attr("action", URI_AJAX_CATEGORY_UPDATE);
	$("#dialog_category").modal({keyboard:false});
	var node = getSelectedCategory();
	var parent = getParentCategory(node);
	var parentCategoryName = null
	if (parent) {
		parentCategoryName = parent.text;
	}
	$("#form_category").form("load", {
		parentId: node.parentId,
		parentCategoryName: parentCategoryName,
		name: node.text,
		createUser: null,
		id: node.id
	});
}

// 删除资源分类
function deleteCategory() {
	var node = getSelectedCategory();
	var parent = getParentCategory(node);
	$.messager.confirm($.i18n.prop("tip.info"), $.i18n.prop("delete.prompt"), function(go){
		if (go){
			$.ajax({
				type : "DELETE",
				url : URI_AJAX_CATEGORY_DELETE + "/" + node.id,
				dataType : "json",
				success : function(json) {
					if (json.code === "200") {
						freshCategoryTree(parent);
						showSuccessTips($.i18n .prop("delete.success"));
					} else {
						showErrorTips(json.message);
					}
				},
				error : function() {
					showErrorTips($.i18n .prop("http.request.failed"));
				}
			});
		}
	});
}

// 刷新分类树
function freshCategoryTree(parent) {
	$.ajax({
		type : "GET",
		url : URI_AJAX_CATEGORY_TREE,
		dataType : "json",
		success : function(json) {
			if (json.code === "200") {
				if (json.data) {
					var array = json.data;
					console.log(JSON.stringify(array));
					$("#tree_category").tree({data:array});
					if (parent) {
						var node = getCategory(parent.id);
						selectCategory(node);
					}
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

function getCategory(id) {
	return $("#tree_category").tree("find", id);
}

function getSelectedCategory() {
	return $("#tree_category").tree("getSelected");
}

function getParentCategory(node) {
	return $("#tree_category").tree("getParent", node.target);
}

function selectCategory(node) {
	if (node) {
		$("#tree_category").tree("select", node.target);
		$("#selectedCategoryId").val(node.id);
		doSearch(URI_AJAX_LIST);
	}
}

function selectFirstCategory() {
	$("#_easyui_tree_1").addClass("tree-node-selected"); // 设置第一个节点高亮 
	var node = getSelectedCategory();
	$("#selectedCategoryId").val(node.id);
	initListPage(error, message);
}

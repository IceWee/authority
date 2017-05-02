var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_HIDDEN = "#form_search"; // 隐藏表单ID
var FORM_ID_CATEGORY = "#form_category"; // 资源分类表单ID
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
var TREE_ID_RESOURCE_CATEGORY = "tree_category";
var DIALOG_ID_CATEGORY = "#dialog_category";

// 初始化资源分类树
function initResourceListPage() {
	$.ajax({
		type : "GET",
		url : URI_AJAX_CATEGORY_TREE,
		dataType : "json",
		success : function(json) {
			if (json.code === "200") {
				if (json.data) {
					var array = json.data;
					$("#tree_category").tree({
						data: array,
						animate: true,
						onClick: function(node){
							selectCategory(node);
						},
						// 右键菜单
						onContextMenu: function(e, node){
							e.preventDefault();
							selectCategory(node);
							$("#menu_category").menu("show", {left: e.pageX, top: e.pageY});
						}
					});
					// 选中第一个节点
					$("#_easyui_tree_1").addClass("tree-node-selected"); // 设置第一个节点高亮 
					var node = getSelectedTreeNode(TREE_ID_RESOURCE_CATEGORY);
					$("#selectedCategoryId").val(node.id);
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
	
	// 关闭分类弹出框
	$("#button_dialog_category_cancel").click(function() {
		$(DIALOG_ID_CATEGORY).modal("hide");
	});
	
	readonlyColor("parentCategoryName");
	
	// 保存分类
	$("#button_dialog_category_save").click(function(){
		var url = $(FORM_ID_CATEGORY).attr("action");
		var data = JSON.stringify($(FORM_ID_CATEGORY).serializeJson());
		var node = getSelectedTreeNode(TREE_ID_RESOURCE_CATEGORY);
		if ($(FORM_ID_CATEGORY).form("validate")) {
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
						$(DIALOG_ID_CATEGORY).modal("hide");
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

// 新增资源分类
function addCategory() {
	$(FORM_ID_CATEGORY).attr("action", URI_AJAX_CATEGORY_SAVE);
	$(DIALOG_ID_CATEGORY).modal({keyboard:false});
	var node = getSelectedTreeNode(TREE_ID_RESOURCE_CATEGORY);
	$(FORM_ID_CATEGORY).form("load", {
		parentId: node.id,
		parentCategoryName: node.text,
		name: null,
		categoryId: null
	});
}

// 编辑资源分类
function editCategory() {
	$(FORM_ID_CATEGORY).attr("action", URI_AJAX_CATEGORY_UPDATE);
	$(DIALOG_ID_CATEGORY).modal({keyboard:false});
	var node = getSelectedTreeNode(TREE_ID_RESOURCE_CATEGORY);
	var parent = getParentTreeNode(TREE_ID_RESOURCE_CATEGORY, node);
	var parentCategoryName = null
	if (parent) {
		parentCategoryName = parent.text;
	}
	$(FORM_ID_CATEGORY).form("load", {
		parentId: node.parentId,
		parentCategoryName: parentCategoryName,
		name: node.text,
		createUser: null,
		id: node.id
	});
}

// 删除资源分类
function deleteCategory() {
	var node = getSelectedTreeNode(TREE_ID_RESOURCE_CATEGORY);
	var parent = getParentTreeNode(TREE_ID_RESOURCE_CATEGORY, node);
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
					loadTreeData(TREE_ID_RESOURCE_CATEGORY, json.data);
					if (parent) {
						var node = getTreeNode(TREE_ID_RESOURCE_CATEGORY, parent.id);
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

// 选中资源分类
function selectCategory(node) {
	if (node) {
		selectTreeNode(TREE_ID_RESOURCE_CATEGORY, node);
		
		$("#selectedCategoryId").val(node.id);
		
		doSearch(URI_AJAX_LIST);
	}
}

/************************************** add/edit begin ********************************************/
function initAddPageExt(error, message) {
	initAddPage(error, message);
	
	initResourceCategoryTree({triggerInputId:"categoryName", onSelectClickCall:onSelectClickCallForDetail});
}

function initEditPageExt(error, message) {
	initEditPage(error, message);
	
	initResourceCategoryTree({triggerInputId:"categoryName", onSelectClickCall:onSelectClickCallForDetail});
}

function onSelectClickCallForDetail(node) {
	$(FORM_ID_DETAIL).form("load", {
		categoryId: node.id,
		categoryName: node.text
	});
}
/************************************** add/edit end ********************************************/

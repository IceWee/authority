// 固定常量，必须这样命名，否则crud.js无法正常运行
var FORM_ID_DETAIL = "#form_default"; // 详情页（新增或编辑）表单ID
var FORM_ID_LIST = "#form_list"; // 列表页表单ID
var URI_AJAX_LIST = "/ajax/system/resource/list"; // 列表页获取列表数据异步URI
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
//自定义常量
var URI_AJAX_CATEGORY_TREE = "/ajax/system/category/tree"; // 资源分类树
var URI_AJAX_CATEGORY_SAVE = "/ajax/system/category/save"; // 保存资源分类
var URI_AJAX_CATEGORY_UPDATE = "/ajax/system/category/update"; // 更新资源分类
var URI_AJAX_CATEGORY_DELETE = "/ajax/system/category/delete"; // 删除资源分类
var TREE_ID_RESOURCE_CATEGORY = "tree_category";
var DIALOG_ID_CATEGORY = "#dialog_category";
var FORM_ID_CATEGORY = "#form_category"; // 资源分类表单ID
var MENU_ID_CATEGORY = "#menu_category"; // 隐藏表单ID
var CATEGORY_NODE_ID_PREFIX = "rc_"; // 资源分类节点ID前缀
var HIDDEN_CATEGORY_ID = "#selectedCategoryId";

// 初始化资源分类树
function initListPageExt(error, message) {
	initTree({
		url: URI_AJAX_CATEGORY_TREE,
		treeId: TREE_ID_RESOURCE_CATEGORY,
		selectCallback: selectCategory,
		contextMenuCallback: function(e, node) {
			selectTreeNode(TREE_ID_RESOURCE_CATEGORY, node);
			$(MENU_ID_CATEGORY).menu("show", {left: e.pageX, top: e.pageY});
		},
		completeCallback: function() {
			var selectedCategoryId = $(HIDDEN_CATEGORY_ID).val();
			if (!selectedCategoryId) {
				var firstNode = selectFirstTreeNode(TREE_ID_RESOURCE_CATEGORY);
				$(HIDDEN_CATEGORY_ID).val(firstNode.attributes.id);
			} else {
				var node = getTreeNode(TREE_ID_RESOURCE_CATEGORY, CATEGORY_NODE_ID_PREFIX + selectedCategoryId);
				selectTreeNode(TREE_ID_RESOURCE_CATEGORY, node);
			}
			initListPage(error, message);
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
						freshCategoryTree(node.id);
						showSuccessTips($.i18n .prop("save.success"));
					} else {
						showErrorTips(json.message, "tips_category");
					}
				},
				error : function() {
					ajaxLoaded();
					showErrorTips($.i18n.prop("http.request.failed"), "tips_category");
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
		parentId: node.attributes.id,
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
		parentId: node.attributes.parentId,
		parentCategoryName: parentCategoryName,
		name: node.text,
		createUser: null,
		id: node.attributes.id
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
				url : URI_AJAX_CATEGORY_DELETE + "/" + node.attributes.id,
				dataType : "json",
				success : function(json) {
					if (json.code === "200") {
						if (parent) {
							freshCategoryTree(parent.id);
						}
						showSuccessTips($.i18n .prop("delete.success"));
					} else {
						showErrorTips(json.message);
					}
				},
				error : function() {
					showErrorTips($.i18n.prop("http.request.failed"));
				}
			});
		}
	});
}

// 刷新分类树
function freshCategoryTree(nodeId) {
	refreshTree({
		url: URI_AJAX_CATEGORY_TREE,
		treeId: TREE_ID_RESOURCE_CATEGORY,
		completeCallback: function() {
			if (nodeId) {
				var node = getTreeNode(TREE_ID_RESOURCE_CATEGORY, nodeId);
				selectCategory(node);
			}
		}
	});
}

// 选中资源分类
function selectCategory(node) {
	if (node) {
		selectTreeNode(TREE_ID_RESOURCE_CATEGORY, node);
		$(HIDDEN_CATEGORY_ID).val(node.attributes.id);
		doSearch(URI_AJAX_LIST);
	}
}

/************************************** add/edit begin ********************************************/
// 初始化新增页面扩展
function initAddPageExt(error, message) {
	initAddPage(error, message);
	
	initDialogTree({
		url: URI_AJAX_CATEGORY_TREE,
		title: $.i18n.prop("resource.category.tree"),
		showFooter: true,
		triggerInputId: "categoryName",
		confirmCallback: confirmCallbackForDetail
	});
}

// 初始化编辑页面扩展
function initEditPageExt(error, message) {
	initEditPage(error, message);
	
	var currCategoryId = $("#categoryId").val();
	
	initDialogTree({
		url: URI_AJAX_CATEGORY_TREE,
		title: $.i18n.prop("resource.category.tree"),
		showFooter: true,
		triggerInputId: "categoryName",
		confirmCallback: confirmCallbackForDetail,
		selectedId: CATEGORY_NODE_ID_PREFIX + currCategoryId // 选中
	});
}

// 选择树节点事件
function confirmCallbackForDetail(node) {
	$(FORM_ID_DETAIL).form("load", {
		categoryId: node.attributes.id,
		categoryName: node.text
	});
}
/************************************** add/edit end ********************************************/

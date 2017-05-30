/*************************固定常量 begin***********************/
// 固定常量，必须这样命名，否则curd.js无法正常运行
var URI_AJAX_LIST = "/ajax/system/resource/list";
var URI_LIST = "/system/resource/list"; // 列表页面URI
var URI_ADD = "/system/resource/add"; // 新增页面URI
var URI_SAVE = "/system/resource/save"; // 保存URI
var URI_EDIT = "/system/resource/edit"; // 编辑页面URI
var URI_UPDATE = "/system/resource/update"; // 更新URI
var URI_DELETE = "/system/resource/delete"; // 删除URI
var FORM_ID_DETAIL = "form_detail"; // 详情页（新增或编辑）表单ID
var FORM_ID_LIST = "form_search"; // 列表页表单ID
var BTN_ADD_ID = "button_add"; // 增加按钮ID
var BTN_SAVE_ID = "button_save"; // 保存按钮ID
var BTN_BACK_ID = "button_back"; // 返回按钮ID
var HIDDEN_ID = "id"; // 隐藏域主键ID属性
/*************************固定常量 end*************************/
//自定义常量
var URI_AJAX_CATEGORY_TREE = "/ajax/system/category/tree"; // 资源分类树
var URI_AJAX_CATEGORY_SAVE = "/ajax/system/category/save"; // 保存资源分类
var URI_AJAX_CATEGORY_UPDATE = "/ajax/system/category/update"; // 更新资源分类
var URI_AJAX_CATEGORY_DELETE = "/ajax/system/category/delete"; // 删除资源分类
var TREE_ID_RESOURCE_CATEGORY = "tree_category";
var DIALOG_ID_CATEGORY = "dialog_category";
var FORM_ID_CATEGORY = "form_category"; // 资源分类表单ID
var CATEGORY_NODE_ID_PREFIX = "rc_"; // 资源分类节点ID前缀
var HIDDEN_CATEGORY_ID = "selectedCategoryId";

// 初始化资源分类树
function initListPageExt(error, message) {
	showErrorOrMessage(error, message);
	
	// 跳转到新增页面
	$("#" + BTN_ADD_ID).click(function() {
		_add();
	});
	
	var contextMenu = $("#contextMenu").ztreeContextMenu();
	
	var table;
	$("#ztree_category").easyzTree({
		url: URI_AJAX_CATEGORY_TREE,
		selectCallback: function(node, tree) {
			if (node) {
				$("#" + HIDDEN_CATEGORY_ID).val(node.rid);
			}
			if (table) {
				table.refresh();
			}
		},
		completeCallback: function(tree) {
			var selectedCategoryId = $("#" + HIDDEN_CATEGORY_ID).val();
			if (!selectedCategoryId) {
				// 选中第一个节点
				var topNode = tree.getRootNode();
				if (topNode) {
					$("#" + HIDDEN_CATEGORY_ID).val(topNode.rid);
					tree.selectNode(topNode).expandNode(topNode);
				}
			} else {
				// 选中节点
				tree.selectNodeById(CATEGORY_NODE_ID_PREFIX + selectedCategoryId).expandNodeById(CATEGORY_NODE_ID_PREFIX + selectedCategoryId);;
			}
			table = $("#container").btable({url: URI_AJAX_LIST});
			bindContextMenuEvents(tree);
		},
		contextCallback: function(event, node, tree) {
			tree.selectNode(node);
			$("#" + HIDDEN_CATEGORY_ID).val(node.rid);
			table.refresh();
			contextMenu.show(event);
		}
	});
}

// 绑定资源分类右键菜单事件
function bindContextMenuEvents(tree) {
	$("#category_add").off();
	$("#category_edit").off();
	$("#category_delete").off();
	$("#button_dialog_category_cancel").off();
	$("#button_dialog_category_save").off();
	
	// 添加资源分类
	$("#category_add").on("click", function() {
		addCategory(tree);
	});
	// 编辑资源分类
	$("#category_edit").on("click", function() {
		editCategory(tree);
	});
	// 删除资源分类
	$("#category_delete").on("click", function() {
		deleteCategory(tree);
	});
	// 关闭分类弹出框
	$("#button_dialog_category_cancel").on("click", function() {
		$("#" + DIALOG_ID_CATEGORY).modal("hide");
	});
	// 保存分类
	$("#button_dialog_category_save").on("click", function() {
		if ($("#" + FORM_ID_CATEGORY).valid()) {
			var url = $("#" + FORM_ID_CATEGORY).attr("action");
			var data = JSON.stringify($("#" +FORM_ID_CATEGORY).serializeJson());
			var node = tree.getSelectedNodes()[0];
			$.ajax({
				type : "POST",
				contentType: "application/json",
				url : url,
				data : data,
				dataType : "json",
				success : function(json) {
					if (json.code == OK) {
						$("#" + DIALOG_ID_CATEGORY).modal("hide");
						$.successTips($.i18n.prop("save.success"));
						tree.refresh();
					} else {
						$.errorTips(json.message);
					}
				},
				error : function() {
					$.errorTips($.i18n.prop("http.request.failed"));
				}
			});
		}
	});
}

// 新增资源分类
function addCategory(tree) {
	$("#" + FORM_ID_CATEGORY).attr("action", URI_AJAX_CATEGORY_SAVE);
	$("#" + DIALOG_ID_CATEGORY).modal({keyboard:false});
	var node = tree.getSelectedNodes()[0];
	$("#" + HIDDEN_CATEGORY_ID).val(node.rid);
	$("#" + FORM_ID_CATEGORY + " #parentId").val(node.rid);
	$("#" + FORM_ID_CATEGORY + " #parentCategoryName").val(node.name);
	$("#" + FORM_ID_CATEGORY + " #name").val(null);
	$("#" + FORM_ID_CATEGORY + " #categoryId").val(null);
	$("#" + FORM_ID_CATEGORY).validate();
}

// 编辑资源分类
function editCategory(tree) {
	$("#" +FORM_ID_CATEGORY).attr("action", URI_AJAX_CATEGORY_UPDATE);
	$("#" +DIALOG_ID_CATEGORY).modal({keyboard:false});
	var node = tree.getSelectedNodes()[0];
	$("#" + HIDDEN_CATEGORY_ID).val(node.rid);
	var parentNode = tree.getNodeById(CATEGORY_NODE_ID_PREFIX + node.parentId);
	var parentCategoryName = null;
	if (parentNode) {
		parentCategoryName = parentNode.name;
	}
	$("#" + FORM_ID_CATEGORY + " #parentId").val(node.parentId);
	$("#" + FORM_ID_CATEGORY + " #parentCategoryName").val(parentCategoryName);
	$("#" + FORM_ID_CATEGORY + " #name").val(node.name);
	$("#" + FORM_ID_CATEGORY + " #categoryId").val(node.rid);
	$("#" + FORM_ID_CATEGORY).validate();
}

// 删除资源分类
function deleteCategory(tree) {
	var node = tree.getSelectedNodes()[0];
	var parentNode = tree.getNodeById(CATEGORY_NODE_ID_PREFIX + node.parentId);
	$("#" + HIDDEN_CATEGORY_ID).val(parentNode.rid);
	parent.layer.confirm($.i18n.prop("delete.prompt"), {
		title: $.i18n.prop("tip.info"),
		closeBtn: 0, // 不显示关闭按钮
		shadeClose: true, // 开启遮罩关闭
		skin: "layui-layer-molv", // 样式类名
	    btn: ["确定", "取消"] //按钮
	}, function(){
		parent.layer.closeAll();
		$.ajax({
			type : "DELETE",
			url : URI_AJAX_CATEGORY_DELETE + "/" + node.rid,
			dataType : "json",
			success : function(json) {
				if (json.code == OK) {
					tree.refresh();
					$.successTips($.i18n.prop("delete.success"));
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
/************************************** add/edit begin ********************************************/
// 初始化新增页面扩展
function initAddPageExt(error, message) {
	initAddPage(error, message);
	// 资源分类树
	$("#_dialog_ztree").dialogzTree({
		title: $.i18n.prop("resource.category.tree"),
		url: URI_AJAX_CATEGORY_TREE,
		inputId: "categoryName",
		showFooter: false,
		closeAfterSelect: true,
		chkEnable: false,
		selectCallback: function(node, tree) {
			if (node) {
				$("#categoryId").val(node.rid);
				$("#categoryName").val(node.name);
			}
		}
	});
}

// 初始化编辑页面扩展
function initEditPageExt(error, message) {
	initEditPage(error, message);
	var categoryId = $("#categoryId").val();
	// 资源分类树
	$("#_dialog_ztree").dialogzTree({
		title: $.i18n.prop("resource.category.tree"),
		url: URI_AJAX_CATEGORY_TREE,
		inputId: "categoryName",
		selectedId: CATEGORY_NODE_ID_PREFIX + categoryId,
		showFooter: false,
		closeAfterSelect: true,
		chkEnable: false,
		selectCallback: function(node, tree) {
			if (node) {
				$("#categoryId").val(node.rid);
				$("#categoryName").val(node.name);
			}
		}
	});
	
}
/************************************** add/edit end ********************************************/

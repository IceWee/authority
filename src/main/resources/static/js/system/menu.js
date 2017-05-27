/*************************固定常量 begin***********************/
// 固定常量，必须这样命名，否则curd.js无法正常运行
var URI_AJAX_LIST = "/ajax/system/menu/list";
var URI_LIST = "/system/menu/list"; // 列表页面URI
var URI_ADD = "/system/menu/add"; // 新增页面URI
var URI_SAVE = "/system/menu/save"; // 保存URI
var URI_EDIT = "/system/menu/edit"; // 编辑页面URI
var URI_UPDATE = "/system/menu/update"; // 更新URI
var URI_DELETE = "/system/menu/delete"; // 删除URI
var FORM_ID_DETAIL = "form_detail"; // 详情页（新增或编辑）表单ID
var FORM_ID_LIST = "form_search"; // 列表页表单ID
var BTN_ADD_ID = "button_add"; // 增加按钮ID
var BTN_SAVE_ID = "button_save"; // 保存按钮ID
var BTN_BACK_ID = "button_back"; // 返回按钮ID
var HIDDEN_ID = "id"; // 隐藏域主键ID属性
/*************************固定常量 end*************************/
// 自定义常量
var URI_AJAX_MENU_TREE = "/ajax/system/menu/tree"; // 菜单树
var URI_AJAX_RESOURCE_TREE = "/ajax/system/resource/tree"; // 资源树
var TREE_ID_MENU = "tree_menu"; // 菜单树ID
var HIDDEN_MENU_ID = "selectedParentMenuId";
var MENU_NODE_ID_PREFIX = "mn_"; // 菜单树节点ID前缀
var RESOURCE_NODE_ID_PREFIX = "rs_"; // 资源树节点ID前缀

// 初始化列表页面
function initListPageExt(error, message) {
	showErrorOrMessage(error, message);
	
	// 跳转到新增页面
	$("#" + BTN_ADD_ID).click(function() {
		_add();
	});
	
	var table;
	$("#ztree_menu").easyzTree({
		url: URI_AJAX_MENU_TREE,
		selectCallback: function(node, tree) {
			if (node) {
				$("#" + HIDDEN_MENU_ID).val(node.id);
			}
			if (table) {
				table.refresh();
			}
		},
		completeCallback: function(tree) {
			var selectedParentMenuId = $("#" + HIDDEN_MENU_ID).val();
			if (!selectedParentMenuId) {
				// 选中第一个节点
				var topNode = tree.getRootNode();
				if (topNode) {
					$("#" + HIDDEN_MENU_ID).val(topNode.id);
					tree.selectNode(topNode).expandNode(topNode);
				}
			} else {
				// 选中节点
				var selectedNode = tree.getNodeById(selectedParentMenuId);
				tree.selectNode(selectedNode).expandNode(selectedNode);;
			}
			table = $("#container").btable({url: URI_AJAX_LIST});
		}
	});
}

/************************************** add/edit begin ********************************************/
// 初始化新增页面扩展
function initAddPageExt(error, message) {
	initAddPage(error, message);
	
	// 资源树
	initDialogTree({
		url: URI_AJAX_RESOURCE_TREE,
		title: $.i18n.prop("resource.tree"),
		showFooter: true,
		autoClose: true,
		triggerInputId: "resourceName",
		confirmCallback: confirmCallbackForResource
	});
	
	// 菜单树
	initDialogTree({
		url: URI_AJAX_MENU_TREE,
		title: $.i18n.prop("menu.tree"),
		showFooter: true,
		autoClose: true,
		triggerInputId: "parentMenuName",
		confirmCallback: confirmCallbackForMenu
	});
}

// 初始化编辑页面扩展
function initEditPageExt(error, message) {
	initEditPage(error, message);
	
	var resourceId = $("#resourceId").val();
	if (resourceId) {
		resourceId = RESOURCE_NODE_ID_PREFIX + resourceId;
	}
	
	// 资源树
	initDialogTree({
		url: URI_AJAX_RESOURCE_TREE,
		title: $.i18n.prop("resource.tree"),
		showFooter: true,
		autoClose: true,
		triggerInputId: "resourceName",
		confirmCallback: confirmCallbackForResource,
		selectedId: resourceId // 选中
	});
	
	var parentMenuId = $("#parentId").val();
	var id = $("#id").val();
	// 菜单树
	initDialogTree({
		url: URI_AJAX_MENU_TREE + "/" + id,
		title: $.i18n.prop("menu.tree"),
		showFooter: true,
		autoClose: true,
		triggerInputId: "parentMenuName",
		confirmCallback: confirmCallbackForMenu,
		selectedId: MENU_NODE_ID_PREFIX + parentMenuId // 选中
	});
}

// 选择资源树节点事件
function confirmCallbackForResource(node) {
	var tipsId = "_tips_dialog_tree";
	hideTips(tipsId);
	if (node) {
		var type = node.attributes.type;
		if (type == 0) { // 资源分类节点
			showTips($.i18n.prop("menu.bind.category.invalid"), "warning", 3);
			$(FORM_ID_DETAIL).form("load", {
				resourceId: null,
				resourceName: node.text
			});
		} else {
			$(FORM_ID_DETAIL).form("load", {
				resourceId: node.attributes.id,
				resourceName: node.text
			});
		}
	}
}

// 选择菜单树节点事件
function confirmCallbackForMenu(node) {
	var tipsId = "_tips_dialog_tree";
	hideTips(tipsId);
	if (node) {
		$(FORM_ID_DETAIL).form("load", {
			parentId: node.attributes.id,
			parentMenuName: node.text
		});
	}
}
/************************************** add/edit end ********************************************/
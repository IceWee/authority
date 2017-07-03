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
				tree.selectNodeById(selectedParentMenuId).expandNodeById(selectedParentMenuId);;
			}
			table = $("#container").btable({url: URI_AJAX_LIST});
		}
	});
}

/************************************** add/edit begin ********************************************/
// 初始化新增页面扩展
function initAddPageExt(error, message) {
	initAddPage(error, message);
	// 菜单树
	$("#_dialog_ztree_menu").dialogzTree({
		title: $.i18n.prop("menu.tree"),
		url: URI_AJAX_MENU_TREE,
		inputId: "parentMenuName",
		showFooter: false,
		closeAfterSelect: true,
		chkEnable: false,
		selectCallback: function(node, tree) {
			$("#parentMenuId").val(node.id);
			$("#parentMenuName").val(node.name);
		}
	});
	// 资源树
	$("#_dialog_ztree_resource").dialogzTree({
		title: $.i18n.prop("resource.tree"),
		url: URI_AJAX_RESOURCE_TREE,
		inputId: "resourceName",
		treeId: "_ztree_resource",
		showFooter: false,
		closeAfterSelect: true,
		chkEnable: false,
		selectCallback: function(node, tree) {
			if (node) {
				var type = node.nodeType;
				if (type == 0) { // 资源分类节点
					$.warnTips($.i18n.prop("menu.bind.category.invalid"));
					$("#resourceId").val(null);
					$("#resourceName").val(node.name);
				} else {
					 // 注意：此处使用自定义rid的含义是资源树中包含两类节点，分类和资源，id属性为了避免重复添加了前缀，使用rid来保存原始PK
					$("#resourceId").val(node.rid);
					$("#resourceName").val(node.name);
				}
			}
		}
	});
}

// 初始化编辑页面扩展
function initEditPageExt(error, message) {
	initEditPage(error, message);
	var parentMenuId = $("#parentMenuId").val();
	var id = $("#id").val();
	// 菜单树
	$("#_dialog_ztree_menu").dialogzTree({
		title: $.i18n.prop("menu.tree"),
		url: URI_AJAX_MENU_TREE + "/" + id,
		inputId: "parentMenuName",
		showFooter: false,
		closeAfterSelect: true,
		chkEnable: false,
		selectedId: parentMenuId, // 选中
		selectCallback: function(node, tree) {
			$("#parentMenuId").val(node.id);
			$("#parentMenuName").val(node.name);
		}
	});
	// 资源树
	var resourceId = $("#resourceId").val();
	if (resourceId) {
		resourceId = RESOURCE_NODE_ID_PREFIX + resourceId;
	}
	$("#_dialog_ztree_resource").dialogzTree({
		title: $.i18n.prop("resource.tree"),
		url: URI_AJAX_RESOURCE_TREE,
		inputId: "resourceName",
		treeId: "_ztree_resource",
		showFooter: false,
		closeAfterSelect: true,
		chkEnable: false,
		selectedId: resourceId, // 选中
		selectCallback: function(node, tree) {
			if (node) {
				var type = node.nodeType;
				if (type == 0) { // 资源分类节点
					$.warnTips($.i18n.prop("menu.bind.category.invalid"));
					$("#resourceId").val(null);
					$("#resourceName").val(node.name);
				} else {
					 // 注意：此处使用自定义rid的含义是资源树中包含两类节点，分类和资源，id属性为了避免重复添加了前缀，使用rid来保存原始PK
					$("#resourceId").val(node.rid);
					$("#resourceName").val(node.name);
				}
			}
		}
	});
}
/************************************** add/edit end ********************************************/
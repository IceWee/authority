// 获取EasyUI Tree节点
// treeId 树ID
// id 节点ID
function getTreeNode(treeId, id) {
	return $("#" + treeId).tree("find", id);
}

// 获得选中EasyUI Tree节点
function getSelectedTreeNode(treeId) {
	return $("#" + treeId).tree("getSelected");
}

// 获得EasyUI Tree上级节点
function getParentTreeNode(treeId, node) {
	return $("#" + treeId).tree("getParent", node.target);
}

// 选中节点
function selectTreeNode(treeId, node) {
	$("#" + treeId).tree("select", node.target);
}

// 加载树数据
function loadTreeData(treeId, data) {
	$("#" + treeId).tree({data:data});
}

// 初始化资源分类树
// triggerInputId 用于绑定click事件, 必须使用EasyUI的form, 即元素的class="easyui-textbox"
// onClickCall 单击树节点回调函数
// onContextMenuCall 右键菜单回调函数
// onSelectClickCall 选择按钮点击回调
// tips 错误提示DIV的ID, 默认tips
function initResourceCategoryTree(options) {
	var URI_AJAX_CATEGORY_TREE = "/ajax/system/category/tree"; // 资源分类树URI
	var BUTTON_SELECT = "#_button_resource_category_tree_select"; // 选择按钮
	var BUTTON_CANCEL = "#_button_resource_category_tree_cancel"; // 取消按钮
	var TREE_ID = "#_tree_resource_category"; // 树ID
	var DIALOG_ID = "#_dialog_resource_category_tree"; // 弹出框ID
	var OK = "200";
	
	var onClickCall = options.onClickCall;
	var onContextMenuCall = options.onContextMenuCall;
	var onSelectClickCall = options.onSelectClickCall;
	var tips = options.tips;
	var formId = options.formId ? "#" + options.formId : "#form_default";
	var triggerInputId = options.triggerInputId ? "#" + options.triggerInputId : "#categoryName";
	
	$.ajax({
		type : "GET",
		url : URI_AJAX_CATEGORY_TREE,
		dataType : "json",
		success : function(json) {
			if (json.code === OK) {
				if (json.data) {
					var array = json.data;
					$(TREE_ID).tree({
						data: array,
						animate: true,
						onClick: function(node){
							if ($.isFunction(onClickCall)) {
								onClickCall(node);
							}
						},
						onContextMenu: function(e, node){
							e.preventDefault();
							if ($.isFunction(onContextMenuCall)) {
								onContextMenuCall(e, node);
							}
						}
					});
				}
			} else {
				showErrorTips(json.message, tips);
			}
		},
		error : function() {
			showErrorTips($.i18n .prop("http.request.failed"), tips);
		}
	});
	
	$(triggerInputId).textbox("textbox").bind("click", function() {  
		$(DIALOG_ID).modal({keyboard:false});
	});
	
	$(BUTTON_CANCEL).click(function() {
		$(DIALOG_ID).modal("hide");
	});
	
	$(BUTTON_SELECT).click(function() {
		var node = $(TREE_ID).tree("getSelected");
		if (node) {
			if ($.isFunction(onSelectClickCall)) {
				onSelectClickCall(node);
			}
			$(DIALOG_ID).modal("hide");
		}
	});
	
}
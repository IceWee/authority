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

// 初始化弹出框树
// 依赖 i18n/utils
// url 必须
// triggerInputId 用于绑定click事件, 必须使用EasyUI的form, 即元素的class="easyui-textbox"
// selectCallback 选择树节点回调函数, 回调参数会将当前选中的节点对象返回
// contextMenuCall 右键菜单回调函数
// confirmCallback 确认按钮回调函数
// showFooter 是否选择弹出框页脚, 默认值 true
// tipsId 错误提示DIV的ID, 默认值tips
// title 弹出框标题
function initDialogTree(options) {
	var url = options.url;
	var triggerInputId = options.triggerInputId;
	if (!url || !triggerInputId) {
		return;
	}
	triggerInputId = "#" + triggerInputId;
	
	console.log(triggerInputId)
	
	var BUTTON_CONFIRM = "#_button_dialog_tree_confirm"; // 确认按钮
	var BUTTON_CANCEL = "#_button_dialog_tree_cancel"; // 取消按钮
	var TREE_ID = "#_dialog_tree"; // 树ID
	var DIALOG_ID = "#_modal_dialog_tree"; // 弹出框ID
	var DIALOG_TITLE_ID = "#_title_dialog_tree";
	var OK = "200";
	
	var selectCallback = options.selectCallback;
	var contextMenuCall = options.contextMenuCall;
	var confirmCallback = options.confirmCallback;
	var tipsId = options.tipsId;
	var showFooter = options.showFooter;
	var title = options.title;
	
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(json) {
			if (json.code === OK) {
				if (json.data) {
					var array = json.data;
					$(TREE_ID).tree({
						data: array,
						animate: true,
						onClick: function(node){
							if ($.isFunction(selectCallback)) {
								selectCallback(node, DIALOG_ID);
							}
						},
						onContextMenu: function(e, node){
							e.preventDefault();
							if ($.isFunction(contextMenuCall)) {
								contextMenuCall(e, node);
							}
						}
					});
				}
			} else {
				showErrorTips(json.message, tipsId);
			}
		},
		error : function() {
			showErrorTips($.i18n .prop("http.request.failed"), tipsId);
		}
	});
	
	// 绑定点击事件到提供的textbox上
	$(triggerInputId).textbox("textbox").bind("click", function() {  
		$(DIALOG_ID).modal({keyboard:false});
	});
	
	$(DIALOG_TITLE_ID).text(title);
	
	if (showFooter) {
		// 取消
		$(BUTTON_CANCEL).click(function() {
			$(DIALOG_ID).modal("hide");
		});
		
		// 确认
		$(BUTTON_CONFIRM).click(function() {
			var node = $(TREE_ID).tree("getSelected");
			if (node) {
				if ($.isFunction(confirmCallback)) {
					confirmCallback(node);
				}
				$(DIALOG_ID).modal("hide");
			}
		});
	} else {
		$("#_footer_dialog_tree").hide();
	}
}
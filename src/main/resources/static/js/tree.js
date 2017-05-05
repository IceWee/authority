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

// 根据节点ID选择节点
function selectTreeNodeById(treeId, nodeId) {
	var node = getTreeNode(treeId, nodeId);
	selectTreeNode(treeId, node);
}


//获取第一个树节点
function selectFirstTreeNode(treeId) {
	$("#" + treeId + " #_easyui_tree_1").addClass("tree-node-selected"); // 设置第一个节点高亮 
	return getSelectedTreeNode(treeId);
}

// 加载树数据
// treeId 必须
//
// completeCallback 加载完成后回调函数
function loadTreeData(options) {
	var treeId = options.treeId;
	var completeCallback = options.completeCallback;
	if (!treeId) {
		console.log("树ID为空");
		return;
	}
	var data = $.isArray(options.data) ? options.data : [];
	$("#" + treeId).tree({
		data: data,
		onLoadSuccess: function() {
			if ($.isFunction(completeCallback)) { // 选择树节点
				completeCallback();
			}
		}
	});
}

// 初始化树
// url 请求树数据的链接, 必须
// treeId EasyUI树ID, 必须
// selectCallback 选择树节点回调函数, 回调参数会将当前选中的节点对象返回
// contextMenuCallback 右键菜单回调函数
// completeCallback 加载完成后回调函数
// selectedId 需要选中的节点ID
// tipsId 错误提示DIV的ID, 默认值tips
// selectFirst 是否选中第一个节点
function initTree(options) {
	var url = options.url;
	if (!url) {
		console.log("树请求url为空");
		return;
	}
	var treeId = options.treeId;
	if (!treeId) {
		console.log("树ID为空");
		return;
	}
	var OK = "200";
	var selectCallback = options.selectCallback;
	var contextMenuCallback = options.contextMenuCallback;
	var completeCallback = options.completeCallback;
	var selectedId = options.selectedId;
	var tipsId = options.tipsId ? options.tipsId : "tips";
	var selectFirst = options.selectFirst;
	
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(json) {
			if (json.code === OK) {
				if (json.data) {
					var array = json.data;
					$("#" + treeId).tree({
						data: array,
						animate: true,
						onClick: function(node) {
							if ($.isFunction(selectCallback)) { // 选择树节点
								selectCallback(node);
							}
						},
						onContextMenu: function(e, node) { // 右键菜单
							e.preventDefault();
							if ($.isFunction(contextMenuCallback)) {
								contextMenuCallback(e, node);
							}
						},
						onLoadSuccess: function() {
							if (selectedId) {
								var node = getTreeNode(treeId, selectedId);
								selectTreeNode(treeId, node);
								if ($.isFunction(selectCallback)) { // 选择树节点
									selectCallback(node);
								}
							}
							if ($.isFunction(completeCallback)) { // 加载完成后回调
								completeCallback();
							}
						}
					});
				}
			} else {
				showErrorTips(json.message, tipsId);
			}
		},
		error : function() {
			showErrorTips($.i18n.prop("http.request.failed"), tipsId);
		}
	});
}

// 刷新树
// url 请求树数据的链接, 必须
// treeId EasyUI树ID, 必须
// tipsId 错误提示DIV的ID, 默认值tips
// completeCallback 加载完成后回调函数
function refreshTree(options) {
	var url = options.url;
	if (!url) {
		console.log("树请求url为空");
		return;
	}
	var treeId = options.treeId;
	if (!treeId) {
		console.log("树ID为空");
		return;
	}
	var tipsId = options.tipsId ? options.tipsId : "tips";
	var completeCallback = options.completeCallback;
	
	var OK = "200";
	
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(json) {
			if (json.code === OK) {
				if (json.data) {
					loadTreeData({
						treeId: treeId,
						data: json.data,
						completeCallback: completeCallback
					});
				}
			} else {
				showErrorTips(json.message, tipsId);
			}
		},
		error : function() {
			showErrorTips($.i18n.prop("http.request.failed"), tipsId);
		}
	});
}

// 初始化弹出框树
// 依赖 i18n/utils
// url 必须
// triggerInputId 必须, 用于绑定click事件, 必须使用EasyUI的form, 即元素的class="easyui-textbox"
// selectCallback 选择树节点回调函数, 回调参数会将当前选中的节点对象返回
// contextMenuCallback 右键菜单回调函数
// confirmCallback 确认按钮回调函数
// showFooter 是否选择弹出框页脚, 默认值 true
// tipsId 错误提示DIV的ID, 默认值tips
// title 弹出框标题
// selectedId 需要选中的节点ID
function initDialogTree(options) {
	var url = options.url;
	var triggerInputId = options.triggerInputId;
	if (!url || !triggerInputId) {
		console.log("树请求url为空或触发弹出框的文本款ID为空");
		return;
	}
	triggerInputId = "#" + triggerInputId;
	
	console.log(triggerInputId)
	
	var BUTTON_CONFIRM = "#_button_dialog_tree_confirm"; // 确认按钮
	var BUTTON_CANCEL = "#_button_dialog_tree_cancel"; // 取消按钮
	var RAW_TREE_ID = "_dialog_tree"; // 树ID
	var TREE_ID = "#" + RAW_TREE_ID; // 树ID
	var RAW_DIALOG_ID = "_modal_dialog_tree";
	var DIALOG_ID = "#" + RAW_DIALOG_ID; // 弹出框ID
	var DIALOG_TITLE_ID = "#_title_dialog_tree";
	var OK = "200";
	
	var selectCallback = options.selectCallback;
	var contextMenuCallback = options.contextMenuCallback;
	var confirmCallback = options.confirmCallback;
	var tipsId = options.tipsId ? options.tipsId : "tips";
	var showFooter = options.showFooter;
	var title = options.title;
	var selectedId = options.selectedId;
	
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
								selectCallback(node, RAW_DIALOG_ID);
							}
						},
						onContextMenu: function(e, node) {
							e.preventDefault();
							if ($.isFunction(contextMenuCall)) {
								contextMenuCallback(e, node);
							}
						},
						onLoadSuccess: function() {
							if (selectedId) {
								var node = getTreeNode(RAW_TREE_ID, selectedId);
								selectTreeNode(RAW_TREE_ID, node);
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
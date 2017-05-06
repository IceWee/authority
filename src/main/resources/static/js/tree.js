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

// 获得全部选中树节点
function getCheckedTreeNodes(treeId) {
	return $("#" + treeId).tree("getChecked");
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
// checkbox 默认false
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
	var selectCallback = options.selectCallback;
	var contextMenuCallback = options.contextMenuCallback;
	var completeCallback = options.completeCallback;
	var selectedId = options.selectedId;
	var tipsId = options.tipsId ? options.tipsId : "tips";
	var checkbox = options.checkbox === undefined ? false : options.checkbox;
	
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(json) {
			if (json.code == CODE_OK) {
				if (json.data) {
					var array = json.data;
					$("#" + treeId).tree({
						data: array,
						animate: true,
						checkbox: checkbox,
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
	
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(json) {
			if (json.code == CODE_OK) {
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
// triggerInputId 用于绑定click事件, 必须使用EasyUI的form, 即元素的class="easyui-textbox"
// selectCallback 选择树节点回调函数, 回调参数会将当前选中的节点对象返回
// contextMenuCallback 右键菜单回调函数
// confirmCallback 确认按钮回调函数
// showFooter 是否选择弹出框页脚, 默认值 true
// tipsId 错误提示DIV的ID, 默认值_tips_dialog_tree
// title 弹出框标题
// selectedId 需要选中的节点ID
// showImmediately 立刻显示
// checkbox 默认false
// autoClose 默认true, 点击确定后自动关闭
function initDialogTree(options) {
	var url = options.url;
	if (!url) {
		console.log("树请求url为空");
		return;
	}
	var DIALOG_TITLE_ID = "#_title_dialog_tree";
	var DIALOG_ID = "#_modal_dialog_tree"; // 弹出框ID
	
	var triggerInputId = options.triggerInputId;
	if (triggerInputId) {
		triggerInputId = "#" + triggerInputId;
	}
	var showImmediately = options.showImmediately;
	var title = options.title;
	var tipsId = options.tipsId ? options.tipsId : "_tips_dialog_tree";
	
	// 立即显示
	if (showImmediately) {
		hideTips(tipsId);
		$(DIALOG_TITLE_ID).text(title);
		$(DIALOG_ID).modal({keyboard:false});
		_initializeEasyUITree(options);
		_initializeButtonEvents(options);
	}
	
	// 绑定点击事件到提供的textbox上
	if (triggerInputId) {
		hideTips(tipsId);
		$(triggerInputId).textbox("textbox").bind("click", function() {  
			$(DIALOG_TITLE_ID).text(title);
			$(DIALOG_ID).modal({keyboard:false});
			_initializeEasyUITree(options);
			_initializeButtonEvents(options);
		});
	}
}

// 初始化按钮点击事件
// initDialogTree方法专用
function _initializeButtonEvents(options) {
	var BUTTON_CONFIRM = "#_button_dialog_tree_confirm"; // 确认按钮
	var BUTTON_CANCEL = "#_button_dialog_tree_cancel"; // 取消按钮
	var DIALOG_ID = "#_modal_dialog_tree"; // 弹出框ID
	var RAW_TREE_ID = "_dialog_tree"; // 树ID
	var FOOTER_ID = "#_footer_dialog_tree";
	
	var showFooter = options.showFooter;
	var checkbox = options.checkbox == undefined ? false : options.checkbox;
	var autoClose = options.autoClose == undefined ? true : options.autoClose;
	var confirmCallback = options.confirmCallback;
	
	if (showFooter) {
		$(BUTTON_CONFIRM).off(); // 注意：隐藏弹出框后必须关闭click事件，否则再次弹出后会触发多次click事件
		
		// 取消
		$(BUTTON_CANCEL).click(function() {
			$(DIALOG_ID).modal("hide");
		});
		
		// 确认
		$(BUTTON_CONFIRM).on("click", function() {
			if (!checkbox) { // 单选
				var node = getSelectedTreeNode(RAW_TREE_ID);
				if (node) {
					if ($.isFunction(confirmCallback)) {
						confirmCallback(node);
					}
					if (autoClose) {
						$(DIALOG_ID).modal("hide");
					}
				}
			} else { // 复选
				var nodes = getCheckedTreeNodes(RAW_TREE_ID);
				if (nodes.length > 0) {
					if ($.isFunction(confirmCallback)) {
						confirmCallback(nodes);
					}
					if (autoClose) {
						$(DIALOG_ID).modal("hide");
					}
				}
			}
			
		});
	} else {
		$(FOOTER_ID).hide();
	}
}

// 初始化树
// initDialogTree方法专用
function _initializeEasyUITree(options) {
	var url = options.url;
	if (!url) {
		console.log("树请求url为空");
		return;
	}
	
	var RAW_TREE_ID = "_dialog_tree"; // 树ID
	var TREE_ID = "#" + RAW_TREE_ID; // 树ID
	var RAW_DIALOG_ID = "_modal_dialog_tree";
	
	var selectCallback = options.selectCallback;
	var contextMenuCallback = options.contextMenuCallback;
	var selectedId = options.selectedId;
	var checkbox = options.checkbox == undefined ? false : options.checkbox;
	var tipsId = options.tipsId ? options.tipsId : "_tips_dialog_tree";
	
	loadTreeData({treeId:RAW_TREE_ID}); // 清空缓存
	
	$.ajax({
		type : "GET",
		url : url,
		dataType : "json",
		success : function(json) {
			if (json.code == CODE_OK) {
				if (json.data) {
					var array = json.data;
					$(TREE_ID).tree({
						data: array,
						animate: true,
						checkbox: checkbox,
						onClick: function(node){
							if ($.isFunction(selectCallback)) {
								selectCallback(node, RAW_DIALOG_ID);
							}
						},
						onContextMenu: function(e, node) {
							e.preventDefault();
							if ($.isFunction(contextMenuCallback)) {
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
}
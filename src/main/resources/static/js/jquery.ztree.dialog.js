// Bootstrap Modal+zTree
(function($) {
	$.fn.dialogzTree = function(options) {
		var self = this;
		var containerId = this.selector;
		this._zTree = {}; // ztree对象引用
		this._settings = {
			url : "",
			async : false, // 异步加载
			asyncUrl : "",
			chkEnable : true,
			chkStyle : "checkbox",
			chkboxType : { "Y": "ps", "N": "ps" },
			showAtonce : false, // 是否立即显示弹出框
			inputId : null, // 触发显示弹出框的输入框ID
			showFooter : true, // 是否显示确认取消按钮
			closeAfterConfirm : false, // 单选选中后自动共关闭弹出框
			checkedIds : [], // 用于回显，已选节点ID数组
			autoParam : ["id"]
		};
		this._init = function() {
			this._settings = $.extend(self._settings, options);
			this._settings.treeId = containerId + " #_ztree";
			this._settings.toolbar = containerId + " #_toolbar_ztree";
			this._settings.btnConfirmId = containerId + " #_button_confirm_ztree";
			this._settings.btnCancelId = containerId + " #_button_cancel_ztree";
			var zTreeSettings = {
				treeId : self._settings.treeId,
				check : {
					enable: self._settings.chkEnable,
					chkStyle: self._settings.chkStyle,
					chkboxType: self._settings.chkboxType
				},
				checkedIds : self._settings.checkedIds,
				async : {
					enable : self._settings.async,
					autoParam: self._settings.autoParam,
					url : self._settings.asyncUrl,
					type : "GET"
				},
				callback : {
					// check事件
					onCheck : function(event, treeId, treeNode) {
						if (treeNode.checked && $.isFunction(self._settings.checkCallback)) {
							self._settings.checkCallback(event, treeId, treeNode);
						}
					},
					// 点击节点事件
					onClick : function(event, treeId, treeNode) {
						var zTree = self._zTree;
						var node = zTree.getNodeByTId(treeNode.tId);
						if (node.isParent) {
							if (node.children.length == 0) {
								$.ajax({  
					                type: "GET",  
					                async : false,  
					                url : self._settings.asyncUrl,  
					                data : {  
					                    id : treeNode.id  
					                },  
					                dataType : "json",  
					                success : function(json) {  
					                    if (json){  
					                        zTree.addNodes(node, json);  
					                    }  
					                },  
					                error : function(event, XMLHttpRequest, ajaxOptions, thrownError){  
					                	$.errorTips($.i18n.prop("load.failed"));
					                }  
					            });
							} else {
								zTree.expandNode(node, true, false, true);
							}
				        }  
					}
				}
			}
			this._initDialog();
			this._initzTree(zTreeSettings);
			this._bindButtonEvents();
		};
		// 关闭
		this.closeTree = function() {
			this._hideDialog();
		};
		// 获取全部选中节点
		this.getCheckedNodes = function() {
			return this._zTree.getCheckedNodes(true);
		};
		// 获取全部选择节点
		this.getSelectedNodes = function() {
			return this._zTree.getSelectedNodes();
		};
		// 初始化弹出框
		this._initDialog = function() {
			if (this._settings.showAtonce) {
				this._showDialog();
			}
		};
		// 显示弹出框
		this._showDialog = function() {
			$(this._settings.dialogId).modal({
				backdrop: false, // 点击空白区域不关闭弹出框
				keyboard: false	 // 按ESC键不关闭弹出框
			});
		};
		// 隐藏弹出框
		this._hideDialog = function() {
			$(this._settings.dialogId).modal("hide");
			$.clearTips();
		};
		// 初始化zTree
		this._initzTree = function(settings) {
			$.ajax({
				type : "GET",
				url : self._settings.url,
				data : {checkedCodes : self._settings.checkedIds},
				dataType : "json",
				success : function(nodes) {
					self._zTree = $.fn.zTree.init($(self._settings.treeId), settings, nodes);
				},
				error : function() {
					$.errorTips($.i18n.prop("load.failed"));
				}
			});
		};
		// 事件按钮绑定
		this._bindButtonEvents = function() {
			var options = this._settings;
			// 触发inputId
			if ($(options.inputId)) {
				$("#" + options.inputId).bind("click", function() {
					self._showDialog();
				});
			}
			// 显示确认、取消按钮
			if (options.showFooter) {
				// 取消
				$(options.btnCancelId).click(function() {
					self._hideDialog();
				});
				
				// 确认
				$(options.btnConfirmId).on("click", function() {
					var nodes = self.getCheckedNodes();
					if ($.isFunction(options.confirmCallback)) {
						options.confirmCallback(nodes);
					}
					if (options.closeAfterConfirm) {
						self._hideDialog();
					}
				});
			} else {
				$(options.footerId).hide();
			}
		};
		self._init();
		return this;
	}
})(jQuery);
// Bootstrap Modal+zTree
(function($) {
	$.fn.dialogzTree = function(options) {
		var self = this;
		var containerId = this.selector;
		this._zTree = {}; // ztree对象引用
		this._settings = {
			url : "",
			async : false, // 异步加载
			treeId : "_ztree",
			asyncUrl : "",
			chkEnable : true,
			chkStyle : "checkbox",
			chkboxType : { "Y": "ps", "N": "ps" },
			showAtonce : false, // 是否立即显示弹出框
			inputId : null, // 触发显示弹出框的输入框ID
			showFooter : true, // 是否显示确认取消按钮
			closeAfterConfirm : false, // 单选选中后自动共关闭弹出框
			closeAfterSelect : false, // 点击后关闭
			checkedIds : [], // 用于回显，已选节点ID数组
			expandAll : false,
			title : "Tree",
			selectedId : null,
			autoParam : ["id"]
		};
		this._init = function() {
			this._settings = $.extend(self._settings, options);
			this._settings.treeId = containerId + " #" + this._settings.treeId;
			this._settings.titleId = containerId + " #_title_ztree";
			this._settings.btnConfirmId = containerId + " #_button_confirm_ztree";
			this._settings.btnCancelId = containerId + " #_button_cancel_ztree";
			this._settings.footerId = containerId + " #_footer_ztree";
			var zTreeSettings = {
				treeId : self._settings.treeId,
				expandAll : self._settings.expandAll,
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
								self.expandNode(node);
							}
				        }
						if ($.isFunction(self._settings.selectCallback)) {
							self._settings.selectCallback(treeNode, self);
							if (self._settings.closeAfterSelect) {
								self._hideDialog();
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
		// 展开节点
		this.expandNode = function(node) {
			if (node) {
				this._zTree.expandNode(node, true, false, true);
			}
			return this;
		};
		// 展开节点ById
		this.expandNodeById = function(id) {
			var node = this.getNodeById(id);
			return this.expandNode(node);
		};
		// 展开全部节点
		this.expandAll = function() {
			this._zTree.expandAll(true);
			return this;
		};
		// 选中节点
		this.selectNode = function(node) {
			if (node){
				this._zTree.selectNode(node);
			}
			return this;
		};
		// 获取根节点
		this.getRootNode = function() {
			return this._zTree.getNodesByFilter(function(node) {
				return node.level == 1;
			}, true);
		};
		// 根据ID获取节点
		this.getNodeById = function(id) {
			return this._zTree.getNodesByFilter(function(node) {
				return node.id == id;
			}, true);
		};
		// 展开顶级节点
		this.expandRootNode = function() {
			var rootNode = this.getRootNode();
			if (rootNode) {
				this.expandNode(rootNode);
			}
			return this;
		};
		// 根据ID选中节点
		this.selectNodeById = function(id) {
			var node = this.getNodeById(id);
			if (node) {
				this.selectNode(node);
			}
		};
		// 初始化弹出框
		this._initDialog = function() {
			var options = this._settings;
			if (options.showAtonce) {
				this._showDialog();
			}
			$(options.titleId).text(options.title);
		};
		// 显示弹出框
		this._showDialog = function() {
			$.clearTips();
			$(containerId).modal({
				backdrop: false, // 点击空白区域不关闭弹出框
				keyboard: false	 // 按ESC键不关闭弹出框
			});
		};
		// 隐藏弹出框
		this._hideDialog = function() {
			$(containerId).modal("hide");
		};
		// 初始化zTree
		this._initzTree = function(settings) {
			$.ajax({
				type : "GET",
				url : self._settings.url,
				data : {checkedCodes : self._settings.checkedIds},
				dataType : "json",
				success : function(json) {
					if (json.code == OK) {
						self._zTree = $.fn.zTree.init($(settings.treeId), settings, json.data);
						if (settings.expandAll) {
							self.expandAll();
						}
						if (options.selectedId) {
							self.selectNodeById(options.selectedId).expandNodeById(options.selectedId);
						} else {
							self.expandRootNode();
						}
						if ($.isFunction(options.completeCallback)) {
							options.completeCallback(self);
						}
					} else {
						$.errorTips(json.message);
					}
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
				$("#" + options.inputId).click(function() {
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
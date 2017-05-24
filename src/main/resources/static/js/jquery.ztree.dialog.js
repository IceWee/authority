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
			autoParam : ["id"],
			suffix : self.selector // 用于同一页面多个弹出框时使用，统一添加后缀
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
					                	self._showErrorTips("数据加载失败");
					                }  
					            });
							} else {
								zTree.expandNode(node, true, false, true);
							}
				        }  
					}
				}
			}
			self._initDialog();
			self._initzTree(zTreeSettings);
			self._bindButtonEvents();
		};
		// 关闭
		self.closeTree = function() {
			self._hideDialog();
		};
		// 获取全部选中节点
		self.getCheckedNodes = function() {
			return self._zTree.getCheckedNodes(true);
		};
		// 获取全部选择节点
		self.getSelectedNodes = function() {
			return self._zTree.getSelectedNodes();
		};
		// 初始化弹出框
		self._initDialog = function() {
			if (self._settings.showAtonce) {
				self._showDialog();
			}
		};
		// 显示弹出框
		self._showDialog = function() {
			$(self._settings.dialogId).modal({
				backdrop: false, // 点击空白区域不关闭弹出框
				keyboard: false	 // 按ESC键不关闭弹出框
			});
		};
		// 隐藏弹出框
		self._hideDialog = function() {
			$(self._settings.dialogId).modal("hide");
			self._clearTips();
		};
		// 清除提示信息
		self._clearTips = function() {
			$(self._settings.tipsId).slideUp();
		};
		// 显示错误提示信息
		self._showErrorTips = function(msg) {
			$this = $(self._settings.tipsId);
			$this.removeClass();
			$this.addClass("alert alert-danger");
			$(self._settings.tipsId + " span:last-child").text(msg);
			$this.slideDown();
		};
		// 初始化zTree
		self._initzTree = function(settings) {
			$.ajax({
				type : "GET",
				url : self._settings.url,
				data : {checkedCodes : self._settings.checkedIds},
				dataType : "json",
				success : function(nodes) {
					self._zTree = $.fn.zTree.init($(self._settings.treeId), settings, nodes);
					var count = self.getCheckedNodes();
					console.log("初始化选中节点数量：【" + count.length + "】");
				},
				error : function() {
					self._showErrorTips("数据加载失败");
				}
			});
		};
		// 事件按钮绑定
		self._bindButtonEvents = function() {
			var options = self._settings;
			// 触发inputId
			if ($(options.inputId)) {
				$("#" + options.inputId).textbox("textbox").bind("click", function() {
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
// JQuery ztree
(function($) {
	$.fn.easyzTree = function(options) {
		var self = this;
		var treeId = this.selector;
		this._zTree = {}; // ztree对象引用
		this._settings = {
			url : "",
			async : false, // 异步加载
			asyncUrl : "",
			chkEnable : false,
			chkStyle : "checkbox",
			chkboxType : { "Y": "ps", "N": "ps" },
			checkedIds : [], // 用于回显，已选节点ID数组
			autoParam : ["id"]
		};
		this._ztreeSettings = {};
		this._init = function() {
			this._settings = $.extend(self._settings, options);
			var zTreeSettings = {
				treeId : treeId,
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
							self._settings.checkCallback(treeNode, self);
						}
					},
					// 右键事件
					onRightClick : function(event, treeId, treeNode) {
						if ($.isFunction(self._settings.contextCallback)) {
							self._settings.contextCallback(event, treeNode, self);
						}
					},
					// 点击节点事件
					onClick : function(event, treeId, treeNode) {
						var zTree = self._zTree;
						var node = zTree.getNodeByTId(treeNode.tId);
						if (node.isParent) {
							if (self._settings.async && node.children.length == 0) {
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
						if ($.isFunction(self._settings.selectCallback)) {
							self._settings.selectCallback(treeNode, self);
						}
					}
				}
			}
			this._initzTree(zTreeSettings);
			this._ztreeSettings = zTreeSettings;
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
		// 选中节点
		this.selectNode = function(node) {
			if (node) {
				this._zTree.selectNode(node);
			}
			return this;
		};
		// 根据ID选中节点
		this.selectNodeById = function(id) {
			var node = this.getNodeById(id);
			if (node) {
				this.selectNode(node);
			}
			return this;
		};
		// 获取根节点
		this.getRootNode = function() {
			return this._zTree.getNodesByFilter(function(node) {
				return node.level == 0;
			}, true);
		};
		// 根据ID获取节点
		this.getNodeById = function(id) {
			return this._zTree.getNodesByFilter(function(node) {
				return node.id == id;
			}, true);
		};
		// 初始化zTree
		this._initzTree = function(settings) {
			var options = self._settings;
			$.ajax({
				type : "GET",
				url : self._settings.url,
				data : {checkedCodes : self._settings.checkedIds},
				dataType : "json",
				success : function(json) {
					if (json.code == OK) {
						self._zTree = $.fn.zTree.init($(treeId), settings, json.data);
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
		// 刷新树
		this.refresh = function() {
			var options = self._settings;
			var settings = self._ztreeSettings;
			$.ajax({
				type : "GET",
				url : options.url,
				data : {checkedCodes : options.checkedIds},
				dataType : "json",
				success : function(json) {
					if (json.code == OK) {
						self._zTree = $.fn.zTree.init($(treeId), settings, json.data);
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
		self._init();
		return this;
	};
})(jQuery);
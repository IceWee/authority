// bootstrap table 封装
(function($) {
	$.fn.btable = function(options) {
		var self = this;
		var containerId = this.selector;
		
		this._settings = {
			tableId: "table_list",
			searchFormId: "form_search", 
			searchBtnId: "btn_search", 
			resetBtnId: "btn_reset",
			toolbar: "toolbar",
			tipsId: "tips",
			sortOrder: "asc",
			enterSearch: true,
			pagination: true,
			sortable: false,
			showColumns: false,
			showRefresh: true,
			clickToSelect: true,
			resetDosearch: true,
			striped: true
		};
		
		// 刷新列表
		this.refresh = function() {
			this._search();
		};
		
		// 初始化
		this._init = function() {
			this._settings = $.extend(self._settings, options);
			this._settings.tableId = containerId + " #" + this._settings.tableId;
			this._settings.searchFormId = containerId + " #" + this._settings.searchFormId;
			this._settings.searchBtnId = containerId + " #" + this._settings.searchBtnId;
			this._settings.resetBtnId = containerId + " #" + this._settings.resetBtnId;
			this._settings.toolbar = containerId + " #" + this._settings.toolbar;
			this._settings.tipsId = containerId + " #" + this._settings.tipsId;
			
			this._initTable();
			this._bindEvents();
		};
		
		// 初始化表格
		this._initTable = function() {
			var options = this._settings;
			$(options.tableId).bootstrapTable({
				url: options.url, //这个接口需要处理bootstrap table传递的固定参数
				dataType: "json",
				responseHandler: function(json) {
					if (json.code == OK) {
						return json.data;
					} else {
						$.errorTips(json.message);
					}
				},
				striped: true,      //是否显示行间隔色
				cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				pagination: options.pagination,     //是否显示分页（*）
				sortable: options.sortable,      //是否启用排序
				sortOrder: options.sortOrder,     //排序方式
				pageNumber: 1,      //初始化加载第一页，默认第一页
				pageSize: 10,      //每页的记录行数（*）
				pageList: [10, 20, 50, 100],  //可供选择的每页的行数（*）
				// 设置为 ''  在这种情况下传给服务器的参数为：pageSize,pageNumber
				queryParamsType: '', //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
				//前端调用服务时，会默认传递上边提到的参数，如果需要添加自定义参数，可以自定义一个函数返回请求参数
				queryParams: function(params) {
					var json = $(options.searchFormId).serializeJson();
					json["pageSize"] = params.pageSize;
					json["pageNumber"] = params.pageNumber;
	    	  		return json;
	      		},
				sidePagination: "server",   //分页方式：client客户端分页，server服务端分页（*）
				search: false,      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
				strictSearch: true, //设置为 true启用 全匹配搜索，否则为模糊搜索
				showColumns: options.showColumns,     //是否显示所有的列
				showRefresh: options.showRefresh,     //是否显示刷新按钮
				minimumCountColumns: 2,    //最少允许的列数
				clickToSelect: options.clickToSelect,    //是否启用点击选中行
				showToggle: true,
				toolbar: options.toolbar,
				iconSize: "outline",
				icons: {
					refresh: "glyphicon-repeat",
					toggle: 'glyphicon-list-alt',
			        columns: "glyphicon-list"
				}
			});
			
			// 窗口大小变化自动该调整表头
			 $(window).resize(function () {
				 $(options.tableId).bootstrapTable("resetView");
			 });
		};
		
		// 绑定事件
		this._bindEvents = function() {
			var options = this._settings;
			// 绑定文本框回车触发查询事件
			if (options.enterSearch) {
				$(options.searchFormId + " input").keyup(function(event) {
					if (event.keyCode == 13) {
						self._search();
					}
				}); 
			}
			// 查询
			$(options.searchBtnId).click(function() {
				self._search();
			});
			
			// 重置表单
			$(options.resetBtnId).click(function() {
				self._resetForm();
			});
		};
		
		// 刷新列表
		this._search = function() {
			var options = this._settings;
			var params = $(options.tableId).bootstrapTable("getOptions"); 
	        $(options.tableId).bootstrapTable("refresh", params);
		};
		
		// 重置表单
		this._resetForm = function() {
			var options = this._settings;
			$(options.searchFormId)[0].reset();
			if (options.resetDosearch) {
				self._search();
			}
		};
		
		this._init();
		return this;
	};
})(jQuery);
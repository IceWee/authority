// 将表单序列化成JSON对象
// 依赖JQuery的serializeArray方法
(function($) {
	$.fn.serializeJson = function() {
		var serializeObj = {};
		var array = this.serializeArray();
		var str = this.serialize();
		$(array).each(
				function() {
					if (serializeObj[this.name]) {
						if ($.isArray(serializeObj[this.name])) {
							serializeObj[this.name].push(this.value);
						} else {
							serializeObj[this.name] = [serializeObj[this.name], this.value ];
						}
					} else {
						serializeObj[this.name] = this.value;
					}
				});
		return serializeObj;
	};
})(jQuery);

// 转换为boolean
$.extend({
	toBool: function(obj) {
		if (obj == undefined || obj == null || obj == false || obj == 0 || obj == "0") {
			return false;
		}
		return true;
	}
});

// bootstrap table 封装
(function($) {
	$.fn.btable = function(options) {
		var defaults = {
			searchFormId: "form_search", 
			searchBtnId: "btn_search", 
			resetBtnId: "btn_reset",
			toolbar: "#toolbar",
			sortOrder: "asc"
		}
		var opts = $.extend(defaults, options); // 使用jQuery.extend 覆盖插件默认参数
		opts.tableId = $(this).attr("id");
		if (!opts.url) {
			console.log("request url is null...");
			return;
		}
		
		init(opts);
		
		var enterSearch = opts.enterSearch == undefined ? true : opts.enterSearch;
		// 绑定文本框回车触发查询事件
		if ($.toBool(enterSearch)) {
			$("#" + opts.searchFormId + " input").keyup(function(event) {
				if (event.keyCode == 13) {
					search(opts);
				}
			}); 
		}
		
		// 查询
		$("#" + opts.searchBtnId).click(function() {
			search(opts);
		});
		
		// 重置表单
		$("#" + opts.resetBtnId).click(function() {
			reset(opts);
		});
	};
	
	
	
	// 表格初始化
	function init(opts) {
		var OK = "200";
		var pagination = opts.pagination == undefined ? true : opts.pagination;
		var sortable = opts.sortable == undefined ? false : opts.sortable;
		var showColumns = opts.showColumns == undefined ? false : opts.showColumns;
		var showRefresh = opts.showRefresh == undefined ? true : opts.showRefresh;
		
		$("#" + opts.tableId).bootstrapTable({
			url: opts.url, //这个接口需要处理bootstrap table传递的固定参数
			dataType: "json",
			responseHandler: function(res) {
				if (res.code == OK) {
					return res.data;
				} else {
					alert("数据加载失败");
				}
			},
			striped: true,      //是否显示行间隔色
			cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: pagination,     //是否显示分页（*）
			sortable: sortable,      //是否启用排序
			sortOrder: opts.sortOrder,     //排序方式
			pageNumber: 1,      //初始化加载第一页，默认第一页
			pageSize: 10,      //每页的记录行数（*）
			pageList: [10, 20, 50, 100],  //可供选择的每页的行数（*）
			// 设置为 ''  在这种情况下传给服务器的参数为：pageSize,pageNumber
			queryParamsType: '', //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
			//前端调用服务时，会默认传递上边提到的参数，如果需要添加自定义参数，可以自定义一个函数返回请求参数
			queryParams: function(params) {
				var json = $("#" + opts.searchFormId).serializeJson();
				json["pageSize"] = params.pageSize;
				json["pageNumber"] = params.pageNumber;
    	  		return json;
      		},
			sidePagination: "server",   //分页方式：client客户端分页，server服务端分页（*）
			search: false,      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true, //设置为 true启用 全匹配搜索，否则为模糊搜索
			showColumns: showColumns,     //是否显示所有的列
			showRefresh: showRefresh,     //是否显示刷新按钮
			minimumCountColumns: 2,    //最少允许的列数
			clickToSelect: true,    //是否启用点击选中行
			showToggle: true,
			toolbar: opts.toolbar,
			iconSize: "outline",
			icons: {
				refresh: "glyphicon-repeat",
				toggle: 'glyphicon-list-alt',
		        columns: "glyphicon-list"
			}
		});
		
		// 窗口大小变化自动该调整表头
		 $(window).resize(function () {
//			 $("#" + opts.tableId).bootstrapTable('resetView');
		 });
	}
	
	// 刷新列表
	function search(opts) {
		var params = $("#" + opts.tableId).bootstrapTable("getOptions"); 
        $("#" + opts.tableId).bootstrapTable("refresh", params);
	}
	
	// 重置表单
	function reset(opts) {
		$("#" + opts.searchFormId)[0].reset();
	    search(opts);
	}
})(jQuery);
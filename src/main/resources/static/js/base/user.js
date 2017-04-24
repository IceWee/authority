var PAGE_SIZE = 10; // 默认显示10条数据
var TABLE_ID = "#datagrid";
var SEARCH_FORM_ID = "#searchForm";

function initPage() {
	i18n();
	
	$(TABLE_ID).datagrid({
		iconCls : "icon-ok",
		pageSize : PAGE_SIZE,
		pageList : [ PAGE_SIZE, 50, 100 ],
		singleSelect : true,
		nowrap : true, // 设置为true，当数据长度超出列宽时将会自动截取  
		striped : true, // 设置为true将交替显示行背景。  
		collapsible : true, // 显示可折叠按钮  
		toolbar : "#search_tools", // 自定义工具
		fitColumns : true, // 允许表格自动缩放，以适应父容器  
		remoteSort : true,
		pagination : true, // 分页 
		rownumbers : true,
		fitColumns : true // 自适应宽度
	// 行数  
	});
	// 自定义分页
	$(TABLE_ID).datagrid("getPager").pagination({
		onSelectPage : function(pageNo, pageSize) { // 翻页
			query();
		}
	});
}

function query() {
	hideTips();
	ajaxLoading($.i18n.prop("loading"));
	var pager = $(TABLE_ID).datagrid("getPager");
	var pageNo = pager.pagination("options").pageNumber;
	if (!pageNo) {
		pageNo = 1;
	}
	var pageSize = pager.pagination("options").pageSize;
	var data = $(SEARCH_FORM_ID).serialize();
	data = data + "&pageNo=" + pageNo + "&pageSize=" + pageSize + "&timestamp=" + new Date().getTime();
	$.ajax({
		type : "GET",
		url : "/data/salers?timestamp=" + new Date().getTime(),
		data : data,
		success : function(json) {
			ajaxLoaded();
			var subCode = json.subCode;
			if (subCode == 1) {
				var totalRows = json.data.totalRows;
				var list = json.data.data;
				$("#salerDatagrid").datagrid("loadData", list);
				pager.pagination({
					total : totalRows, // 总条数  
					pageNumber : pageNo, // 页号
					pageSize : pageSize // 每页行数
				});
			} else {
				var subMsg = json.subMessage;
				showErrorTips(subMsg);
			}
		},
		error : function() {
			ajaxLoaded();
			showErrorTips($.i18n .prop("requestFailed"));
		}
	});
}
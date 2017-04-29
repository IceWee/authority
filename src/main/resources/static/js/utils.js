var CODE_OK = 200; // 请求状态码，正常

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

/** 
 * JQuery扩展方法，用户对JQuery EasyUI的DataGrid控件进行操作。 
 */  
$.fn.extend({  
    /** 
     * 修改DataGrid对象的默认大小，以适应页面宽度。 
     *  
     * @param heightMargin 
     *            高度对页内边距的距离。 
     * @param widthMargin 
     *            宽度对页内边距的距离。 
     * @param minHeight 
     *            最小高度。 
     * @param minWidth 
     *            最小宽度。 
     *  
     */  
    resizeDataGrid : function(heightMargin, widthMargin, minHeight, minWidth) {  
        var height = $(document.body).height() - heightMargin;  
        var width = $(document.body).width() - widthMargin;  
  
        height = height < minHeight ? minHeight : height;  
        width = width < minWidth ? minWidth : width;  
  
        $(this).datagrid('resize', {  
            height : height,  
            width : width  
        });  
    }  
});

// 采用jquery easyui loading css效果
function ajaxLoading(msg) {   
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");   
    $("<div class=\"datagrid-mask-msg\" style=\"background-color:#FFF;border:1px solid #8E8E8E;height:44px;line-height:22px\"></div>").html(msg).appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});   
}

// 加载完毕
function ajaxLoaded() {   
     $(".datagrid-mask").remove();   
     $(".datagrid-mask-msg").remove();               
}

// 获取所给元素的HTML文本
function cloneSelf(id) {
	return $("<p>").append($("#" + id).clone()).html()
}

// 使用方法，在html中定义一个提示div，样式采用Bootstrap
// 显示提示信息，除msg外其他参数都为可选，但必须按顺序传递
// msg required
// type: success/info/warning/danger
// timeout: 单位是秒
function showTips(msg, type, timeout, id) {
	if (msg) {
		console.log("msg is null, nothing to show...")
		return;
	}
	type = arguments[1] ? arguments[1] : "success";
	timeout = arguments[2] ? arguments[2] : 0;
	id = arguments[3] ? "#" + arguments[3] : "#tips";
	$this = $(id);
	$this.removeClass();
	$this.addClass("alert alert-" + type);
	$(id + " span:last-child").text(msg);
	$this.slideDown();
	if (timeout > 0) {
		$this.delay(timeout * 1000).slideUp();
	}
}

// 显示失败提示，提示用不消失
// msg required
// id optional
function showErrorTips(msg, id) {
	showTips(msg, "danger", 0, id);
}

// 显示成功提示，一般信息显示后消失
// msg required
// timeout optional，默认值：3秒
// id optional
function showSuccessTips(msg, timeout, id) {
	timeout = arguments[1] ? arguments[1] : 3;
	showTips(msg, "success", timeout, id);
}

// id optional
function hideTips(id) {
	id = arguments[0] ? "#" + arguments[0] : "#tips";
	$(id).slideUp();
}

/* 
 * 初始化easyui datagrid, 依赖_search函数
 * 如果一个面有多个datagrid, 需要传递的参数有: url, tableId, toolbarId, searchFormId, searchBtnId, resetBtnId, tipsId
 * 
 * options 类型 JSON, 属性列表
 * url, 必填 , 查询使用
 * tableId, 可选, 默认值: datagrid
 * singleSelect, 可选, 默认值: true
 * toolbarId, 可选, 默认值: search_tools
 * pageSize, 可选, 默认值: 10
 * formSearchId, 可选, 默认值: form_search
 * buttonSearchId, 可选, 默认值: button_search
 * buttonResetId, 可选, 默认值: button_reset
 * minHeight, 可选, 默认值: 300
 * minWidth, 可选, 默认值: 600
 * tipsId, 可选, 默认值: tips
 * autoLoad, 可选，默认值: false, 是否自动加载数据
 */
function initDatagrid(options) {
	if (!options.url) {
		console.log("查询url为空，数据将不会加载...");
		return;
	}
	var url = options.url;
	var tableId = options.tableId ? "#" + options.tableId : "#datagrid";
	var singleSelect = options.singleSelect ? options.singleSelect : true;
	var toolbarId = options.toolbarId ? "#" + options.toolbarId : "#search_tools";
	var pageSize = options.pageSize ? options.pageSize : 10;
	var formSearchId = options.formSearchId ? "#" + options.formSearchId : "#form_search";
	var buttonSearchId = options.buttonSearchId ? "#" + options.buttonSearchId : "#button_search";
	var buttonResetId = options.buttonResetId ? "#" + options.buttonResetId : "#button_reset";
	var minHeight = options.minHeight ? options.minHeight : 300;
	var minWidth = options.minWidth ? options.minWidth : 600;
	var tipsId = options.tipsId ? options.tipsId : "tips";
	var autoLoad = options.autoLoad ? options.autoLoad : false;
	$(tableId).datagrid({
		iconCls : "icon-ok",
		pageSize : pageSize,
		pageList : [ pageSize, 50, 100 ],
		singleSelect : singleSelect, //  只允许单选
		nowrap : true, // 设置为true，当数据长度超出列宽时将会自动截取  
		striped : true, // 设置为true将交替显示行背景。  
		collapsible : true, // 显示可折叠按钮  
		toolbar : toolbarId, // 自定义工具
		fitColumns : true, // 允许表格自动缩放，以适应父容器  
		remoteSort : true,
		pagination : true, // 分页 
		rownumbers : true,
		fitColumns : true // 自适应宽度
	// 行数  
	});
	
	// 自定义分页
	$(tableId).datagrid("getPager").pagination({
		onSelectPage : function() { // 翻页
			doSearch(url, tableId, searchFormId, tipsId);
		}
	});
	
	// 查询
	$(buttonSearchId).click(function() {
		doSearch(url, tableId, formSearchId, tipsId);
	});
	
	// 表单重置
	$(buttonResetId).click(function() {
		$(formSearchId).form("reset");
	});
	
	// 当窗口大小发生变化时，调整DataGrid的大小  
    $(window).resize(function() {  
        $(tableId).resizeDataGrid(0, 0, minHeight, minWidth);  
    }); 
}

// 列表查询
function doSearch(url, tableId, formSearchId, tipsId) {
	var RESPONSE_OK = 200;
	hideTips(tipsId);
	ajaxLoading($.i18n.prop("load.loading"));
	var pager = $(tableId).datagrid("getPager");
	var pageNo = pager.pagination("options").pageNumber;
	if (!pageNo) {
		pageNo = 1;
	}
	var pageSize = pager.pagination("options").pageSize;
	var data = $(formSearchId).serialize();
	data = data + "&pageNo=" + pageNo + "&pageSize=" + pageSize + "&timestamp=" + new Date().getTime();
	$.ajax({
		type : "GET",
		url : url,
		data : data,
		success : function(json) {
			ajaxLoaded();
			if (json.code == RESPONSE_OK) {
				if (json.data) {
					var totalRows = json.data.totalRows;
					var list = json.data.data;
					$(tableId).datagrid("loadData", list);
					pager.pagination({
						total : totalRows, // 总条数  
						pageNumber : pageNo, // 页号
						pageSize : pageSize // 每页行数
					});
				}
			} else {
				showErrorTips(json.message, tipsId);
			}
		},
		error : function() {
			ajaxLoaded();
			showErrorTips($.i18n .prop("http.request.failed"), tipsId);
		}
	});
}

// 只读背景色
function readonlyColor(id) {
	$("#" + id).textbox("textbox").css("background-color", "#F1F1F1");
}
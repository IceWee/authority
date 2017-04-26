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
// 举例：提示框id是"myTips"，那么type必须指定
// msg required
// type: success/info/warning/danger
function showTips(msg, type, timeout, id) {
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

// 清空输入条件
function resetForm(formId) {
	$("#" + formId).form("reset");
}

// 初始化datagrid
// url 必填，查询URL
// tableId，可选，默认值：datagrid
// toolbarId，可选，默认值：search_tools
// pageSize，可选，默认值：10
// searchFormId，可选，默认值：search_form
// searchBtnId，可选，默认值：btn_search
// resetBtnId，可选，默认值：btn_reset
function _initDatagrid(url, tableId, toolbarId, pageSize, searchFormId, searchBtnId, resetBtnId) {
	if (!url) {
		console.log("查询url为空，数据将不会加载...");
		return;
	}
	tableId = arguments[1] ? "#" + arguments[1] : "#datagrid";
	toolbarId = arguments[2] ? "#" + arguments[2] : "#search_tools";
	pageSize = arguments[3] ? arguments[3] : 10;
	searchFormId = arguments[4] ? "#" + arguments[4] : "#search_form";
	searchBtnId = arguments[5] ? "#" + arguments[5] : "#btn_search";
	resetBtnId = arguments[6] ? "#" + arguments[6] : "#btn_reset";
	$(tableId).datagrid({
		iconCls : "icon-ok",
		pageSize : pageSize,
		pageList : [ pageSize, 50, 100 ],
		singleSelect : true,
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
			_search(url, tableId, searchFormId);
		}
	});
	
	// 查询
	$(searchBtnId).click(function() {
		_search(url, tableId, searchFormId);
	});
	
	// 表单重置
	$(resetBtnId).click(function() {
		$(searchFormId).form("reset");
	});
}

// 列表查询
function _search(url, tableId, searchFormId) {
	var RESPONSE_OK = 200;
	hideTips();
	ajaxLoading($.i18n.prop("loading"));
	var pager = $(tableId).datagrid("getPager");
	var pageNo = pager.pagination("options").pageNumber;
	if (!pageNo) {
		pageNo = 1;
	}
	var pageSize = pager.pagination("options").pageSize;
	var data = $(searchFormId).serialize();
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
				showErrorTips(json.message);
			}
		},
		error : function() {
			ajaxLoaded();
			showErrorTips($.i18n .prop("requestFailed"));
		}
	});
}

// 只读背景色
function readonlyColor(id) {
	$("#" + id).textbox("textbox").css("background-color", "#F1F1F1");
}
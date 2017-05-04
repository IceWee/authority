// 初始化左右列表框
// 依赖Bootstrap样式和脚本/EasyUI样式和脚本/utils.js
// 需要在页面中引入html-lrListBox
function openLRListBoxDialog(options) {
	var leftList = $.isArray(options.leftList) ? options.leftList : [];
	var rightList = $.isArray(options.rightList) ? options.rightList : [];
	var saveCallback = options.saveCallback;
	var valueField = options.valueField ? options.valueField : "id";
	var textField = options.textField ? options.textField : "name";
	
	// 显示模态框
	$("#_dialog_lr_list_box").modal({
		backdrop: false, // 点击空白区域不关闭弹出框
		keyboard: false	 // 按ESC键不关闭弹出框
	});
	
	// 初始化左列表
	$("#_lr_list_box_left").datalist({
		singleSelect: false,
	    checkbox: true,
	    valueField: valueField,
	    textField: textField,
	    data: leftList,
	    lines: true
	});
	
	// 初始化右列表
	$("#_lr_list_box_right").datalist({
		singleSelect: false,
	    checkbox: true,
	    valueField: valueField,
	    textField: textField,
	    data: rightList,
	    lines: true
	});
	
	// 添加元素
	$("#_button_lr_list_box_left").on("click", function() {
		var checkedRows = $("#_lr_list_box_left").datalist("getChecked");
		var rowIndex;
		for (var i = 0; i < checkedRows.length; i++) {
			rowIndex = $("#_lr_list_box_left").datalist("getRowIndex", checkedRows[i]);
			$("#_lr_list_box_left").datalist("deleteRow", rowIndex);
			$("#_lr_list_box_right").datalist("appendRow", checkedRows[i]);
		}
	});
		
	// 删除元素
	$("#_button_lr_list_box_right").on("click", function() {
		var checkedRows = $("#_lr_list_box_right").datalist("getChecked");
		var rowIndex;
		for (var i = 0; i < checkedRows.length; i++) {
			rowIndex = $("#_lr_list_box_right").datalist("getRowIndex", checkedRows[i]);
			$("#_lr_list_box_right").datalist("deleteRow", rowIndex);
			$("#_lr_list_box_left").datalist("appendRow", checkedRows[i]);
		}
	});
	
	// 取消
	$("#_button_lr_list_box_cancel").on("click", function() {
		$("#_dialog_lr_list_box").modal("hide");
		$("button[id^=_button_lr_list_box]").off();
		hideTips("_tips_lr_list_box");
	});
	
	// 保存
	$("#_button_lr_list_box_save").on("click", function() {
		if ($.isFunction(saveCallback)) {
			var checkedRows = $("#_lr_list_box_right").datalist("getRows");
			saveCallback(checkedRows);
		}
	});
}
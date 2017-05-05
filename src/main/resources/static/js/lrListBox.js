// 初始化左右列表框
// 依赖Bootstrap样式和脚本/EasyUI样式和脚本/utils.js
// 需要在页面中引入html-lrListBox
function openLRListBoxDialog(options) {
	var title =  options.title ? options.title : "";
	var leftTitle =  options.leftTitle;
	var rightTitle =  options.rightTitle;
	var leftList = $.isArray(options.leftList) ? options.leftList : [];
	var rightList = $.isArray(options.rightList) ? options.rightList : [];
	var saveCallback = options.saveCallback;
	var valueField = options.valueField ? options.valueField : "id";
	var textField = options.textField ? options.textField : "name";
	
	var TITLE = "#_title_lr_list_box";
	var BOX_LEFT = "#_lr_list_box_left";
	var BOX_RIGHT = "#_lr_list_box_right";
	var DIALOG = "#_dialog_lr_list_box";
	var BUTTON_LEFT = "#_button_lr_list_box_left";
	var BUTTON_RIGHT = "#_button_lr_list_box_right";
	var BUTTON_CANCEL = "#_button_lr_list_box_cancel";
	var BUTTON_SAVE = "#_button_lr_list_box_save";
	var TIPS = "_tips_lr_list_box";
	
	$(TITLE).text(title);
	
	// 显示模态框
	$(DIALOG).modal({
		backdrop: false, // 点击空白区域不关闭弹出框
		keyboard: false	 // 按ESC键不关闭弹出框
	});
	
	// 初始化左列表
	$(BOX_LEFT).datalist({
		singleSelect: false,
	    checkbox: true,
	    valueField: valueField,
	    textField: textField,
	    data: leftList,
	    lines: true
	});
	if (leftTitle) {
		$(BOX_LEFT).datalist("getPanel").panel("setTitle", leftTitle);
	}
	
	// 初始化右列表
	$(BOX_RIGHT).datalist({
		singleSelect: false,
	    checkbox: true,
	    valueField: valueField,
	    textField: textField,
	    data: rightList,
	    lines: true
	});
	if (rightTitle) {
		$(BOX_RIGHT).datalist("getPanel").panel("setTitle", rightTitle);
	}
	
	// 添加元素
	$(BUTTON_LEFT).on("click", function() {
		var checkedRows = $(BOX_LEFT).datalist("getChecked");
		var rowIndex;
		for (var i = 0; i < checkedRows.length; i++) {
			rowIndex = $(BOX_LEFT).datalist("getRowIndex", checkedRows[i]);
			$(BOX_LEFT).datalist("deleteRow", rowIndex);
			$(BOX_RIGHT).datalist("appendRow", checkedRows[i]);
		}
	});
		
	// 删除元素
	$(BUTTON_RIGHT).on("click", function() {
		var checkedRows = $(BOX_RIGHT).datalist("getChecked");
		var rowIndex;
		for (var i = 0; i < checkedRows.length; i++) {
			rowIndex = $(BOX_RIGHT).datalist("getRowIndex", checkedRows[i]);
			$(BOX_RIGHT).datalist("deleteRow", rowIndex);
			$(BOX_LEFT).datalist("appendRow", checkedRows[i]);
		}
	});
	
	// 取消
	$(BUTTON_CANCEL).on("click", function() {
		$(DIALOG).modal("hide");
		$("button[id^=_button_lr_list_box]").off();
		hideTips(TIPS);
	});
	
	// 保存
	$(BUTTON_SAVE).on("click", function() {
		if ($.isFunction(saveCallback)) {
			var checkedRows = $(BOX_RIGHT).datalist("getRows");
			saveCallback(checkedRows);
		}
	});
}
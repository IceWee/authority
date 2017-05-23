// 初始化左右列表框
// 依赖Bootstrap样式和脚本/EasyUI样式和脚本/utils.js
// 需要在页面中引入html-lrListBox

(function($) {
	$.fn.selectBox = function(options) {
		var self = this;
		var OK = "200";
		var containerId = this.selector;
		
		this._settings = {
			title: "选择框",
			leftTitle: "未选", 
			rightTitle: "已选",
			leftList: [],
			rightList: [],
			idField: "id",
			textField: "name",
			showText: "Name",
			singleSelect: false,
			checkboxHeader: true,
			clickToSelect: true,
			titleId: "title_select_box",
			leftTitleId: "title_left_select_box",
			rightTitleId: "title_right_select_box",
			leftBtnId: "button_left_select_box",
			rightBtnId: "button_right_select_box",
			cancelBtnId: "button_cancel_select_box",
			saveBtnId: "button_save_select_box",
			leftBoxId: "left_select_box",
			rightBoxId: "right_select_box"
		};
		
		// 初始化
		this._init = function() {
			this._settings = $.extend(self._settings, options);
			this._settings.titleId = containerId + " #" + this._settings.titleId;
			this._settings.leftTitleId = containerId + " #" + this._settings.leftTitleId;
			this._settings.rightTitleId = containerId + " #" + this._settings.rightTitleId;
			this._settings.leftBtnId = containerId + " #" + this._settings.leftBtnId;
			this._settings.rightBtnId = containerId + " #" + this._settings.rightBtnId;
			this._settings.cancelBtnId = containerId + " #" + this._settings.cancelBtnId;
			this._settings.saveBtnId = containerId + " #" + this._settings.saveBtnId;
			this._settings.leftBoxId = containerId + " #" + this._settings.leftBoxId;
			this._settings.rightBoxId = containerId + " #" + this._settings.rightBoxId;
			this._initDialog();
			this._initTables();
			this._bindEvents();
		};
		
		// 初始化弹出框
		this._initDialog = function() {
			var options = this._settings;
			$(options.titleId).text(options.title);
			$(options.leftTitleId).text(options.leftTitle);
			$(options.rightTitleId).text(options.rightTitle);
			this._showDialog();
		};
		
		// 显示弹出框
		this._showDialog = function() {
			$(containerId).modal({
				backdrop: false, // 点击空白区域不关闭弹出框
				keyboard: false	 // 按ESC键不关闭弹出框
			});
		};
		
		// 隐藏弹出框
		this._hideDialog = function() {
			$(containerId).modal("hide");
		};
		
		// 初始化表格
		this._initTables = function() {
			var options = self._settings;
			// 未选
			$(options.leftBoxId).bootstrapTable("removeAll"); // 清空
			$(options.leftBoxId).bootstrapTable({
				idField: options.idField,
				clickToSelect: options.clickToSelect,
				columns: [
					[
						{
	                        checkbox: true,
	                        align: "center",
	                        valign: "middle"
	                    }, {
	                    	field: options.textField,
	                        title: options.showText,
	                        align: "center",
	                        valign: "middle"
	                    }
	                ]
				]
			});
			$(options.leftBoxId).bootstrapTable("load", options.leftList); // 加载
			// 已选
			$(options.rightBoxId).bootstrapTable("removeAll"); // 清空
			$(options.rightBoxId).bootstrapTable({
				idField: options.idField,
				clickToSelect: options.clickToSelect,
				columns: [
					[
						{
	                        field: options.valueField,
	                        checkbox: true,
	                        align: "center",
	                        valign: "middle"
	                    }, {
	                    	field: options.textField,
	                        title: options.showText,
	                        align: "center",
	                        valign: "middle"
	                    }
	                ]
				]
			});
			$(options.rightBoxId).bootstrapTable("load", options.rightList); // 加载
		};
		
		// 绑定事件
		this._bindEvents = function() {
			var options = this._settings;
			$(options.cancelBtnId).off();
			$(options.saveBtnId).off();
			$(options.leftBtnId).off();
			$(options.rightBtnId).off();
			
			// 取消
			$(options.cancelBtnId).on("click", function() {
				self._hideDialog();
			});
			
			// 保存
			$(options.saveBtnId).on("click", function() {
				if ($.isFunction(options.saveCallback)) {
					// 获取选中记录
					var checkedRows = self._getAllRows(options.rightBoxId);
					options.saveCallback(checkedRows);
				}
			});
			
			// 添加
			$(options.leftBtnId).on("click", function() {
				var options = self._settings;
				var checkedRows = self._getCheckedRows(options.leftBoxId);
				var checkedIds = self._getCheckedRowIds(options.leftBoxId);
				$(options.leftBoxId).bootstrapTable("remove", {field: options.idField, values: checkedIds});
				$(options.rightBoxId).bootstrapTable("append", checkedRows);
			});
			
			// 移除
			$(options.rightBtnId).on("click", function() {
				var options = self._settings;
				var checkedRows = self._getCheckedRows(options.rightBoxId);
				var checkedIds = self._getCheckedRowIds(options.rightBoxId);
				$(options.rightBoxId).bootstrapTable("remove", {field: options.idField, values: checkedIds});
				$(options.leftBoxId).bootstrapTable("append", checkedRows);
			});
		};
		
		// 获取选中的记录
		this._getCheckedRows = function(tableId) {
	        return $(tableId).bootstrapTable("getAllSelections");
	    }
		
		// 获取选中的记录主键
		this._getCheckedRowIds = function(tableId) {
			var options = self._settings;
			var idArray = [];
	        var checkedRows = $(tableId).bootstrapTable("getAllSelections");
	        $.each(checkedRows, function(i, row) {
	        	idArray.push(row[options.idField]);
	        });
	        return idArray;
	    }
		
		// 获取全部记录
		this._getAllRows = function(tableId) {
	        return $(tableId).bootstrapTable("getData");
	    }
		
		this._init();
		return this;
	};
})(jQuery);
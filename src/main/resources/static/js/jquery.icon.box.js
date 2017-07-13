// 图标选择弹出框
(function($) {
	$.fn.iconBox = function(options) {
		var self = this;
		var containerId = this.selector;
		
		this._settings = {
			showFooter : true, // 是否显示确认取消按钮
			closeAfterConfirm : false, // 单选选中后自动共关闭弹出框
			closeAfterSelect : false, // 点击后关闭
			showAtonce : false, // 是否立即显示弹出框
			triggerId : null // 触发显示弹出框的元素ID
		};
		
		// 初始化
		this._init = function() {
			this._settings = $.extend(self._settings, options);
			this._settings.btnConfirmId = containerId + " #_button_confirm_icon_box";
			this._settings.btnCancelId = containerId + " #_button_cancel_icon_box";
			this._settings.footerId = containerId + " #_footer_icon_box";
			
			this._initDialog();
			this._bindButtonEvents();
		};
		
		// 初始化弹出框
		this._initDialog = function() {
			var options = this._settings;
			if (options.showAtonce) {
				this._showDialog();
			}
		};
		// 显示弹出框
		this._showDialog = function() {
			$(containerId).modal({
				keyboard: false	 // 按ESC键不关闭弹出框
			});
		};
		// 隐藏弹出框
		this._hideDialog = function() {
			$(containerId).modal("hide");
		};
		// 事件按钮绑定
		this._bindButtonEvents = function() {
			var options = this._settings;
			$(options.btnCancelId).off();
			$(options.btnConfirmId).off();
			
			// 元素点击触发弹出框
			if (options.triggerId) {
				$("#" + options.triggerId).click(function() {
					self._showDialog();
				});
			}
			// 显示确认、取消按钮
			if (options.showFooter) {
				// 取消
				$(options.btnCancelId).on("click", function() {
					self._hideDialog();
				});
				
				// 确认
				$(options.btnConfirmId).on("click", function() {
					// TODO 获得选中的图标并调用回调函数
					if (options.closeAfterConfirm) {
						self._hideDialog();
					}
				});
			} else {
				$(options.footerId).hide();
			}
		};
		
		this._init();
		return this;
	};
})(jQuery);
// jQuery+Bootstrap+zTree contextMenu
(function($) {
	$.fn.ztreeContextMenu = function(options) {
		var self = this;
		var selector = this.selector;
		
		// 初始化
		this._init = function() {
			$(document).click(function() { 
				self.hide();
			});
		};
		// 显示右键菜单
		this.show = function(event) {
			$(selector).css("top", event.clientY + "px");
			$(selector).css("left", event.clientX + "px");
			$(selector).css("display", "block");
		};
		// 隐藏右键菜单
		this.hide = function() {
			$(selector).css("display", "none");
		};
		
		this._init();
		return this;
	};
})(jQuery);
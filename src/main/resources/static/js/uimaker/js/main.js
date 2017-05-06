var mainPlatform = {
	init : function() {
		this.bindEvent();
	},
	bindEvent : function() {
		var self = this;

		// 一级菜单
		$(document).on("click", ".sider-nav li", function() {
			$(".sider-nav li").removeClass("current");
			$(this).addClass("current");
		});
		
		// 二级菜单
		$(document).on("click", ".sider-nav-s li", function() {
			$(".sider-nav li").removeClass("active");
			$(this).addClass("active");
			var url = $(this).attr("url");
			$("#pf-page").find("iframe").eq(0).attr("src", url);
		});
		$(document).on("mouseover", ".sider-nav-s li", function() {
			$(this).addClass("msover");
		});
		$(document).on("mouseout", ".sider-nav-s li", function() {
			$(this).removeClass("msover");
		});
		

		// 退出
		$(document).on("click", ".pf-logout", function() {
			location.href = "/logout";
		});
		
		// 菜单显示/隐藏条
		$(document).on("click", ".toggle-icon", function() {
			$(this).closest("#pf-bd").toggleClass("toggle");
			setTimeout(function() {
				$(window).resize();
			}, 300);
		});

		// 修改密码
		$(document).on("click", ".pf-modify-pwd", function() {
			$("#pf-page").find("iframe").eq(0).attr("src", "/system/user/password");
		});

		// 用户信息
		$(document).on("click", "#user_info", function() {
			$("#pf-page").find("iframe").eq(0).attr("src", "/system/user/info");
		});
	}

};

$(function() {
	mainPlatform.init();
	
	$(window).resize(function() {
		$(".tabs-panels").height($("#pf-page").height() - 46);
		$(".panel-body").height($("#pf-page").height() - 76)
	}).resize();
})
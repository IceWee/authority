
// 初始化列表页面
function initListPage() {
	$("#table_list").btable({url: "/ajax/system/user/list"});
}

// 操作按钮
function operationFormatter(value, row, index) {
	var id = row.id;
	var html = "";
	var label_edit = $.i18n.prop("operate.edit");
	var label_delete = $.i18n.prop("operate.delete");
	var label_auth = $.i18n.prop("operate.authorize");
	var label_lock = $.i18n.prop("operate.lock");
	var label_unlock = $.i18n.prop("operate.unlock");
	if (id) {
		html += "<a href=\"#\" onclick=\"_edit('" + id + "')\"><span class=\"label label-primary\">" + label_edit + "</span></a>";  
		html += "&nbsp;&nbsp;";
		html += "<a href=\"#\" onclick=\"_delete('" + id + "')\"><span class=\"label label-danger\">" + label_delete + "</span></a>";  
		html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"openUserRoleAuth('" + row.id + "', '" + row.name + "')\"><span class=\"label label-warning\">" + label_auth + "</span></a>";
		if (row.status == 1) {
			html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"unlockUser('" + row.id + "')\"><span class=\"label label-success\">" + label_unlock + "</span></a>";
		} else {
			html += "&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"lockUser('" + row.id + "')\"><span class=\"label label-default\">" + label_lock + "</span></a>";
		}
	}
	return html;
}

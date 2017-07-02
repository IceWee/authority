package bing.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import bing.constant.GlobalConstants;
import bing.system.domain.MenuTreeNode;

/**
 * URL拦截器
 * 
 * @author IceWee
 */
@Component("uriHandlerInterceptor")
public class UriHandlerInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(UriHandlerInterceptor.class);

	/**
	 * Controller方法调用之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		String requestMenuId = request.getParameter("requestMenuId");
		LOGGER.debug("当前请求的URI为：{}，请求菜单ID：{}", uri, requestMenuId);
		activeRequestMenus(request, requestMenuId);
		return true;
	}

	/**
	 * 请求处理之后进行调用，但是在视图被渲染之前，即Controller方法调用之后
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行，主要是用于进行资源清理工作
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	/**
	 * 解析请求菜单并激活选中状态
	 * 
	 * @param request
	 * @param requestMenuId
	 */
	@SuppressWarnings({"unchecked"})
	private void activeRequestMenus(HttpServletRequest request, String requestMenuId) {
		HttpSession session = request.getSession(false);
		if (session != null && StringUtils.isNotBlank(requestMenuId)) {
			List<MenuTreeNode> menus = (List<MenuTreeNode>) session.getAttribute(GlobalConstants.SESSION_ATTRIBUTE_MENUS);
			if (menus != null) {
				recurveClearMenuActive(menus);
				activeMenu(recurveFindMenuById(menus, requestMenuId));
			}
		}
	}

	/**
	 * 选中菜单
	 * 
	 * @param menu
	 */
	private void activeMenu(MenuTreeNode menu) {
		if (menu != null) {
			menu.setActive(true);
			MenuTreeNode parent = menu.getParentMenu();
			activeMenu(parent);
		}
	}

	/**
	 * 递归查找菜单对象
	 * 
	 * @param menus
	 * @param menuId
	 * @return
	 */
	private MenuTreeNode recurveFindMenuById(List<MenuTreeNode> menus, String menuId) {
		MenuTreeNode findMenu = null;
		String currId;
		for (MenuTreeNode menuTreeNode : menus) {
			currId = menuTreeNode.getId();
			if (StringUtils.equals(currId, menuId)) {
				findMenu = menuTreeNode;
				break;
			}
			if (menuTreeNode.hasChild()) {
				return recurveFindMenuById(menuTreeNode.getChildren(), menuId);
			}
		}
		return findMenu;
	}

	/**
	 * 递归清除菜单选中状态
	 * 
	 * @param menus
	 */
	private void recurveClearMenuActive(List<MenuTreeNode> menus) {
		for (MenuTreeNode menuTreeNode : menus) {
			menuTreeNode.setActive(false);
			if (menuTreeNode.hasChild()) {
				recurveClearMenuActive(menuTreeNode.getChildren());
			}
		}
	}

}

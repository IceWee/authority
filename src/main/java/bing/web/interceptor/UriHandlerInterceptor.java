package bing.web.interceptor;

import bing.constant.GlobalConstants;
import bing.system.domain.MenuTreeNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * URL拦截器
 *
 * @author IceWee
 */
@Slf4j
@Component("uriHandlerInterceptor")
public class UriHandlerInterceptor implements HandlerInterceptor {

    /**
     * Controller方法调用之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        String menuId = request.getParameter(GlobalConstants.PARAM_MENU_ID);
        log.debug("当前请求的URI为：{}，请求菜单ID：{}", uri, menuId);
        if (StringUtils.isBlank(menuId)) {
            menuId = (String) request.getSession().getAttribute(GlobalConstants.PARAM_MENU_ID);
        }
        request.getSession().setAttribute(GlobalConstants.PARAM_MENU_ID, menuId);
        activeRequestMenus(request, menuId);
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前，即Controller方法调用之后
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行，主要是用于进行资源清理工作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
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
                recursionClearMenuActive(menus);
                recursionActiveMenu(menus, requestMenuId);
            }
        }
    }

    /**
     * 递归激活菜单选中状态
     *
     * @param menus
     * @param menuId
     * @Description
     */
    private void recursionActiveMenu(List<MenuTreeNode> menus, String menuId) {
        MenuTreeNode menu = recursionFindMenuById(menus, menuId);
        if (menu != null) {
            menu.setActive(true);
            recursionActiveMenu(menus, menu.getParentId());
        }
    }

    /**
     * 递归查找菜单对象
     *
     * @param menus
     * @param menuId
     * @return
     */
    private MenuTreeNode recursionFindMenuById(List<MenuTreeNode> menus, String menuId) {
        MenuTreeNode findMenu = null;
        if (StringUtils.isBlank(menuId)) {
            return null;
        }
        String currId;
        for (MenuTreeNode menuTreeNode : menus) {
            currId = menuTreeNode.getId();
            if (StringUtils.equals(currId, menuId)) {
                findMenu = menuTreeNode;
                break;
            }
            if (menuTreeNode.hasChild()) {
                findMenu = recursionFindMenuById(menuTreeNode.getChildren(), menuId);
                if (findMenu != null) {
                    break;
                }
            }
        }
        return findMenu;
    }

    /**
     * 递归清除菜单选中状态
     *
     * @param menus
     */
    private void recursionClearMenuActive(List<MenuTreeNode> menus) {
        for (MenuTreeNode menuTreeNode : menus) {
            menuTreeNode.setActive(false);
            if (menuTreeNode.hasChild()) {
                recursionClearMenuActive(menuTreeNode.getChildren());
            }
        }
    }

}

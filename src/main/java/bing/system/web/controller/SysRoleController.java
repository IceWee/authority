package bing.system.web.controller;

import bing.constant.GlobalConstants;
import bing.constant.LogPrefixes;
import bing.constant.MessageKeys;
import bing.domain.CurrentLoggedUser;
import bing.domain.GenericPage;
import bing.system.condition.SysRoleCondition;
import bing.system.model.SysOperateLog;
import bing.system.model.SysRole;
import bing.system.model.SysUser;
import bing.system.service.SysOperateLogService;
import bing.system.service.SysRoleService;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysRoleVO;
import bing.system.vo.UserRoleVO;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/")
public class SysRoleController extends GenericController {

    private static final String MODULE_NAME = "角色管理";

    private static final String LOG_PREFIX = LogPrefixes.ROLE;
    private static final String PREFIX = "system/role";
    private static final String AJAX_ROLE_LIST = "ajax/system/role/list";
    private static final String AJAX_ROLE_SAVE = "ajax/system/role/save";

    private static final String LIST = PREFIX + "/list";
    private static final String ADD = PREFIX + "/add";
    private static final String SAVE = PREFIX + "/save";
    private static final String EDIT = PREFIX + "/edit";
    private static final String UPDATE = PREFIX + "/update";
    private static final String DELETE = PREFIX + "/delete";
    private static final String REDIRECT_LIST = "redirect:/" + LIST;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysOperateLogService sysOperateLogService;

    @RequestMapping(LIST)
    public String list() {
        return LIST;
    }

    @ResponseBody
    @RequestMapping(value = AJAX_ROLE_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResponse<GenericPage<SysRoleVO>> roles(SysRoleCondition condition) {
        RestResponse<GenericPage<SysRoleVO>> response = new RestResponse<>();
        GenericPage<SysRoleVO> page = sysRoleService.listByPage(condition);
        response.setData(page);
        return response;
    }

    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AJAX_ROLE_LIST + "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResponse<UserRoleVO> getUserRoles(@PathVariable Integer userId) {
        RestResponse<UserRoleVO> response = new RestResponse<>();
        UserRoleVO data = sysRoleService.getUserRoles(userId);
        response.setData(data);
        return response;
    }

    /**
     * 保存用户角色列表
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AJAX_ROLE_SAVE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResponse<RoleUserVO> saveUserRoles(Integer userId, Integer[] roleIds, @CurrentLoggedUser SysUser currentUser) {
        String username = currentUser.getUsername();
        sysRoleService.saveUserRoles(userId, roleIds, username);
        String operateContent = "配置了用户与角色关系，用户ID[" + userId + "]，角色ID列表[" + Arrays.toString(roleIds) + "]";
        sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_ADD, currentUser.getId(), currentUser.getName(), operateContent));
        return new RestResponse<>();
    }

    @RequestMapping(ADD)
    public String add(Model model) {
        addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysRole());
        return ADD;
    }

    @RequestMapping(value = SAVE, method = RequestMethod.POST)
    public String save(@Valid SysRole entity, BindingResult bindingResult, Model model, RedirectAttributesModelMap redirectModel, @CurrentLoggedUser SysUser currentUser) {
        addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
        if (hasErrors(bindingResult, model)) {
            return ADD;
        }
        try {
            Date now = new Date();
            entity.setCreateUser(currentUser.getName());
            entity.setCreateDate(now);
            entity.setUpdateUser(currentUser.getName());
            entity.setUpdateDate(now);
            sysRoleService.save(entity);
            String operateContent = "添加了角色[" + entity + "]";
            sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_ADD, currentUser.getId(), currentUser.getName(), operateContent));
            setMessage(MessageKeys.SAVE_SUCCESS, redirectModel);
        } catch (Exception e) {
            log.error("{}保存异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
            setError(e, model);
            return ADD;
        }
        return REDIRECT_LIST;
    }

    @RequestMapping(EDIT)
    public String edit(@RequestParam(value = "id", required = true) Integer id, Model model, RedirectAttributesModelMap redirectModel) {
        SysRole entity = sysRoleService.getById(id);
        if (entity == null) {
            setError(MessageKeys.ENTITY_NOT_EXIST, redirectModel);
            return REDIRECT_LIST;
        }
        addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
        return EDIT;
    }

    @RequestMapping(value = UPDATE, method = RequestMethod.POST)
    public String update(@Valid SysRole entity, BindingResult bindingResult, Model model, RedirectAttributesModelMap redirectModel, @CurrentLoggedUser SysUser currentUser) {
        addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
        if (hasErrors(bindingResult, model)) {
            return EDIT;
        }
        try {
            entity.setUpdateUser(currentUser.getName());
            entity.setUpdateDate(new Date());
            sysRoleService.update(entity);
            String operateContent = "修改了角色信息[" + entity + "]";
            sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_MODIFY, currentUser.getId(), currentUser.getName(), operateContent));
            setMessage(MessageKeys.UPDATE_SUCCESS, redirectModel);
        } catch (Exception e) {
            log.error("{}更新异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
            setError(e, model);
            return EDIT;
        }
        return REDIRECT_LIST;
    }

    @RequestMapping(DELETE)
    public String delete(@RequestParam(value = "id", required = true) Integer id, RedirectAttributesModelMap redirectModel, @CurrentLoggedUser SysUser currentUser) {
        try {
            String username = currentUser.getUsername();
            sysRoleService.deleteById(id, username);
            String operateContent = "删除了角色[" + id + "]";
            sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_DELETE, currentUser.getId(), currentUser.getName(), operateContent));
            setMessage(MessageKeys.DELETE_SUCCESS, redirectModel);
        } catch (Exception e) {
            log.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
            setError(e, redirectModel);
        }
        return REDIRECT_LIST;
    }

}

package bing.system.web.controller;

import bing.constant.Charsets;
import bing.constant.GenderEnum;
import bing.constant.GlobalConstants;
import bing.constant.LogPrefixes;
import bing.constant.MessageKeys;
import bing.domain.CrudGroups;
import bing.domain.CurrentLoggedUser;
import bing.domain.GenericPage;
import bing.domain.LabelValueBean;
import bing.exception.BusinessException;
import bing.system.condition.SysUserCondition;
import bing.system.exception.UserExceptionCodes;
import bing.system.model.SysOperateLog;
import bing.system.model.SysRole;
import bing.system.model.SysUser;
import bing.system.service.SysOperateLogService;
import bing.system.service.SysRoleService;
import bing.system.service.SysUserService;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysUserVO;
import bing.system.vo.UserRoleVO;
import bing.util.EasyPOIUtils;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "User", description = "用户接口")
@Slf4j
@Controller
@RequestMapping("/")
public class SysUserController extends GenericController {

    private static final String MODULE_NAME = "用户管理";

    private static final String LOG_PREFIX = LogPrefixes.USER;
    private static final String PREFIX = "system/user";
    private static final String AJAX_USER_LIST = "ajax/system/user/list";
    private static final String AJAX_USER_SAVE = "ajax/system/user/save";
    private static final String AJAX_USER_MINE_UPDATE = "ajax/system/user/mine/update";
    private static final String AJAX_USER_PASSWORD = "ajax/system/user/password";
    private static final String AJAX_USER_RESET_PASSWORD = "ajax/system/user/resetPassword";

    private static final String LIST = PREFIX + "/list";
    private static final String ADD = PREFIX + "/add";
    private static final String SAVE = PREFIX + "/save";
    private static final String EDIT = PREFIX + "/edit";
    private static final String UPDATE = PREFIX + "/update";
    private static final String DELETE = PREFIX + "/delete";
    private static final String MINE = PREFIX + "/mine"; // 我的信息
    private static final String LOCK = PREFIX + "/lock"; // 锁定用户
    private static final String UNLOCK = PREFIX + "/unlock"; // 解除锁定用户
    private static final String EXPORT = PREFIX + "/export"; // 导出
    private static final String REDIRECT_LIST = "redirect:/" + LIST;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysOperateLogService sysOperateLogService;

    @ModelAttribute("roleList")
    protected List<SysRole> roleList() {
        return sysRoleService.listAll();
    }

    /**
     * 性别列表
     *
     * @return
     */
    @ModelAttribute("genderList")
    protected List<LabelValueBean> genderList() {
        List<LabelValueBean> genderList = new ArrayList<>();
        genderList.add(new LabelValueBean("男", String.valueOf(GenderEnum.MALE.ordinal())));
        genderList.add(new LabelValueBean("女", String.valueOf(GenderEnum.FEMALE.ordinal())));
        return genderList;
    }

    @RequestMapping(LIST)
    public String list() {
        return LIST;
    }

    @ResponseBody
    @RequestMapping(value = AJAX_USER_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "用户数据查询", notes = "用户数据查询")
    @ApiResponse(code = 200, response = RestResponse.class, message = "")
    public RestResponse<GenericPage<SysUserVO>> users(SysUserCondition condition) {
        RestResponse<GenericPage<SysUserVO>> response = new RestResponse<>();
        GenericPage<SysUserVO> page = sysUserService.listByPage(condition);
        response.setData(page);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = AJAX_USER_LIST + "/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "根据角色获取用户列表", notes = "根据角色ID获取属于该角色的用户列表")
    @ApiResponse(code = 200, response = RestResponse.class, message = "")
    public RestResponse<RoleUserVO> getRoleUsers(@PathVariable Integer roleId) {
        RestResponse<RoleUserVO> response = new RestResponse<>();
        RoleUserVO data = sysUserService.getRoleUsers(roleId);
        response.setData(data);
        return response;
    }

    /**
     * 保存角色用户关联关系
     *
     * @param roleId
     * @param userIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AJAX_USER_SAVE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "保存角色用户关联关系", notes = "保存角色用户关联关系")
    @ApiResponse(code = 200, response = RestResponse.class, message = "")
    public RestResponse<Object> saveRoleUsers(Integer roleId, Integer[] userIds, @CurrentLoggedUser SysUser currentUser) {
        RestResponse<Object> response = new RestResponse<>();
        String username = currentUser.getUsername();
        sysUserService.saveRoleUsers(roleId, userIds, username);
        String operateContent = "配置了角色与用户关系，角色ID[" + roleId + "]，用户ID列表[" + Arrays.toString(userIds) + "]";
        sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_ADD, currentUser.getId(), currentUser.getName(), operateContent));
        return response;
    }

    @RequestMapping(ADD)
    public String add(Model model) {
        addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysUser());
        return ADD;
    }

    @RequestMapping(value = SAVE, method = RequestMethod.POST)
    public String save(@Validated(CrudGroups.Create.class) SysUser entity, BindingResult bindingResult, Model model, RedirectAttributesModelMap redirectModel, @CurrentLoggedUser SysUser currentUser) {
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
            sysUserService.save(entity);
            String operateContent = "添加了用户[" + entity + "]";
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
    public String edit(@RequestParam(value = "id") Integer id, Model model, RedirectAttributesModelMap redirectModel) {
        SysUser entity = sysUserService.getById(id);
        if (entity == null) {
            setError(MessageKeys.ENTITY_NOT_EXIST, redirectModel);
            return REDIRECT_LIST;
        }
        UserRoleVO userRoleVO = sysRoleService.getUserRoles(id);
        List<SysRole> roles = userRoleVO.getSelectedRoles();
        List<Integer> roleIdList = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
        Integer[] roleIds = new Integer[roleIdList.size()];
        roleIdList.toArray(roleIds);
        entity.setRoleIds(roleIds);
        addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
        return EDIT;
    }

    @RequestMapping(value = UPDATE, method = RequestMethod.POST)
    public String update(@Valid SysUser entity, BindingResult bindingResult, Model model, RedirectAttributesModelMap redirectModel, @CurrentLoggedUser SysUser currentUser) {
        addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
        if (hasErrors(bindingResult, model)) {
            return EDIT;
        }
        try {
            entity.setUpdateUser(currentUser.getName());
            entity.setUpdateDate(new Date());
            sysUserService.updateWithRole(entity);
            String operateContent = "修改了用户信息[" + entity + "]";
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
    public String delete(@RequestParam(value = "id") Integer id, RedirectAttributesModelMap redirectModel, @CurrentLoggedUser SysUser currentUser) {
        try {
            String username = currentUser.getName();
            sysUserService.deleteById(id, username);
            String operateContent = "删除了用户[" + id + "]";
            sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_DELETE, currentUser.getId(), currentUser.getName(), operateContent));
            setMessage(MessageKeys.DELETE_SUCCESS, redirectModel);
        } catch (Exception e) {
            log.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
            setError(e, redirectModel);
        }
        return REDIRECT_LIST;
    }

    /**
     * 保存修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AJAX_USER_PASSWORD, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "当前登录用户修改密码", notes = "当前登录用户修改密码")
    @ApiResponse(code = 200, response = RestResponse.class, message = "")
    public RestResponse<Object> savePassword(@RequestParam(name = "oldPassword") String oldPassword, @RequestParam(name = "newPassword") String newPassword, @CurrentLoggedUser SysUser currentUser) {
        sysUserService.changePassword(currentUser.getId(), oldPassword, newPassword);
        return new RestResponse<>();
    }

    /**
     * 重置密码
     *
     * @param userId
     * @param newPassword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AJAX_USER_RESET_PASSWORD, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码")
    @ApiResponse(code = 200, response = RestResponse.class, message = "")
    public RestResponse<Object> resetPassword(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "newPassword") String newPassword, @CurrentLoggedUser SysUser currentUser) {
        sysUserService.resetPassword(userId, newPassword, currentUser.getName());
        String operateContent = "重置了用户[" + userId + "]密码";
        sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_MODIFY, currentUser.getId(), currentUser.getName(), operateContent));
        return new RestResponse<>();
    }

    /**
     * 我的信息
     *
     * @return
     */
    @RequestMapping(MINE)
    public String mine(Model model, @CurrentLoggedUser SysUser currentUser) {
        SysUser entity = sysUserService.getById(currentUser.getId());
        addAttribute(model, GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
        return MINE;
    }

    /**
     * 保存我的信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AJAX_USER_MINE_UPDATE, method = RequestMethod.PUT)
    @ApiOperation(value = "当前登录用户修改个人信息", notes = "当前登录用户修改个人信息")
    @ApiResponse(code = 200, response = RestResponse.class, message = "")
    public RestResponse<Object> updateMine(SysUser mine, @CurrentLoggedUser SysUser currentUser) {
        if (StringUtils.isBlank(mine.getName())) {
            throw new BusinessException(UserExceptionCodes.NAME_IS_NULL);
        }
        mine.setId(currentUser.getId());
        sysUserService.update(mine);
        return new RestResponse<>();
    }

    /**
     * 锁定用户
     *
     * @param id
     * @param redirectModel
     * @param currentUser
     * @return
     */
    @RequestMapping(LOCK)
    public String lock(@RequestParam(value = "id") Integer id, RedirectAttributesModelMap redirectModel, @CurrentLoggedUser SysUser currentUser) {
        try {
            String username = currentUser.getName();
            sysUserService.lockById(id, username);
            String operateContent = "锁定了用户[" + id + "]";
            sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_MODIFY, currentUser.getId(), currentUser.getName(), operateContent));
            setMessage(MessageKeys.LOCK_SUCCESS, redirectModel);
        } catch (Exception e) {
            log.error("{}锁定异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
            setError(e, redirectModel);
        }
        return REDIRECT_LIST;
    }

    /**
     * 解除锁定用户
     *
     * @param id
     * @param redirectModel
     * @param currentUser
     * @return
     */
    @RequestMapping(UNLOCK)
    public String unlock(@RequestParam(value = "id") Integer id, RedirectAttributesModelMap redirectModel, @CurrentLoggedUser SysUser currentUser) {
        try {
            String username = currentUser.getName();
            sysUserService.unlockById(id, username);
            String operateContent = "解除锁定了用户[" + id + "]";
            sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_MODIFY, currentUser.getId(), currentUser.getName(), operateContent));
            setMessage(MessageKeys.UNLOCK_SUCCESS, redirectModel);
        } catch (Exception e) {
            log.error("{}解除锁定异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
            setError(e, redirectModel);
        }
        return REDIRECT_LIST;
    }

    /**
     * 数据导出
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = EXPORT)
    public ResponseEntity<byte[]> export(SysUserCondition condition) {
        try {
            condition.setPageNumber(1);
            condition.setPageSize(Integer.MAX_VALUE);
            GenericPage<SysUserVO> page = sysUserService.listByPage(condition);
            HttpHeaders headers = new HttpHeaders();
            List<SysUserVO> rows = page.getRows();
            Workbook workbook = EasyPOIUtils.exportExcel(SysUserVO.class, rows);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            String filename = "用户列表." + System.currentTimeMillis() + ".xls";
            filename = new String(filename.getBytes(Charsets.CHARSET_UTF_8), Charsets.CHARSET_ISO_8859_1);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("{}导出Excel异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
            throw new RuntimeException(e);
        }

    }

}

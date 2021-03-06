package bing.system.service;

import bing.constant.StatusEnum;
import bing.domain.GenericPage;
import bing.exception.BusinessException;
import bing.system.condition.SysUserCondition;
import bing.system.dao.SysUserDao;
import bing.system.dao.SysUserRoleDao;
import bing.system.exception.UserExceptionCodes;
import bing.system.model.SysUser;
import bing.system.model.SysUserRole;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysUserVO;
import bing.util.PasswordUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public GenericPage<SysUserVO> listByPage(SysUserCondition condition) {
        int pageNumber = condition.getPageNumber();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNumber, condition.getPageSize());
        List<SysUserVO> list = sysUserDao.listByCondition(condition);
        PageInfo<SysUserVO> pageInfo = new PageInfo<>(list);
        return new GenericPage<>(pageNumber, pageSize, pageInfo.getTotal(), list);
    }

    @Override
    @Transactional
    public void save(SysUser entity) {
        entity.setId(null);
        SysUser persistUser = sysUserDao.getByUsername(entity.getUsername());
        if (persistUser != null) {
            throw new BusinessException(UserExceptionCodes.USERNAME_REGISTERED);
        }
        entity.setCreateDate(new Date());
        entity.setUpdateDate(new Date());
        // 密码加密保存
        String rawPassword = entity.getPassword();
        String encryptPasswd = PasswordUtils.encrypt(rawPassword);
        entity.setPassword(encryptPasswd);
        sysUserDao.insert(entity);
        Integer userId = entity.getId();
        String username = entity.getUpdateUser();
        // 前端选择了角色
        Integer[] roleIds = entity.getRoleIds();
        if (ArrayUtils.isNotEmpty(roleIds)) {
            List<SysUserRole> entities = Arrays.asList(roleIds).stream().map(roleId -> createUserRole(userId, roleId, username)).collect(Collectors.toList());
            sysUserRoleDao.insertBatch(entities);
        }
    }

    @Override
    public void update(SysUser entity) {
        SysUser persistUser = sysUserDao.selectByPrimaryKey(entity.getId());
        String originUsername = persistUser.getUsername();
        String newUsername = entity.getUsername();
        if (!StringUtils.equals(originUsername, newUsername)) {
            throw new BusinessException(UserExceptionCodes.USERNAME_FORBIDDEN_MODIFY);
        }
        entity.setCreateDate(new Date());
        entity.setUpdateDate(new Date());
        sysUserDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public SysUser getById(Integer id) {
        return sysUserDao.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void deleteById(Integer id, String username) {
        SysUser entity = new SysUser();
        entity.setId(id);
        entity.setStatus(StatusEnum.DELETED.ordinal());
        entity.setUpdateUser(username);
        entity.setUpdateDate(new Date());
        sysUserDao.updateByPrimaryKeySelective(entity);
        sysUserRoleDao.deleteByUserId(id);
    }

    @Override
    public RoleUserVO getRoleUsers(Integer roleId) {
        List<SysUser> users = sysUserDao.listAll();
        List<SysUser> selectedUsers = sysUserDao.listByRoleId(roleId);
        List<Integer> selectedUserIds = selectedUsers.stream().map(selectedUser -> selectedUser.getId()).collect(Collectors.toList());
        List<SysUser> unselectUsers = users.stream().filter(user -> !selectedUserIds.contains(user.getId())).collect(Collectors.toList());
        RoleUserVO roleUsers = new RoleUserVO();
        roleUsers.setRoleId(roleId);
        roleUsers.setUnselectUsers(unselectUsers);
        roleUsers.setSelectedUsers(selectedUsers);
        return roleUsers;
    }

    @Override
    @Transactional
    public void saveRoleUsers(Integer roleId, Integer[] userIds, String username) {
        sysUserRoleDao.deleteByRoleId(roleId);
        if (ArrayUtils.isNotEmpty(userIds)) {
            List<SysUserRole> entities = Arrays.asList(userIds).stream().map(userId -> createUserRole(userId, roleId, username)).collect(Collectors.toList());
            sysUserRoleDao.insertBatch(entities);
        }
    }

    @Override
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        SysUser user = sysUserDao.selectByPrimaryKey(userId);
        if (!PasswordUtils.match(oldPassword, user.getPassword())) {
            throw new BusinessException(UserExceptionCodes.ORIGION_PASSWORD_WRONG);
        }
        String encryptPassword = PasswordUtils.encrypt(newPassword);
        user.setPassword(encryptPassword);
        sysUserDao.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional
    public void updateWithRole(SysUser entity) {
        update(entity);
        Integer userId = entity.getId();
        String username = entity.getUpdateUser();
        // 重新授权角色
        sysUserRoleDao.deleteByUserId(userId);
        Integer[] roleIds = entity.getRoleIds();
        if (ArrayUtils.isNotEmpty(roleIds)) {
            List<SysUserRole> entities = Arrays.asList(roleIds).stream().map(roleId -> createUserRole(userId, roleId, username)).collect(Collectors.toList());
            sysUserRoleDao.insertBatch(entities);
        }
    }

    @Override
    public void lockById(Integer id, String username) {
        SysUser entity = new SysUser();
        entity.setId(id);
        entity.setStatus(StatusEnum.LOCKED.ordinal());
        entity.setUpdateUser(username);
        entity.setUpdateDate(new Date());
        sysUserDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public void unlockById(Integer id, String username) {
        SysUser entity = new SysUser();
        entity.setId(id);
        entity.setStatus(StatusEnum.NORMAL.ordinal());
        entity.setUpdateUser(username);
        entity.setUpdateDate(new Date());
        sysUserDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public void resetPassword(Integer userId, String newPassword, String username) {
        SysUser user = sysUserDao.selectByPrimaryKey(userId);
        String encryptPasswd = PasswordUtils.encrypt(newPassword);
        user.setPassword(encryptPasswd);
        user.setUpdateUser(username);
        user.setUpdateDate(new Date());
        sysUserDao.updateByPrimaryKeySelective(user);
    }

    private SysUserRole createUserRole(Integer userId, Integer roleId, String username) {
        SysUserRole entity = new SysUserRole(userId, roleId);
        Date now = new Date();
        entity.setCreateDate(now);
        entity.setCreateUser(username);
        entity.setUpdateDate(now);
        entity.setUpdateUser(username);
        return entity;
    }

}
package bing.dao;

import bing.model.SysMenuPermission;

public interface SysMenuPermissionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMenuPermission record);

    int insertSelective(SysMenuPermission record);

    SysMenuPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMenuPermission record);

    int updateByPrimaryKey(SysMenuPermission record);
}
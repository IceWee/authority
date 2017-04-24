package bing.dao;

import bing.model.SysGroupPermission;

public interface SysGroupPermissionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysGroupPermission record);

    int insertSelective(SysGroupPermission record);

    SysGroupPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysGroupPermission record);

    int updateByPrimaryKey(SysGroupPermission record);
}
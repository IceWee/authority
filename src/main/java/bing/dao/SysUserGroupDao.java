package bing.dao;

import bing.model.SysUserGroup;

public interface SysUserGroupDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserGroup record);

    int insertSelective(SysUserGroup record);

    SysUserGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserGroup record);

    int updateByPrimaryKey(SysUserGroup record);
}
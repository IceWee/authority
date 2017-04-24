package bing.dao;

import bing.model.SysRoleRight;

public interface SysRoleRightDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleRight record);

    int insertSelective(SysRoleRight record);

    SysRoleRight selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleRight record);

    int updateByPrimaryKey(SysRoleRight record);
}
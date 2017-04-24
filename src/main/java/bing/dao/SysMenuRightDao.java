package bing.dao;

import bing.model.SysMenuRight;

public interface SysMenuRightDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMenuRight record);

    int insertSelective(SysMenuRight record);

    SysMenuRight selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMenuRight record);

    int updateByPrimaryKey(SysMenuRight record);
}
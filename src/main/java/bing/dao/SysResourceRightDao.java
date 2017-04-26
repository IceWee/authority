package bing.dao;

import bing.model.SysResourceRight;

public interface SysResourceRightDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysResourceRight record);

    int insertSelective(SysResourceRight record);

    SysResourceRight selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysResourceRight record);

    int updateByPrimaryKey(SysResourceRight record);
}
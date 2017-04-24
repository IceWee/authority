package bing.dao;

import bing.model.SysRight;

public interface SysRightDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRight record);

    int insertSelective(SysRight record);

    SysRight selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRight record);

    int updateByPrimaryKey(SysRight record);
}
package bing.dao;

import bing.model.SysGroupRight;

public interface SysGroupRightDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysGroupRight record);

    int insertSelective(SysGroupRight record);

    SysGroupRight selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysGroupRight record);

    int updateByPrimaryKey(SysGroupRight record);
}
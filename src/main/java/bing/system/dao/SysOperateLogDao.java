package bing.system.dao;

import java.util.List;

import bing.system.condition.SysOperateLogCondition;
import bing.system.model.SysOperateLog;
import bing.system.vo.SysOperateLogVO;

public interface SysOperateLogDao {

	int insert(SysOperateLog entity);

	List<SysOperateLogVO> listByCondition(SysOperateLogCondition condition);

}
package bing.system.service;

import bing.domain.GenericPage;
import bing.system.condition.SysOperateLogCondition;
import bing.system.model.SysOperateLog;
import bing.system.vo.SysOperateLogVO;

public interface SysOperateLogService {

	void log(SysOperateLog log);

	GenericPage<SysOperateLogVO> listByPage(SysOperateLogCondition condition);

}

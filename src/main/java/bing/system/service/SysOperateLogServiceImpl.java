package bing.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bing.system.dao.SysOperateLogDao;
import bing.system.model.SysOperateLog;
import bing.util.ExceptionUtils;

/**
 * @author IceWee
 */
@Service
public class SysOperateLogServiceImpl implements SysOperateLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysOperateLogServiceImpl.class);

	@Autowired
	private SysOperateLogDao sysOperateLogDao;

	/**
	 * 异步
	 */
	@Async
	@Override
	public void log(SysOperateLog log) {
		try {
			sysOperateLogDao.insert(log);
		} catch (Exception e) {
			LOGGER.error("记录系统操作日志时出现了异常：\n{}", ExceptionUtils.parseStackTrace(e));
		}
	}

}

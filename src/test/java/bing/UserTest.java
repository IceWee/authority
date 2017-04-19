package bing;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import bing.dao.SysUserDao;
import bing.model.SysUser;

public class UserTest extends BaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserTest.class);

	@Autowired
	private SysUserDao sysUserDao;

	@Test
	public void testGetUser() {
		SysUser sysUser = sysUserDao.selectByPrimaryKey(1);
		LOGGER.info("user: {}", sysUser);
	}

}

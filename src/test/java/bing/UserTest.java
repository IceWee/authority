package bing;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import bing.dao.SysUserDao;
import bing.domain.GenericPage;
import bing.model.SysUser;
import bing.service.SysUserService;

public class UserTest extends BaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserTest.class);

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysUserService sysUserService;

	@Test
	public void testGetByPK() {
		SysUser sysUser = sysUserDao.selectByPrimaryKey(1);
		LOGGER.info("user: {}", sysUser);
	}

	@Test
	public void testGetByUsername() {
		SysUser sysUser = sysUserDao.getByUsername("admin");
		LOGGER.info("user: {}", sysUser);
	}

	@Test
	public void testListByName() {
		GenericPage<SysUser> page = sysUserService.listByPage(1, 10, "A");
		LOGGER.info("Pages: {}", page.getTotalPages());
		LOGGER.info("Rows: {}", page.getTotalRows());
	}
	
	@Test
	public void testGenPassword() {
		String rawPass = "admin";
		String salt = "admin";
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		String encodedPass = encoder.encodePassword(rawPass, "");
		LOGGER.info("encoded password: {}", encodedPass);
	}

}

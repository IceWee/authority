package bing;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthorityApplication.class)
@Transactional
@Rollback(value = false)
public class BaseTest {

	public BaseTest() {
		super();
	}

}

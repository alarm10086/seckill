package org.seckill;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务逻辑测试
 */

/** 配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit */
@RunWith(SpringJUnit4ClassRunner.class)
/** 告诉junit spring配置文件 */
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
/** 配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用 */
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class BaseTest {
    @Test
    public void test() {
    }
}

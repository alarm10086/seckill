package org.seckill.dao;

import org.junit.Test;
import org.seckill.BaseTest;
import org.seckill.entity.SuccessKilled;

import javax.annotation.Resource;

public class SuccessKilledDaoTest extends BaseTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void testInsertSuccessKilled() throws Exception {
        /**
         * 第一次:insertCount=1
         * 第二次:insertCount=0
         */
        final long id = 1001;
        final long phone = 13631231234L;
        final int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println("insertCount=" + insertCount);
    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {
        final long id = 1001;
        final long phone = 13631231234L;
        final SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}

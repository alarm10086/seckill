package org.seckill.service.impl;

import org.junit.Test;
import org.seckill.BaseTest;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SeckillServiceImplTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception {
        final List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void testGetById() throws Exception {
        final long id = 1000;
        final Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    // 测试代码完整逻辑，注意可重复执行
    @Test
    public void testSeckillLogic() throws Exception {
        final long id = 1001;
        final Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            final long phone = 13631231234L;
            final String md5 = exposer.getMd5();
            try {
                final SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
                logger.info("execution={}", execution);
            } catch (final RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (final SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            // 秒杀未开启
            logger.error("exposer={}", exposer);
        }
    }

    @Test
    public void testExecuteSeckillProcedure() throws Exception {
        final long seckillId = 1001;
        final long phone = 13631231234L;
        final Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            final String md5 = exposer.getMd5();
            final SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone,
                    md5);
            logger.info(execution.getStateInfo());
        }
    }

}

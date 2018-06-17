package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller // @Service @Componet
@RequestMapping("/seckill") // url:/模块/资源/{id}/细分 /seckill/list
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(final Model model) {
        // 获取列表页
        final List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        // list.jsp + model = ModelAndView
        return "list";// WEB-INF/jsp/"list".jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") final Long seckillId, final Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        final Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    // ajax json
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,
            produces = { "application/json; charset=utf-8" })
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") final Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            final Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,
            produces = { "application/json; charset=utf-8" })
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") final Long seckillId,
            @PathVariable("md5") final String md5,
            @CookieValue(value = "killPhone", required = false) final Long phone) {
        // springmvc valid
        if (phone == null) {
            return new SeckillResult<>(false, "未注册");
        }
        try {
            // 存储过程调用
            final SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId,
                    phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (final RepeatKillException e) {
            final SeckillExecution execution = new SeckillExecution(seckillId,
                    SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (final SeckillCloseException e) {
            final SeckillExecution execution = new SeckillExecution(seckillId,
                    SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            final SeckillExecution execution = new SeckillExecution(seckillId,
                    SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        final Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

}

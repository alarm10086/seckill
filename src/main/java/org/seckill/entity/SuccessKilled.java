package org.seckill.entity;

import java.util.Date;

/**
 * 成功秒杀实体
 *
 * @author 李奕锋
 */
public class SuccessKilled {

    private long seckillId;

    private long userPhone;

    private short state;

    private Date creteTime;

    // 多对一的复合属性
    private Seckill seckill;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(final long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(final long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(final short state) {
        this.state = state;
    }

    public Date getCreteTime() {
        return creteTime;
    }

    public void setCreteTime(final Date creteTime) {
        this.creteTime = creteTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(final Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled [seckillId=" + seckillId + ", userPhone=" + userPhone + ", state=" +
                state
                + ", creteTime=" + creteTime + "]";
    }

}

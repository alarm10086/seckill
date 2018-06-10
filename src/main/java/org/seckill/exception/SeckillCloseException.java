package org.seckill.exception;

/**
 * 秒杀关闭异常
 *
 * @author 李奕锋
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(final String message) {
        super(message);
    }

    public SeckillCloseException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

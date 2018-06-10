package org.seckill.exception;

/**
 * 重复秒杀异常（运行期异常）
 *
 * @author 李奕锋
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(final String message) {
        super(message);
    }

    public RepeatKillException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

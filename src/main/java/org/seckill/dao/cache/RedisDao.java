package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    private final RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(final String ip, final int port) {
        jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(final long seckillId) {
        // redis操作逻辑
        try {
            final Jedis jedis = jedisPool.getResource();
            try {
                final String key = "seckill:" + seckillId;
                // 并没有实现内部序列化操作
                // get -> byte[] -> 反序列化 -> object[Seckill]
                // 采用自定义序列化
                // protostuff : pojo.
                final byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    final Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    // seckill被反序列化
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String putSeckill(final Seckill seckill) {
        // set Object(Seckill) -> 序列号 -> byte[]
        try {
            final Jedis jedis = jedisPool.getResource();
            try {
                final String key = "seckill:" + seckill.getSeckillId();
                final byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 超时缓存
                final int timeout = 60 * 60;
                final String result = jedis.setex(key.getBytes(), timeout, bytes); // 单位秒
                return result;
            } finally {
                jedis.close();
            }
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}

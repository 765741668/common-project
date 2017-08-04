package com.yz.common.core.queue.redis;

import com.alibaba.fastjson.JSON;
import com.yz.common.core.queue.IMessageQueue;
import com.yz.common.core.queue.QueueHandler;
import com.yz.common.core.redis.RedisUtil;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * 基于redis实现消息队列
 * Created by yangzhao on 16/11/14.
 */
@Component("rMessageQueue")
public class RMessageQueue implements IMessageQueue {
    @Override
    public boolean publish(String channel, Object o) {
        Jedis jedis = RedisUtil.getInstance().getJedis();
        Long publisher = jedis.publish(channel, JSON.toJSONString(o));
        logger.info("订阅该频道的人数：" + publisher);
        jedis.close();
        return true;
    }

    @Override
    public void subscribe(String channel, QueueHandler queueHandler) throws Exception {
        Jedis jedis = RedisUtil.getInstance().getJedis();
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                queueHandler.responseData(message);
            }
        }, channel);
//		    jedis.close();
    }

    @Override
    public void put(String channel, Object o) throws InterruptedException {
        this.publish(channel, o);
    }
}

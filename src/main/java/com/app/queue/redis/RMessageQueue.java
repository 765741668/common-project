package com.app.queue.redis;

import com.app.queue.IMessageQueue;
import com.app.queue.QueueHandler;
import com.app.redis.RedisUtil;
import com.app.utils.JsonUtil;
import org.springframework.stereotype.Component;

/**
 * 基于redis实现消息队列
 * Created by yangzhao on 16/11/14.
 */
@Component("rMessageQueue")
public class RMessageQueue implements IMessageQueue {
    @Override
    public boolean publish(String channel, Object o) {
        RedisUtil.getInstance().publish(channel, JsonUtil.parse(o));
        return true;
    }

    @Override
    public void subscribe(String channel, QueueHandler queueHandler) throws Exception {
        new Thread(()->{
            RedisUtil.getInstance().subscribe(channel,queueHandler);
        }).start();
    }

    @Override
    public void put(String channel, Object o) throws InterruptedException {
        this.publish(channel, o);
    }
}

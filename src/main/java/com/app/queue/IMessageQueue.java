package com.app.queue;


public interface IMessageQueue {


    public boolean publish (String channel,Object o);


    public void subscribe(String channel,QueueHandler queueHandler) throws Exception;


    public void put(String channel,Object o) throws InterruptedException;
}

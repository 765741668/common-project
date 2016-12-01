# common-project
Java后端常用工具类、缓存接口封装、消息队列接口封装。

1.缓存接口介绍(com.app.cache)：

    ① 基于JVM方法区数据缓存

    ② 基于redis进行数据缓存

2.消息队列（com.app.queue 目前只实现了两种）

    ① 基于Java ArrayBlockingQueue

    ② 基于redis 发布订阅实现消息队列

  ▲ 后期会维护kafka和阿里巴巴RocketMQ

3.常用工具类(com.app.utils)

    网络类(HttpUtil)

    时间类(DateUtils)

    JSON类(JsonUtil)

    安全类(MD5加密、AES加密、Base64编码、3DES加密、RSA加密、SHA256)

    图片处理(GraphicsMagick)

    等等...

4.Redis模块

    ①redis常用API接口(RedisUtil)

    ②基于redis实现分布式锁(RedisLockUtil)

5.微信、支付宝支付（com.app.pay包下）

    使用PayFactory工厂类统一管理

    ①创建订单（createOrder）
    
    ②退款(refund）
    
    ③生成移动端调起微信和支付宝时所需要的参数（createPayParams）
    
    ④订单查询（queryOrder）

QQ:208275451
邮箱：yangzhao_java@163.com

# 实现生产者-消费者模型

## 要求

用不同方法实现生产者/消费者模型:

- 生产者生产10个随机的整数供消费者使用（随机数通过`new Random().nextInt()`获得）

- 消费者 **依次** 消费这10个随机的整数

标准输出应该得到这样的结果：

```
Producing 42
Consuming 42
Producing -1
Consuming -1
...
Producing 10086
Consuming 10086
Producing -12345678
Consuming -12345678
```



## 说明



![image-20230803130054366](https://pic-migrate.oss-cn-guangzhou.aliyuncs.com/202308031301489.png)

| 使用不同的方式实现 |                    |
| ------------------ | ------------------ |
| 类名               | 使用工具           |
| ProducerConsumer1  | Object.wait/notify |
| ProducerConsumer2  | Lock/Condition     |
| ProducerConsumer3  | BlockingQueue      |
| ProducerConsumer4  | volatile           |
| ProducerConsumer5  | Exchanger          |
| ProducerConsumer6  | Semaphore          |



## 测试

`ProducerConsumerTest` 会测试所有的`ProducerConsumerX`类，运行`mvn clean test `测试


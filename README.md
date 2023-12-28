# Design of Project SECOND

作为一个MQ，基础的架构模型就是producer-mq-consumer，参考的设计都关联到apache RocketMQ。
区别于RocketMQ，对于MQ的特征，我只打算将MQ关联到一个topic，并且topic关联的queue，进行水平扩展的时候可能会比较方便。
那么对于一个MQ，类设计如下：

```java
import lombok.Data;

@Data
public class MessageQueue {
    Id mqId;
    String boundTopic;
}
```
这里就是最简洁的一个MQ的特征。通过mqId来唯一确定集群中的一个mq。
对于mq，还会添加多种tag。tag的管理，我的理想想法是通过一个int或者long就进行标志，通过MySQL进行持久化，在每次mq上线（其实这里我有一个问题，
假如对于topic：second.test.topic，按照RocketMQ的设计，应该是通过broker来固定管理多个topic的mq。但是这种关系是怎么维护的呢？

后面看到RocketMQ也是带有tag机制的，但是RocketMQ 5.X版本是对于一个Message设置一个tag进行topic下的更细节的区分。

那么，我的目标就是：
- 解除只能设置一个tag的约束
- 尝试对topic也进行tag进行区分




# thread_pool
简单的线程池的实现

本示例自定义实现了一个线程池类 `MythreadPool`，核心功能包括：

- 核心线程池数量控制
- 最大线程池数量控制
- 阻塞队列缓存任务
- 支持自定义拒绝策略（两种：丢弃并重试、抛出异常）

主程序Main方法：

```java
MythreadPool myThreadPool = new MythreadPool(
            2,                                // 核心线程数
            4,                                // 最大线程数
            1, TimeUnit.SECONDS,              // 非核心线程超时时间
            new ArrayBlockingQueue<>(2),      // 阻塞队列容量
            new DiscardRejectHandle()         // 拒绝策略
        );
```

设置设置线程池的参数

然后下面是测试程序

**共提交 8 个任务**，线程池 `core = 2`，`max = 4`，阻塞队列容量为 2。

超出最大容量后执行拒绝策略 `DiscardRejectHandle`。

自定义线程池类 `MythreadPool`：

分为**核心线程**和**非核心线程**

```java
void execute(Runnable command){
    if (coreList.size() < corePoolSize){
        // 创建核心线程
        Thread thread = new CoreThread(command);
        coreList.add(thread);
        thread.start();
        return;
    }

    if (blockingQueue.offer(command)) {
        // 放入阻塞队列
        return;
    }

    if (coreList.size() + supportList.size() < maxSize){
        // 创建非核心线程
        Thread thread = new SupportThread(command);
        supportList.add(thread);
        thread.start();
        return;
    }

    // 队列也满了，执行拒绝策略
    if (!blockingQueue.offer(command)){
        rejectHandle.reject(command, this);
    }
}

```

**核心线程是否有空位**

**能否进入阻塞队列**

**是否还可以创建非核心线程**

**执行拒绝策略**

拒绝策略接口 & 实现:

抛出异常

```java
public class ThrowRejectHandle implements RejectHandle {
    @Override
    public void reject(Runnable rejectCommand, MythreadPool mythreadPool) {
        throw new RuntimeException("阻塞队列满了");
    }
}

```

丢弃队列中最早的任务，再执行新任务

```java
public class DiscardRejectHandle implements RejectHandle {
    @Override
    public void reject(Runnable rejectCommand, MythreadPool mythreadPool) {
        mythreadPool.blockingQueue.poll(); // 丢弃最老任务
        mythreadPool.execute(rejectCommand);
    }
}

```


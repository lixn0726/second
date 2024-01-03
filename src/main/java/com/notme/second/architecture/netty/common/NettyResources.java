package com.notme.second.architecture.netty.common;

import com.notme.second.architecture.netty.NettyEnvironment;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author monstaxl
 **/
public final class NettyResources {

    private static final boolean useEpoll = NettyEnvironment.useEpoll();

    private static final List<EventLoopGroup> producedNettyResourcesNettyResources = new CopyOnWriteArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (producedNettyResourcesNettyResources.isEmpty()) {
                return;
            }
            // ensure resources will be closed as expected.
            for (EventLoopGroup elg : producedNettyResourcesNettyResources) {
                if (Objects.isNull(elg) || elg.isShuttingDown() || elg.isShutdown()) {
                    continue;
                }
                elg.shutdownGracefully();
            }
        }));
    }

    public static EventLoopGroup bossEventLoopGroup(int nThread, String eventLoopThreadPrefix) {
        return eventLoopGroup(nThread, eventLoopThreadPrefix);
    }

    public static EventLoopGroup workerEventLoopGroup(int nThread, String eventLoopThreadPrefix) {
        return eventLoopGroup(nThread, eventLoopThreadPrefix);
    }

    public static int randomPort() {
        return NettyEnvironment.randomPort();
    }

    private static EventLoopGroup eventLoopGroup(int nThread, String eventLoopThreadPrefix) {
        EventLoopGroup res = NettyFunctions.produceEventLoopGroup.apply(nThread, eventLoopThreadPrefix);
        producedNettyResourcesNettyResources.add(res);
        return res;
    }

    static class NettyFunctions {

        private static final Map<String, Integer> nameCounter = new ConcurrentHashMap<>();

        private static final Function<String, ThreadFactory>
                produceThreadFactory = prefix -> new ThreadFactory() {

            {
                // todo: 这里通过代码段来进行判断
                int current = nameCounter.containsKey(prefix) ?
                        nameCounter.get(prefix) + 1 :
                        1;
                this.poolId = current;
                nameCounter.put(prefix, current);
            }

            private int threadCounter = 1;

            private final int poolId;

            // todo: 是否Daemon
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                // 这里主要是，对于同一个prefix，那么要对对应的线程工厂做一个排序，或者说要给一个区分的Id
                t.setName(prefix + "-" + poolId + "-" + threadCounter++);
                return t;
            }
        };

        private static final BiFunction<Integer, String, EventLoopGroup>
                produceEventLoopGroup = (nThread, eventLoopThreadPrefix) -> useEpoll ?
                new EpollEventLoopGroup(nThread, produceThreadFactory.apply(eventLoopThreadPrefix)) :
                new NioEventLoopGroup(nThread, produceThreadFactory.apply(eventLoopThreadPrefix));
    }

}

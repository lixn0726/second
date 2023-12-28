package com.notme.second.netty;

import java.util.List;

/**
 * @author listen
 **/
public class NettyEnvironment {

    public static boolean useEpoll() {
        // todo 默认不使用epoll
        return false;
    }

    public static int suitableHandlerThreadCount() {
        return cpuCount() >> 1;
    }

    public static int cpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public class Root {
        private List<Child> childs;

        public void update() {

        }
    }

    public class Child {

    }

}

package com.notme.second.architecture.netty;

import com.notme.second.architecture.utils.Strings;
import io.netty.channel.epoll.Epoll;

import java.util.Locale;
import java.util.Properties;

/**
 * @author monstaxl
 * Utility class for Netty framework
 **/
public class NettyEnvironment {

    private static final String defaultNettyPropertiesFilePath = "classpath:second-netty.properties";

    public static boolean useEpoll() {
        return HostMachineNetworkEnvironment.supportEpoll() && NettyConfiguration.decideUseEpoll();
    }

    public static int workerEventLoopThreadNumber() {
        return Math.max(HostMachineOperationSystem.availableProcessors() >> 1,
                );
    }

    // helper classes below

    private static class NettyConfiguration {

        // todo: 添加读取配置文件的步骤
        private static Properties props;

        public static boolean decideUseEpoll() {
            return (boolean) props.getOrDefault("epoll.activate", Boolean.FALSE);
        }

        public static int decideWorkerEventLoopThreadNumber() {
            return (int) props.getOrDefault("server.threads.worker", Integer.MIN_VALUE);
        }
    }

    private static class HostMachineNetworkEnvironment {

        public static boolean supportEpoll() {
            return HostMachineOperationSystem.isLinux()
                    // todo: 这里是依赖Netty的判断，本质是通过系统调用来实现的，所以后续看看能不能自己实现
                    && Epoll.isAvailable();
        }

    }

    private static class HostMachineOperationSystem {

        private static final String osName = System.getProperty("os.name");

        private static boolean isLinux = false;

        private static boolean isWindows = false;

        private static final Runtime runtime = Runtime.getRuntime();

        private static final int availableProcessors = runtime.availableProcessors();

        static {
            if (Strings.notBlank(osName)) {
                final String lowerCaseOsName = osName.toLowerCase(Locale.ENGLISH);
                isLinux = lowerCaseOsName.contains("linux");
                isWindows = lowerCaseOsName.contains("windows");
            }
        }

        public static String lowerCaseOsName() {
            return osName.toUpperCase(Locale.ENGLISH);
        }

        public static boolean isLinux() {
            return isLinux;
        }

        public static boolean isWindows() {
            return isWindows;
        }

        public static int availableProcessors() {
            return availableProcessors;
        }

    }

}

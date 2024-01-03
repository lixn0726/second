package com.notme.second.architecture.netty;

import com.notme.second.architecture.utils.Strings;
import io.netty.channel.epoll.Epoll;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Statement;
import java.util.BitSet;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author monstaxl
 * Utility class for Netty framework
 **/
public final class NettyEnvironment {

    private NettyEnvironment() {}

    private static final String defaultNettyPropertiesFilePath = "classpath:second-netty.properties";

    public static boolean useEpoll() {
        return HostMachineNetworkEnvironment.supportEpoll() && NettyConfiguration.decideUseEpoll();
    }

    public static int bossEventLoopGroupThreads() {
        return 3;
    }

    public static int workerEventLoopGroupThreads() {
        return Math.max(HostMachineOperationSystem.availableProcessors() >> 1, 3);
    }

    public static boolean channelKeepAlive() {
        return false;
    }

    public static int randomPort() {
        return HostMachineNetworkEnvironment.nextRandomPort();
    }

    // helper classes below

    // todo: 这个后面考虑单独搞出来，参考MyBatis，从reader读取配置文件开始
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

        private static final int linuxUsablePorts = 65536;

        private static final int linuxMaxPort = 65535;

        private static final int randomPortOffset = 30_000;

        private static final int portValueRange = 10_000;

        private static final BitSet usablePorts = new BitSet(linuxUsablePorts);

        public static boolean supportEpoll() {
            return HostMachineOperationSystem.isLinux()
                    // todo: 这里是依赖Netty的判断，本质是通过系统调用来实现的，所以后续看看能不能自己实现
                    && Epoll.isAvailable();
        }

        public static int nextRandomPort() {
            int random = randomPortOffset + ThreadLocalRandom.current().nextInt(portValueRange);
            return ensurePortAvailable(random);
        }

        private static int ensurePortAvailable(int port) {
            // 基本上不会发生
            if (port >= linuxMaxPort) {
                return ensurePortAvailable(nextRandomPort());
            }
            if (usablePorts.get(port)) {
                // 已存在，那么获取下一个随机值
                return ensurePortAvailable(nextRandomPort());
            } else {
                try (ServerSocket tryConnect = new ServerSocket(port) ){
                    usablePorts.set(port);
                } catch (Exception e) {
                    // 无法使用也标记为used
                    usablePorts.set(port);
                    return ensurePortAvailable(nextRandomPort());
                }
                // 可以连接，并且也没有标记过
                return port;
            }
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

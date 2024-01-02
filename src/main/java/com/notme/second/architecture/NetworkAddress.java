package com.notme.second.architecture;

import lombok.Getter;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Function;

/**
 * @author monstaxl
 **/
@Getter
public class NetworkAddress implements Serializable {

    private static final long serialVersionUID = -99988776664L;

    // todo: 这里我的想法是，对通用的函数进行抽象比较好，不然的话，在类中自定义方法可能会是更加直观优雅的方式
    //          后续如果有全局的通用函数，我留了一个 GlobalFunctions类，应该会用泛型或者什么全部放在里面
    private static final Function<NetworkAddress, String> addressToString = na -> na.getIp() + ":" + na.getPort();

    private final String ip;

    /**
     * 不使用int是为了避免出现默认int属性为0的情况导致发生意料之外的错误
     */
    private final Integer port;

    public NetworkAddress(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public static NetworkAddress fromSocketAddress(final SocketAddress sa) {
        InetSocketAddress isa = (InetSocketAddress) sa;
        return new NetworkAddress(isa.getHostString(), isa.getPort());
    }

    public String get() {
        return getAsString();
    }

    public String getAsString() {
        return this.getIp() + ":" + this.port;
    }

}

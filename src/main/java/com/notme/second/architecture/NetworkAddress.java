package com.notme.second.architecture;

import lombok.Getter;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author monstaxl
 * todo: 有没有必要保留这个呢，我的想法是后面不知道会不会加一些什么属性上去
 **/
@Getter
public class NetworkAddress implements Serializable {

    private static final long serialVersionUID = -99988776664L;

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

    @Override
    public boolean equals(Object o) {
        if (Objects.isNull(o)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetworkAddress)) {
            return false;
        }
        NetworkAddress that = (NetworkAddress) o;
        if (Objects.isNull(that.ip) || Objects.isNull(that.port)) {
            return false;
        }
        return that.port.equals(this.port)
                && that.ip.equals(this.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIp(), getPort());
    }

    @Override
    public String toString() {
        return "NetworkAddress{" +
                ip + ":" + port +
                '}';
    }
}

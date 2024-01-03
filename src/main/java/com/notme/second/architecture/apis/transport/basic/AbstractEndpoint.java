package com.notme.second.architecture.apis.transport.basic;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Endpoint;

/**
 * @author monstaxl
 **/
public abstract class AbstractEndpoint implements Endpoint {

    protected final String id;

    protected volatile NetworkAddress address;

    protected volatile boolean closed = true;

    static String nextId() {
        return "";
    }

    public AbstractEndpoint(NetworkAddress address) {
        this.id = nextId();
        this.address = address;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public NetworkAddress address() {
        if (closed) {
            throw new IllegalStateException("Endpoint {} is closed. Cannot fetch its address.");
        }
        return address;
    }

    protected void setAddress(NetworkAddress address) {
        this.address = address;
    }

    protected void setClosed(boolean closed) {
        this.closed = closed;
    }
}

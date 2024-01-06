package com.notme.second.core.netty_process.protocols;

import io.netty.channel.ChannelFuture;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author monstaxl
 **/
public class Request extends AbstractCommand implements Serializable {

    private static final RequestIdGenerator idGenerator = new RequestIdGenerator();

    // mapping to a response.
    // initialize it when instantiation triggered
//    private final RequestId id = idGenerator.nextId();

    /** directly passed from Msg. {@link Msg}
     */
    private byte[] body;

    public Request(CommandCode code, CommandHeader header) {
        super(code, header);
    }

    @Override
    protected void doBuildHeader() {

    }

    @Override
    public Id<?> id() {
        return null;
    }

    static class RequestId implements Id<Integer> {

        private final Integer id;

        public RequestId(Integer id) {
            this.id = id;
        }

        @Override
        public Integer get() {
            return this.id;
        }
    }

    static class RequestIdGenerator implements IdGenerator<Integer> {

        private static final AtomicInteger idCounter = new AtomicInteger(0);

        @Override
        public Id<Integer> nextId() {
            return new RequestId(idCounter.getAndIncrement());
        }
    }

    static class RequestHeader implements CommandHeader {

        @Override
        public CommandHeader build() {
            return null;
        }

        @Override
        public String get(String key) {
            return null;
        }

        @Override
        public void set(String key, String value) {

        }
    }
}

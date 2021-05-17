package com.github.wu.core.rpc.remoting.transport;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wangyongxu
 */

@Data
public class Request {

    private static final AtomicLong REQUEST_ID = new AtomicLong(0);
    private SengProtocolHeader header;
    private Invocation body;

    public Request(SengProtocolHeader header, Invocation body) {
        this.header = header;
        this.body = body;
    }

    public Request(Invocation body) {
        this.header = sengProtocolHeader(newReqId());
        this.body = body;
    }

    private SengProtocolHeader sengProtocolHeader(long reqId) {
        SengProtocolHeader sengProtocolHeader = new SengProtocolHeader();
        sengProtocolHeader.setMsgType(SengProtocolHeader.REQUEST);
        sengProtocolHeader.setSerializerId((byte) 1);
        sengProtocolHeader.setReqId(reqId);
        return sengProtocolHeader;
    }

    private long newReqId() {
        return REQUEST_ID.getAndIncrement();
    }
}

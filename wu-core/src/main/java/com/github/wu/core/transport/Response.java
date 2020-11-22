package com.github.wu.core.transport;

import lombok.Data;

/**
 * @author wangyongxu
 */
@Data
public class Response {
    private SengProtocolHeader header;
    private Object body;

    public Response() {
    }

    public Response(SengProtocolHeader header, Object body) {
        this.header = header;
        this.body = body;
    }

    public Response(Request request, Object body) {
        this.header = generateResponseHeader(request);
        this.body = body;
    }


    private SengProtocolHeader generateResponseHeader(Request request) {
        SengProtocolHeader responseHeader = new SengProtocolHeader();
        responseHeader.setVersion(request.getHeader().getVersion());
        responseHeader.setMsgType(SengProtocolHeader.RESPONSE);
        responseHeader.setSerializerId(request.getHeader().getSerializerId());
        responseHeader.setStatusCode(SengProtocolHeader.OK);
        responseHeader.setReqId(request.getHeader().getReqId());
        return responseHeader;
    }

    public boolean isResponse() {
        return header.getMsgType() == SengProtocolHeader.RESPONSE;
    }

    public long getRequestId() {
        return header.getReqId();
    }
}

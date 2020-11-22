package com.github.wu.core.transport;

/**
 * customize protocol
 * <pre>
 * =======================================================================================
 * magic | version | msg_type | serializer_id | status_code| remain | req_id | data_length
 * 16    | 8       | 4        | 4             | 5          | 3      | 64     | 32
 *
 * </pre>
 *
 * @author qiankewei
 */
public class SengProtocolHeader {
    private static final short SENG_PROTOCOL_MAGIC = (short) 0xFDAC;

    /* ---------------- msgType -------------- */
    public static final byte REQUEST = 0x00;
    public static final byte RESPONSE = 0x01;
    public static final byte ONE_WAY = 0x02;
    public static final byte PING = 0x03;
    public static final byte PONG = 0x04;
    /* ---------------- msgType -------------- */

    /* ---------------- statusCode -------------- */
    public static final byte OK = 0x00;
    public static final byte TIME_OUT = 0x01;
    public static final byte CLIENT_ERROR = 0x02;
    public static final byte SERVER_ERROR = 0x03;
    /* ---------------- statusCode -------------- */

    private final short magic = SENG_PROTOCOL_MAGIC;
    private byte version = 1;
    private byte msgType;
    private byte serializerId = 1;
    private byte statusCode;
    private long reqId;
    private int dataLength;

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public short getMagic() {
        return magic;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getSerializerId() {
        return serializerId;
    }

    public void setSerializerId(byte serializerId) {
        this.serializerId = serializerId;
    }

    public byte getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(byte statusCode) {
        this.statusCode = statusCode;
    }

    public long getReqId() {
        return reqId;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }
}

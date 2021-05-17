package com.github.wu.core.rpc.remoting.transport;

import com.github.wu.common.exception.WuRuntimeException;
import com.github.wu.core.rpc.remoting.serialize.Serializer;
import com.github.wu.core.rpc.remoting.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * wu message encoder
 *
 * @author qiankewei
 */
public class SengMessageEncoder extends MessageToByteEncoder<Object> {
    private static final Logger logger = LoggerFactory.getLogger(SengMessageEncoder.class);

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        logger.info("encode: {}", msg);

        if (msg instanceof Request) {
            Request request = (Request) msg;
            byte serializerId = request.getHeader().getSerializerId();
            Serializer serializer = SerializerFactory.getSerializer(serializerId);
            SengProtocolHeader header = request.getHeader();
            out.writeShort(header.getMagic());
            out.writeByte(header.getVersion());
            byte b = (byte) (((header.getMsgType() & 15) << 4) + (header.getSerializerId() & 15));
            out.writeByte(b);
            b = (byte) ((header.getStatusCode() & 7) << 3);
            out.writeByte(b);
            // requestId
            out.writeLong(header.getReqId());
            // body length
            byte[] body = encodeRequest(request.getBody(), serializer);
            out.writeInt(body.length);
            // serializer body
            // write body
            out.writeBytes(body);

        } else if (msg instanceof Response) {
            Response response = (Response) msg;
            byte serializerId = response.getHeader().getSerializerId();
            Serializer serializer = SerializerFactory.getSerializer(serializerId);
            SengProtocolHeader header = response.getHeader();
            out.writeShort(header.getMagic());
            out.writeByte(header.getVersion());
            byte b = (byte) (((header.getMsgType() & 15) << 4) + (header.getSerializerId() & 15));
            out.writeByte(b);
            b = (byte) ((header.getStatusCode() & 7) << 3);
            out.writeByte(b);
            // requestId
            out.writeLong(header.getReqId());
            // serializer body
            // write body
            byte[] body = encodeResponse(response.getBody(), serializer);
            // body length
            out.writeInt(body.length);
            out.writeBytes(body);
        }

    }


    /**
     * 编码调用方的请求
     */
    public byte[] encodeRequest(Invocation invocation, Serializer serializer) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = createOutput(outputStream);

        output.writeUTF(invocation.getServiceName());
        output.writeUTF(invocation.getMethodName());
        output.writeUTF(invocation.getArgsDesc());

        Object[] args = invocation.getArgs();

        if (args != null) {
            for (Object arg : args) {
                if (arg == null) {
                    output.writeObject(null);
                } else {
                    output.writeObject(serializer.serialize(arg));
                }
            }
        }
        byte[] bytes = outputStream.toByteArray();
        output.close();
        return bytes;
    }

    private ObjectInput createInput(InputStream in) {
        try {
            return new ObjectInputStream(in);
        } catch (Exception e) {
            throw new WuRuntimeException(this.getClass().getSimpleName() + " createInputStream error", e);
        }
    }

    private ObjectOutput createOutput(OutputStream outputStream) {
        try {
            return new ObjectOutputStream(outputStream);
        } catch (Exception e) {
            throw new WuRuntimeException(this.getClass().getSimpleName() + " createOutputStream error", e);
        }
    }


    /**
     * 编码服务端的响应
     */
    private byte[] encodeResponse(Object body, Serializer serializer) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = createOutput(outputStream);
        output.writeUTF(body.getClass().getName());
        output.writeObject(serializer.serialize(body));
        return outputStream.toByteArray();
    }
}

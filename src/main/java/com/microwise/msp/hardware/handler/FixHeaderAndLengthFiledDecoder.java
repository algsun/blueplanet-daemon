package com.microwise.msp.hardware.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 固定包头，且有包长字段. 目前支持 v3 协议
 * <p/>
 * <pre>
 * 55,   AA, xx, xx, xx, xx
 * [PREFIX], [3 byte  ], [LENGTH_FIELD]
 * </pre>
 */
public class FixHeaderAndLengthFiledDecoder extends ByteToMessageDecoder {
    public static final Logger log = LoggerFactory.getLogger(FixHeaderAndLengthFiledDecoder.class);

    // 包头
    public static final int[] HEAD_PREFIX = new int[]{0x55, 0xAA};

    // 长度字段的相对于包头的偏移量
    public static final int LENGTH_FIELD_OFFSET = 3;

    // 固定包头长度
    public static final int HEAD_LENGTH = HEAD_PREFIX.length + LENGTH_FIELD_OFFSET + 1;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.debug("decode: {}", ByteBufUtil.hexDump(in).toUpperCase());

        // 数据大于包头
        while (in.readableBytes() > 1) {
            int readIndex = in.readerIndex();


            // 读包头
            // 不包头完毕，读下两个字节
            if ((in.readByte() & 0xFF) != HEAD_PREFIX[0]) {
                continue;
            }

            if((in.readByte() & 0xFF) != HEAD_PREFIX[1]){
                continue;
            }

            // 没有包长字段
            if (in.readableBytes() < LENGTH_FIELD_OFFSET + 1) {
                // 跳出，等待数据接收。 重置读取位置到包头。
                in.readerIndex(readIndex);
                break;
            }

            in.skipBytes(LENGTH_FIELD_OFFSET);
            // 读取包长
            short contentLength = (short) (in.readByte() & 0xFF);
            // 包内容不满足长度
            if (in.readableBytes() < contentLength) {
                // 跳出，等待数据接收。 重置读取位置到包头。
                in.readerIndex(readIndex);
                break;
            }

            in.skipBytes(contentLength);
            ByteBuf frame = ctx.alloc()
                    .buffer(HEAD_LENGTH + contentLength)
                    .writeBytes(in, readIndex, HEAD_LENGTH + contentLength);
            out.add(frame);
        }

        log.debug("end decode: {}", ByteBufUtil.hexDump(in).toUpperCase());
    }
}

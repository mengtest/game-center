package github.eddy.game.werewolf.protocol;

import github.eddy.game.werewolf.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import java.util.List;

public class Byte2MessageCodec extends ByteToMessageCodec<Message> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
    out.writeInt(msg.getLength())
        .writeLong(msg.getUserId())
        .writeShort(msg.getVersion())
        .writeShort(msg.getType().getCode())
        .writeShort(msg.getAction().getCode())
        .writeShort(msg.getService().getCode())
        .writeBytes(msg.getContent());
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    out.add(
        new Message(
            in.readInt(),
            in.readLong(),
            in.readShort(),
            in.readShort(),
            in.readShort(),
            in.readShort(),
            in.readBytes(in.readableBytes()))
    );
  }
}

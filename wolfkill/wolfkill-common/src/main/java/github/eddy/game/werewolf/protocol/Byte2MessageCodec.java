package github.eddy.game.werewolf.protocol;

import com.alibaba.fastjson.JSON;
import github.eddy.game.werewolf.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import java.util.List;

public class Byte2MessageCodec extends ByteToMessageCodec<Message> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
    String jsonString = JSON.toJSONString(msg);
    out.writeBytes(jsonString.getBytes());
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    byte[] bytes = new byte[in.readableBytes()];
    in.readBytes(bytes);
    Message message = JSON.parseObject(new String(bytes), Message.class);
    out.add(message);
  }
}

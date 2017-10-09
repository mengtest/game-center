package github.eddy.game.server.services;

import github.eddy.game.server.pool.ChannelManager;
import github.eddy.game.werewolf.common.ActionCode;
import github.eddy.game.werewolf.common.ServiceCode;
import github.eddy.game.werewolf.common.TypeCode;
import github.eddy.game.werewolf.message.Message;
import github.eddy.game.werewolf.tools.GameTimer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceHandler {

  ChannelManager channelManager = ChannelManager.getChannelManager();
  GameTimer serverTimer = new GameTimer();

  public void timerService(Channel incoming, Message msg) {
    for (Channel channel : channelManager.getAll()) {
      byte[] content = "TIMER START".getBytes();
      ByteBuf byteBuf = channel.alloc().buffer(content.length);
      byteBuf.writeBytes(content);
      channel.writeAndFlush(
          new Message(TypeCode.NOTICE, ActionCode.TIMER, ServiceCode.DEFAULT, byteBuf));
    }
    //服务器计时任务
    serverTimer.schedule(() -> {
      for (Channel channel : channelManager.getAll()) {
        byte[] content = "TIMER END".getBytes();
        ByteBuf byteBuf = channel.alloc().buffer(content.length);
        byteBuf.writeBytes(content);
        channel.writeAndFlush(
            new Message(TypeCode.NOTICE, ActionCode.TIMER, ServiceCode.DEFAULT, byteBuf));
      }
    }, 10 * 1000);
  }

  public void chatService(Channel incoming, Message msg) {
    ByteBuf content = msg.getContent();
    byte[] contentByte = new byte[content.readableBytes()];
    content.readBytes(contentByte);

    for (Channel channel : channelManager.getAll()) {
      if (channel != incoming) {
        ByteBuf byteBuf = channel.alloc().buffer(contentByte.length);
        byteBuf.writeBytes(contentByte);
        channel.writeAndFlush(
            new Message(TypeCode.NOTICE, ActionCode.CHAT, ServiceCode.HALL_CHAT, byteBuf));
      } else {
        ByteBuf byteBuf = channel.alloc().buffer(contentByte.length);
        byteBuf.writeBytes(contentByte);
        channel.writeAndFlush(
            new Message(TypeCode.RESPONSE, ActionCode.CHAT, ServiceCode.HALL_CHAT,
                byteBuf));
      }
    }
  }
}

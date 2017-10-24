package github.eddy.werewolf.server.handler;

import static github.eddy.game.common.MsgModuleCode.CHAT;
import static github.eddy.game.common.MsgModuleCode.TIMER;
import static github.eddy.game.common.MsgServiceCode.HALL_CHAT;
import static github.eddy.game.common.MsgTypeCode.NOTICE;
import static github.eddy.game.common.MsgTypeCode.RESPONSE;

import github.eddy.game.common.MsgServiceCode;
import github.eddy.game.protocol.Message;
import github.eddy.game.server.pool.ChannelManager;
import github.eddy.werewolf.common.tools.GameTimer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceHandler {

  @Getter
  public static ServiceHandler serviceHandler = new ServiceHandler();

  private ChannelManager channelManager = ChannelManager.getChannelManager();
  private GameTimer serverTimer = new GameTimer();

  public void timerService(Channel incoming, Message msg) {
    for (Channel channel : channelManager.getConnected()) {
      byte[] content = "TIMER START".getBytes();
      ByteBuf byteBuf = channel.alloc().buffer(content.length);
      byteBuf.writeBytes(content);
      channel.writeAndFlush(
          Message.write(NOTICE, TIMER, MsgServiceCode.DEFAULT, byteBuf));
    }
    //服务器计时任务
    serverTimer.schedule(() -> {
      for (Channel channel : channelManager.getConnected()) {
        byte[] content = "TIMER END".getBytes();
        ByteBuf byteBuf = channel.alloc().buffer(content.length);
        byteBuf.writeBytes(content);
        channel.writeAndFlush(
            Message.write(NOTICE, TIMER, MsgServiceCode.DEFAULT, byteBuf));
      }
    }, 10 * 1000);
  }

  public void chatService(Channel incoming, Message msg) {
    ByteBuf content = msg.getContent();
    byte[] contentByte = new byte[content.readableBytes()];
    content.readBytes(contentByte);

    for (Channel channel : channelManager.getConnected()) {
      if (channel != incoming) {
        ByteBuf byteBuf = channel.alloc().buffer(contentByte.length);
        byteBuf.writeBytes(contentByte);
        channel.writeAndFlush(
            Message.write(NOTICE, CHAT, HALL_CHAT, byteBuf));
      } else {
        ByteBuf byteBuf = channel.alloc().buffer(contentByte.length);
        byteBuf.writeBytes(contentByte);
        channel.writeAndFlush(
            Message.write(RESPONSE, CHAT, HALL_CHAT, byteBuf));
      }
    }
  }
}

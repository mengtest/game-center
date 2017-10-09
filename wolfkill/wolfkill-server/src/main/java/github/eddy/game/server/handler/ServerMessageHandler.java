package github.eddy.game.server.handler;

import github.eddy.game.werewolf.common.ActionCode;
import github.eddy.game.werewolf.common.ServiceCode;
import github.eddy.game.werewolf.common.TypeCode;
import github.eddy.game.werewolf.message.Message;
import github.eddy.game.werewolf.tools.GameTimer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {

  public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  GameTimer serverTimer = new GameTimer();

  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();

//    channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
    channels.add(ctx.channel());
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
    Channel incoming = ctx.channel();

//    channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
    Channel incoming = ctx.channel();
    switch (msg.getType()) {
      case REQUEST:
        switch (msg.getAction()) {
          case TIMER:   //TODO 测试 ,服务器客户端同步计时
            for (Channel channel : channels) {
              byte[] content = "TIMER START".getBytes();
              ByteBuf byteBuf = channel.alloc().buffer(content.length);
              byteBuf.writeBytes(content);
              channel.writeAndFlush(
                  new Message(TypeCode.NOTICE, ActionCode.TIMER, ServiceCode.DEFAULT, byteBuf));
            }
            //服务器计时任务
            serverTimer.schedule(() -> {
              for (Channel channel : channels) {
                byte[] content = "TIMER END".getBytes();
                ByteBuf byteBuf = channel.alloc().buffer(content.length);
                byteBuf.writeBytes(content);
                channel.writeAndFlush(
                    new Message(TypeCode.NOTICE, ActionCode.TIMER, ServiceCode.DEFAULT, byteBuf));
              }
            }, 10 * 1000);
            return;
          case CHAT: //发送消息到聊天室
            ByteBuf content = msg.getContent();
            byte[] contentByte = new byte[content.readableBytes()];
            content.readBytes(contentByte);

            for (Channel channel : channels) {
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
            return;
          case WHISPER:
            return;
        }
        return;
      default: //服务器不处理response和notice类型的消息
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
    Channel incoming = ctx.channel();
    log.debug("SimpleChatClient:" + incoming.remoteAddress() + "在线");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
    Channel incoming = ctx.channel();
    log.debug("SimpleChatClient:" + incoming.remoteAddress() + "掉线");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
    Channel incoming = ctx.channel();
    log.debug("SimpleChatClient:" + incoming.remoteAddress() + "异常");
    // 当出现异常就关闭连接
    log.error("", cause);
    ctx.close();
  }
}
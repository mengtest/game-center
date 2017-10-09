package github.eddy.game.server.handler;

import github.eddy.game.werewolf.tools.GameTimer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerStringHandler extends SimpleChannelInboundHandler<String> {

  public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  GameTimer serverTimer = new GameTimer();

  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();

    channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
    channels.add(ctx.channel());
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
    Channel incoming = ctx.channel();

    channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
    // A closed Channel is automatically removed from ChannelGroup,
    // so there is no need to do "channels.remove(ctx.channel());"
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
    Channel incoming = ctx.channel();

    //特殊的计时消息
    if (s.equals("TIMER")) {
      for (Channel channel : channels) {
        channel.writeAndFlush("[SERVER]TIMER start\n");
      }
      serverTimer.schedule(() -> {
        for (Channel channel : channels) {
          channel.writeAndFlush("[SERVER]TIMER end\n");
        }
      }, 10 * 1000);
    } else {
      for (Channel channel : channels) {
        if (channel != incoming) {
          channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
        } else {
          channel.writeAndFlush("[you]" + s + "\n");
        }
      }
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
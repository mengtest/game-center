package github.eddy.game.server.handler;

import github.eddy.game.server.actions.ActionHandler;
import github.eddy.game.werewolf.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {

  ActionHandler actionHandler = new ActionHandler();

  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    actionHandler.channelAdded(ctx.channel());
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
    actionHandler.channelRemoved(ctx.channel());
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
    switch (msg.getType()) {
      case REQUEST:
        actionHandler.requestAction(ctx.channel(),msg);
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
package github.eddy.game.client.handler;

import github.eddy.game.client.actions.ActionHandler;
import github.eddy.game.werewolf.message.Message;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {

  ActionHandler actionHandler = new ActionHandler();

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
    switch (msg.getType()) {
      case RESPONSE:
        actionHandler.responseAction(msg);
        return;
      case NOTICE:
        actionHandler.noticeAction(msg);
        return;
      default:
    }
  }
}
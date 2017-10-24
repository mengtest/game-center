package github.eddy.werewolf.server.handler;

import static github.eddy.game.common.MsgTypeEnum.REQUEST;

import github.eddy.game.protocol.AbstractMessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeHandler extends AbstractMessageHandler {

  private ModuleHandler moduleHandler = ModuleHandler.getModuleHandler();

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    moduleHandler.channelAdded(ctx.channel());
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
    moduleHandler.channelRemoved(ctx.channel());
  }

  @Override
  public void channelReadMessage(Channel channel, github.eddy.game.protocol.Message message) {
    switch (message.getType()) {
      case REQUEST:
        moduleHandler.requestAction(channel, message);
        return;
      default: //服务器不处理response和notice类型的消息
    }
  }
}
package github.eddy.werewolf.client.handler;

import static github.eddy.game.common.MsgTypeCode.NOTICE;
import static github.eddy.game.common.MsgTypeCode.RESPONSE;

import github.eddy.game.protocol.AbstractMessageHandler;
import github.eddy.game.protocol.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class ClientHandler extends AbstractMessageHandler {

  private ActionHandler actionHandler = ActionHandler.getActionHandler();

  @Override
  public void channelReadMessage(Channel channel, Message message) {
    switch (message.getType()) {
      case RESPONSE:
        actionHandler.responseAction(message);
        return;
      case NOTICE:
        actionHandler.noticeAction(message);
        return;
      default:
    }
  }
}
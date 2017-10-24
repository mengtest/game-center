package github.eddy.werewolf.server.handler;

import static github.eddy.game.common.MsgModuleEnum.CHAT;
import static github.eddy.game.common.MsgModuleEnum.TIMER;
import static github.eddy.game.common.MsgModuleEnum.WHISPER;

import github.eddy.game.protocol.Message;
import github.eddy.game.server.pool.ChannelManager;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModuleHandler {

  @Getter
  public static ModuleHandler moduleHandler = new ModuleHandler();

  private ServiceHandler serviceHandler = ServiceHandler.getServiceHandler();
  private ChannelManager channelManager = ChannelManager.getChannelManager();

  public void requestAction(Channel incoming, Message msg) {
    switch (msg.getModule()) {
      case TIMER:
        serviceHandler.timerService(incoming, msg); //TODO 测试 ,服务器客户端同步计时
        return;
      case CHAT: //发送消息到聊天室
        serviceHandler.chatService(incoming, msg);
        return;
      case WHISPER:
        return;
    }
  }

  public void channelAdded(Channel incoming) {
    channelManager.connect(incoming);
    log.info("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
  }

  public void channelRemoved(Channel incoming) {
    log.info("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
  }
}

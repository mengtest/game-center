package github.eddy.game.server.actions;

import github.eddy.game.server.pool.ChannelManager;
import github.eddy.game.server.services.ServiceHandler;
import github.eddy.game.werewolf.message.Message;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionHandler {

  ChannelManager channelManager = ChannelManager.getChannelManager();
  ServiceHandler serviceHandler = new ServiceHandler();

  public void requestAction(Channel incoming,Message msg) {
    switch (msg.getAction()) {
      case TIMER:
        serviceHandler.timerService(incoming,msg); //TODO 测试 ,服务器客户端同步计时
        return;
      case CHAT: //发送消息到聊天室
        serviceHandler.chatService(incoming,msg);
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

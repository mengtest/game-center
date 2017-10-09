package github.eddy.game.client.actions;

import com.google.common.base.Charsets;
import github.eddy.game.client.services.ServiceHandler;
import github.eddy.game.werewolf.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionHandler {

  ServiceHandler serviceHandler = new ServiceHandler();

  public void responseAction(Message msg) {
    switch (msg.getAction()) {
      case CHAT:
      case WHISPER:
        log.debug("[You] : " + msg.getContent().toString(Charsets.UTF_8));
        return;
      default:
    }
  }

  public void noticeAction(Message msg) {
    switch (msg.getAction()) {
      case CHAT:
        log.debug("[" + msg.getUserId() + "] : " + msg.getContent().toString(Charsets.UTF_8));
        return;
      case TIMER:
        serviceHandler.timerService(msg);
        return;
      default:
    }
  }
}

package github.eddy.werewolf.client.handler;

import com.google.common.base.Charsets;
import github.eddy.game.common.MsgModuleCode;
import github.eddy.game.protocol.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionHandler {

  @Getter
  private static ActionHandler actionHandler = new ActionHandler();

  private ServiceHandler serviceHandler = new ServiceHandler();

  public void responseAction(Message msg) {
    switch (msg.getModule()) {
      case MsgModuleCode.CHAT:
      case MsgModuleCode.WHISPER:
        log.debug("[You] : " + msg.getContent().toString(Charsets.UTF_8));
        return;
      default:
    }
  }

  public void noticeAction(Message msg) {
    switch (msg.getModule()) {
      case MsgModuleCode.CHAT:
        log.debug(msg.getContent().toString(Charsets.UTF_8));
        return;
      case MsgModuleCode.TIMER:
        serviceHandler.timerService(msg);
        return;
      default:
    }
  }
}

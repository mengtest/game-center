package github.eddy.werewolf.client.handler;

import static github.eddy.game.common.MsgServiceCode.TIMER_END;
import static github.eddy.game.common.MsgServiceCode.TIMER_START;

import github.eddy.game.protocol.Message;
import github.eddy.werewolf.common.tools.GameTimer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceHandler {

  @Getter
  private static ServiceHandler serviceHandler = new ServiceHandler();

  GameTimer clientTimer = new GameTimer();
  Integer time = 0;

  public void timerService(Message msg) {
    switch (msg.getService()) {
      case TIMER_START:
        log.debug("get server time start");
        time = 10;
        clientTimer.schedule(() -> log.debug("time:{}", time--), 0, 1000);
        return;
      case TIMER_END:
        clientTimer.cancel();
        log.error("get server time end");
        return;
      default:
    }
  }
}

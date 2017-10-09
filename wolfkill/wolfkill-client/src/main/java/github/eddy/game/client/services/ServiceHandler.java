package github.eddy.game.client.services;

import github.eddy.game.werewolf.message.Message;
import github.eddy.game.werewolf.tools.GameTimer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceHandler {

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

package github.eddy.game.werewolf.tools;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

  Timer timer = new Timer();

  public void schedule(Runnable runnable, long delay, long period) {
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        runnable.run();
      }
    }, delay, period);
  }

  public void schedule(Runnable runnable, long delay) {
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        runnable.run();
      }
    }, delay);
  }

  public void cancel() {
    timer.cancel();
  }
}

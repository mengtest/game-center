package github.eddy.game.client.handler;

import github.eddy.game.werewolf.tools.GameTimer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientStringHandler extends SimpleChannelInboundHandler<String> {

  GameTimer clientTimer = new GameTimer();
  Integer time = 0;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
    if (s.contains("TIMER start")) {
      log.debug("get server time start");
      time = 10;
      clientTimer.schedule(() -> log.debug("time:{}", time--), 0, 1000);
    } else if (s.contains("TIMER end")) {
      clientTimer.cancel();
      log.error("get server time end");
    } else {
      log.debug(s);
    }
  }
}
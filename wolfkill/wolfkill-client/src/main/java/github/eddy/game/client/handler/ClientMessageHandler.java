package github.eddy.game.client.handler;

import com.google.common.base.Charsets;
import github.eddy.game.werewolf.message.Message;
import github.eddy.game.werewolf.tools.GameTimer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {

  GameTimer clientTimer = new GameTimer();
  Integer time = 0;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
    switch (msg.getType()) {
      case RESPONSE:
        switch (msg.getAction()) {
          case CHAT:
          case WHISPER:
            log.debug("[You] : " + msg.getContent().toString(Charsets.UTF_8));
            return;
        }
        return;
      case NOTICE:
        switch (msg.getAction()) {
          case CHAT:
            log.debug("[" + msg.getUserId() + "] : " + msg.getContent().toString(Charsets.UTF_8));
            return;
          case TIMER:
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
            }
            return;
        }
        return;
    }
  }
}
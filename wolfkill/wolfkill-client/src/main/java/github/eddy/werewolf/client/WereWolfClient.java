package github.eddy.werewolf.client;

import com.google.common.base.Charsets;
import github.eddy.game.client.RainClient;
import github.eddy.game.client.handler.ClientChannelInitializer;
import github.eddy.game.common.MsgModuleCode;
import github.eddy.game.common.MsgServiceCode;
import github.eddy.game.common.MsgTypeCode;
import github.eddy.game.protocol.Message;
import github.eddy.werewolf.client.handler.ClientTypeHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WereWolfClient {

  private static final int DEFAULT_PORT = 65535;
  private static final String DEFAULT_HOST = "localhost";
  private RainClient client;

  public void start() throws Exception {
    client = new RainClient(DEFAULT_HOST, DEFAULT_PORT);
    ClientChannelInitializer clientChannelInitializer = new ClientChannelInitializer();
    clientChannelInitializer.registerMessageHandler(ClientTypeHandler.class);
    client.start(clientChannelInitializer);
  }

  public static void main(String[] args) throws Exception {
    WereWolfClient client = new WereWolfClient();
    client.start();

    Channel channel = client.client.getLocalChannel();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Boolean stop = false;
    while (!stop) {
      ByteBuf byteBuf = channel.alloc().buffer();
      byteBuf.writeCharSequence(in.readLine(), Charsets.UTF_8);
      channel.writeAndFlush(Message
          .write(MsgTypeCode.REQUEST, MsgModuleCode.CHAT, MsgServiceCode.HALL_CHAT, byteBuf));
    }
  }
}


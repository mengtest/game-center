package github.eddy.werewolf.server;

import github.eddy.game.server.RainServer;
import github.eddy.game.server.handler.ServerChannelInitializer;
import github.eddy.werewolf.server.handler.TypeHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Nettt服务器
 */
@Slf4j
public class WereWolfServer {

  private static final int DEFAULT_PORT = 65535;
  private RainServer server;

  public void start() throws Exception {
    this.server = new RainServer(DEFAULT_PORT);
    ServerChannelInitializer serverChannelInitializer = new ServerChannelInitializer();
    serverChannelInitializer.registerMessageHandler(TypeHandler.class);
    server.start(serverChannelInitializer);
  }

  public static void main(String[] args) throws Exception {
    new WereWolfServer().start();
  }
}

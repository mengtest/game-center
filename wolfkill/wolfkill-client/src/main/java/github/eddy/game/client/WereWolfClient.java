package github.eddy.game.client;

import github.eddy.game.client.handler.ClientChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WereWolfClient {

  private final String host;
  private final Integer port;
  Boolean stop = false;

  public WereWolfClient(String host, Integer port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap()
          .group(group)
          .channel(NioSocketChannel.class)
          .handler(new ClientChannelInitializer());

      ChannelFuture f = b.connect(host, port).sync();

      Channel channel = f.channel();
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      while (!stop) {
        channel.writeAndFlush(in.readLine() + "\r\n");
      }
      f.channel().closeFuture().sync();
    } catch (Exception e) {
      log.error("", e);
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {
    new WereWolfClient("localhost", 65535).start();
  }
}


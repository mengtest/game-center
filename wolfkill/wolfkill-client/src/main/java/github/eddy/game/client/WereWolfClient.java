package github.eddy.game.client;

import com.google.common.base.Charsets;
import github.eddy.game.client.handler.ClientChannelInitializer;
import github.eddy.game.werewolf.common.ActionCode;
import github.eddy.game.werewolf.common.ServiceCode;
import github.eddy.game.werewolf.common.TypeCode;
import github.eddy.game.werewolf.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WereWolfClient {

  private final String host;
  private final Integer port;
  private final Integer userId;

  Boolean stop = false;

  public WereWolfClient(String host, Integer port, Integer userId) {
    this.host = host;
    this.port = port;
    this.userId = userId;
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
        ByteBuf byteBuf = channel.alloc().buffer();
        byteBuf.writeCharSequence(in.readLine(), Charsets.UTF_8);
        Message message = new Message(userId, TypeCode.REQUEST, ActionCode.CHAT,
            ServiceCode.HALL_CHAT,
            byteBuf);
        channel.writeAndFlush(message);
      }
      f.channel().closeFuture().sync();
    } catch (Exception e) {
      log.error("", e);
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {
    Integer userId = Long.valueOf(new Date().getTime()).intValue();
    new WereWolfClient("localhost", 65535, userId).start();
  }
}


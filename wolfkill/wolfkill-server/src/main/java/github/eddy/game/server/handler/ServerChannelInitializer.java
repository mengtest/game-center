package github.eddy.game.server.handler;

import github.eddy.game.werewolf.protocol.Byte2MessageCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Server处理类加载器 ,编解码和控制器
 */
@Slf4j
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

  @Override
  public void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, -4, 0, false));
    pipeline.addLast(new Byte2MessageCodec());
    pipeline.addLast(new ServerMessageHandler());

    log.debug("SimpleChatClient:" + ch.remoteAddress() + "连接上");
  }
}

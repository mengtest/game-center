package github.eddy.game.server.pool;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.Map;
import lombok.Getter;

public class ChannelManager {

  @Getter
  private static final ChannelManager channelManager = new ChannelManager();

  private ChannelManager() {
  }

  //---------------------------------------------------------------------------------
  private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  private Map<Long, ChannelGroup> roomChannel;
  private Map<Long, Channel> userChannel;

  /**
   * 客户端连接服务器
   */
  public void connect(Channel incoming) {

  }

  /**
   * 登录
   */
  public void login(Long userId, Channel incoming) {

  }

  /**
   *
   * @param userId
   */
  public void logout(Long userId, Channel incoming) {

  }

  /**
   * 加入房间
   */
  public void joinRoom(Long userId, Channel incoming) {

  }

  /**
   * 退出房间
   */
  public void leftRoom(Long userId, Channel incoming) {

  }

  public ChannelGroup getAll() {
    return channels;
  }
}

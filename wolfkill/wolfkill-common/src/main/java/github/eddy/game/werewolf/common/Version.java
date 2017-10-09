package github.eddy.game.werewolf.common;

/**
 * 版本记录 ,发送包中带有版本号用来确定客户端与服务器版本是否同步
 */
public interface Version {

  long SERVER_USERID = 0L;
  short CURRENT_VERSION = 0;
}

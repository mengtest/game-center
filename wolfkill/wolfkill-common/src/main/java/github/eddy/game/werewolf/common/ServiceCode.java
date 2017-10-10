package github.eddy.game.werewolf.common;

import github.eddy.game.werewolf.excepution.ServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceCode {
  DEFAULT(-1),
  TIMER_START(11), TIMER_END(12),
  USER_LOGIN(21), USER_LOGOUT(22), ROOM_IN(23), ROOM_OUT(24),//用户相关
  HALL_CHAT(31), ROOM_CHAT(32),//聊天消息发送对象
  ;

  int code;

  public static ServiceCode transform(int codeValue) {
    for (ServiceCode type : ServiceCode.values()) {
      if (type.code == codeValue) {
        return type;
      }
    }
    throw new ServerException("枚举值转换失败:" + codeValue);
  }
}

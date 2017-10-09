package github.eddy.game.werewolf.common;

import github.eddy.game.werewolf.excepution.ServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceCode {
  DEFAULT(-1),
  TIMER_START(11),TIMER_END(12),
  HALL_CHAT(21), ROOM_CHAT(22),//聊天消息发送对象
  ;

  int code;

  public static ServiceCode transform(int codeValue) {
    for (ServiceCode type : ServiceCode.values()) {
      if (type.code == codeValue) {
        return type;
      }
    }
    throw new ServerException("枚举值转换失败:"+codeValue);
  }
}

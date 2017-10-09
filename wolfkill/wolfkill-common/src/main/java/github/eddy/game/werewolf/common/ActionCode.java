package github.eddy.game.werewolf.common;

import github.eddy.game.werewolf.excepution.ServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionCode {
  DEFAULT(-1),
  TIMER(1), //测试用
  CHAT(21), WHISPER(22),//聊天
  ;

  int code;

  public static ActionCode transform(int codeValue) {
    for (ActionCode type : ActionCode.values()) {
      if (type.code == codeValue) {
        return type;
      }
    }
    throw new ServerException("枚举值转换失败");
  }
}

package github.eddy.game.werewolf.common;

import github.eddy.game.werewolf.excepution.ServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TypeCode {
  REQUEST(1), RESPONSE(2), NOTICE(3);

  @Getter
  int code;

  public static TypeCode transform(int codeValue) {
    for (TypeCode type : TypeCode.values()) {
      if (type.code == codeValue) {
        return type;
      }
    }
    throw new ServerException("枚举值转换失败");
  }
}
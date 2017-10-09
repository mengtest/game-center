package github.eddy.game.werewolf.common;

import github.eddy.game.werewolf.excepution.ServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TypeCode {
  REQUEST(1),//客户端主动向服务器发送请求
  RESPONSE(2), //客户端主动发送请求后收到的回复
  NOTICE(3)//客户端被动收到服务器发送的请求
  ;

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
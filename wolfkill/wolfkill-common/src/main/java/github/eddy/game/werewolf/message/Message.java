package github.eddy.game.werewolf.message;

import github.eddy.game.werewolf.common.ActionCode;
import github.eddy.game.werewolf.common.ServiceCode;
import github.eddy.game.werewolf.common.TypeCode;
import github.eddy.game.werewolf.common.Version;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据传输协议定义 ,具体的数据内容包含在content中 ,由各个action/service解析
 */
@Getter
@Setter
public class Message {

  long userId;//8 由谁发送
  int length;//4 Message长度
  short version = Version.CURRENT_VERSION;//2 版本号

  TypeCode type;//2 消息类型
  ActionCode action;//2 业务模块
  ServiceCode service;//2 具体服务

  ByteBuf content;//消息体

  public Message(long userId,
      TypeCode type, ActionCode action, ServiceCode service, ByteBuf content) {
    this.length = 20 + content.readableBytes();

    this.userId = userId;
    this.type = type;
    this.action = action;
    this.service = service;
    this.content = content;
  }

  public Message(long userId, int length, short version,
      short type, short action, short service, ByteBuf content) {
    this.userId = userId;
    this.length = length;
    this.version = version;
    this.type = TypeCode.transform(type);
    this.action = ActionCode.transform(action);
    this.service = ServiceCode.transform(service);
    this.content = content;
  }
}

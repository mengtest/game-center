package github.eddy.game.werewolf.message;

import static github.eddy.game.werewolf.common.Version.SERVER_USERID;

import github.eddy.game.werewolf.common.ActionCode;
import github.eddy.game.werewolf.common.ServiceCode;
import github.eddy.game.werewolf.common.TypeCode;
import github.eddy.game.werewolf.common.Version;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据传输协议定义 ,具体的数据内容包含在content中 ,由各个action/service解析
 */
@Getter
@Setter
public class Message {

  int length;//4 Message长度

  long userId;//8 由谁发送
  short version = Version.CURRENT_VERSION;//2 版本号

  TypeCode type;//2 消息类型
  ActionCode action;//2 业务模块
  ServiceCode service;//2 具体服务

  ByteBuf content;//消息体

  public Message(TypeCode type, ActionCode action, ServiceCode service,
      ByteBuf content) {
    this(SERVER_USERID, type, action, service, content);
  }


  public Message(long userId, TypeCode type, ActionCode action, ServiceCode service,
      ByteBuf content) {
    this.length = 20 + content.readableBytes();

    this.userId = userId;
    this.type = type;
    this.action = action;
    this.service = service;
    this.content = content;
  }

  public Message(int length, long userId, short version,
      short type, short action, short service, ByteBuf content) {
    this.length = length;
    this.userId = userId;
    this.version = version;
    this.type = TypeCode.transform(type);
    this.action = ActionCode.transform(action);
    this.service = ServiceCode.transform(service);
    this.content = content;
  }

  public Message copyMessage(Channel channel) {
    ByteBuf byteBuf = channel.alloc().buffer(content.writableBytes());
    byteBuf.writeBytes(content);
    return new Message(userId, type, action, service, byteBuf);
  }
}

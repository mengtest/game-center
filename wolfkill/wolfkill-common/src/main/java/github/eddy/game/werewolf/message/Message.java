package github.eddy.game.werewolf.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

  Long userId;
  MessageTypeEnum type;
  Long roomId;
  String content;
}

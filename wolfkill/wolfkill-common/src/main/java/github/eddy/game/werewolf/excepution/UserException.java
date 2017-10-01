package github.eddy.game.werewolf.excepution;

public class UserException extends RuntimeException {

  public UserException(String message) {
    super(message);
  }

  public UserException(Throwable cause) {
    super(cause);
  }
}

package github.eddy.werewolf.common.excepution;

public class UserException extends RuntimeException {

  public UserException(String message) {
    super(message);
  }

  public UserException(Throwable cause) {
    super(cause);
  }
}

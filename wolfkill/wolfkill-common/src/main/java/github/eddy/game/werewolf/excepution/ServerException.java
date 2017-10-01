package github.eddy.game.werewolf.excepution;

public class ServerException extends RuntimeException {

  public ServerException(String message) {
    super(message);
  }

  public ServerException(Throwable cause) {
    super(cause);
  }
}

package exception;

/**
 * @author create by Xiao Han 9/17/19
 * @version 1.0
 * @since jdk 1.8
 */
public class ArgsException extends RuntimeException {
  /**
   * Constructs a new runtime exception with the specified detail message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for
   *                later retrieval by the {@link #getMessage()} method.
   */
  public ArgsException(String message) {
    super(message);
  }
}

package org.kat.app.util.exception;

public class TreeHasNoRootException extends RuntimeException {

  public TreeHasNoRootException() {
    super("Tree has no root.");
  }

  public TreeHasNoRootException(String message) {
    super(message);
  }

  public TreeHasNoRootException(String message, Throwable cause) {
    super(message, cause);
  }

  public TreeHasNoRootException(Throwable cause) {
    super(cause);
  }
}

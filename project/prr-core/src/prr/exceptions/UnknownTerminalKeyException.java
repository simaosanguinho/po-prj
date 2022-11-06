package prr.exceptions;

import java.io.Serial;

/**
 * Exception for unknown Terminal keys.
 */
public class UnknownTerminalKeyException extends Exception {

    /** Class serial number. */
    @Serial
    private static final long serialVersionUID = 202208091753L;

    /* key of the unknown terminal */
    private String key;

  /** @param key Unknown key provided. */
  public UnknownTerminalKeyException(String key) {
    super();
    this.key = key;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return this.key;
  }
}
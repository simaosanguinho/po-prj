package prr.exceptions;

import java.io.Serial;

/**
 * Exception for terminals that are already off
 */
public class TerminalAlreadyOffException extends Exception {

    /** Class serial number. */
    @Serial
    private static final long serialVersionUID = 202208091753L;

    /* key of the terminal */
    private String key;

  /** @param key key provided. */
  public TerminalAlreadyOffException(String key) {
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
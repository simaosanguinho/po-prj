package prr.exceptions;

import java.io.Serial;

/**
 * Exception for unknown client keys.
 */
public class UnknownClientKeyException extends Exception {
    
    /** Class serial number. */
    @Serial
    private static final long serialVersionUID = 202208091753L;

    /* key of the unknown client */
    private String key;

  /** @param key Unknown key provided. */
  public UnknownClientKeyException(String key) {
    super();
    this.key = key;
  }

  /** 
   * @param key   Unknown key provided.
   * @param cause What exception caused this one.
   */
  public UnknownClientKeyException(String key, Exception cause) {
    super(cause);
    this.key = key;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return this.key;
  }
}
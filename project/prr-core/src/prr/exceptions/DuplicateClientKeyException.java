package prr.exceptions;

import java.io.Serial;

public class DuplicateClientKeyException extends Exception {
  
  /** Class serial number. */
  @Serial
  private static final long serialVersionUID = 202208091753L;

  /** The agent's key. */
  private final String _key;

  /** @param key */
  public DuplicateClientKeyException(String key) {
    _key = key;
  }

  /** @return the key */
  public String getKey() {
    return _key;
  }

}
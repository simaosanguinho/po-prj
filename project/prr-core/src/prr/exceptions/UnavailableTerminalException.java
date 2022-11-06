package prr.exceptions;

import java.io.Serial;

/**
 * Exception for terminals who cannot receive a communication
 */
public class UnavailableTerminalException extends Exception {

    /** Class serial number. */
    @Serial
    private static final long serialVersionUID = 202208091753L;

      /* terminal current status */
    private final String _status;

    public UnavailableTerminalException(String status) {
        _status = status;
    }

    /** @return the key */
    public String getStatus() {
        return _status;
    }

}
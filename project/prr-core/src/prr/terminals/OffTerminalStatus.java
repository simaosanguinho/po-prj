package prr.terminals;

import java.io.Serial;

public class OffTerminalStatus extends Terminal.Status{
    
    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202208091753L;
        
    public OffTerminalStatus(Terminal terminal) {
        terminal.super();
    }
        
    @Override
    public String getName() {
        return "OFF";
    }
}


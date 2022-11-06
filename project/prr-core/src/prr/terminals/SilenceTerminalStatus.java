package prr.terminals;

import java.io.Serial;

public class SilenceTerminalStatus extends Terminal.Status{
     
    /** Serial number for serialization. */
     @Serial
     private static final long serialVersionUID = 202208091753L;
         
     public SilenceTerminalStatus(Terminal terminal) {
         terminal.super();
     }
         
     @Override
     public String getName() {
         return "SILENCE";
     }
}

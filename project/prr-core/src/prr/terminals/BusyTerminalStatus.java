package prr.terminals;

import java.io.Serial;

public class BusyTerminalStatus extends Terminal.Status{
    
     /** Serial number for serialization. */
     @Serial
     private static final long serialVersionUID = 202208091753L;
         
     public BusyTerminalStatus(Terminal terminal) {
         terminal.super();
     }
         
     @Override
     public String getName() {
         return "BUSY";
     }
}

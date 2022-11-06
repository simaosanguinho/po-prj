package prr.communications;

import prr.terminals.Terminal;
import java.io.Serial;
import java.io.Serializable;
import prr.clients.Client;

public class VoiceCommunication extends InteractiveCommunication{
    
        /** Serial number for serialization. */
        @Serial
        private static final long serialVersionUID = 202208091753L;

    public VoiceCommunication(int key, Terminal sender, Terminal receiver, String status){
        super(key, sender, receiver, status);
    
    }

    public String getType(){
        return "VOICE";
    }

}

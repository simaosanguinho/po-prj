package prr.communications;
import prr.communications.Communication;

import prr.terminals.Terminal;
import java.io.Serial;
import java.io.Serializable;

import prr.clients.Client;


public class TextCommunication extends Communication {
    
    private String _message;

    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202208091753L;


    public TextCommunication(int key, Terminal sender, Terminal receiver,
                        String message, String status){
        super(key, sender, receiver, status);
        _message = message;
        setUnits(message.length());

    }

    public String getType(){
        return "TEXT";
    }

    public String getMessage(){
        return _message;
    }

   
    
}

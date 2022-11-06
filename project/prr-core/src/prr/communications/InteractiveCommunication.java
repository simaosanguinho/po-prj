package prr.communications;

import prr.communications.Communication;
import java.io.Serial;
import java.io.Serializable;
import prr.terminals.Terminal;

abstract public class InteractiveCommunication extends Communication {
    
    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202208091753L;

    private long _duration;


    public InteractiveCommunication(int key, Terminal sender, Terminal receiver, String status){
        super(key, sender, receiver, status);
        
        

    }

    public void setDuration(long duration){
        _duration = duration; 
        setUnits(_duration);
    }

    public long getDuration(){
        return _duration;
    }
    
    
    

}

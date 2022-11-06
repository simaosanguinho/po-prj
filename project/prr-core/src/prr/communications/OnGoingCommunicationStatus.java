package prr.communications;

import prr.communications.Communication;
import java.io.Serial;
import java.io.Serializable;

public class OnGoingCommunicationStatus extends Communication.Status{

    /** Serial number for serialization.*/
    @Serial
    private static final long serialVersionUID = 202208091753L;


    public OnGoingCommunicationStatus(Communication communication) {
        communication.super();
   }
    
    @Override
    public String getName() {
        return "ONGOING";
    }
}

package prr.clients;

import java.io.Serial;
import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.VideoCommunication;


public class NormalClientType extends Client.Type {

    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202208091753L;

        
    public NormalClientType(Client client) {
        client.super();
    }
        
    @Override
    public String getName() {
        return "NORMAL";
    }

    /* changes in type occur after a payment */
    public void shouldPromote(){
        /* Promotion to GOLD */
        if(this.getClient().calculateClientBalance() > 500){
            this.getClient().resetControlVariablesClientType();
            this.setType(new GoldClientType(this.getClient()));
        }

        /* Promotion to PLATINUM */
        /* currently unavailable  */
    }

    public void shouldDemote(){
        /* can't demote from this type */
    }
}

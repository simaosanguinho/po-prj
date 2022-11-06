package prr.clients;

import java.io.Serial;
import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.VideoCommunication;
import java.util.Collection;

public class GoldClientType extends Client.Type {

    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202208091753L;
    
    public GoldClientType(Client client) {
        client.super();
    }
    
    @Override
    public String getName() {
      return "GOLD";
    }

    /* changes in type occur after ending a communication */
    public void shouldPromote(){
        /* Promotion to PLATINUM */
        if(this.getClient().calculateClientBalance() > 0){
            
            Collection<Communication> lastComms = this.getClient()
                                            .getLastNCommunicationsPerformed(5);
            
            if(lastComms.size()<5)
                return;
            
            /* are those 5 communications all VIDEO? */
            for(Communication c : lastComms)
                if(!c.isVideo())
                    return;

            /* all comms are video - upgrade */
            this.setType(new PlatinumClientType(this.getClient()));
            this.getClient().resetControlVariablesClientType();

        }
    }

    public void shouldDemote(){
        if(this.getClient().calculateClientBalance() < 0){
            this.getClient().resetControlVariablesClientType();
            this.setType(new NormalClientType(this.getClient()));
        }

    }

}

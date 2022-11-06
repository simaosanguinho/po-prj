package prr.clients;

import java.io.Serial;
import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.VideoCommunication;
import java.util.Collection;

public class PlatinumClientType extends Client.Type{

  /** Serial number for serialization. */
  @Serial
	private static final long serialVersionUID = 202208091753L;

    
  public PlatinumClientType(Client client) {
      client.super();
  }
    
  @Override
  public String getName() {
    return "PLATINUM";
  }

  
	/* changes in type occur after ending a communication */
    public void shouldDemote(){
      /* demotion to NORMAL */
      if(this.getClient().calculateClientBalance() < 0){
        this.getClient().resetControlVariablesClientType();
        this.setType(new NormalClientType(this.getClient()));
      }

		    /* demotion to GOLD */
      	/* positive balance */
      else{
			Collection<Communication> lastComms = this.getClient()
                                            .getLastNCommunicationsPerformed(2);
            
			  if(lastComms.size()<2)
				  return;

            /* are those 2 communications all TEXT? */
            for(Communication c : lastComms)
                if(!c.isText())
                    return;
                
            /* all comms are text - demote */
            this.getClient().resetControlVariablesClientType();
            this.setType(new GoldClientType(this.getClient()));

        }
      	
  	}

  	public void shouldPromote(){
      	/* further promotions are currently unavailable */

  	}

}

package prr.notifications;

import java.io.Serial;
import prr.terminals.Terminal;

public class B2INotification extends Notification {
    
    /** Serial number for serialization. */
    @Serial
	private static final long serialVersionUID = 202208091753L;

    private final String _type = "B2I";

    public B2INotification(Terminal notificationSender){
        super(notificationSender);
    }


    public String getType(){
        return _type;
    }
}

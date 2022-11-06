package prr.notifications;

import java.io.Serial;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;

import prr.clients.Client;

/* method of delivering notifications in the app */
public class NotificationDeliveryApp implements NotificationDeliveryMethod{
    
    /** Serial number for serialization. */
    @Serial
	private static final long serialVersionUID = 202208091753L;

    private final Client _receiver;

    public NotificationDeliveryApp(Client receiver){
        _receiver = receiver;
    }


    @Override
    public void deliver(Notification n){

        /* cannot admit duplicate notifications */
        for(Notification notification : _receiver.getNotifications()){
            if(notification.equals(n))
                return;   
        }
        _receiver.addAppNotifications(n);     
    }
}

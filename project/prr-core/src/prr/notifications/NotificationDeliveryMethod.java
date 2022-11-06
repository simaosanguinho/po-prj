package prr.notifications;

import java.io.Serializable;
import prr.clients.Client;

public interface NotificationDeliveryMethod extends Serializable{

    void deliver(Notification notification);
}

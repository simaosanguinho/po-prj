package prr.utilities;

import prr.clients.Client;
import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.communications.Communication;
import prr.notifications.Notification;
import java.io.Serializable;


public abstract class Visitor<V> implements Serializable {
    
    public abstract V visit(Client client);

    public abstract V visit(BasicTerminal terminal);

    public abstract V visit(FancyTerminal terminal);

    public abstract V visit(Communication communication);

    public abstract V visit(Notification notification);
}

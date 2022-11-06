package prr.notifications;

import java.io.Serializable;
import prr.utilities.Visitable;
import prr.utilities.Visitor;
import prr.terminals.Terminal;

abstract public class Notification implements Visitable, Serializable{
    

    private final Terminal _terminal;

    public Notification(Terminal t){
        _terminal = t;
    }

    abstract public String getType();


    public Terminal getTerminal(){
        return _terminal;
    }

    @Override
    public <V> V accept(Visitor<V> visitor){
            return visitor.visit(this);
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Notification){
            Notification n = (Notification) other;
            return this.getType().equals(n.getType())
                    && _terminal.getKey().equals(n.getTerminal().getKey());                            
        }
        return false;
    }
}

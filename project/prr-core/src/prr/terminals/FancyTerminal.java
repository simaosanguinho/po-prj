package prr.terminals;

import java.io.Serial;

import prr.utilities.Visitable;
import prr.utilities.Visitor;

public class FancyTerminal extends Terminal{
    
    /** Serial number for serialization. */
    @Serial
	private static final long serialVersionUID = 202208091753L;

    private final String _type = "FANCY";

    public FancyTerminal(String key, String clientId, String status){
        super(key, clientId, status);

    }

    @Override
    public String getType(){ return _type;}

    @Override
        public <V> V accept(Visitor<V> visitor){
                return visitor.visit(this);
        }

}

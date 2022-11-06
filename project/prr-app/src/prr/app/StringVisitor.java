package prr.app;

import prr.clients.Client;
import prr.communications.Communication;
import prr.notifications.Notification;
import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.utilities.Visitor;
import java.util.StringJoiner;
import static java.lang.Math.round;


public class StringVisitor extends Visitor<String>{

    @Override
    public String visit(Client client){
        return new StringJoiner("|")
                .add("CLIENT")
                .add(client.getKey())
                .add(client.getName())
                .add(Integer.toString(client.getTaxId()))
                .add(client.getTypeName())
                .add(client.isNotifiableString())
                .add(Integer.toString(client.NumberTerminals()))
                .add(Long.toString(client.getPayments()))
                .add(Long.toString(client.getDebts()))
                .toString();
    }
    
    @Override
    public String visit(BasicTerminal terminal){
        String sj = new StringJoiner("|")
                .add(terminal.getType())
                .add(terminal.getKey())
                .add(terminal.getClientId())
                .add(terminal.getStatus())
                .add(Long.toString(terminal.getBalancePaid()))
                .add(Long.toString(terminal.getBalanceDebts()))
                .toString();

                return sj + (!terminal.getFriends().isEmpty() 
                        ? ("|" + terminal.getFriendsIds()) :  "");
    }

    @Override
    public String visit(FancyTerminal terminal){
        String sj = new StringJoiner("|")
                .add(terminal.getType())
                .add(terminal.getKey())
                .add(terminal.getClientId())
                .add(terminal.getStatus())
                .add(Long.toString(terminal.getBalancePaid()))
                .add(Long.toString(terminal.getBalanceDebts()))
                .toString();
                
                return sj + (!terminal.getFriends().isEmpty() 
                        ? ("|" + terminal.getFriendsIds()) :  "");
    }

    @Override
    public String visit(Communication c){
        return new StringJoiner("|")
                .add(c.getType())
                .add(Integer.toString(c.getKey()))
                .add(c.getSenderId())
                .add(c.getReceiverId())
                .add(Long.toString(c.getUnits()))
                .add(Long.toString(round(c.getPrice())))
                .add(c.getStatus())
                .toString();
    }

    @Override
    public String visit(Notification n){
        return new StringJoiner("|")
                .add(n.getType())
                .add(n.getTerminal().getKey())
                .toString();
    }


}

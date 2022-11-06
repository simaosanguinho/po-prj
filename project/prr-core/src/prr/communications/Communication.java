package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.utilities.Visitor;
import prr.utilities.Visitable;
import prr.clients.Client;


import java.io.Serial;

abstract public class Communication implements Serializable, Visitable {
    
    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202208091753L;

    private int _key;
    private Terminal _sender;
    private Terminal _receiver;
    private long _units = 0;
    private double _price = 0;
    private Status _status;
    private boolean _paid = false;  
   
    


    public Communication(int key, Terminal sender, Terminal receiver, String status){
        _key = key;
        _sender = sender;
        _receiver = receiver;
        setStatus(status);
    }


    public int getKey(){ return _key;}

    public String getSenderId(){return _sender.getKey();}

    public String getReceiverId(){return _receiver.getKey();}

    public Terminal getSender(){ return _sender;}

    public Terminal getReceiver() { return _receiver;}

    public long getUnits() {return _units;}

    public double getPrice() {return _price;}

    public String getStatus(){ return _status.getName();}

    public boolean isPaid(){return _paid;}

    public void setUnits(long units){ _units = units;}

    /* Status of Communication */
    public abstract class Status implements Serializable{

        public abstract String getName();
    }

    public void pay(){ _paid = true;}


    public void setStatus(String status){
        _status = switch(status) {
        case "ONGOING" -> new OnGoingCommunicationStatus(this);
        case "FINISHED" -> new FinishedCommunicationStatus(this);
        default -> new FinishedCommunicationStatus(this);
        };
    }

    public void addCommunicationToSender(){
        _sender.addPerformedCommunication(this);
    }

    public void addCommunicationToReceiver(){
        _receiver.addReceivedCommunication(this);
    }

    public abstract String getType();

    @Override
    public <V> V accept(Visitor<V> visitor){
            return visitor.visit(this);
    }

    /* is the given terminal part of the communication */
    public boolean isTerminalInCommunication(Terminal t){
        if(t == _sender || t == _receiver)
            return true;
        
        return false;
    }

    public void definePrice(double price){
        _price = price;
        _sender.increaseDebt(price);
    
    }

    public boolean isFinished(){
        if(_status.getName() == "FINISHED"){
            return true;
        }
        return false;
    }

    public boolean isOnGoing(){   
        if(_status.getName() == "ONGOING"){
            return true;
    }
        return false;
    }

    public boolean isText(){   
        if(getType() == "TEXT"){
            return true;
    }
        return false;
    }

    public boolean isVoice(){   
        if(getType() == "VOICE"){
            return true;
    }
        return false;
    }

    public boolean isVideo(){   
        if(getType() == "VIDEO"){
            return true;
    }
        return false;
    }


    

}

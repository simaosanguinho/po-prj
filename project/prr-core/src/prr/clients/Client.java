package prr.clients;

import java.io.Serializable;
import java.io.Serial;

import prr.terminals.Terminal;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import prr.utilities.Visitor;
import prr.utilities.Visitable;

import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.VideoCommunication;
import prr.notifications.Notification;
import prr.notifications.NotificationDeliveryApp;
import prr.notifications.NotificationDeliveryMethod;
import prr.notifications.O2INotification;
import prr.notifications.O2SNotification;
import prr.notifications.S2INotification;
import prr.notifications.B2INotification;
import prr.clients.TariffPlan;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class Client implements Serializable, Comparable<Client>, Visitable{
    
    /** Serial number for serialization. */
    @Serial
	private static final long serialVersionUID = 202208091753L;

    private final String _key;
    private final String _name;
    private final int _taxId;
    private long _payments;
    private long _debts;
    private Type _type;
    private boolean _notifiable = true;


    private TariffPlan _tariffPlan = new BaseTariffPlan();
    private final NotificationDeliveryMethod _deliveryMethod = 
                                    new NotificationDeliveryApp(this);


    /* list of terminals that belong to this client */
    Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();

    /* list of communications in the app */
    private Set<Notification> _appNotifications = new HashSet<Notification>();


    /* for verifying type changes - Control Variables for Client Type */
    /* list of communication's references performed by this client - to test type changes */
    List<Communication> _cPerformed = new ArrayList<Communication>();

    
    
    public Client(String key, String name, int taxId){
        _key = key;
        _name = name;
        _taxId = taxId;
        _type = new NormalClientType(this);
    }

    public String getKey() {return _key;}

    public String getName() {return _name;}

    public int getTaxId() {return _taxId;}

    public long getPayments() {return _payments;}
    
    public long getDebts() {return _debts;}

    public String getTypeName() {return _type.getName();}

    public Client.Type getType() {return _type;}

    public TariffPlan getTariffPlan() {return _tariffPlan;} 


    public NotificationDeliveryMethod getNotificationDeliveryMethod(){
        return _deliveryMethod;
    }

    public Collection<Terminal> getTerminals() {return _terminals.values();}

    public Collection<Communication> getCommunicationsPerformed(){
        return _cPerformed;
    }

    public Collection<Notification> getNotifications(){
        return _appNotifications;
    }

    public boolean isNotifiable(){
        return _notifiable;
    }

    public void setNotifiable(boolean notifiable){
        _notifiable = notifiable;
    }

    public String isNotifiableString() {
        if(_notifiable) {return "YES";}
        return "NO";
    }

    /* change the client's tariff plan */
    public void setTariffPlan(String plan){
        _tariffPlan = switch(plan) {
            case "BASE" -> new BaseTariffPlan();
            default -> new BaseTariffPlan();
            };

    }

    @Override
    public <V> V accept(Visitor<V> visitor){
        return visitor.visit(this);
    }

    
    @Override
    public int compareTo(Client client){
        if(_key.equals(client.getKey()))
            return 1;
        return 0;
    }

    @Override 
    public boolean equals(Object other){
        if(other instanceof Client){
            Client o = (Client) other;
            return this.compareTo(o) == 0;
        }
        return false;
    }
    
    public abstract class Type implements Serializable {

        public abstract String getName();
        
        public Client getClient(){return Client.this;}

        protected void setType(Type type){
            Client.this._type = type;
        }

        /* transition between client types */
         public abstract void shouldPromote();
         public abstract void shouldDemote();
    }

    public boolean isNormalClient(){
        return _type.getName().equals("NORMAL");
    }

    public boolean isGoldClient(){
        return _type.getName().equals("GOLD");
    }

    public boolean isPlatinumClient(){
        return _type.getName().equals("Platinum");
    }
    

    public void addTerminalInClient(Terminal t){
        _terminals.put(t.getKey(), t);
    }

    /* returns the number of terminals associated with the Client */
    public int NumberTerminals() {
        return _terminals.size();
    }

    public void increaseDebt(double increment){
        _debts += increment;
    }

    
    public void decreaseDebt(double decrement){
        _debts -= decrement;
    }

    public void increasePayments(double increment){
            _payments += increment;

    }


    public void payCommunication(double price){
        decreaseDebt(price);
        increasePayments(price);

        /* if client type is Normal try a promotion */
        if(this.isNormalClient()){
            shouldTypeChange();
        }   
    }

    public void shouldTypeChange(){
        _type.shouldPromote();
        _type.shouldDemote();
    }
    
    public long calculateClientBalance(){return _payments - _debts;}

    public Collection<Communication> getLastNCommunicationsPerformed(int n){
        /* if there are fewer than n, it returns an empty list */
       return _cPerformed.subList(Math.max(_cPerformed.size() - n, 0),
                                    _cPerformed.size());
    }

    public void clearClientNotifications(){
        _appNotifications.clear();
    }

    public void addAppNotifications(Notification n){
        _appNotifications.add(n);
        
    }

    public void resetControlVariablesClientType(){
        /* past communication are not considered for new type change */
        _cPerformed.clear();
    }

}

package prr.terminals;

import prr.Network;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadyOnException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.DestinationTerminalOffException;
import prr.exceptions.InvalidCommunicationException;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.UnrecognizedEntryException;
import prr.notifications.O2SNotification;
import prr.notifications.S2INotification;
import prr.exceptions.BasicTerminalDestinationException;
import prr.exceptions.BasicTerminalOriginException;
import prr.exceptions.CommunicationPaymentUnavailableException;
import prr.exceptions.UnavailableTerminalException;
import prr.exceptions.NoOnGoingCommunicationException;
import java.io.Serializable;
import java.io.IOException;
import java.io.Serial;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.HashMap;
import prr.utilities.Visitor;
import prr.utilities.Visitable;
import prr.clients.TariffPlan;
import prr.communications.Communication;
import prr.communications.InteractiveCommunication;
import java.util.Comparator;
import java.util.Collection;
import prr.clients.Client;

import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.communications.VoiceCommunication;
import prr.notifications.Notification;
import prr.notifications.NotificationDeliveryMethod;
import prr.notifications.O2INotification;
import prr.notifications.O2SNotification;
import prr.notifications.S2INotification;
import prr.notifications.B2INotification;


/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable, Comparable<Terminal>, Visitable{

	/** Serial number for serialization. */
        @Serial
	private static final long serialVersionUID = 202208091753L;

        private final String _key;
        private final String _clientId;
        private Status _status;
        private Status _prevStatus;
        private long _balancePaid;
        private long _balanceDebts;

        /* collection of friendly terminals */
        Map<String, Terminal> _friends = new TreeMap<String, Terminal>(); 

        /* collection of this terminal's past communications */
        Map<Integer, Communication> _communicationsReceived = 
                        new TreeMap<Integer, Communication>();

                        /** change to COmmunications  */
        Map<Integer, Communication> _communicationsPerformed = 
                        new TreeMap<Integer, Communication>();

        List<Client> _clientToNotify = new LinkedList<Client>();

        public Terminal(String key, String clientId, String status){
                _key = key;
                _clientId = clientId;
                _balanceDebts = 0;
                _balancePaid = 0;
                //_prevStatus = _status;
                setStatus(status);
                _prevStatus = _status;
        }

        public String getKey(){ return _key;}

        public String getClientId(){ return _clientId;}

        public long getBalancePaid(){ return _balancePaid;}

        public long getBalanceDebts(){ return _balanceDebts;}

        public String getStatus(){ return _status.getName();}

        public Status getPrevStatus(){return _prevStatus;}

        public Map<String, Terminal> getFriends(){ return _friends;}

        public Collection<Communication> getCommunicationsReceived() {
                return _communicationsReceived.values();
        }

        public Collection<Communication> getCommunicationsPerformed() {
                return _communicationsPerformed.values();
        }

        public abstract String getType();

        /* Status of Terminal */
        public abstract class Status implements Serializable {

                public abstract String getName();

        }

        public void setStatus(String status){

                setPrevStatus(_status);

                _status = switch(status) {
                case "OFF" -> new OffTerminalStatus(this);
                case "IDLE" -> new IdleTerminalStatus(this);
                case "BUSY" -> new BusyTerminalStatus(this);
                case "SILENCE" -> new SilenceTerminalStatus(this);
                default -> new IdleTerminalStatus(this);
                };


        }
        public void setPrevStatus(Status status){ _prevStatus = status;}

        @Override
        public int compareTo(Terminal terminal){
                return this.getKey().compareTo(terminal.getKey());
        }


        public boolean isBalancePositive(){
                return _balancePaid > _balanceDebts;
        }


        public String getFriendsIds(){

                if(_friends.isEmpty()){
                       return null;
                }
                List<String> l = new ArrayList<String>(_friends.keySet());

                return String.join(",", l);
        }

        public void addFriend(String keyFriend, Network network) 
                throws UnknownTerminalKeyException{
                
                Terminal friend = network.showTerminal(keyFriend);

                if(!_friends.containsKey(keyFriend) && !_key.equals(keyFriend)){
                        _friends.put(keyFriend, friend);
                }

        }

        public void removeFriend(String keyFriend)
			throws UnknownTerminalKeyException{

		
		if(!_friends.containsKey(keyFriend)){
                      throw new UnknownTerminalKeyException(keyFriend);
		}
                 _friends.remove(keyFriend);

	}

        
        public void turnOnTerminal() throws TerminalAlreadyOnException,
                        UnrecognizedEntryException{

                String status = this.getStatus();

		if(status == "IDLE"){
			throw new TerminalAlreadyOnException(getKey());
		}
                /* send Notifications */
                switch(status) {
                        case "BUSY" -> sendNotifications("B2I");
                        case "OFF" -> sendNotifications("O2I");
                        case "SILENCE" -> sendNotifications("S2I");
                        default -> throw new UnrecognizedEntryException(status);
                        };
        
		this.setStatus("IDLE");

	}


	public void silenceTerminal() throws TerminalAlreadySilentException,    
                        UnrecognizedEntryException{

		if(this.getStatus() == "SILENCE"){
			throw new TerminalAlreadySilentException(getKey());
		}

                sendNotifications("O2S");
		this.setStatus("SILENCE");
	}


        public void turnOffTerminal() throws TerminalAlreadyOffException{

		if(this.getStatus() == "OFF"){
			throw new TerminalAlreadyOffException(getKey());
		}
		this.setStatus("OFF");
	}

        public void sendTextCommunication(String receiverId, String message, 
                        String status, Network network)
			throws DestinationTerminalOffException, UnknownTerminalKeyException, 
                                UnknownClientKeyException, UnrecognizedEntryException{
                
                
		Terminal receiver = network.showTerminal(receiverId);
		Client client = network.showClient(this.getClientId());


		if(receiver.getStatus() == "OFF"){
                        /*receiver registers clients to notify */
                        receiver._clientToNotify.add(network.showClient(this.getClientId()));
			throw new DestinationTerminalOffException();
		}
                
		/* only increment if no exceptions were thrown */
		network.incrementNCommunications();

		TextCommunication tc = new TextCommunication(network.getNCommunications(), 
					        this, receiver, message, status);
		network.registerCommunication(tc, client);

		/* text has been sent */
		tc.setStatus("FINISHED");
		
		/* register price in Network */
		double price = calculateCommunicationPrice(tc, client);
		tc.definePrice(price);
		client.increaseDebt(price);

                /* attempt client type change */
                client.shouldTypeChange();
                 

	}

	public void startInteractiveCommunication(String receiverId,
                String status, String type, Network network) throws 
                        UnrecognizedEntryException, UnknownTerminalKeyException, 
                        BasicTerminalOriginException, BasicTerminalDestinationException, 
                        UnavailableTerminalException, UnknownClientKeyException {

                Terminal receiver = network.showTerminal(receiverId);
                Client client = network.showClient(this.getClientId());
                
                /* receiver must be IDLE */
                if(!receiver.getStatus().equals("IDLE")){
                        /* receiver registers clients to notify */
                        receiver._clientToNotify.add(network.showClient(this.getClientId()));
                        throw new UnavailableTerminalException(receiver.getStatus());
                }

                /* Basic Terminals cannot communicate through video */
                if(type.equals("VIDEO")){
                        if(!this.canStartVideoCommunication()){
                                throw new BasicTerminalOriginException();
                        }
                        if(!receiver.canStartVideoCommunication()){
                                throw new BasicTerminalDestinationException();
                        }
                }

                /* terminal cannot communicate with itself */
                if(_key.equals(receiverId)){
                        throw new UnavailableTerminalException(receiver.getStatus());
                }
                
                Communication c = registerInteractiveCommunication(network, receiver, 
                        network.getNCommunications() + 1, type, status);

                /* only increment if no exceptions were thrown */
                network.incrementNCommunications();
                network.registerCommunication(c, client);
        }

        public Communication registerInteractiveCommunication(Network network, Terminal receiver,
                        int key, String type, String status) throws UnrecognizedEntryException{
                
                Communication ic;
                switch(type){
                case "VOICE" -> ic = new VoiceCommunication(key, this, receiver, status);
                case "VIDEO" -> ic = new VideoCommunication(key, this, receiver, status);
                default -> throw new UnrecognizedEntryException(type);
                }

                /* change status of terminals */
                receiver.setStatus("BUSY");
                setStatus("BUSY");
                return ic;
        }
        
        	

	public double endInteractiveCommunication(int duration, Network network)
                throws UnknownClientKeyException, UnrecognizedEntryException,
                        NoOnGoingCommunicationException, TerminalAlreadySilentException,
                        TerminalAlreadyOnException{

                Communication c = this.getOnGoingCommunication();

                Terminal receiver = c.getReceiver();

                c.setStatus("FINISHED");
                c.setUnits(duration);

                /* change status of terminals */
                this.changeStatusBack();
                receiver.changeStatusBack();
                

                /* register price in Network */
                Client client = network.showClient(this.getClientId());
                
                double price = calculateCommunicationPrice(c, client);
                
                c.definePrice(price);
                client.increaseDebt(price);

                /* attempt client type change */
                client.shouldTypeChange();

                return c.getPrice();
        }    

        /* get old status */
        public void changeStatusBack() throws TerminalAlreadySilentException,
                        TerminalAlreadyOnException, UnrecognizedEntryException{

                if(_prevStatus.getName().equals("IDLE")){
                        this.turnOnTerminal();      
                }
                        

                if(_prevStatus.getName().equals("SILENCE")){
                        this.silenceTerminal();
                }
                        
        }


        /* get ongoing communication where terminal t is involved */
	public Communication showOngoingCommunication(Network network)
                throws NoOnGoingCommunicationException {

                Communication c = null;
                /* no ongoing communication happening for terminal t */
                if(this.getStatus() != "BUSY"){
                        throw new NoOnGoingCommunicationException();
                }

                for(Communication com : network.showAllCommunications()){
                       /* ongoing communication */
                        if(com.getStatus() == "ONGOING" && com.isTerminalInCommunication(this))
                                return com; 
                }

                throw new NoOnGoingCommunicationException();    
        }                
                             
        /**
         * Checks if this terminal can end the current interactive communication.
         *
         * @return true if this terminal is busy (i.e., it has an active interactive communication) and
         *          it was the originator of this communication. 
         **/
        public boolean canEndCurrentCommunication() {
                if(_status.getName() == "BUSY"){
                        for(Map.Entry<Integer, Communication> entry: _communicationsPerformed.entrySet()){   

                                Communication c = entry.getValue();
                                if(c.getSender() == this)
                                        return true;
                        }
                }
                return false;
                
        }
        /**
         * Checks if this terminal can start a new communication.
         *
         * @return true if this terminal is neither off neither busy, false otherwise.
         **/
        public boolean canStartCommunication() {
                if(_status.getName() == "BUSY" || _status.getName() == "OFF")
                        return false;
                return true;
        }


        /* must be IDLE/SILENCE and FANCY */
        public boolean canStartVideoCommunication(){
                if(this.canStartCommunication() && this.getType() == "FANCY"){
                        return true;
                }
                return false;
        }



        /* returns the ongoing communication started by this terminal */
        public Communication getOnGoingCommunication()
                throws NoOnGoingCommunicationException{

                Communication c;

                for(Map.Entry<Integer, Communication> entry: _communicationsPerformed.entrySet()){

			c = entry.getValue();
                        if(c.getStatus().equals("ONGOING"))
                                return  c;
                }
                throw new NoOnGoingCommunicationException();

        }


        /**
	 * Verifies if a string is numeric. 
	 * 
	 * @param key String that is to be tested if numeric or not
	 * @return true if string is only build by numbers and false otherwise
	 */
	public boolean isNumericString(String key){
		if(key == null){
			return false;
		}
		try{
			int i = Integer.parseInt(key);
			
		} catch (NumberFormatException e){
			return false;
		}
		return true;
	}

	/**
	 * Verifies if a string is suitable to be a terminal key
	 * 
	 * @param key String that is to be tested if valid or not
	 * @return true if is valid to be a terminal key and false otherwise
	 */
	public boolean isTerminalKeyValid(String key){
		return isNumericString(key) && (key.length() == 6);
	}


        public void addPerformedCommunication(Communication c){
                _communicationsPerformed.put(c.getKey(), c);
        }

        public void addReceivedCommunication(Communication c){
                _communicationsReceived.put(c.getKey(), c);
        }

        public boolean isFriend(Terminal other){
                return _friends.containsKey(other.getKey());
        }

        public void increaseDebt(double increment){
                _balanceDebts += increment;

        }

        public void decreaseDebt(double decrement){
                _balanceDebts -= decrement;
        }

        public void increasePayments(double increment){
                _balancePaid += increment;

        }


        public double calculateCommunicationPrice(Communication communication, Client sender)
                throws UnrecognizedEntryException{
                
                //String type = communication.getType();
                TariffPlan plan = sender.getTariffPlan();
                
                boolean friends = communication.getSender().isFriend(communication.getReceiver());
                
                double price = switch(sender.getTypeName()) {
                        case "NORMAL" -> plan.calculateNormalPrice(communication, friends);
                        case "GOLD" -> plan.calculateGoldPrice(communication, friends);
                        case "PLATINUM" -> plan.calculatePlatinumPrice(communication, friends);
                        default -> throw new UnrecognizedEntryException(sender.getTypeName());
                        };

                return price;
           
        }

        public void performPayment(int commId, Network network) 
                throws InvalidCommunicationException, UnknownClientKeyException,
                CommunicationPaymentUnavailableException {

                Communication c = network.fetchCommunication(commId);
                double price = c.getPrice();

                if(!c.getSender().equals(this) || c.isPaid() || !c.isFinished()){
                        throw new CommunicationPaymentUnavailableException();
                }

                decreaseDebt(price);
                increasePayments(price);

                Client client = network.showClient(_clientId);
                client.payCommunication(price);

                c.pay();
        }


        public void sendNotifications(String type) throws UnrecognizedEntryException{
                for(Client c : _clientToNotify){
                        
                        Notification n = switch(type){
                                case "O2S" -> new O2SNotification(this);
                                case "S2I" -> new S2INotification(this);
                                case "O2I" -> new O2INotification(this);
                                case "B2I" -> new S2INotification(this);
                                default -> throw new UnrecognizedEntryException(type);
                        };

                        /* deliver notifications with client's preferred method */
                        NotificationDeliveryMethod deliveryMethod =    
                                        c.getNotificationDeliveryMethod();
                        deliveryMethod.deliver(n);

                }
        }
           
}

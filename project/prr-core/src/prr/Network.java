package prr;

import java.io.Serializable;
import java.io.IOException;
import java.io.Serial;
import java.lang.NumberFormatException;
import java.util.stream.Collectors;
import prr.clients.Client;
import prr.communications.Communication;
import prr.communications.InteractiveCommunication;
import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.communications.VoiceCommunication;
import prr.exceptions.UnrecognizedEntryException;
import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.terminals.Terminal;
import prr.exceptions.DuplicateClientKeyException;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.ImportFileException;
import prr.exceptions.DuplicateTerminalKeyException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DestinationTerminalOffException;
import prr.exceptions.TerminalAlreadyOnException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.UnavailableTerminalException;
import prr.exceptions.BasicTerminalOriginException;
import prr.exceptions.BasicTerminalDestinationException;
import prr.exceptions.NoOnGoingCommunicationException;
import prr.exceptions.NotificationsAlreadyDisabledException;
import prr.exceptions.NotificationsAlreadyEnabledException;
import prr.exceptions.InvalidCommunicationException;
import prr.exceptions.NoClientsWithDebtsException;
import prr.notifications.Notification;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.Collection;
import prr.utilities.Visitor;
import prr.utilities.Visitable;
import java.util.Comparator;


/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

	/** Serial number for serialization. */
	@Serial
	private static final long serialVersionUID = 202208091753L;
	
	/* collection of clients registered */
	private Map<String, Client> _clients = new  TreeMap<String,Client>(String.CASE_INSENSITIVE_ORDER);

	/* collection of terminals registered */
	private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>(String.CASE_INSENSITIVE_ORDER);

	/* number of communications in Network */
	private int _nCommunications = 0;

	/* list of communications in  Network */
	private Map<Integer, Communication> _communications = new TreeMap<Integer, Communication>();

	/* compare clients by debts and if equal by key  - not serializable */
	transient Comparator<Client>  _compareClientsDebts = Comparator.comparing(Client::getDebts)
														.reversed()
														.thenComparing(Client::getKey);
    										
	/**
	 * It increments the number of communications used for communications' keys
	 */
	public void incrementNCommunications(){
		_nCommunications++;
	}
	
	/**
	 * It gets the current number of communications in network
	 * 
	 * @return number of communications in network
	 */
	public int getNCommunications(){ 
		return _nCommunications;
	}	
	
	
	/**
	 * Read text input file and create corresponding domain entities.
	 * 
	 * @param filename Name of the text input file
     * @throws UnrecognizedEntryException if some entry is not correct
	 * @throws IOException if there is an IO error while processing the text file
	 * @throws ImportFileException if there is an IO error while importing the file
	 */
	public void importFile(String filename) 
		throws UnrecognizedEntryException, IOException, ImportFileException
			 {
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			  String line;
			  /* read file until the end, ignoring the | characters */
				while ((line = br.readLine()) != null) {
					String[] fields = line.split("\\|");
			
					try {
						registerEntry(fields);
			  		} catch (UnrecognizedEntryException| DuplicateClientKeyException| 
						DuplicateTerminalKeyException |InvalidTerminalKeyException|
						UnknownClientKeyException| UnknownTerminalKeyException e) {
						e.printStackTrace();
			  		}
				}
			} catch (IOException e) {
				throw new ImportFileException(filename);
		    }	
     }

	/**
	 * Receives an entry from a file and registers it in the network.
	 * 
	 * @param fields Set of fields of the entry to be registered
     * @throws UnrecognizedEntryException if some entry is not correct
	 * @throws DuplicateClientKeyException if the client key has already been assigned						
	 * @throws DuplicateTerminalKeyException if the terminal key has already been assigned
	 * @throws UnknownClientKeyException if a client that does not exists is assessed
	 * @throws UnknownTerminalKeyException if a terminal that does not exists is assessed
	 * @throws InvalidTerminalKeyException if a terminal key is not numeric and exactly 
	 * 										6 digits long
	 */
	 public void registerEntry(String... fields) 
	 		throws UnrecognizedEntryException, DuplicateClientKeyException, 
				UnknownClientKeyException, UnknownTerminalKeyException,
				DuplicateTerminalKeyException, InvalidTerminalKeyException {
   		switch (fields[0]) {
   		case "CLIENT" -> registerClient(fields[1], fields[2], Integer.parseInt(fields[3]));
   		case "BASIC" -> registerTerminal(fields[0], fields[1], fields[2], fields[3]);
		case "FANCY" -> registerTerminal(fields[0], fields[1], fields[2], fields[3]);
		case "FRIENDS" -> registerFriends(fields[1], fields[2]);
   		default -> throw new UnrecognizedEntryException(fields[0]);
   		}
 	}


	/**
	 * Creates a client and registers it to the network.
	 * 
	 * @param key Chain of characters used to identify this client
	 * @param name This client's name
	 * @param taxId This client's tax identifier - must be an integer
	 * @return the client that was created
	 * @throws DuplicateClientKeyException if the client key has already been assigned
	 */
	public Client registerClient(String key, String name, int taxId) 
			throws DuplicateClientKeyException {	

		if(_clients.containsKey(key)){
			throw new DuplicateClientKeyException(key);
		}
		Client c = new Client(key, name, taxId);
		_clients.put(key, c);

		return c;
	}


	/**
	 * Receives a client key and finds such client.
	 * 
	 * @param key Chain of characters used to identify a client
	 * @return the client associated with the key
	 * @throws UnknownClientKeyException if a client that does not exists is assessed
	 */
	public Client showClient(String key) throws UnknownClientKeyException {
		Client c = _clients.get(key);
		if(c == null){
			throw new UnknownClientKeyException(key);
		}
		return c;	
	}

	/**
	 * Receives a client key and returns the notifications
	 * 
	 * @param clientId Chain of characters used to identify a client
	 * @return a collection of notifications to be read by a client
	 * @throws UnknownClientKeyException if a client that does not exists is assessed
	 */
	public Collection<Notification> readClientNotificationsApp(String clientId)
				throws UnknownClientKeyException{
		Client c = showClient(clientId);
		Collection<Notification> notifications = new LinkedList<Notification>(c.getNotifications());	
		c.clearClientNotifications();
		return notifications;
	}


	/**
	 * It gets a collection of all clients registered in the network.
	 * 
	 * @return sorted (by key) list of clients 
	 */
	public Collection<Client> showAllClients() {
		return _clients.values();
	}

	/**
	 *  It gets all clients without debts in the network
	 * 
	 * @return a collection of all clients in the network without any debts
	 */
	public Collection<Client> showClientsWithoutDebts(){

		Map<String, Client> clientsWithoutDebts = new HashMap<String, Client>();

		for(Map.Entry<String, Client> entry: _clients.entrySet()){
			Client c = entry.getValue();
			if(c.getDebts() == 0)
				clientsWithoutDebts.put(c.getName(), c);	
		}
		return clientsWithoutDebts.values();
	}

	/**
	 * It gets all clients with debts in the network
	 * 
	 * @return a collection of all clients in the network with debts
	 * @throws NoClientsWithDebtsException if there are not any clients with debts
	 */
	public Collection<Client> showClientsWithDebts()
			throws NoClientsWithDebtsException{

		List<Client> clientsWithDebts = new LinkedList<Client>();

		for(Map.Entry<String, Client> entry: _clients.entrySet()){
			Client c = entry.getValue();
			if(c.getDebts() > 0)
				clientsWithDebts.add(c);
		}
		/* no clients with debts */
		if(clientsWithDebts.isEmpty())
			throw new NoClientsWithDebtsException();

		clientsWithDebts = 
					clientsWithDebts.stream()
								    .sorted(_compareClientsDebts)
						 		    .collect(Collectors.toList());
		return clientsWithDebts;
	}

	/**
	 * Creates a terminal and registers it in the network.
	 * 
	 * @param type New terminal's type - Fancy or Basic
	 * @param key Chain of characters used to identify this terminal
	 * @param clientId Key of the client that is owner of the terminal
	 * @param status Terminal's status - Idle, Busy, Off or Silence
	 * @return the terminal that was created
	 * @throws UnrecognizedEntryException if some entry is not correct
	 * @throws DuplicateTerminalKeyException if the terminal key has already been assigned
	 * @throws InvalidTerminalKeyException if a terminal key is not numeric and exactly 
	 * 										6 digits long
	 * @throws UnknownClientKeyException if a client that does not exists is assessed
	 * 
	 */
	public Terminal registerTerminal(String type, String key, String clientId, String status) 
			throws UnrecognizedEntryException, DuplicateTerminalKeyException, InvalidTerminalKeyException,
				UnknownClientKeyException {
			

			/* tests if the client key is already registered */
			if(!_clients.containsKey(clientId)){
				throw new UnknownClientKeyException(clientId);
			}

			/* tests if there are not any other terminals with the 
			 * same key 
			 */
			if(_terminals.containsKey(key)){
				throw new DuplicateTerminalKeyException(key);
			}
			Terminal t;

			switch (type) {
			case "BASIC" -> t = new BasicTerminal(key, clientId, status);
			case "FANCY" -> t = new FancyTerminal(key, clientId, status);
			default -> throw new UnrecognizedEntryException(type);
			}

			/* tests if the terminal key is valid */
			if(!t.isTerminalKeyValid(key)){
				throw new InvalidTerminalKeyException(key);
			}

			/* add terminal to owner's terminal's list */
			Client c = showClient(clientId);
			c.addTerminalInClient(t);

			this._terminals.put(key, t);
			return t;
	}

	/**
	 * It gets a specific terminal.
	 * 
	 * @param key Chain of characters used to identify this terminal 
	 * @return the terminal associated with the key
	 * @throws UnknownTerminalKeyException if a terminal that does not exists 
	 * 										is assessed
	 */
	public Terminal showTerminal(String key) throws UnknownTerminalKeyException {
		Terminal t = _terminals.get(key);
		if(t == null){
			throw new UnknownTerminalKeyException(key);
		}
		return t;
		
	}
	

	/**
	 * It gets a collection of all terminals registered in the network.
	 * 
	 * @return sorted (by key) list of terminals
	 */
	public Collection<Terminal> showAllTerminals() {
		return _terminals.values();
	}

	/**
	 * Adds a terminal to another terminal's list of friends.
	 * 
	 * @param idTerminal Chain of characters used to identify this terminal
	 * @param friends String containing all the terminal friends' keys
	 * @throws UnknownTerminalKeyException if a terminal that does not exists 
	 * 										is assessed
	 */
	public void registerFriends(String idTerminal, String friends) 
			throws UnknownTerminalKeyException {

		Terminal terminal = showTerminal(idTerminal);
		
		/* gets the friends ids from the string */
		String[] friendsIds = friends.split(",");

		/* adds the friends to terminal */
		for(String id : friendsIds){
			terminal.getFriends().put(id, showTerminal(id));

		}
	}



	/**
	 * It gets a collection of all the unused terminals, that have not
	 * 		completed a communication.
	 * 
	 * @return sorted (by key) collection of unused terminals
	 */
	public Collection<Terminal> showUnusedTerminals() {

		List<Terminal> unusedTerminals = new LinkedList<Terminal>();

		for(Map.Entry<String, Terminal> entry: _terminals.entrySet()){
			Terminal t = entry.getValue();

			/*  terminal is unused there are no past communications */
			if(t.getCommunicationsPerformed().size() == 0 &&
			   t.getCommunicationsReceived().size() == 0)
				unusedTerminals.add(t);
		}
		return unusedTerminals;
	}


	/**
	 * It gets all terminals with a positive balance
	 * 
	 * @return all terminals with a positive balance registered in this network
	 */
	public Collection<Terminal> showPositiveBalanceTerminals(){
		List<Terminal> positiveBalanceTerminals = new LinkedList<Terminal>();

		for(Map.Entry<String, Terminal> entry: _terminals.entrySet()){
			Terminal t = entry.getValue();

			if(t.isBalancePositive()){
				positiveBalanceTerminals.add(t);
			}
		}
		return positiveBalanceTerminals;
	}

	/**
	 * It gets the value for global debt
	 * 
	 * @return sum of all debts of all clients
	 */
	public long showGlobalDebts(){
		
		long _debts = 0;

		for(Map.Entry<String, Client> entry: _clients.entrySet()){
			Client c = entry.getValue();
			_debts += c.getDebts();
		}
		return _debts;
	}

	/**
	 * It gets the value for global payments
	 * 
	 * @return sum of all payments of all clients
	 */
	public long showGlobalPayments(){
		
		long _payments = 0;
		
		for(Map.Entry<String, Client> entry: _clients.entrySet()){
			Client c = entry.getValue();
			_payments += c.getPayments();
		}
		return _payments;
	}


	/**
	 * Registers communication in the network
	 * 
	 * @param communication Communication to be registered
	 * @param sender Client that sends the given communication
	 */
	public void registerCommunication(Communication communication, Client sender){
		
		/* add communication to respective terminals */
		communication.addCommunicationToReceiver();
		communication.addCommunicationToSender();

		/* add to the global list of communications */
		_communications.put(_nCommunications, communication);
		sender.getCommunicationsPerformed().add(communication);
	}

	/**
	 * It gets the collection of all communications in the network
	 * 
	 * @return all communications in the network
	 */
	public Collection<Communication> showAllCommunications(){
			return _communications.values();
		}

	/**
	 * It gets a collection of communications sent by a client
	 * 
	 * @param clientId Key of the client that is owner of the terminal
	 * @return all communications from a client 
	 * @throws UnknownClientKeyException if a client that does not exists is assessed
	 */
	public Collection<Communication> showCommunicationsFromClient(String clientId)
			throws UnknownClientKeyException{

		Client client = showClient(clientId);
		Collection<Terminal> _terminals = client.getTerminals();

		List<Communication> communications = new LinkedList<Communication>();

		for(Terminal t: _terminals){
			for(Communication c: t.getCommunicationsPerformed()){
				communications.add(c);
			}
		}
		return communications;
	}

	/**
	 * It gets a collection of communications received by a client
	 * 
	 * @param clientId Key of the client that is owner of the terminal
	 * @return all communications received by a client
	 * @throws UnknownClientKeyException if a client that does not exists is assessed
	 */
	public Collection<Communication> showCommunicationsToClient(String clientId)
			throws UnknownClientKeyException{
		Client client = showClient(clientId);
		Collection<Terminal> _terminals = client.getTerminals();

		List<Communication> communications = new LinkedList<Communication>();
		for(Terminal t: _terminals){
			for(Communication c: t.getCommunicationsReceived()){
				communications.add(c);
			}
		}
		return communications;
	}

	/**
	 * It finds/gets a communication registered in this network
	 * 
	 * @param commId Key of a communication registered in this network
	 * @return communication with 'commId' as the key
	 * @throws InvalidCommunicationException if a communication does not exist
	 * 								in this network
	 */
	public Communication fetchCommunication(int commId)
		throws InvalidCommunicationException{
		
		/* communication does not exist */
		if(!_communications.containsKey(commId)){
			throw new InvalidCommunicationException();
		}
		return _communications.get(commId);
	}

	
	/**
	 * It enables the given client to be alerted by notifications
	 * 
	 * @param clientId Key of a client registered in this network
	 * @throws UnknownClientKeyException if a client that does not exists is assessed
	 * @throws NotificationsAlreadyEnabledException if the client has already its 
	 * 										notifications enabled
	 */
	public void enableClientNotifications(String clientId)
		throws UnknownClientKeyException, NotificationsAlreadyEnabledException{
		Client client = showClient(clientId);

		if(client.isNotifiable()){
			throw new NotificationsAlreadyEnabledException();
		}
		client.setNotifiable(true);

	}

	/**
	 * It disables the given client to be alerted by notifications
	 *
	 * @param clientId Key of a client registered in this network
	 * @throws UnknownClientKeyException if a client that does not exists is assessed
	 * @throws NotificationsAlreadyDisabledException if the client has already its 
	 * 										notifications disabled
	 */
	public void disableClientNotifications(String clientId)
		throws UnknownClientKeyException, NotificationsAlreadyDisabledException{
		Client client = showClient(clientId);

		if(!client.isNotifiable()){
			throw new NotificationsAlreadyDisabledException();
		}
		client.setNotifiable(false);
	}
	
}


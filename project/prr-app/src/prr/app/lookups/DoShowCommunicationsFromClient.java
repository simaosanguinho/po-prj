package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed
import prr.app.StringVisitor;


/**
 * Show communications from a client.
 */
class DoShowCommunicationsFromClient extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();

	DoShowCommunicationsFromClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_FROM_CLIENT, receiver);
		addStringField("key", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		
		try{
			_receiver.showCommunicationsFromClient(stringField("key"))
			.stream()
			.map(v -> v.accept(stringvisitor))
			.forEach(_display::popup);
			
		} catch(prr.exceptions.UnknownClientKeyException e){
			throw new prr.app.exceptions.UnknownClientKeyException(e.getKey());
		}

	}
}

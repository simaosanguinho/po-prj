package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.exceptions.UnrecognizedEntryException;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);
		addStringField("key", Prompt.terminalKey());
		addOptionField("type", Prompt.terminalType(), "BASIC", "FANCY");
    	addStringField("client", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.registerTerminal(stringField("type"), 
				stringField("key"), stringField("client"), "IDLE");

		}catch(prr.exceptions.InvalidTerminalKeyException e){
			throw new InvalidTerminalKeyException(e.getKey());

		}catch(prr.exceptions.DuplicateTerminalKeyException e){
			throw new DuplicateTerminalKeyException(e.getKey());

		}catch(prr.exceptions.UnknownClientKeyException e){
			throw new UnknownClientKeyException(e.getKey());

		}catch(prr.exceptions.UnrecognizedEntryException e){
			e.printStackTrace();
		}
	
	}
}

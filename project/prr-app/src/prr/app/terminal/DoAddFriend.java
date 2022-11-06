package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DuplicateTerminalKeyException;




/**
 * Add a friend. - FIXXXXX
 */
class DoAddFriend extends TerminalCommand {


	DoAddFriend(Network context, Terminal terminal) {
		super(Label.ADD_FRIEND, context, terminal);
		addStringField("key", Prompt.terminalKey());
	}	

	@Override
	protected final void execute() throws CommandException {
       /* */
	   try{
			_receiver.addFriend(stringField("key"), _network);
		
		} catch(prr.exceptions.UnknownTerminalKeyException e){
			throw new prr.app.exceptions.UnknownTerminalKeyException(e.getKey());
		}
	}

}
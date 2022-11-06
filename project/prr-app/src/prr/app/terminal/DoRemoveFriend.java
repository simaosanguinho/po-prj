package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DuplicateTerminalKeyException;

//FIXME add more imports if needed

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {


	DoRemoveFriend(Network context, Terminal terminal) {
		super(Label.REMOVE_FRIEND, context, terminal);
		addStringField("key", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
	       /* */
		   try{
			_receiver.removeFriend(stringField("key"));
		
		} catch(prr.exceptions.UnknownTerminalKeyException e){
			//throw new UnknownTerminalKeyException(e.getKey());
			/* do nothing */

		} 
	}
}

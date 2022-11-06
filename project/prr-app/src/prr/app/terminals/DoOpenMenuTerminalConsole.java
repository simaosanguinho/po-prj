package prr.app.terminals;

import prr.Network;
import prr.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.terminals.Terminal;

/**
 * Open a specific terminal's menu.
 */
class DoOpenMenuTerminalConsole extends Command<Network> {

	DoOpenMenuTerminalConsole(Network receiver) {
		super(Label.OPEN_MENU_TERMINAL, receiver);
		addStringField("key", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
			try{
				(new prr.app.terminal.Menu(_receiver, 
					_receiver.showTerminal(stringField("key")))).open();
			
			}catch (prr.exceptions.UnknownTerminalKeyException e){
				throw new prr.app.exceptions.UnknownTerminalKeyException(e.getKey());
			}
			

	}
}

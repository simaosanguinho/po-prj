package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.UnrecognizedEntryException;

/**
 * Silence the terminal.
 */
class DoSilenceTerminal extends TerminalCommand {

	DoSilenceTerminal(Network context, Terminal terminal) {
		super(Label.MUTE_TERMINAL, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
        try{
			_receiver.silenceTerminal();
		}catch(TerminalAlreadySilentException e){
			_display.popup(Message.alreadySilent());
		}catch(UnrecognizedEntryException e){
			/* do nothing */
		}
	}
}

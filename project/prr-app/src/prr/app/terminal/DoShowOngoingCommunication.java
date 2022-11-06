package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.StringVisitor;
import prr.exceptions.NoOnGoingCommunicationException;

/**
 * Command for showing the ongoing communication.
 */
class DoShowOngoingCommunication extends TerminalCommand {

	private final StringVisitor stringvisitor = new StringVisitor();


	DoShowOngoingCommunication(Network context, Terminal terminal) {
		super(Label.SHOW_ONGOING_COMMUNICATION, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {

		try{
			_display.popup(_receiver.showOngoingCommunication(_network)
					.accept(stringvisitor));
		}catch(NoOnGoingCommunicationException e){
			_display.popup(Message.noOngoingCommunication());
		}
               
	}
}

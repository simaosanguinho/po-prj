package prr.app.terminal;

import prr.Network;
import prr.exceptions.NoOnGoingCommunicationException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadyOnException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.UnrecognizedEntryException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
import static java.lang.Math.round;
//FIXME add more imports if needed

/**
 * Command for ending communication.
 */
class DoEndInteractiveCommunication extends TerminalCommand {

	DoEndInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.END_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canEndCurrentCommunication());
		addIntegerField("duration", Prompt.duration());
	}

	@Override
	protected final void execute() throws CommandException {
        
		try{
			double _price = _receiver.endInteractiveCommunication(integerField("duration"),
										_network);

			_display.popup(Message.communicationCost(round(_price)));
		}catch(prr.exceptions.UnknownClientKeyException e){
			e.printStackTrace();
		}catch(UnrecognizedEntryException e){
			e.printStackTrace();
		}catch(NoOnGoingCommunicationException | TerminalAlreadySilentException |
				TerminalAlreadyOnException e){
			e.printStackTrace();
		}
	}
}

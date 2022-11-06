package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnavailableTerminalException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.BasicTerminalDestinationException;
import prr.exceptions.BasicTerminalOriginException;
import prr.exceptions.UnknownClientKeyException;


/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
		 
		addStringField("key", Prompt.terminalKey());
		addOptionField("commType", Prompt.commType(), "VOICE", "VIDEO");
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_receiver.startInteractiveCommunication(stringField("key"), "ONGOING", 
					optionField("commType"), _network);
		}catch(UnavailableTerminalException e){
			
			/* destination terminal is off */
			if(e.getStatus() == "OFF"){
				_display.popup(Message.destinationIsOff(stringField("key")));
			}

			/* destination terminal is busy */
			else if(e.getStatus() == "BUSY" || 
					_receiver.getKey().equals(stringField("key"))){
				_display.popup(Message.destinationIsBusy(stringField("key")));
			}

			/* destination terminal is silent */
			else{
				_display.popup(Message.destinationIsSilent(stringField("key")));
			}
		}catch(BasicTerminalOriginException e){
			_display.popup(Message.unsupportedAtOrigin(_receiver.getKey(), optionField("commType")));
		
		}catch(BasicTerminalDestinationException e){
			_display.popup(Message.unsupportedAtDestination(stringField("key"), optionField("commType")));

		}catch(UnrecognizedEntryException | UnknownClientKeyException e){
			/* never happens */
			e.printStackTrace();

		}catch(prr.exceptions.UnknownTerminalKeyException e){
			throw new UnknownTerminalKeyException(e.getKey());
		}



	}
}

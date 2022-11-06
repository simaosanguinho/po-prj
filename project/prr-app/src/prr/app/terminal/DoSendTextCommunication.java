package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DestinationTerminalOffException;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.UnrecognizedEntryException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

        DoSendTextCommunication(Network context, Terminal terminal) {
                super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
                addStringField("key", Prompt.terminalKey());
                addStringField("message", Prompt.textMessage());
        }

        @Override
        protected final void execute() throws CommandException {
                
                try{   
                         _receiver.sendTextCommunication(stringField("key"),
                                                 stringField("message"), "ONGOING", _network);
                

                        // CHANGE
                
                }catch(prr.exceptions.UnknownTerminalKeyException e){
                       throw new prr.app.exceptions.UnknownTerminalKeyException(e.getKey()); 

                }catch(DestinationTerminalOffException e){
                        _display.popup(Message.destinationIsOff(stringField("key")));
                }catch(UnknownClientKeyException e){
                        e.printStackTrace();
                }catch(UnrecognizedEntryException e){
                        e.printStackTrace();
                }
        }
}
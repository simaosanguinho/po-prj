package prr.app.clients;

import prr.Network;
import prr.exceptions.NotificationsAlreadyEnabledException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.UnknownClientKeyException;


/**
 * Enable client notifications.
 */
class DoEnableClientNotifications extends Command<Network> {

	DoEnableClientNotifications(Network receiver) {
		super(Label.ENABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("key", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
            _receiver.enableClientNotifications(stringField("key"));
		}catch(NotificationsAlreadyEnabledException e){
			_display.popup(Message.clientNotificationsAlreadyEnabled());
		}catch(UnknownClientKeyException e){
			/* do nothing */
		}
	}
}

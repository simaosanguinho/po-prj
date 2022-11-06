package prr.app.clients;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.NotificationsAlreadyDisabledException;
import prr.exceptions.UnknownClientKeyException;

/**
 * Disable client notifications.
 */
class DoDisableClientNotifications extends Command<Network> {

	DoDisableClientNotifications(Network receiver) {
		super(Label.DISABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("key", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
            _receiver.disableClientNotifications(stringField("key"));
		}catch(NotificationsAlreadyDisabledException e){
			_display.popup(Message.clientNotificationsAlreadyDisabled());
		}catch(UnknownClientKeyException e){
			/* do nothing */
		}
	}
}

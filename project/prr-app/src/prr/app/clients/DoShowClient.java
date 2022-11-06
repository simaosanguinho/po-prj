package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.StringVisitor;

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();

	DoShowClient(Network receiver) {
		super(Label.SHOW_CLIENT, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
        try {
			_display.popup(_receiver.showClient(stringField("clientKey"))
				.accept(stringvisitor));

			_receiver.readClientNotificationsApp(stringField("clientKey"))
					.stream()
					.map(v -> v.accept(stringvisitor))
					.forEach(_display::popup);

		} catch(prr.exceptions.UnknownClientKeyException e){
			throw new UnknownClientKeyException(e.getKey());
		}
	}
}

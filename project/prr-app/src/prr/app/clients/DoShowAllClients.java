package prr.app.clients;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.StringVisitor;

/**
 * Show all clients.
 */
class DoShowAllClients extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();

	DoShowAllClients(Network receiver) {
		super(Label.SHOW_ALL_CLIENTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
			/* display clients */
            _receiver.showAllClients()
			.stream()
            .map(c -> c.accept(stringvisitor))
            .forEach(_display::popup);
  }

	
}

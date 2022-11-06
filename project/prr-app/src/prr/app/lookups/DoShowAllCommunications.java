package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.StringVisitor;

/**
 * Command for showing all communications.
 */
class DoShowAllCommunications extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();

	DoShowAllCommunications(Network receiver) {
		super(Label.SHOW_ALL_COMMUNICATIONS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
        _receiver.showAllCommunications()
		.stream()
		.map(v -> v.accept(stringvisitor))
		.forEach(_display::popup);
	}
}

package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.StringVisitor;

/**
 * Show clients with positive balance.
 */
class DoShowClientsWithoutDebts extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();

	DoShowClientsWithoutDebts(Network receiver) {
		super(Label.SHOW_CLIENTS_WITHOUT_DEBTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
        _receiver.showClientsWithoutDebts()
		.stream()
		.map(v -> v.accept(stringvisitor))
		.forEach(_display::popup);
	}
}

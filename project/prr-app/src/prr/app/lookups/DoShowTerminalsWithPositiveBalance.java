package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed
import prr.app.StringVisitor;

/**
 * Show terminals with positive balance.
 */
class DoShowTerminalsWithPositiveBalance extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();

	DoShowTerminalsWithPositiveBalance(Network receiver) {
		super(Label.SHOW_TERMINALS_WITH_POSITIVE_BALANCE, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		_receiver.showPositiveBalanceTerminals()
		.stream()
		.map(v -> v.accept(stringvisitor))
		.forEach(_display::popup);
	}
}
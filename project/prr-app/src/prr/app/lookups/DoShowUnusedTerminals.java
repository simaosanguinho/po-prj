package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.StringVisitor;

/**
 * Show unused terminals (without communications).
 */
class DoShowUnusedTerminals extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();	
	
	DoShowUnusedTerminals(Network receiver) {
		super(Label.SHOW_UNUSED_TERMINALS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		_receiver.showUnusedTerminals()
		.stream()
		.map(v -> v.accept(stringvisitor))
		.forEach(_display::popup);
	}
}

package prr.app.terminals;

import java.util.Collections;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import prr.app.StringVisitor;

/**
 * Show all terminals.
 */
class DoShowAllTerminals extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();

	DoShowAllTerminals(Network receiver) {
		super(Label.SHOW_ALL_TERMINALS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		/* display terminals */
		_receiver.showAllTerminals()
		.stream()
		.map(t -> t.accept(stringvisitor))
		.forEach(_display::popup);
	}
}

package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.StringVisitor;
import prr.exceptions.NoClientsWithDebtsException;

/**
 * Show clients with negative balance.
 */
class DoShowClientsWithDebts extends Command<Network> {

	private final StringVisitor stringvisitor = new StringVisitor();

	DoShowClientsWithDebts(Network receiver) {
		super(Label.SHOW_CLIENTS_WITH_DEBTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_receiver.showClientsWithDebts()
			.stream()
			.map(v -> v.accept(stringvisitor))
			.forEach(_display::popup);
		}catch(NoClientsWithDebtsException e){
			/* do nothing */
		}
	}
}

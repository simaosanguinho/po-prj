package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import static java.lang.Math.round;

/**
 * Show balance.
 */
class DoShowTerminalBalance extends TerminalCommand {

	DoShowTerminalBalance(Network context, Terminal terminal) {
		super(Label.SHOW_BALANCE, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
		_display.popup(Message.terminalPaymentsAndDebts(_receiver.getKey(),
			round(_receiver.getBalancePaid()), round(_receiver.getBalanceDebts())));
	}
}

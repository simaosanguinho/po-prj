package prr.app;

import prr.NetworkManager;
import prr.exceptions.ImportFileException;
import pt.tecnico.uilib.Dialog;

/**
 * Application entry-point.
 */
public class App {

	public static void main(String[] args) {
		try (var ui = Dialog.UI) {
			NetworkManager receiver = new NetworkManager();

			String datafile = System.getProperty("import");
			//String datafile = "/Users/simaosanguinho/Documents/PO-Project/project/test01.txt";
			//String datafile = "/Users/simaosanguinho/Documents/PO-Project/project/tests/A-20-01-M-ok.import";
			if (datafile != null) {
				try {
					receiver.importFile(datafile);
				} catch (ImportFileException e) {
					// no behavior described: just present the problem
					e.printStackTrace();
				}
			
			}

			(new prr.app.main.Menu(receiver)).open();
		}
	}

}
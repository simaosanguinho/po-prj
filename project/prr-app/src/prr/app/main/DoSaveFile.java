package prr.app.main;

import prr.NetworkManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import prr.exceptions.MissingFileAssociationException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to save a file.
 */
class DoSaveFile extends Command<NetworkManager> {

	DoSaveFile(NetworkManager receiver) {
		super(Label.SAVE_FILE, receiver);
	}

	@Override
	public final void execute() throws CommandException {
		try{
			try {
				
			_receiver.save();

			 } catch (MissingFileAssociationException e) {
				_receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}catch (FileNotFoundException e){
			_display.popup(Message.fileNotFound());
		}catch (MissingFileAssociationException e) {
			/* do nothing */
		}catch (IOException e) {
			
			e.printStackTrace();
		
		}
	}
  
	private void saveAs() throws IOException {
	  try {
		_receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
	  } catch (MissingFileAssociationException e) {
		e.printStackTrace();

	  } catch (FileNotFoundException e){
		_display.popup(Message.fileNotFound());
	  }

	}

  
 }


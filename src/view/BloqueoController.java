package view;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
public class BloqueoController {
	private MainApp mainApp;
	@FXML
    private TextField desbloqueoTxt;
	public void handleContinuar(){
		if(desbloqueoTxt.getText().trim().equals("I love Alex")){
			try{
				ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(MainApp.UNLOCKFILE));
				ous.writeInt(3);
				ous.close();
			}catch(IOException ioe){}
			mainApp.initGeneralView(3);
		}
		else desbloqueoTxt.setText("Wrong unlocking word.");
	}
	public void setMainApp(MainApp m){
		mainApp=m;
	}
}

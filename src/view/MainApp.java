package view;
	import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
	public class MainApp extends Application {
		public static final File UNLOCKFILE = new File("resources/desbloqueo.dat");
		private Stage primaryStage;
		private AnchorPane generalView,desbloqueo;
		/**
		 * Constructor
		 */
		public MainApp() {
			// Add some sample dataS
		}

		@Override
		public void start(Stage primaryStage) {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Cryptomessage");
			
			try{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(UNLOCKFILE));
				 int n = ois.readInt();
				if(n>0)initGeneralView(n);
				else initDesbloqueo();
				ois.close();
			}catch(IOException fnfe){
				initDesbloqueo();
			}
		}

		/**
		 * Initializes the root layout and tries to load the last opened
		 * recipe file.
		 */
		public void initGeneralView(int n) {
			primaryStage.setMaxHeight(440);
			primaryStage.setMaxWidth(425);
			primaryStage.setMinHeight(440);
			primaryStage.setMinWidth(425);
			try {
				// Load root layout from fxml file.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("GeneralView.fxml"));
				generalView = (AnchorPane) loader.load();
				// Show the scene containing the root layout.
				Scene scene = new Scene(generalView);
				primaryStage.setScene(scene);
				
				// Give the controller access to the main app.
				GeneralViewController controller = loader.getController();
				controller.setMainApp(this);
				controller.setContadorFallos(n);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		public void initDesbloqueo() {
			primaryStage.setMaxHeight(85);
			primaryStage.setMaxWidth(375);
			primaryStage.setMinHeight(85);
			primaryStage.setMinWidth(375);
			try {
				// Load root layout from fxml file.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("Desbloqueo.fxml"));
				desbloqueo = (AnchorPane) loader.load();
				// Show the scene containing the root layout.
				Scene scene = new Scene(desbloqueo);
				primaryStage.setScene(scene);
				
				// Give the controller access to the main app.
				BloqueoController controller = loader.getController();
				controller.setMainApp(this);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		public static void main(String[] args) {
			launch(args);
		}
}

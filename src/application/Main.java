package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
	
			scrollPane.setFitToHeight(true); // AJUSTANDO A ALTURA DO SCROLLPANE (STAGE)
			scrollPane.setFitToWidth(true); // AJUSTANDO A LARGURA
			
			mainScene = new Scene(scrollPane); // CENA
			primaryStage.setScene(mainScene); // PALCO
			primaryStage.setTitle("Sample JavaFX application"); //PALCO
			primaryStage.show(); // PALCO
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
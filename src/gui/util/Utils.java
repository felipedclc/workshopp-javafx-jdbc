package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event){ // PALCO ATUAL (ação para acionar o botão)
		return (Stage) ((Node)event.getSource()).getScene().getWindow();
	}
}

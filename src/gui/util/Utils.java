package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event){ // PALCO ATUAL (ação para acionar o botão)
		return (Stage) ((Node)event.getSource()).getScene().getWindow();
	}
	
	public static Integer tryParseToInt(String str) { // METODO PARA TENTAR CONVERTER UM STRING PARA INTEIRO
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e) { // CASO NÃO SEJA UM NÚMERO, RETORNARÁ NULO 
			return null;
		}
	}
}

package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout; 
	
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListControler controller) -> {
			controller.setSellerService(new SellerService()); // Iniciando o controller(SellerListController)
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListControler controller) -> {
			controller.setDepartmentService(new DepartmentService()); // Iniciando o controller(DepartmentListController)
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x ->{});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}

	private synchronized  <T> void loadView(String absoluteName, Consumer<T> initializingAction) { // FUNÇÃO PARA ABRIR OUTRA VIEW
		try { // CARREGANDO A FUNÇÃO VIEW
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); 
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent(); // ACESSANDO O TODO O CONTEUDO DA TELA(SCROLLPANE)
			
			Node mainMenu = mainVBox.getChildren().get(0); // ACESSANDO O PRIMEIRO "FILHO" DO VBOX(XML)
			mainVBox.getChildren().clear(); // LIMPANDO OS FILHOS DO MAINVBOX
			mainVBox.getChildren().add(mainMenu); // ADD O MAIN MENU
			mainVBox.getChildren().addAll(newVBox.getChildren()); // ADICIONANDO TODOS OS FILHOS DO NEW VBOX 
			
			T controller = loader.getController();  // GET CONTROLLER RETORNA O CONTROLLER DE QUALQUER TIPO
			initializingAction.accept(controller); // INICIA O COMANDO ACIMA
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		} 
	}
}

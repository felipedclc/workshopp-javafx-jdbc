package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout; 
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}

	private synchronized  void loadView(String absoluteName) { // FUNÇÃO PARA ABRIR OUTRA VIEW
		try { // CARREGANDO A FUNÇÃO VIEW
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); 
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent(); // ACESSANDO O TODO O CONTEUDO DA TELA(SCROLLPANE)
			
			Node mainMenu = mainVBox.getChildren().get(0); // ACESSANDO O PRIMEIRO "FILHO" DO VBOX(XML)
			mainVBox.getChildren().clear(); // LIMPANDO OS FILHOS DO MAINVBOX
			mainVBox.getChildren().add(mainMenu); // ADD O MAIN MENU
			mainVBox.getChildren().addAll(newVBox.getChildren()); // ADICIONANDO TODOS OS FILHOS DO NEW VBOX 
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		} 
	}
	
	private synchronized  void loadView2(String absoluteName) { // FUNÇÃO PARA ABRIR OUTRA VIEW
		try { // CARREGANDO A FUNÇÃO VIEW
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); 
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent(); // ACESSANDO O TODO O CONTEUDO DA TELA(SCROLLPANE)
			
			Node mainMenu = mainVBox.getChildren().get(0); // ACESSANDO O PRIMEIRO "FILHO" DO VBOX(XML)
			mainVBox.getChildren().clear(); // LIMPANDO OS FILHOS DO MAINVBOX
			mainVBox.getChildren().add(mainMenu); // ADD O MAIN MENU
			mainVBox.getChildren().addAll(newVBox.getChildren()); // ADICIONANDO TODOS OS FILHOS DO NEW VBOX 
			
			DepartmentListControler controller = loader.getController();
			controller.setDepartmentService(new DepartmentService()); // INJEÇÃO DE DEPENDÊNCIA 
			controller.updateTableView();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		} 
	}
}

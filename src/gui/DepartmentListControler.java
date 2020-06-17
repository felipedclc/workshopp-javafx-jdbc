package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listener.DataChangeListner;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListControler implements Initializable, DataChangeListner {
	
	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumId;
	
	@FXML
	private TableColumn<Department, String> tableColumName;
	
	@FXML
	private Button btNew;
		
	private ObservableList<Department> obsList; // LISTA CRIADA PARA CARREGAR OS DEPARTMENTOS

	@FXML
	private void onBtNewAction(ActionEvent event) {  // REFERENCIA PARA O CONTROLE QUE RECEBEU O EVENTO 
		Stage parentStage = gui.util.Utils.currentStage(event); // ACESSANDO O STAGE (PALCO)
		Department obj = new Department(); // FORMUL�RIO DEVE COME�AR VAZIO AP�S APERTAR O BOT�O "NEW"
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) { // METTODO SOLID, N�O FOI INSTANCIADO O DEPARTMENT SERVICE
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumId.setCellValueFactory(new PropertyValueFactory<>("Id")); // METODO PADRAO PARA INICIAR O COMPORTAMENTO DAS COLUNAS
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // METODO PARA O TABLE VIEW SE ADAPTAR AO TAMANHO DA JANELA
	}

	public void updateTableView() { // METODO RESPONS�VEL POR ATUALIZAR OS DEPARTAMENTOS E INSERIR NA LISTA OBSLIST
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); // RECUPERA OS DADOS DO CADASTRO 
		obsList = FXCollections.observableArrayList(list); // JOGA TUDO NO OBSLIST
		tableViewDepartment.setItems(obsList); // CARREGA OS ITENS NA TELA
		
	}
	
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); //CARREGANDO A JANELA (VIEW "absoluteName")
			Pane pane = loader.load(); // CARREGANDO A VIEW
			
			DepartmentFormController controller = loader.getController(); // PEGANDO O CONTROLADOR DA TELA CARREGADA ACIMA
			controller.setDepartment(obj); // INJETANDO O DEPARTAMENTO NO CONTROLLER
			controller.setDepartmentService(new DepartmentService()); // INJETANDO O DEPARTMENT SERVICE NO CONTROLLER
			controller.subscribeDataChangeListener(this); // INSCREVENDO O METODO PARA ATUALIZAR A JANELA COM O METODO (ON DATA CHANGED)
			controller.updateFormData(); // CARREGANDO OS DADOS NO FORMULARIO
			
			Stage dialogStage = new Stage(); // CRIANDO UMA JANELA PARA ABRIR EM CIMA DA OUTRA
			dialogStage.setTitle("Enter Department data"); 
			dialogStage.setScene(new Scene(pane)); // CARREGANDO O NOVO PAINEL NA NOVA CENA
			dialogStage.setResizable(false); // A JANELA N�O PODE SER REDIMENSIONADA(FALSE)
			dialogStage.initOwner(parentStage); // O PARENT STAGE � O "PAI" DA JANELA 
			dialogStage.initModality(Modality.WINDOW_MODAL); // JANELA FICA TRAVADA E N�O DEIXA ACESSAR A ANTERIOR 
			dialogStage.showAndWait(); // COMANDO PARA CARREGAR 
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() { // METODO QUE ATUALIZA A LISTA APOS A EXECU��O NA JANELA "FORM CONTROLLER"
		updateTableView(); 
		
	}
}

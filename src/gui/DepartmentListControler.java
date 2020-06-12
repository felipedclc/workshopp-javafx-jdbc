package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListControler implements Initializable {
	
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
	private void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	public void setDepartmentService(DepartmentService service) { // METTODO SOLID, NÃO FOI INSTANCIADO O DEPARTMENT SERVICE
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

	public void updateTableView() { // METODO RESPONSÁVEL POR ATUALIZAR OS DEPARTAMENTOS E INSERIR NA LISTA OBSLIST
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); // RECUPERA OS DADOS DO CADASTRO 
		obsList = FXCollections.observableArrayList(list); // JOGA TUDO NO OBSLIST
		tableViewDepartment.setItems(obsList); // CARREGA OS ITENS NA TELA
		
	}
}

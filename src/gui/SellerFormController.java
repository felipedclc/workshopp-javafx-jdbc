package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listener.DataChangeListner;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity; // CRIADO PARA PREENCHER A LISTA DE DEPARTAMENTOS NA JANELA

	private SellerService service;

	private DepartmentService departmentService;

	private List<DataChangeListner> dataChangeListner = new ArrayList<>(); // LISTA DE OBJETOS QUE RECEBEM O EVENTO

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Department> obsList;

	public void setSeller(Seller entity) { // INJEÇÃO DE DEPENDÊNCIA
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListner listener) { // CLASSE VAI SOBRESCREVER A LISTA (ATUALIZAR)
		dataChangeListner.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) { // PROTEÇÃO CASO O PROGRADADOR ESQUEÇA DE INJETAR A DEPENDENCIA DO DEPARTMENT NO
								// CONTROLLER
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) { // PROTEÇÃO CASO O PROGRADADOR ESQUEÇA DE INJETAR A DEPENDENCIA DO DEPARTMENT
								// SERVICE NO CONTROLLER
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData(); // RODA O PROGRAMA E APRESENTA OS ERROS SE NECESSARIO
			service.saveOrUpdate(entity);
			notifyDataChangeListeners(); // METODO QUE AVISA O CHANGE LISTENER QUE DEVE SER ATIVADO
			Utils.currentStage(event).close(); // FECHA A JANELA APÓS APERTAR O BOTÃO SAVE
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) { // EXCEÇÃO PARA O BANCO DE DADOS
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() { // FUNÇÃO QUE EMITE O EVENTO PARA OS LISTENERS
		for (DataChangeListner listener : dataChangeListner) {
			listener.onDataChanged();
		}
	}

	private Seller getFormData() { // AÇÃO PARA PEGAR O NOME E ID DO FORMULARIO
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error"); //

		obj.setId(Utils.tryParseToInt(txtId.getText())); // CONVERTE PARA INTEIRO OU RETORNA NULL

		if (txtName.getText() == null || txtName.getText().trim().equals(" ")) {// TRIM REMOVE ESPAÇO EM BRANCO QUE
																				// ESTEJA NO INICIO OU FIM
			exception.addErro("name", "Field can't be empty");
		}
		obj.setName(txtName.getText()); // EDITA O NOME

		if (exception.getErros().size() > 0) { // SE A QUANTIDADE DE ERROS FOR MAIOR QUE 0
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close(); // COMANDO PARA FECHAR A JANELA
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() { // CRIANDO AS RESTRIÇÕES
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment(); // INICIANDO O COMBO BOX
	}

	public void updateFormData() { // METODO PARA ATUALIZAR OS DADOS DO DEPARTMENT COM A LISTA
		if (entity == null) { // SE NÃO HOUVER A INJEÇÃO DE DEPENDÊNCIA (PROGRAMAÇÃO DEFENSIVA)
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId())); // TRANSFORMANDO EM STRING
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if(entity.getDepartment() == null) { // SE O DEPARTAMENTO ESTIVER VAZIO(FOR NOVO)
			comboBoxDepartment.getSelectionModel().selectFirst(); // SELECIONA O PRIMEIRO ELEMENTO
		}
		else {
		comboBoxDepartment.setValue(entity.getDepartment()); // PREENCHENDO OS DADOS DO VENDEDOR NO COMBO BOX
		}
	}

	public void loadAssociatedObjects() { // CARREGANDO OS OBJETOS ASSOCIADOS
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null "); // O PROGRAMADOR ESQUECEU DE INSTANCIAR
		}
		List<Department> list = departmentService.findAll(); // PROCURANDO TODOS OS DEPARTAMENTOS
		obsList = FXCollections.observableArrayList(list); // CARREGANDO OS DEPARTAENTOS NA OBSLIST
		comboBoxDepartment.setItems(obsList); // SETANDO(EDITANDO) O OBSLIST E ASSOCIANDO AO COMBOBOX DEPARTMENT
	}

	private void setErrorMessages(Map<String, String> errors) { // COLEÇÃO QUE PERCORRE A LISTA/COLEÇÃO(MAP) CARRGA OS
																// ERROS
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) { // SE NO SET(FIELDS) CONTER A CHAVE NAME
			labelErrorName.setText(errors.get("name")); // O TEXTO LABEL APRESENTARÁ A MENSAGEM DE ERRO
		}
	}

	private void initializeComboBoxDepartment() { // CODIGO PARA INICIAR O COMBO BOX
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}

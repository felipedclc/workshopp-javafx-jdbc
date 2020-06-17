package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listener.DataChangeListner;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity; // CRIADO PARA PREENCHER A LISTA DE DEPARTAMENTOS NA JANELA
	
	private DepartmentService service; 
	
	private List<DataChangeListner> dataChangeListner = new ArrayList<>(); // LISTA DE OBJETOS QUE RECEBEM O EVENTO
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) { // INJEÇÃO DE DEPENDÊNCIA
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListner listener) { // CLASSE VAI SOBRESCREVER A LISTA (ATUALIZAR)
		dataChangeListner.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) { // PROTEÇÃO CASO O PROGRADADOR ESQUEÇA DE INJETAR A DEPENDENCIA DO DEPARTMENT NO CONTROLLER 
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) { // PROTEÇÃO CASO O PROGRADADOR ESQUEÇA DE INJETAR A DEPENDENCIA DO DEPARTMENT SERVICE NO CONTROLLER 
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity); 
			notifyDataChangeListeners(); // METODO QUE AVISA O CHANGE LISTENER QUE DEVE SER ATIVADO
			Utils.currentStage(event).close(); // FECHA A JANELA APÓS APERTAR O BOTÃO SAVE
		}
		catch (DbException e) { // EXCEÇÃO PARA O BANCO DE DADOS
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() { // FUNÇÃO QUE EMITE O EVENTO PARA OS LISTENERS
		for(DataChangeListner listener : dataChangeListner) {
			listener.onDataChanged();
		}
	}

	private Department getFormData() {
		Department obj = new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getText())); // CONVERTE PARA INTEIRO OU RETORNA NULL
		obj.setName(txtName.getText());
		
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
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() { // METODO PARA ATUALIZAR OS DADOS DO DEPARTMENT COM A LISTA
		if(entity == null) { // SE NÃO HOUVER A INJEÇÃO DE DEPENDÊNCIA (PROGRAMAÇÃO DEFENSIVA)
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId())); // TRANSFORMANDO EM STRING
		txtName.setText(entity.getName());
	}

}

package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
	
	public void setDepartment(Department entity) { // INJE��O DE DEPEND�NCIA
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) { // PROTE��O CASO O PROGRADADOR ESQUE�A DE INJETAR A DEPENDENCIA DO DEPARTMENT NO CONTROLLER 
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) { // PROTE��O CASO O PROGRADADOR ESQUE�A DE INJETAR A DEPENDENCIA DO DEPARTMENT SERVICE NO CONTROLLER 
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close(); // FECHA A JANELA AP�S APERTAR O BOT�O SAVE
		}
		catch (DbException e) { // EXCE��O PARA O BANCO DE DADOS
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
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
	
	private void initializeNodes() { // CRIANDO AS RESTRI��ES
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() { // METODO PARA ATUALIZAR OS DADOS DO DEPARTMENT COM A LISTA
		if(entity == null) { // SE N�O HOUVER A INJE��O DE DEPEND�NCIA (PROGRAMA��O DEFENSIVA)
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId())); // TRANSFORMANDO EM STRING
		txtName.setText(entity.getName());
	}

}

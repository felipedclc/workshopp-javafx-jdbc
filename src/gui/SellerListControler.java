package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listener.DataChangeListner;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListControler implements Initializable, DataChangeListner {

	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumId;

	@FXML
	private TableColumn<Seller, String> tableColumName;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList; // LISTA CRIADA PARA CARREGAR OS DEPARTMENTOS

	@FXML
	private void onBtNewAction(ActionEvent event) { // REFERENCIA PARA O CONTROLE QUE RECEBEU O EVENTO
		Stage parentStage = gui.util.Utils.currentStage(event); // ACESSANDO O STAGE (PALCO)
		Seller obj = new Seller(); // FORMULÁRIO DEVE COMEÇAR VAZIO APÓS APERTAR O BOTÃO "NEW"
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	public void setSellerService(SellerService service) { // METTODO SOLID, NÃO FOI INSTANCIADO O DEPARTMENT
															// SERVICE
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumId.setCellValueFactory(new PropertyValueFactory<>("Id")); // METODO PADRAO PARA INICIAR O
																			// COMPORTAMENTO DAS COLUNAS
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("Name"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty()); // METODO PARA O TABLE VIEW SE ADAPTAR AO
																			// TAMANHO DA JANELA
	}

	public void updateTableView() { // METODO RESPONSÁVEL POR ATUALIZAR OS DEPARTAMENTOS E INSERIR NA LISTA OBSLIST
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll(); // RECUPERA OS DADOS DO CADASTRO
		obsList = FXCollections.observableArrayList(list); // JOGA TUDO NO OBSLIST
		tableViewSeller.setItems(obsList); // CARREGA OS ITENS NA TELA
		initEditButtons(); // CHAMADA DO NOVO BOTAO "EDIT" NAS LINHAS DA TABELA
		initRemoveButtons(); // CHAMADA DO NOVO BOTAO "REMOVE" NAS LINHAS DA TABELA
	}

	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {

		
	}

	@Override
	public void onDataChanged() { // METODO QUE ATUALIZA A LISTA APOS A EXECUÇÃO NA JANELA "FORM CONTROLLER"
		updateTableView();

	}

	private void initEditButtons() { // METODO ESPECÍFICO QUE FUNCIONA O BOTÃO "EDIT" NA LISTA DE DEPARTAMENTOS
										// DENTRO
										// DA JANELA
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() { // METODO ESPECÍFICO QUE FUNCIONA O BOTÃO "REMOVE" NA LISTA DE DEPARTAMENTOS
										// DENTRO
										// DA JANELA
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?"); // AÇÃO DO
																											// BOTAO
																											// REMOVE

		if (result.get() == ButtonType.OK) { // SE APERTAR NO BOTAO OK (USAR O GET PORQUE O RESULT PODE SER NULO)
			if (service == null) {
				throw new IllegalStateException("Service was null"); // PROGRAMADOR ESQUECEU DE INJETAR(INSTANCIAR) O
																		// SERVICE
			}
			try {
				service.remove(obj); // REMOVE O QUE FOI SELECIONADO
				updateTableView(); // ATUALIZA EM TEMPO REAL
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}

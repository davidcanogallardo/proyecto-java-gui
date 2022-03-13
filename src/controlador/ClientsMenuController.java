package controlador;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Client;
import model.ClientDAO;

public class ClientsMenuController {

	private ClientDAO dao;

	private ResourceBundle texts;
	// Elements gràfics de la UI
	private Stage ventana;

	// Injecció dels panells i controls de la UI definida al fitxer fxml
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnList;
	// @FXML private Button btnProducts;
	@FXML
	private Button btnReturn;

	@FXML
	private void initialize() throws IOException {
		dao = new ClientDAO();
		dao.load();
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		System.out.println("seteo ventana");
		this.ventana = ventana;
	}

	public void sortir() {
		System.out.println("cerrar");
		// TODO guardar weas
	}

	@FXML
	private void onActionSortir(ActionEvent e) throws IOException {
		System.out.println("salgo de clients");
		// TODO sortir();

		ventana.close();
	}

	@FXML
	private void onAction(ActionEvent e) throws Exception {
		if (e.getSource() == btnAdd) {// verifica si el botón es igual al que llamo al evento
			System.out.println("clients view");
			changeScene("/vista/ClientsView.fxml", "Client");
		} else if (e.getSource() == btnList) {
			for (Client client : dao.getMap().values()) {
				System.out.println(client.toString() + "\n");
			}
		} else if (e.getSource() == btnReturn) {
			Platform.exit();
		}
	}

	private void changeScene(String path, String title) throws IOException {
		// Carrega el fitxer amb la interficie d'usuari
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

		// Carregar fitxer de textos multiidioma de la localització actual
		Locale localitzacioDisplay = Locale.getDefault(Category.DISPLAY);
		texts = ResourceBundle.getBundle("vista.Texts", localitzacioDisplay);
		// fins aquí tot igual, només falta assignar el fitxer de recursos al formulari
		loader.setResources(texts);

		// Crea una nova finestra i l'obre
		Stage stage = new Stage();
		Scene fm_scene = new Scene(loader.load());
		stage.setTitle(title);
		stage.setScene(fm_scene);
		stage.show();

		/************** Modificar ************/
		// Crear un objecte de la clase PersonasController ja que necessitarem accedir
		// al mètodes d'aquesta classe
		if (title.equals("Client")) {
			ClientsController clientsAdd = loader.getController();
			clientsAdd.setVentana(stage);

			// Programem l'event que s'executará quan es tanqui la finestra
			stage.setOnCloseRequest((WindowEvent we) -> {
				try {
					clientsAdd.sortir();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

}

package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Locale.Category;

import org.controlsfx.validation.Validator;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.GenericFormatter;

public class IniciBotonsController extends Application {

	private ResourceBundle texts;

	// Injecció dels panells i controls de la UI definida al fitxer fxml
	@FXML
	private AnchorPane root;
	@FXML
	private Button btnPersones;
	@FXML
	private Button btnProducts;
	@FXML
	private Button btnClients;
	@FXML
	private Button btnClockInOut;
	@FXML
	private Button btnSortir;
	@FXML
	private TextArea ewe;


	private ValidationSupport vs;
	private ArrayList<TextField> owoContainer = new ArrayList<>();

    @FXML
    private void initialize() throws IOException {
        vs = new ValidationSupport();
		//         vs.registerValidator(ewe, Validator.createRegexValidator("Dni incorrecto", "\\^(([0-9]{9})([,][0-9]{9})*)$", Severity.ERROR));

        vs.registerValidator(ewe, Validator.createRegexValidator("Dni incorrecto", "^(([0-9]{9})([,][0-9]{9})*)$", Severity.ERROR));
    }

	@Override
	public void start(Stage primaryStage) throws IOException {
		// Carrega el fitxer amb la interficie d'usuari inicial (Scene)
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/IniciBotonsView.fxml"));

		// Carregar fitxer de textos multiidioma de la localització actual
		Locale localitzacioDisplay = Locale.getDefault(Category.DISPLAY);
		texts = ResourceBundle.getBundle("vista.Texts", localitzacioDisplay);
		// fins aquí tot igual, només falta assignar el fitxer de recursos al formulari
		loader.setResources(texts);

		Scene fm_inici = new Scene(loader.load());

		// Li assigna la escena a la finestra inicial (primaryStage) i la mostra
		primaryStage.setScene(fm_inici);
		primaryStage.setTitle(texts.getString("title.agenda"));
		primaryStage.show();

	}

	private Integer y = 20;
	private Integer id = 1;

	@FXML
	private void onAction(ActionEvent e) throws Exception {
		if (e.getSource() == btnPersones) {// verifica si el botón es igual al que llamo al evento
			System.out.println(vs.isInvalid());
			// changeScene("/vista/PersonesView.fxml", "Persones");
			// TextField owo = new TextField("owo");
			// owo.setLayoutX(174);
			// owo.setLayoutY(y);
			// y+=30;
			// owo.setId(id.toString());
			// id++;
			// root.getChildren().add(owo);
			
			// owoContainer.add(owo);
			// System.out.println();
		} else if (e.getSource() == btnProducts) {
			// for (TextField textField : owoContainer) {
				// root.getChildren().remove(textField);
				// if (textField.getId().equals("1")) {
				// 	System.out.println("1z");
				// } else {
				// 	vs.registerValidator(textField, true, Validator.createEmptyValidator("ID obligatori"));
				// }
			// }
			// System.out.println("voy a prods1");
			// changeScene("/vista/ProductsMenuView.fxml", "Productos");
		} else if (e.getSource() == btnClients) {
			System.out.println("voy a clie1");
			changeScene("/vista/ClientsMenuView.fxml", "Clientes");
		} else if (e.getSource() == btnClockInOut) {
			changeScene("/vista/PresenceMenuView.fxml", "Fichar");
		} else if (e.getSource() == btnSortir) {
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
		if (title.equals("Productos")) {
			ProductsMenuController productsMenu = loader.getController();
			System.out.println("voy a prods");
			productsMenu.setVentana(stage);

			// Programem l'event que s'executará quan es tanqui la finestra
			stage.setOnCloseRequest((WindowEvent we) -> {
				productsMenu.sortir();
			});
		} else if (title.equals("Persones")) {
			PersonesController personasControler = loader.getController();
			personasControler.setVentana(stage);

			// Programem l'event que s'executará quan es tanqui la finestra
			stage.setOnCloseRequest((WindowEvent we) -> {
				personasControler.sortir();
			});
		} else if (title.equals("Clientes")) {
			ClientsMenuController clientMenu = loader.getController();
			System.out.println("voy a cliente");
			clientMenu.setVentana(stage);

			// Programem l'event que s'executará quan es tanqui la finestra
			stage.setOnCloseRequest((WindowEvent we) -> {
				clientMenu.sortir();
			});
		} else if (title.equals("Fichar")) {
			PresenceMenuController presenceMenu = loader.getController();
			System.out.println("voy a cliente");
			presenceMenu.setVentana(stage);

			// Programem l'event que s'executará quan es tanqui la finestra
			stage.setOnCloseRequest((WindowEvent we) -> {
				presenceMenu.sortir();
			});
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

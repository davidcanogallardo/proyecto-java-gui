package controlador;

import java.io.IOException;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.GenericFormatter;

public class IniciBotonsController extends Application {

	private ResourceBundle texts;

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
	private void initialize() throws IOException {
		GenericFormatter.setLocale();
		texts = GenericFormatter.getText();
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/IniciBotonsView.fxml"));

		GenericFormatter.setLocale();
		texts = GenericFormatter.getText();
		loader.setResources(texts);

		Scene fm_inici = new Scene(loader.load());

		primaryStage.setScene(fm_inici);
		primaryStage.setTitle(texts.getString("home.title"));
		primaryStage.show();
	}

	@FXML
	private void onAction(ActionEvent e) throws Exception {
		if (e.getSource() == btnPersones) {
			changeScene("/vista/List.fxml", "ewe");
		} else if (e.getSource() == btnProducts) {
			changeScene("/vista/ProductsMenuView.fxml", texts.getString("prodmenu.title"));
		} else if (e.getSource() == btnClients) {
			changeScene("/vista/ClientsMenuView.fxml", texts.getString("clientmenu.title"));
		} else if (e.getSource() == btnClockInOut) {
			changeScene("/vista/PresenceMenuView.fxml", texts.getString("clockinout.title"));
		} else if (e.getSource() == btnSortir) {
			Platform.exit();
		}
	}

	private void changeScene(String path, String title) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

		texts = GenericFormatter.getText();
		loader.setResources(texts);

		Stage stage = new Stage();
		Scene fm_scene = new Scene(loader.load());
		stage.setTitle(title);
		stage.setScene(fm_scene);
		stage.show();

		if (title.equals(texts.getString("prodmenu.title"))) {
			ProductsMenuController productsMenu = loader.getController();
			System.out.println("voy a prods");
			productsMenu.setVentana(stage);
		} else if (title.equals(texts.getString("clientmenu.title"))) {
			ClientsMenuController clientMenu = loader.getController();
			System.out.println("voy a cliente");
			clientMenu.setVentana(stage);
		} else if (title.equals(texts.getString("clockinout.title"))) {
			PresenceMenuController presenceMenu = loader.getController();
			System.out.println("voy a cliente");
			presenceMenu.setVentana(stage);
		} else if (title.equals("ewe")) {
			List list = loader.getController();
			list.setVentana(stage);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

package controlador;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ClientDAO;
import model.Product;
import model.ProductDAO;
import utils.GenericFormatter;

public class ProductsMenuController {
	private ResourceBundle texts;
	private Stage ventana;
	private ProductDAO dao = new ProductDAO();  

	@FXML
	private Button btnAdd;
	@FXML
	private Button btnList;
	@FXML
	private Button btnListD;
	@FXML
	private Button btnReturn;

	@FXML
	private void initialize() throws IOException {
		texts = GenericFormatter.getText();
		dao.load();
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	public void onCloseWindow() {
		// TODO borrar si no lo uso
	}

	@FXML
	private void onActionExit(ActionEvent e) throws IOException {
		ventana.close();
	}

	@FXML
	private void onAction(ActionEvent e) throws Exception {
		if (e.getSource() == btnAdd) {
			System.out.println("products view");
			changeScene("/vista/ProductsView.fxml", texts.getString("prodform.title"));
		} else if (e.getSource() == btnList) {
			// System.out.println("listar");
			for (Product product : dao.getMap().values()) {
				System.out.println(product.toString()+"\t");
			}

		} else if (e.getSource() == btnListD) {
			System.out.println("listar descatalogados");
		} else if (e.getSource() == btnReturn) {
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

		if (title.equals("Product")) {
			ProductsController productsAdd = loader.getController();
			productsAdd.setVentana(stage);

			stage.setOnCloseRequest((WindowEvent we) -> {
				try {
					productsAdd.onCloseWindow();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

}

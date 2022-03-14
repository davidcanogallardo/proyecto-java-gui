package controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.Locale.Category;

import model.Pack;
import model.Product;
import model.ProductDAO;
import utils.Alert2;
import utils.GenericFormatter;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DiscontinuedProdController {

    private ProductDAO dao;

    @FXML
    private DatePicker date;

    private Stage ventana;

	private ResourceBundle texts;

    // utilizo un validador para packs para que cuando se quiera crear un producto
    // no valide los campos de pack
    private ValidationSupport vs;


    @FXML
    private void initialize() throws IOException {
        dao = new ProductDAO();
        dao.load();

        texts = GenericFormatter.getText();

        vs = new ValidationSupport();
        vs.registerValidator(date, true, Validator.createEmptyValidator(texts.getString("alert.prodlist.date")));
    }

    public Stage getVentana() {
        return ventana;
    }

    public void setVentana(Stage ventana) {
        this.ventana = ventana;
    }

    public void onCloseWindow() throws IOException {
        dao.save();
    }

    @FXML
    private void onActionExit(ActionEvent e) throws IOException {
        dao.save();
        ventana.close();
    }

    private boolean isDateValid() {
        if (vs.isInvalid()) {
            String errors = vs.getValidationResult().getMessages().toString();
            String title = GenericFormatter.getText().getString("alert.title");
            String header = GenericFormatter.getText().getString("alert.message");
            Alert2.showAlertWindow(ventana, title, header, errors);

            return false;
        }

        return true;
    }



    @FXML
    private void list() {
        if (isDateValid()) {
            List<Product> list;
            list = dao.getDiscontinuedProducts(date.getValue());
            for (Product prod : list) {
                System.out.print(texts.getString("prodlist.days"));
                System.out.println(ChronoUnit.DAYS.between(prod.getEndCatalog(), date.getValue()));
                System.out.println(prod.toString() + "\n");
            }
        }
    }

}


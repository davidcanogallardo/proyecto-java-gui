package controlador;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

import model.Product;
import model.ProductDAO;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ProductsController {

    private ProductDAO dao;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField stockTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private CheckBox isPack;

    // Elements gràfics de la UI
    private Stage ventana;

    @FXML
    private void initialize() throws IOException {
        dao = new ProductDAO();
        dao.load();
    }

    public Stage getVentana() {
        return ventana;
    }

    public void setVentana(Stage ventana) {
        System.out.println("seteo ventana");
        this.ventana = ventana;
    }

    public void sortir() throws IOException {
        System.out.println("cerrar");
        dao.save();
        // TODO guardar weas
    }

    @FXML
    private void onActionSortir(ActionEvent e) throws IOException {
        System.out.println("salgo de products");
        // TODO sortir();
        dao.save();
        ventana.close();
    }

    @FXML
    private void addProd() {
        System.out.println(isPack.isSelected());
        if (!isPack.isSelected()) {
            Product prod = new Product(Integer.parseInt(idTextField.getText()),
                    nameTextField.getText(), Double.parseDouble(priceTextField.getText()),
                    Integer.parseInt(stockTextField.getText()), startDatePicker.getValue(), endDatePicker.getValue());
            if (dao.get(Integer.parseInt(idTextField.getText())) == null) {
                System.out.println("el producto no existe lo añado");
                dao.add(prod);
            } else {
                System.out.println("el producto existe lo modifico");
                dao.modify(prod);
            }
        } else {
            // TODO pack
        }
        // System.out.println("--------------------");
        // System.out.println(endDatePicker.getValue());
        // System.out.println(startDatePicker.getValue());
        // System.out.println(stockTextField.getText());
        // System.out.println(priceTextField.getText());
        // System.out.println(nameTextField.getText());
        // System.out.println(idTextField.getText());
        // System.out.println("--------------------");
    }

    @FXML
    private void deleteProd() {
        System.out.println(isPack.isSelected());
        if (dao.get(Integer.parseInt(idTextField.getText())) != null) {
            System.out.println("borro producto...");
            dao.delete(dao.get(Integer.parseInt(idTextField.getText())));
        } else {
            System.out.println("el producto no existe no borro na");
            // TODO pack
        }
    }

    @FXML
    private void list() {
        System.out.println(dao.getMap());
    }

    
}

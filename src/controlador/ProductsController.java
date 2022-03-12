package controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.Locale.Category;

import model.Pack;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
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
    @FXML
    private TextField guiDiscount;
    @FXML
    private TextArea guiProdList;

    @FXML
    private GridPane packContainer;

    // Elements gràfics de la UI
    private Stage ventana;

    private ValidationSupport vs;
    private ValidationSupport vsPack;

    @FXML
    private void initialize() throws IOException {
        dao = new ProductDAO();
        dao.load();

        vs = new ValidationSupport();
        vsPack = new ValidationSupport();
        vs.registerValidator(idTextField, true, Validator.createEmptyValidator("ID obligatori"));
        vs.registerValidator(nameTextField, true, Validator.createEmptyValidator("nombre obligatori"));
        vs.registerValidator(priceTextField, Validator.createRegexValidator("precio incorrecto",
                "[0-9]{1,9}\\.[0-9]{1,2}|[0-9]{1,9}", Severity.ERROR));
        vs.registerValidator(stockTextField,
                Validator.createRegexValidator("stock incorrecte", "\\d{1,9}", Severity.ERROR));
        vs.registerValidator(startDatePicker, true, Validator.createEmptyValidator("fecha ini obligatori"));
        vs.registerValidator(endDatePicker, true, Validator.createEmptyValidator("fecha fin obligatori"));
        vsPack.registerValidator(guiDiscount,
                Validator.createRegexValidator("descuento obligatorio", "\\d{1,9}", Severity.ERROR));
        vsPack.registerValidator(guiProdList, Validator.createRegexValidator("prod list incorrecto",
                "^(([0-9]{1,9})([,][0-9]{1,9})*)$", Severity.ERROR));
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
        if ((!isPack.isSelected() && isDatosValidos(vs)) || (isPack.isSelected() && isDatosValidos(vs) && isDatosValidos(vsPack))) {
            Product prod = getProductFromGui();
            if (dao.get(Integer.parseInt(idTextField.getText())) == null) {
                System.out.println("el producto no existe lo añado");
                dao.add(prod);
            } else {
                System.out.println("el producto existe lo modifico");
                dao.modify(prod);
            }
        }
    }

    private boolean isDatosValidos(ValidationSupport vs) {
        // Comprovar si totes les dades són vàlides
        if (vs.isInvalid()) {
            String errors = vs.getValidationResult().getMessages().toString();
            // Mostrar finestra amb els errors
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(ventana);
            alert.setTitle("Camps incorrectes");
            alert.setHeaderText("Corregeix els camps incorrectes");
            alert.setContentText(errors);
            alert.showAndWait();

            return false;
        }

        return true;
    }

    @FXML
    private void onKeyPressedId(KeyEvent e) throws IOException {

        if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB) {
            // Comprovar si existeix la persona indicada en el control idTextField
            if (dao.get(getProductId()) != null) {
                if (dao.get(getProductId()) instanceof Pack) {
                    isPack.setSelected(true);
                    packContainer.setVisible(true);
                    Pack product = (Pack) dao.get(getProductId());

                    guiDiscount.setText(product.getDiscount().toString());
                    setProd(product);
                    // guiDiscount.setText(arg0);
                } else {
                    Product product = dao.get(getProductId());
                    stockTextField.setText(product.getStock().toString());
                    isPack.setSelected(false);
                    packContainer.setVisible(false);
                    setProd(product);

                }
                // nameTextField.setText();
            }
            // Product prod = dao.get(getProductId());
            // if (prod != null) {
            // // posar els valors per modificarlos
            // guiDni.setText(prod.getDni());
            // guiName.setText(prod.getName());
            // guiSurname.setText(prod.getSurname());
            // guiLocality.setText(prod.getFullAddress().getLocality());
            // guiProvince.setText(prod.getFullAddress().getProvince());
            // guiCp.setText(prod.getFullAddress().getZipCode());
            // guiAddress.setText(prod.getFullAddress().getAddress());
            // guiPhone.setText("");
            // for (String phone : prod.getPhoneNumber()) {
            // if (prod.getPhoneNumber().toArray()[prod.getPhoneNumber().size() -
            // 1].equals(phone)) {
            // guiPhone.setText(guiPhone.getText() + phone);
            // } else {
            // guiPhone.setText(guiPhone.getText() + phone + ",");
            // }
            // }
            // } else {
            // // nou registre
            // guiDni.setText("");
            // guiName.setText("");
            // guiSurname.setText("");
            // guiLocality.setText("");
            // guiProvince.setText("");
            // guiCp.setText("");
            // guiAddress.setText("");
            // guiPhone.setText("");
            // }
        }
    }

    private void setProd(Product prod) {
        nameTextField.setText(prod.getName());
        priceTextField.setText(prod.getPrice().toString());
        startDatePicker.setValue(prod.getStartCatalog());
        endDatePicker.setValue(prod.getEndCatalog());
    }

    private Product getProductFromGui() {
        Integer id = Integer.parseInt(idTextField.getText());
        String name = nameTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        LocalDate startCatalog = startDatePicker.getValue();
        LocalDate endCatalog = endDatePicker.getValue();

        if (!isPack.isSelected()) {
            Integer stock = Integer.parseInt(stockTextField.getText());
            Product product = new Product(id, name, price, stock, startCatalog, endCatalog);
            return (Product) product;
        } else {
            Integer discount = Integer.parseInt(guiDiscount.getText());

            TreeSet<Product> productList = new TreeSet<>();
            Pack product = new Pack(productList, discount, id, name, price, startCatalog, endCatalog);
            // Product product = new Product(id, name, price, stock, startCatalog,
            // endCatalog);
            return (Product) product;
        }

    }

    private Integer getProductId() {
        return Integer.parseInt(idTextField.getText());
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

    @FXML
    private void pack() {
        if (!isPack.isSelected()) {
            packContainer.setVisible(false);
            // guiDiscount.setVisible(false);
            // guiProdList.setVisible(false);
        } else {
            packContainer.setVisible(true);
            // guiDiscount.setVisible(true);
            // guiProdList.setVisible(true);
        }
    }

}

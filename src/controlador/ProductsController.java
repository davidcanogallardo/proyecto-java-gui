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
import utils.Alert2;
import utils.GenericFormatter;

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

    private Stage ventana;

    // utilizo un validador para packs para que cuando se quiera crear un producto
    // no valide los campos de pack
    private ValidationSupport vsProd;
    private ValidationSupport vsPack;

    private ResourceBundle texts;

    @FXML
    private void initialize() throws IOException {
        dao = new ProductDAO();
        dao.load();

        texts = GenericFormatter.getText();
        String priceRegex = "[0-9]{1,9}\\.[0-9]{1,2}|[0-9]{1,9}";
        String numRegex = "\\d{1,9}";
        String prodListRegex = "^(([0-9]{1,9})([,][0-9]{1,9})*)$";

        vsProd = new ValidationSupport();
        vsPack = new ValidationSupport();
        vsProd.registerValidator(idTextField, true, Validator.createEmptyValidator(texts.getString("alert.prod.id")));
        vsProd.registerValidator(nameTextField, true, Validator.createEmptyValidator("nombre obligatori"));
        vsProd.registerValidator(priceTextField,
                Validator.createRegexValidator("precio incorrecto", priceRegex, Severity.ERROR));
        vsProd.registerValidator(stockTextField,
                Validator.createRegexValidator("stock incorrecte", numRegex, Severity.ERROR));
        vsProd.registerValidator(startDatePicker, true, Validator.createEmptyValidator("fecha ini obligatori"));
        vsProd.registerValidator(endDatePicker, true, Validator.createEmptyValidator("fecha fin obligatori"));
        vsPack.registerValidator(guiDiscount,
                Validator.createRegexValidator("descuento obligatorio", numRegex, Severity.ERROR));
        vsPack.registerValidator(guiProdList, Validator.createRegexValidator("prod list incorrecto",
                prodListRegex, Severity.ERROR));
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

    @FXML
    private void addProd() {
        // TODO hacerlo leíble
        if ((!isPack.isSelected() && isProductValid(vsProd))
                || (isPack.isSelected() && isProductValid(vsProd) && isProductValid(vsPack))) {
            Product prod = getProductFromForm();
            if (dao.get(Integer.parseInt(idTextField.getText())) == null) {
                System.out.println("el producto no existe lo añado");
                dao.add(prod);
            } else {
                System.out.println("el producto existe lo modifico");
                dao.modify(prod);
            }
        }
    }

    private boolean isProductValid(ValidationSupport vs) {
        if (vs.isInvalid()) {
            String errors = vs.getValidationResult().getMessages().toString();
            String title = GenericFormatter.getText().getString("alert.title");
            String header = GenericFormatter.getText().getString("alert.message");
            Alert2.showAlertWindow(ventana, title, header, errors);
            // Alert alert = new Alert(AlertType.CONFIRMATION);
            // alert.initOwner(ventana);
            // alert.setTitle(texts.getString("alert.title"));
            // alert.setHeaderText("Corregeix els camps incorrectes");
            // alert.setContentText(errors);
            // alert.showAndWait();

            return false;
        }

        return true;
    }

    @FXML
    private void onKeyPressedId(KeyEvent e) throws IOException {
        if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB) {
            if (dao.get(getProductId()) != null) {
                if (dao.get(getProductId()) instanceof Pack) {
                    isPack.setSelected(true);
                    packContainer.setVisible(true);
                    Pack product = (Pack) dao.get(getProductId());

                    guiDiscount.setText(product.getDiscount().toString());
                    guiProdList.setText("");
                    for (Product prod : product.getProdList()) {
                        if (product.getProdList().last().equals(prod)) {
                            guiProdList.setText(guiProdList.getText() + prod.getId());
                        } else {
                            guiProdList.setText(guiProdList.getText() + prod.getId() + ",");

                        }
                    }
                    setProductFields(product);
                } else {
                    Product product = dao.get(getProductId());
                    stockTextField.setText(product.getStock().toString());
                    isPack.setSelected(false);
                    packContainer.setVisible(false);
                    setProductFields(product);

                }
            }
        }
    }

    private void setProductFields(Product prod) {
        nameTextField.setText(prod.getName());
        priceTextField.setText(prod.getPrice().toString());
        startDatePicker.setValue(prod.getStartCatalog());
        endDatePicker.setValue(prod.getEndCatalog());
    }

    private Product getProductFromForm() {
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
            String[] prodIds = guiProdList.getText().split(",");
            for (String idProd : prodIds) {
                if (dao.get(Integer.parseInt(idProd)) != null && dao.get(Integer.parseInt(idProd)) instanceof Product) {
                    productList.add(dao.get(Integer.parseInt(idProd)));
                }
            }
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
        } else {
            packContainer.setVisible(true);
        }
    }

}

package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Locale.Category;

import model.Address;
import model.Client;
import model.ClientDAO;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientsController {

    private ClientDAO dao;

    @FXML
    private TextField guiId;
    @FXML
    private TextField guiDni;
    @FXML
    private TextField guiName;
    @FXML
    private TextField guiSurname;
    @FXML
    private TextField guiLocality;
    @FXML
    private TextField guiProvince;
    @FXML
    private TextField guiCp;
    @FXML
    private TextField guiAddress;
    @FXML
    private TextArea guiPhone;

    @FXML
    private GridPane pane;

    // Elements gràfics de la UI
    private Stage ventana;

	private ResourceBundle texts;

    private ValidationSupport vs;
    private ArrayList<TextField> phoneFields = new ArrayList<>();

    @FXML
    private void initialize() throws IOException {
        dao = new ClientDAO();
        dao.load();

        texts = GenericFormatter.getText();
        vs = new ValidationSupport();
        vs.registerValidator(guiId, true, Validator.createEmptyValidator(texts.getString("alert.client.id")));
        vs.registerValidator(guiDni,
                Validator.createRegexValidator(texts.getString("alert.client.dni"), "\\d{8}[a-zA-Z]{1}", Severity.ERROR));
        vs.registerValidator(guiName, true, Validator.createEmptyValidator(texts.getString("alert.client.name")));
        vs.registerValidator(guiSurname, true, Validator.createEmptyValidator(texts.getString("alert.client.surname")));
        vs.registerValidator(guiLocality, true, Validator.createEmptyValidator(texts.getString("alert.client.locality")));
        vs.registerValidator(guiProvince, true, Validator.createEmptyValidator(texts.getString("alert.client.province")));
        vs.registerValidator(guiCp,
                Validator.createRegexValidator(texts.getString("alert.client.zipcode"), "\\d{5}", Severity.ERROR));
        vs.registerValidator(guiAddress, true, Validator.createEmptyValidator(texts.getString("alert.client.address")));
        vs.registerValidator(guiPhone,
                Validator.createRegexValidator(texts.getString("alert.client.phone"), "^(([0-9]{9})([,][0-9]{9})*)$", Severity.ERROR));
    }

    public Stage getVentana() {
        return ventana;
    }

    public void setVentana(Stage ventana) {
        this.ventana = ventana;
    }

    public void sortir() throws IOException {
        dao.save();
    }

    @FXML
    private void onActionSortir(ActionEvent e) throws IOException {
        // TODO sortir();
        dao.save();
        ventana.close();
    }

    @FXML
    private void addClient() {
        String dni = guiDni.getText();
        if (isDatosValidos()) {
            if (isDniLetterValid(dni.substring(8), Integer.parseInt(dni.substring(0, 8)))) {
                Client client = getClientFromGui();

                if (dao.add(client) != null) {
                    Alert2.showAlertWindow(ventana, texts.getString("alert.client.addtitle"), texts.getString("alert.client.addcontent"), "");
                } else {
                    Alert2.showAlertWindow(ventana, texts.getString("alert.client.addtitle"), texts.getString("alert.client.clientmodify"), "");
                    dao.modify(client);
                }
            } else {
                Alert2.showAlertWindow(ventana, texts.getString("alert.client.dnititle"), texts.getString("alert.client.dniletter"), "");
            }
        }
    }

    @FXML
    private void deleteClient() {
        if (dao.get(getClientId()) != null) {
            Alert2.showAlertWindow(ventana, texts.getString("alert.client.deletetitle"), texts.getString("alert.client.deletecontent"), "");
            dao.delete(dao.get(getClientId()));
        } else {
            Alert2.showAlertWindow(ventana, texts.getString("alert.client.deletetitle"), texts.getString("alert.client.notfound"), "");
            // TODO pack
        }
    }

    private Client getClientFromGui() {
        Integer id = Integer.parseInt(guiId.getText());
        String dni = guiDni.getText();
        String name = guiName.getText();
        String surname = guiSurname.getText();
        String locality = guiLocality.getText();
        String province = guiProvince.getText();
        String zipCode = guiCp.getText();
        String address = guiAddress.getText();
        String phoneGui = guiPhone.getText();

        Address fullAddress = new Address(locality, province, zipCode, address);
        LinkedHashSet<String> phoneNumber = new LinkedHashSet<>();

        String[] phones = phoneGui.split(",");
        for (String phone : phones) {
            phoneNumber.add(phone);
        }

        Client client = new Client(id, dni, name, surname, fullAddress, phoneNumber);
        return client;
    }

    private Integer getClientId() {
        return Integer.parseInt(guiId.getText());
    }

    private boolean isDatosValidos() {

        // Comprovar si totes les dades són vàlides
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
        System.out.println(dao.getMap());
    }

    public boolean isDniLetterValid(String letter, int num) {
        String[] letters = {
                "t", "r", "w", "a", "g", "m", "y", "f", "p", "d", "x", "b",
                "n", "j", "z", "s", "q", "v", "h", "l", "c", "k", "e"
        };
        return letter.equalsIgnoreCase(letters[num % 23]);
    }

    @FXML
    private void onKeyPressedId(KeyEvent e) throws IOException {

        if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB) {
            // Comprovar si existeix la persona indicada en el control idTextField
            Client clie = dao.get(getClientId());
            if (clie != null) {
                // posar els valors per modificarlos
                guiDni.setText(clie.getDni());
                guiName.setText(clie.getName());
                guiSurname.setText(clie.getSurname());
                guiLocality.setText(clie.getFullAddress().getLocality());
                guiProvince.setText(clie.getFullAddress().getProvince());
                guiCp.setText(clie.getFullAddress().getZipCode());
                guiAddress.setText(clie.getFullAddress().getAddress());
                guiPhone.setText("");
                for (String phone : clie.getPhoneNumber()) {
                    if (clie.getPhoneNumber().toArray()[clie.getPhoneNumber().size()-1].equals(phone)) {
                        guiPhone.setText(guiPhone.getText() + phone);
                    } else {
                        guiPhone.setText(guiPhone.getText() + phone + ",");
                    }
                }
            } else {
                // nou registre
                guiDni.setText("");
                guiName.setText("");
                guiSurname.setText("");
                guiLocality.setText("");
                guiProvince.setText("");
                guiCp.setText("");
                guiAddress.setText("");
                guiPhone.setText("");
            }
        }
    }

    @FXML
    private void addPhone() {
        // GridPane pane = new GridPane();
        TextField text = new TextField();
        // text.setId("owo");
        vs.registerValidator(text, Validator.createRegexValidator("movil incorrecte", "\\d{9}", Severity.ERROR));
        pane.addRow(pane.getRowCount() + 1, text);
    }

    @FXML
    private void deletePhone() {
        System.out.println(pane.getChildren().get(0));
        vs.getRegisteredControls().remove(pane.getChildren().get(0));
        // pane.getChildren().remove(0);
        // pane.getChildren().remove(0);
    }
}

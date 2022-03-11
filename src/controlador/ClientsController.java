package controlador;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

import model.Address;
import model.Client;
import model.ClientDAO;
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
    private TextField guiPhone;

    // Elements gràfics de la UI
    private Stage ventana;
    
	private ValidationSupport vs;

    @FXML
    private void initialize() throws IOException {
        dao = new ClientDAO();
        dao.load();


        vs = new ValidationSupport();
		vs.registerValidator(guiId, true, Validator.createEmptyValidator("ID obligatori"));
		vs.registerValidator(guiDni, Validator.createRegexValidator("Dni incorrecto", "\\d{8}[a-zA-Z]{1}", Severity.ERROR));
		vs.registerValidator(guiName, true, Validator.createEmptyValidator("Nombre obligatori"));
        vs.registerValidator(guiSurname, true, Validator.createEmptyValidator("Apelldios obligatori"));
		vs.registerValidator(guiLocality, true, Validator.createEmptyValidator("localidad obligatori"));
		vs.registerValidator(guiProvince, true, Validator.createEmptyValidator("provincia obligatori"));
        vs.registerValidator(guiCp, Validator.createRegexValidator("codigo postal incorrecte", "\\d{5}", Severity.ERROR));
		vs.registerValidator(guiAddress, true, Validator.createEmptyValidator("direccion obligatori"));
        vs.registerValidator(guiPhone, Validator.createRegexValidator("movil incorrecte", "\\d{9}", Severity.ERROR));
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
    }

    @FXML
    private void onActionSortir(ActionEvent e) throws IOException {
        System.out.println("salgo de client");
        // TODO sortir();
        dao.save();
        ventana.close();
    }

    @FXML
    private void addClient() {
        String dni = guiDni.getText();
        if(isDatosValidos()){
            if (isDniLetterValid(dni.substring(8), Integer.parseInt(dni.substring(0, 8)))) {
                Client client = getClientFromGui();
        
                if (dao.add(client) != null) {
                    System.out.println("\nCliente añadido!\n");
                } else {
                    dao.modify(client);
                    System.out.println("el cliente ya existe lo modifico");
                }
            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.initOwner(ventana);
                alert.setTitle("DNI incorrecto");
                alert.setHeaderText("Corrige la letra del dni");
                alert.setContentText("Corrige la letra del dni");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void deleteClient() {
        if (dao.get(getClientId()) != null) {
            System.out.println("borro cliente...");
            dao.delete(dao.get(getClientId()));
        } else {
            System.out.println("el cliente no existe no borro na");
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
        String phone = guiPhone.getText();

        Address fullAddress = new Address(locality, province, zipCode, address);
        LinkedHashSet<String> phoneNumber = new LinkedHashSet<>();
        phoneNumber.add(phone);

        Client client = new Client(id, dni, name, surname, fullAddress, phoneNumber);
        return client;
    }

    private Integer getClientId() {
        return Integer.parseInt(guiId.getText());
    }

    private boolean isDatosValidos() {

		//Comprovar si totes les dades són vàlides
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

	@FXML private void onKeyPressedId(KeyEvent e) throws IOException {

		if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB){
			//Comprovar si existeix la persona indicada en el control idTextField
			Client clie = dao.get(getClientId());
			if(clie != null){ 
				//posar els valors per modificarlos
                guiDni.setText(clie.getDni());
                guiName.setText(clie.getName());
                guiSurname.setText(clie.getSurname());
                guiLocality.setText(clie.getFullAddress().getLocality());
                guiProvince.setText(clie.getFullAddress().getProvince());
                guiCp.setText(clie.getFullAddress().getZipCode());
                guiAddress.setText(clie.getFullAddress().getAddress());
                guiPhone.setText(clie.getPhoneNumber().stream().findFirst().get());
			} else{ 
				//nou registre
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

}

package controlador;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

import model.PresenceRegisterDAO;
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

public class PresenceController {

    private PresenceRegisterDAO dao;

    @FXML
    private TextField idTextField;


    @FXML
    private Button guiClockIn;
    @FXML
    private Button guiClockOut;

    // Elements gràfics de la UI
    private Stage ventana;

    @FXML
    private void initialize() throws IOException {
        dao = new PresenceRegisterDAO();
        // dao.load();
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
        // dao.save();
        // TODO guardar weas
    }

    @FXML
    private void onActionSortir(ActionEvent e) throws IOException {
        System.out.println("salgo de products");
        // TODO sortir();
        // dao.save();
        ventana.close();
    }

    @FXML
	private void onAction(ActionEvent e) throws Exception {
		if (e.getSource() == guiClockIn) {// verifica si el botón es igual al que llamo al evento
			System.out.println("ficho entrada");
		} else if (e.getSource() == guiClockOut) {
            System.out.println("ficho salida");
		}
	}

    @FXML
    private void list() {
        dao.list();
    }

    
}

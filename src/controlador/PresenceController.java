package controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

import model.Address;
import model.Client;
import model.ClientDAO;
import model.Presence;
import model.PresenceRegisterDAO;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PresenceController {

    private PresenceRegisterDAO dao;

    @FXML
    private TextField guiId;

    @FXML
    private Button guiClockIn;
    @FXML
    private Button guiClockOut;

    // Elements gràfics de la UI
    private Stage ventana;

    private ValidationSupport vs;
	private ResourceBundle texts;


    @FXML
    private void initialize() throws IOException {
        dao = new PresenceRegisterDAO();
        dao.load();
        texts = GenericFormatter.getText();

        vs = new ValidationSupport();
        // vs.registerValidator(guiId, Validator.createRegexValidator("id obligatorio",
        // "\\d{9}", Severity.ERROR));
        vs.registerValidator(guiId, true, Validator.createEmptyValidator(texts.getString("alert.presence.id")));
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
    private void onActionSortir(ActionEvent e) throws IOException {
        dao.save();
        ventana.close();
    }

    @FXML
    private void onAction(ActionEvent e) throws Exception {
        if (e.getSource() == guiClockIn) {// verifica si el botón es igual al que llamo al evento
            if (isDatosValidos()) {
                Presence presence = new Presence(Integer.parseInt(guiId.getText()), LocalDate.now(), LocalTime.now());
                if (dao.add(presence) == null) {
                    Alert2.showAlertWindow(ventana, "Error", texts.getString("alert.presence.clockin"),
                            "");
                }
            }
        } else if (e.getSource() == guiClockOut) {
            if (isDatosValidos()) {

                if (!dao.addLeaveTime(Integer.parseInt(guiId.getText()))) {
                    Alert2.showAlertWindow(ventana, "Error", texts.getString("alert.presence.clockout"), "");
                }
            }
        }
    }

    @FXML
    private void list() {
        dao.list();
    }

}

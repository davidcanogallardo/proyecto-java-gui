package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Alert2 {
    public static void showAlertWindow(Stage ventana, String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(ventana);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

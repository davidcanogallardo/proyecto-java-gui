package controlador;

import javafx.stage.Stage;

public class ProductsController {
    // Elements gràfics de la UI
    private Stage ventana;
    
    public Stage getVentana() {
        return ventana;
    }

    public void setVentana(Stage ventana) {
        System.out.println("seteo ventana");
        this.ventana = ventana;
    }

    public void sortir() {
        System.out.println("cerrar");
        // TODO guardar weas
    }
}

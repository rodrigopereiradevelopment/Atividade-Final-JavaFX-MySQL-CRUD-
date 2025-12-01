
package com.example.fxmysqlfxml;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AboutController {
    @FXML private Label lblInfo;

    @FXML
    public void initialize() {
        lblInfo.setText("Etec - Pedro Ferreira Alves\nTabalho Final DS\nBruno Capra");
    }
}

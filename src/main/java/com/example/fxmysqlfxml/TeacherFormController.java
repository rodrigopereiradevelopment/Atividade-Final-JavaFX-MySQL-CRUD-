
package com.example.fxmysqlfxml;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TeacherFormController {

    @FXML private TextField txtId;
    @FXML private TextField txtNome;
    @FXML private TextField txtMateria;
    @FXML private TextField txtPeriodo;

    private Teacher resultado;

    public void setProfessor(Teacher t) {
        if (t != null) {
            txtId.setText(t.getId() == null ? "" : String.valueOf(t.getId()));
            txtNome.setText(t.getNome());
            txtMateria.setText(t.getMateria());
            txtPeriodo.setText(t.getPeriodo());
        }
    }

    public Teacher getResultado() { return resultado; }

    @FXML
    private void tSalvar() {
        String nome = txtNome.getText().trim();
        String materia = txtMateria.getText().trim();
        String periodo = txtPeriodo.getText().trim();
        if (nome.isEmpty() || materia.isEmpty() || periodo.isEmpty()) {
            alert("Validação", "Nome, Materia e Periodo são obrigatórios.");
            return;
        }
        Long id = txtId.getText().isBlank() ? null : Long.parseLong(txtId.getText());
        resultado = new Teacher(id, nome, materia, periodo);
        fechar();
    }

    @FXML
    private void tCancelar() {
        resultado = null;
        fechar();
    }

    private void fechar() {
        ((Stage) txtNome.getScene().getWindow()).close();
    }

    private void alert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }
}

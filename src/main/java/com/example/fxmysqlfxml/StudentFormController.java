
package com.example.fxmysqlfxml;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class StudentFormController {

    @FXML private TextField txtId;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtSala;
    @FXML private TextField txtModulo;
    @FXML private TextField txtCurso;

    private Student resultado;

    public void setAluno(Student s) {
        if (s != null) {
            txtId.setText(s.getId() == null ? "" : String.valueOf(s.getId()));
            txtNome.setText(s.getNome());
            txtEmail.setText(s.getEmail());
            txtSala.setText(s.getSala());
            txtModulo.setText(s.getModulo());
            txtCurso.setText(s.getCurso());
        }
    }

    public Student getResultado() { return resultado; }

    @FXML
    private void sSalvar(ActionEvent event) {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String sala = txtSala.getText().trim();
        String modulo = txtModulo.getText().trim();
        String curso = txtCurso.getText().trim();
        if (nome.isEmpty() || email.isEmpty() || sala.isEmpty() || modulo.isEmpty() || curso.isEmpty()) {
            alert("Validação", "Nome, e-mail, sala, modulo e curso são obrigatórios.");
            return;
        }
        Long id = txtId.getText().isBlank() ? null : Long.parseLong(txtId.getText());

        // CORREÇÃO 1: Usar o construtor vazio e setters
        resultado = new Student();
        resultado.setId(id);
        resultado.setNome(nome);
        resultado.setEmail(email);

        // Campos que não estão no FXML (definir valores seguros para o DB)
        resultado.setBirthDate(null);
        resultado.setPhone("");
        resultado.setActive(true);

        // Campos que estão no FXML
        resultado.setSala(sala);
        resultado.setModulo(modulo);
        resultado.setCurso(curso);

        fechar();
    }

    @FXML
    private void sCancelar(ActionEvent event) {
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

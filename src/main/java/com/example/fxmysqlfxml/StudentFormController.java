package com.example.fxmysqlfxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Optional; // Necessário para a caixa de diálogo de confirmação

public class StudentFormController {

    // 1. NOVOS @FXML: Correspondendo ao StudentForm.fxml
    @FXML private TextField txtId;
    @FXML private TextField txtName; // Corrigido para 'txtName' (se o FXML foi corrigido)
    @FXML private TextField txtEmail;
    @FXML private DatePicker dpBirthDate; // Novo: Para birth_date
    @FXML private TextField txtPhone;      // Novo: Para phone
    @FXML private CheckBox cbActive;      // Novo: Para active

    // Variáveis de instância
    private Student student;
    private StudentDao studentDao = new StudentDao();

    /**
     * Define o objeto Student a ser editado e preenche o formulário.
     */
    public void setStudent(Student s) {
        this.student = s;
        if (s != null) {
            txtId.setText(s.getId() == null ? "" : String.valueOf(s.getId()));

            // Campos base
            txtName.setText(s.getName());
            txtEmail.setText(s.getEmail());

            // NOVOS CAMPOS:
            dpBirthDate.setValue(s.getBirthDate()); // LocalDate
            txtPhone.setText(s.getPhone());
            // Garante que o CheckBox lide com valores nulos, assumindo ativo se null
            cbActive.setSelected(s.getActive() != null ? s.getActive() : true);
        }
    }

    /**
     * Retorna o Student preenchido (usado para atualizar a listagem após salvar)
     */
    public Student getStudent() {
        return student;
    }

    // 2. MÉTODOS DE AÇÃO: Correspondendo aos 'onAction' do FXML

    @FXML
    private void handleSalvar(ActionEvent event) {
        // 1. Validação
        if (!isInputValid()) {
            return;
        }

        // 2. Coletar dados do formulário e carregar no objeto student
        updateStudentFromForm();

        try {
            // 3. Chamar a lógica de persistência (DAO)
            if (student.getId() == null) {
                studentDao.insert(student);
            } else {
                studentDao.update(student);
            }

            // 4. Fechar o formulário após salvar com sucesso
            closeStage(event);

        } catch (RuntimeException e) {
            // 5. Tratamento de Erro de Persistência (SQL/Conexão)
            alert(Alert.AlertType.ERROR, "Erro de Persistência", "Não foi possível salvar os dados no banco de dados.");
            e.printStackTrace(); // Imprime o erro no console para debug
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        this.student = null; // Zera o objeto se cancelar
        closeStage(event);
    }

    // -----------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // -----------------------------------------------------------------

    /**
     * Transfere os dados do formulário para o objeto Student.
     */
    private void updateStudentFromForm() {
        if (this.student == null) {
            // Se for novo, inicializa um objeto com o ID se houver
            Long id = txtId.getText().isBlank() ? null : Long.parseLong(txtId.getText());
            this.student = new Student();
            this.student.setId(id);
        }

        // Campos base
        this.student.setName(txtName.getText().trim());
        this.student.setEmail(txtEmail.getText().trim());

        // NOVOS CAMPOS:
        this.student.setBirthDate(dpBirthDate.getValue()); // Pega o LocalDate do DatePicker
        this.student.setPhone(txtPhone.getText().trim());
        this.student.setActive(cbActive.isSelected());
    }

    /**
     * Realiza a validação mínima dos campos obrigatórios.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
            errorMessage += "Nome é obrigatório!\n";
        }
        if (txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
            errorMessage += "E-mail é obrigatório!\n";
        }
        if (txtPhone.getText() != null && txtPhone.getText().length() > 20) {
            errorMessage += "Telefone inválido (máximo 20 caracteres)!\n";
        }
        // Validação da Data de Nascimento: Não pode ser no futuro
        if (dpBirthDate.getValue() != null && dpBirthDate.getValue().isAfter(LocalDate.now())) {
            errorMessage += "Data de Nascimento não pode ser futura!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            alert(Alert.AlertType.WARNING, "Campos Inválidos", errorMessage);
            return false;
        }
    }

    /**
     * Obtém o Stage (janela) a partir do componente e fecha-o.
     */
    private void closeStage(ActionEvent event) {
        // Usa qualquer componente para obter a Stage
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    /**
     * Método auxiliar para exibir alertas.
     */
    private void alert(Alert.AlertType type, String t, String m) {
        Alert a = new Alert(type);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }

    public void setAluno(Student s) {
    }

    public Student getResultado() {
        return null;
    }
}
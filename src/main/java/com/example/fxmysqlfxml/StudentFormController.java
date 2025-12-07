package com.example.fxmysqlfxml;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.time.LocalDate; // Import necessário para o DatePicker

public class StudentFormController {

    // 1. DECLARAÇÕES FXML (TODOS OS 9 CAMPOS DA TELA)
    @FXML private TextField txtId;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;

    @FXML private DatePicker dpBirthDate; // NOVO: Data de Nascimento
    @FXML private TextField txtPhone;      // NOVO: Telefone
    @FXML private CheckBox chkActive;     // NOVO: Ativo

    @FXML private TextField txtSala;
    @FXML private TextField txtModulo;
    @FXML private TextField txtCurso;

    private Student resultado;

    @FXML
    public void initialize() {
        // O corpo pode ficar vazio.
        // A simples presença deste método garante a injeção correta de todas as variáveis @FXML (incluindo dpBirthDate).
        // Se precisar de alguma configuração inicial (ex: formatar a data), você a faria aqui.
    }

    // ==========================================================
    // MÉTODO setAluno (Carrega dados do DB para a tela)
    // ==========================================================
    public void setAluno(Student s) {
        this.resultado = s == null ? new Student() : s;

        if (s != null) {
            // Campos já existentes
            txtId.setText(s.getId() == null ? "" : String.valueOf(s.getId()));
            txtNome.setText(s.getNome());
            txtEmail.setText(s.getEmail());
            txtSala.setText(s.getSala());
            txtModulo.setText(s.getModulo());
            txtCurso.setText(s.getCurso());

            // CORREÇÃO 1: Carregar os novos campos do Student para a interface
            dpBirthDate.setValue(s.getBirthDate());
            txtPhone.setText(s.getPhone());
            chkActive.setSelected(s.getActive());
        } else {
            // Define valores padrão ao criar um Novo Aluno
            chkActive.setSelected(true);
        }
    }

    public Student getResultado() { return resultado; }

    // ==========================================================
    // MÉTODO sSalvar (Lê dados da tela para o objeto Student)
    // ==========================================================
    @FXML
    private void sSalvar(ActionEvent event) {
        // Coleta de dados com trim
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String sala = txtSala.getText().trim();
        String modulo = txtModulo.getText().trim();
        String curso = txtCurso.getText().trim();

        // NOVO: Coleta dos campos Dt. Nasc., Telefone e Ativo
        LocalDate birthDate = dpBirthDate.getValue();
        String phone = txtPhone.getText().trim();
        Boolean active = chkActive.isSelected();

        System.out.println("Data de Nascimento lida da tela: " + birthDate);
        System.out.println("Telefone lido da tela: " + phone);

        // Validação (ajustada para ignorar a validação de Dt. Nasc. e Telefone por enquanto)
        if (nome.isEmpty() || email.isEmpty() || sala.isEmpty() || modulo.isEmpty() || curso.isEmpty()) {
            alert("Validação", "Nome, e-mail, sala, modulo e curso são obrigatórios.");
            return;
        }
        // AQUI COLOCAMOS A ÚLTIMA VERIFICAÇÃO ANTES DE SALVAR (Substitua o trecho que estava duplicado):
        if (birthDate == null) {
            // Agora, se for nulo, o programa alertará o usuário e não tentará salvar no DB
            alert("Erro FXML", "A Data de Nascimento está nula. Falha de mapeamento de componente. (Tente selecionar novamente a data)");
            return; // Retorna para não salvar dados inconsistentes
        }

        // 2. SALVAR OS DADOS LIDOS NO OBJETO 'resultado'

        // Pega o ID (para edição)
        Long id = txtId.getText().isBlank() ? null : Long.parseLong(txtId.getText());
        resultado.setId(id);

        // Campos de texto e tela
        resultado.setNome(nome);
        resultado.setEmail(email);
        resultado.setSala(sala);
        resultado.setModulo(modulo);
        resultado.setCurso(curso);

        // CORREÇÃO 2: Salvar os dados LIDOS da interface
        resultado.setBirthDate(birthDate);
        resultado.setPhone(phone);
        resultado.setActive(active);

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
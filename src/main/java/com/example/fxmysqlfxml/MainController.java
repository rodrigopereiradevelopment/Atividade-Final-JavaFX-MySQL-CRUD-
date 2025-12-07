
package com.example.fxmysqlfxml;

import javafx.scene.control.cell.PropertyValueFactory; //
import java.time.LocalDate;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML private TableView<Student> tableAluno;
    @FXML private TableColumn<Student, Long> colIdAluno;
    @FXML private TableColumn<Student, String> colNomeAluno;
    @FXML private TableColumn<Student, String> colEmailAluno;
    @FXML private TableColumn<Student, String> colSalaAluno;
    @FXML private TableColumn<Student, String> colModuloAluno;
    @FXML private TableColumn<Student, String> colCursoAluno;
    // DECLARAÇÕES CORRETAS (Conforme Student.java e MainView.fxml)
    @FXML private TableColumn<Student, LocalDate> colBirthDateAluno; // Para Dt. Nasc.
    @FXML private TableColumn<Student, String> colPhoneAluno;         // Para Telefone
    @FXML private TableColumn<Student, Boolean> colActiveAluno;       // Para Ativo
    private final StudentDao sDao = new StudentDao();
    private final ObservableList<Student> sData = FXCollections.observableArrayList();

    @FXML private TableView<Teacher> tableProfessor;
    @FXML private TableColumn<Teacher, Long> colIdProfessor;
    @FXML private TableColumn<Teacher, String> colNomeProfessor;
    @FXML private TableColumn<Teacher, String> colMateriaProfessor;
    @FXML private TableColumn<Teacher, String> colPeriodoProfessor;

    private final TeacherDao tDao = new TeacherDao();
    private final ObservableList<Teacher> tData = FXCollections.observableArrayList();

    // No MainController.java (Método initialize)
    @FXML
    public void initialize() {
        // 1. Mapeamentos para a Tabela de Alunos (Student)
        colIdAluno.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getId()==null?0:c.getValue().getId()).asObject());
        colNomeAluno.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colEmailAluno.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        // Mapeamentos para os campos de 9 colunas (corrigidos)
        // Usaremos PropertyValueFactory para os tipos simples e objetos
        colBirthDateAluno.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colPhoneAluno.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colActiveAluno.setCellValueFactory(new PropertyValueFactory<>("active"));

        // NOVOS CAMPOS DO FXML:
        colSalaAluno.setCellValueFactory(new PropertyValueFactory<>("sala"));
        colModuloAluno.setCellValueFactory(new PropertyValueFactory<>("modulo"));
        colCursoAluno.setCellValueFactory(new PropertyValueFactory<>("curso"));

        tableAluno.setItems(sData);

        // 2. Mapeamentos para a Tabela de Professores (Teacher)
        colIdProfessor.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getId()==null?0:c.getValue().getId()).asObject());
        colNomeProfessor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colMateriaProfessor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMateria()));
        colPeriodoProfessor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPeriodo()));
        tableProfessor.setItems(tData);

        carregar();
    }

    private void carregar() {
        sData.setAll(sDao.findAll());

        tData.setAll(tDao.findAll());
    }

    @FXML
    private void sNovo() throws IOException {
        Student novo = showFormS(null);
        if (novo != null) {
            sDao.insert(novo);
            sData.add(novo);
            tableAluno.getSelectionModel().select(novo);
        }
    }

    @FXML
    private void tNovo() throws IOException {
        Teacher novo = showFormT(null);
        if (novo != null) {
            tDao.insert(novo);
            tData.add(novo);
            tableProfessor.getSelectionModel().select(novo);
        }
    }

    // No MainController.java
    // No MainController.java
    @FXML
    private void sEditar() throws IOException {
        Student sel = tableAluno.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alert(Alert.AlertType.ERROR, "Seleção", "Selecione um aluno.");
            return;
        }

        // Cria uma cópia usando setters para garantir que todos os 9 campos sejam incluídos (CORRETO).
        Student copia = new Student();
        copia.setId(sel.getId());
        copia.setNome(sel.getNome());
        copia.setEmail(sel.getEmail());

        // Campos adicionais do DB:
        copia.setBirthDate(sel.getBirthDate());
        copia.setPhone(sel.getPhone());
        copia.setActive(sel.getActive());

        // Campos que estavam no construtor antigo:
        copia.setSala(sel.getSala());
        copia.setModulo(sel.getModulo());
        copia.setCurso(sel.getCurso());

        Student editado = showFormS(copia); // Passa a cópia completa para o formulário

        // Continuação do método...
        if (editado != null) {
            try {
                // Usa o objeto 'editado' retornado pelo formulário
                sDao.update(editado); // <-- ESTE CÓDIGO PODE LANÇAR RuntimeException

                // Se o update foi bem-sucedido:
                int idx = sData.indexOf(sel);
                if (idx >= 0) sData.set(idx, editado);
                tableAluno.getSelectionModel().select(editado);

                // Opcional: Alerta de sucesso
                alert(Alert.AlertType.INFORMATION, "Sucesso", "Aluno atualizado com sucesso!");

            } catch (RuntimeException e) {
                // Se o DAO falhar (ex: por causa de NOT NULL ou conversão de data/telefone):
                // O getMessage() deve conter a mensagem de erro do SQLException
                alert(Alert.AlertType.ERROR, "Erro no Banco de Dados", "Falha ao atualizar o aluno. Detalhe: " + e.getMessage());
            }
        }
    }
    @FXML
    private void tEditar() throws IOException {
        Teacher sel = tableProfessor.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alert(Alert.AlertType.ERROR, "Seleção", "Selecione um professor.");
            return;
        }
        Teacher editado = showFormT(new Teacher(sel.getId(), sel.getNome(), sel.getMateria(), sel.getPeriodo()));
        if (editado != null) {
            tDao.update(editado);
            int idx = tData.indexOf(sel);
            if (idx >= 0) tData.set(idx, editado);
            tableProfessor.getSelectionModel().select(editado);
        }
    }


    @FXML
    private void sExcluir() {
        Student sel = tableAluno.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alert(Alert.AlertType.ERROR, "Seleção", "Selecione um aluno.");
            return;
        }
        if (confirm("Excluir", "Deseja excluir o registro?")) {
            sDao.deleteById(sel.getId());
            sData.remove(sel);
        }
    }

    @FXML
    private void tExcluir() {
        Teacher sel = tableProfessor.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alert(Alert.AlertType.ERROR, "Seleção", "Selecione um professor.");
            return;
        }
        if (confirm("Excluir", "Deseja excluir o registro?")) {
            tDao.deleteById(sel.getId());
            tData.remove(sel);
        }
    }

    @FXML
    private void onAtualizar() {
        carregar();
    }

    @FXML
    private void onSobre() throws IOException {
        Stage dlg = new Stage();
        dlg.setTitle("Sobre");
        dlg.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutView.fxml"));
        dlg.setScene(new Scene(loader.load()));
        dlg.showAndWait();
    }

    private Student showFormS(Student s) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxmysqlfxml/StudentForm.fxml"));
        BorderPane pane = loader.load();
        StudentFormController controller = loader.getController();
        controller.setAluno(s);
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle(s == null ? "Novo Aluno" : "Editar Aluno");
        dlg.setScene(new Scene(pane));
        dlg.showAndWait();
        return controller.getResultado();
    }

    private Teacher showFormT(Teacher t) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherForm.fxml"));
        BorderPane pane = loader.load();
        TeacherFormController controller = loader.getController();
        controller.setProfessor(t);
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle(t == null ? "Novo Professor" : "Editar Professor");
        dlg.setScene(new Scene(pane));
        dlg.showAndWait();
        return controller.getResultado();
    }

    private void alert(Alert.AlertType t, String title, String msg) {
        Alert a = new Alert(t); a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
    private boolean confirm(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION); a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        return a.showAndWait().filter(b -> b== ButtonType.OK).isPresent();
    }
}

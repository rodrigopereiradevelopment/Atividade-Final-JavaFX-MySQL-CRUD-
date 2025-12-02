
package com.example.fxmysqlfxml;

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

    private final StudentDao sDao = new StudentDao();
    private final ObservableList<Student> sData = FXCollections.observableArrayList();

    @FXML private TableView<Teacher> tableProfessor;
    @FXML private TableColumn<Teacher, Long> colIdProfessor;
    @FXML private TableColumn<Teacher, String> colNomeProfessor;
    @FXML private TableColumn<Teacher, String> colMateriaProfessor;
    @FXML private TableColumn<Teacher, String> colPeriodoProfessor;

    private final TeacherDao tDao = new TeacherDao();
    private final ObservableList<Teacher> tData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colIdAluno.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getId()==null?0:c.getValue().getId()).asObject());
        colNomeAluno.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colEmailAluno.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        colSalaAluno.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSala()));
        colModuloAluno.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModulo()));
        colCursoAluno.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCurso()));
        tableAluno.setItems(sData);

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

    @FXML
    private void sEditar() throws IOException {
        Student sel = tableAluno.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alert(Alert.AlertType.ERROR, "Seleção", "Selecione um aluno.");
            return;
        }
        Student editado = showFormS(new Student(sel.getId(), sel.getNome(), sel.getEmail(), sel.getSala(), sel.getModulo(), sel.getCurso()));
        if (editado != null) {
            sDao.update(editado);
            int idx = sData.indexOf(sel);
            if (idx >= 0) sData.set(idx, editado);
            tableAluno.getSelectionModel().select(editado);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentForm.fxml"));
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

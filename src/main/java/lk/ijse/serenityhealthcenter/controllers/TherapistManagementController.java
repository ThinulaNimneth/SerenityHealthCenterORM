package lk.ijse.serenityhealthcenter.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.ProgramBO;
import lk.ijse.serenityhealthcenter.bo.custom.TherapistBO;
import lk.ijse.serenityhealthcenter.dto.ProgramDto;
import lk.ijse.serenityhealthcenter.dto.TherapistDto;

import java.sql.SQLException;

public class TherapistManagementController {
    public TextField txtTherapistId;
    public TextField txtName;
    public TextField txtSpecialization;
    public TextField txtContact;
    public ComboBox<String> cmbProgram;

    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnSearch;

    public TableView<TherapistDto> tblTherapists;
    public TableColumn<TherapistDto, String> colId;
    public TableColumn<TherapistDto, String> colName;
    public TableColumn<TherapistDto, String> colSpec;
    public TableColumn<TherapistDto, String> colContact;
    public TableColumn<TherapistDto, String> colStatus;

    TherapistBO therapistBO = (TherapistBO) BOFactory.getInstance().getBO(BOFactory.BOType.THERAPIST);
    ProgramBO programBO    = (ProgramBO)    BOFactory.getInstance().getBO(BOFactory.BOType.PROGRAM);

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        loadAll();
        loadPrograms();
    }

    private void loadAll() throws SQLException, ClassNotFoundException {
        tblTherapists.setItems(therapistBO.getAllTherapists());
    }

    private void loadPrograms() throws SQLException, ClassNotFoundException {
        ObservableList<String> ids = FXCollections.observableArrayList();
        for (ProgramDto p : programBO.getAllPrograms()) ids.add(p.getProgramId());
        cmbProgram.setItems(ids);
    }

    public void addTherapist(ActionEvent e) throws SQLException, ClassNotFoundException {
        if (!isValid()) { new Alert(Alert.AlertType.ERROR, "Invalid contact number.").show(); return; }
        String programId = cmbProgram.getValue();
        TherapistDto dto = new TherapistDto(txtTherapistId.getText().trim(), txtName.getText().trim(),
                txtSpecialization.getText().trim(), txtContact.getText().trim(),
                programId == null ? "Available" : "Not Available");
        therapistBO.addTherapist(dto, programId);
        new Alert(Alert.AlertType.INFORMATION, "Therapist added.").show();
        loadAll(); clear();
    }

    public void updateTherapist(ActionEvent e) throws SQLException, ClassNotFoundException {
        String programId = cmbProgram.getValue();
        TherapistDto dto = new TherapistDto(txtTherapistId.getText().trim(), txtName.getText().trim(),
                txtSpecialization.getText().trim(), txtContact.getText().trim(),
                programId == null ? "Available" : "Not Available");
        if (therapistBO.updateTherapist(dto, programId)) {
            new Alert(Alert.AlertType.INFORMATION, "Therapist updated.").show();
            loadAll(); clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Update failed.").show();
        }
    }

    public void deleteTherapist(ActionEvent e) throws SQLException, ClassNotFoundException {
        String id = txtTherapistId.getText().trim();
        if (id.isEmpty()) { new Alert(Alert.AlertType.ERROR, "Enter Therapist ID.").show(); return; }
        if (therapistBO.deleteTherapist(id)) {
            new Alert(Alert.AlertType.INFORMATION, "Therapist deleted.").show();
            loadAll(); clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Delete failed.").show();
        }
    }

    public void searchTherapist(ActionEvent e) {
        // search logic can be added
    }

    public void onTableClicked(MouseEvent e) {
        TherapistDto dto = tblTherapists.getSelectionModel().getSelectedItem();
        if (dto != null) {
            txtTherapistId.setText(dto.getTherapistId());
            txtName.setText(dto.getName());
            txtSpecialization.setText(dto.getSpecialization());
            txtContact.setText(dto.getContactNo());
        }
    }

    private void clear() {
        txtTherapistId.clear(); txtName.clear();
        txtSpecialization.clear(); txtContact.clear(); cmbProgram.setValue(null);
    }

    private boolean isValid() {
        return txtContact.getText().matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$");
    }

    public void contactValidation(KeyEvent e) {
        txtContact.setStyle(txtContact.getText().matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$")
                ? "-fx-border-color: green; -fx-border-width: 2;" : "-fx-border-color: red; -fx-border-width: 2;");
    }
}

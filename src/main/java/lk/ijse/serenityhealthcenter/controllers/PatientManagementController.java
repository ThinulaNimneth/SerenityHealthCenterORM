package lk.ijse.serenityhealthcenter.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.serenityhealthcenter.exception.RegistrationException;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.PatientBO;
import lk.ijse.serenityhealthcenter.dto.PatientDto;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class PatientManagementController {
    public TextField txtPatientId;
    public TextField txtName;
    public TextField txtEmail;
    public TextField txtAddress;
    public TextField txtTel;
    public DatePicker dpRegDate;

    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnSearch;
    public Button btnPatientHistory;

    public TableView<PatientDto> tblPatients;
    public TableColumn<PatientDto, String> colId;
    public TableColumn<PatientDto, String> colName;
    public TableColumn<PatientDto, String> colEmail;
    public TableColumn<PatientDto, String> colAddress;
    public TableColumn<PatientDto, String> colTel;
    public TableColumn<PatientDto, String> colDate;

    PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOType.PATIENT);

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("registerDate"));
        loadAll();
    }

    private void loadAll() throws SQLException, ClassNotFoundException {
        ObservableList<PatientDto> list = patientBO.getAllPatients();
        tblPatients.setItems(list);
    }

    public void addPatient(ActionEvent e) {
        if (!isValid()) { new Alert(Alert.AlertType.ERROR, "Please enter valid data.").show(); return; }
        try {
            PatientDto dto = getFormData();
            if (patientBO.addPatient(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Patient saved successfully.").show();
                loadAll(); clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save patient.").show();
            }
        } catch (RegistrationException ex) {
            new Alert(Alert.AlertType.ERROR, "Registration Error: " + ex.getMessage()).show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
        }
    }

    public void updatePatient(ActionEvent e) {
        if (!isValid()) { new Alert(Alert.AlertType.ERROR, "Please enter valid data.").show(); return; }
        try {
            PatientDto dto = getFormData();
            if (patientBO.updatePatient(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Patient updated.").show();
                loadAll(); clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update failed.").show();
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
        }
    }

    public void deletePatient(ActionEvent e) {
        String id = txtPatientId.getText().trim();
        if (id.isEmpty()) { new Alert(Alert.AlertType.ERROR, "Enter Patient ID to delete.").show(); return; }
        try {
            if (patientBO.deletePatient(id)) {
                new Alert(Alert.AlertType.INFORMATION, "Patient deleted.").show();
                loadAll(); clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Delete failed.").show();
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
        }
    }

    public void searchPatient(ActionEvent e) {
        String id = txtPatientId.getText().trim();
        PatientDto dto = patientBO.searchPatient(id);
        if (dto != null) {
            txtName.setText(dto.getName());
            txtEmail.setText(dto.getEmail());
            txtAddress.setText(dto.getAddress());
            txtTel.setText(dto.getTel());
            dpRegDate.setValue(LocalDate.parse(dto.getRegisterDate()));
        } else {
            new Alert(Alert.AlertType.ERROR, "Patient not found.").show();
        }
    }

    public void onTableClicked(MouseEvent e) {
        PatientDto dto = tblPatients.getSelectionModel().getSelectedItem();
        if (dto != null) {
            txtPatientId.setText(dto.getId());
            txtName.setText(dto.getName());
            txtEmail.setText(dto.getEmail());
            txtAddress.setText(dto.getAddress());
            txtTel.setText(dto.getTel());
            dpRegDate.setValue(LocalDate.parse(dto.getRegisterDate()));
        }
    }

    public void openPatientHistory(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/PatientHistory.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Patient History");
        stage.show();
    }

    private PatientDto getFormData() {
        return new PatientDto(
                txtPatientId.getText().trim(),
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtAddress.getText().trim(),
                txtTel.getText().trim(),
                dpRegDate.getValue().toString()
        );
    }

    private void clear() {
        txtPatientId.clear(); txtName.clear(); txtEmail.clear();
        txtAddress.clear(); txtTel.clear(); dpRegDate.setValue(null);
    }

    private boolean isValid() {
        String email = txtEmail.getText().trim();
        String tel   = txtTel.getText().trim();
        return email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
                && tel.matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$")
                && dpRegDate.getValue() != null;
    }

    public void emailValidation(KeyEvent e) {
        String email = txtEmail.getText();
        txtEmail.setStyle(email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
                ? "-fx-border-color: green; -fx-border-width: 2;" : "-fx-border-color: red; -fx-border-width: 2;");
    }

    public void telValidation(KeyEvent e) {
        String tel = txtTel.getText();
        txtTel.setStyle(tel.matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$")
                ? "-fx-border-color: green; -fx-border-width: 2;" : "-fx-border-color: red; -fx-border-width: 2;");
    }

}

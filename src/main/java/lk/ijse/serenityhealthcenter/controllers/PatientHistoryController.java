package lk.ijse.serenityhealthcenter.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.PatientBO;
import lk.ijse.serenityhealthcenter.bo.custom.SessionBO;
import lk.ijse.serenityhealthcenter.dto.PatientDto;
import lk.ijse.serenityhealthcenter.dto.TherapyProgramHistoryDto;

import java.sql.SQLException;
import java.util.List;

public class PatientHistoryController {
    public ComboBox<String> cmbPatientId;
    public Button btnSearch;

    public Label lblName;
    public Label lblContact;
    public Label lblEmail;
    public Label lblAddress;
    public Label lblRegDate;

    public TableView<TherapyProgramHistoryDto> tblHistory;
    public TableColumn<TherapyProgramHistoryDto, String> colProgramId;
    public TableColumn<TherapyProgramHistoryDto, String> colProgramName;
    public TableColumn<TherapyProgramHistoryDto, String> colTherapist;
    public TableColumn<TherapyProgramHistoryDto, String> colFee;
    public TableColumn<TherapyProgramHistoryDto, Double> colRemaining;
    public TableColumn<TherapyProgramHistoryDto, String> colDate;

    PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOType.PATIENT);
    SessionBO sessionBO = (SessionBO) BOFactory.getInstance().getBO(BOFactory.BOType.SESSION);

    public void initialize() throws SQLException, ClassNotFoundException {
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colRemaining.setCellValueFactory(new PropertyValueFactory<>("remainingAmount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        ObservableList<String> ids = FXCollections.observableArrayList();
        for (PatientDto p : patientBO.getAllPatients()) ids.add(p.getId());
        cmbPatientId.setItems(ids);
    }

    public void searchPatient(ActionEvent e) {
        String patientId = cmbPatientId.getValue();
        if (patientId == null) return;

        PatientDto patient = patientBO.searchPatient(patientId);
        if (patient != null) {
            lblName.setText(patient.getName());
            lblContact.setText(patient.getTel());
            lblEmail.setText(patient.getEmail());
            lblAddress.setText(patient.getAddress());
            lblRegDate.setText(patient.getRegisterDate());
        }
        List<TherapyProgramHistoryDto> history = sessionBO.getPatientTherapyHistory(patientId);
        tblHistory.setItems(FXCollections.observableArrayList(history));
    }
}

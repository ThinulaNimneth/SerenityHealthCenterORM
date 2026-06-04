package lk.ijse.serenityhealthcenter.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.*;
import lk.ijse.serenityhealthcenter.dto.*;
import lk.ijse.serenityhealthcenter.exception.PaymentException;
import lk.ijse.serenityhealthcenter.exception.SchedulingException;

import java.io.IOException;
import java.sql.SQLException;

public class TherapySessionController {
    public ComboBox<String> cmbPatientId;
    public ComboBox<String> cmbProgramId;
    public ComboBox<String> cmbTherapistId;
    public ComboBox<String> cmbPaymentType;
    public DatePicker dpSessionDate;

    public TextField txtPatientName;
    public TextField txtPatientContact;
    public TextField txtProgramName;
    public TextField txtProgramFee;
    public TextField txtAmountPaid;

    public Button btnBook;
    public Button btnReschedule;
    public Button btnCancel;
    public Button btnInvoice;

    SessionBO  sessionBO  = (SessionBO)  BOFactory.getInstance().getBO(BOFactory.BOType.SESSION);
    ProgramBO  programBO  = (ProgramBO)  BOFactory.getInstance().getBO(BOFactory.BOType.PROGRAM);
    TherapistBO therapistBO = (TherapistBO) BOFactory.getInstance().getBO(BOFactory.BOType.THERAPIST);
    PatientBO  patientBO  = (PatientBO)  BOFactory.getInstance().getBO(BOFactory.BOType.PATIENT);

    public void initialize() throws SQLException, ClassNotFoundException {
        cmbPaymentType.getItems().addAll("Full Payment", "Installment Payment");
        txtPatientName.setDisable(true);
        txtPatientContact.setDisable(true);
        txtProgramName.setDisable(true);
        txtProgramFee.setDisable(true);
        loadPatients();
        loadPrograms();
        loadTherapists();
    }

    private void loadPatients() throws SQLException, ClassNotFoundException {
        ObservableList<String> ids = FXCollections.observableArrayList();
        for (PatientDto p : patientBO.getAllPatients()) ids.add(p.getId());
        cmbPatientId.setItems(ids);
    }

    private void loadPrograms() throws SQLException, ClassNotFoundException {
        ObservableList<String> ids = FXCollections.observableArrayList();
        for (ProgramDto p : programBO.getAllPrograms()) ids.add(p.getProgramId());
        cmbProgramId.setItems(ids);
    }

    private void loadTherapists() {
        ObservableList<String> ids = FXCollections.observableArrayList();
        ids.addAll(therapistBO.getAvailableTherapistIds());
        cmbTherapistId.setItems(ids);
    }

    public void onPatientSelected(ActionEvent e) {
        String patientId = cmbPatientId.getValue();
        if (patientId == null) return;
        PatientDto dto = patientBO.searchPatient(patientId);
        if (dto != null) {
            txtPatientName.setText(dto.getName());
            txtPatientContact.setText(dto.getTel());
        }
    }

    public void onProgramSelected(ActionEvent e) throws SQLException, ClassNotFoundException {
        String programId = cmbProgramId.getValue();
        if (programId == null) return;
        ProgramDto p = programBO.searchProgram(programId);
        if (p != null) {
            txtProgramName.setText(p.getName());
            txtProgramFee.setText(p.getFee());
        }
        String therapistId = sessionBO.search(programId);
        if (therapistId != null) {
            cmbTherapistId.setValue(therapistId);
            cmbTherapistId.setDisable(true);
        } else {
            loadTherapists();
            cmbTherapistId.setDisable(false);
        }
    }

    public void bookSession(ActionEvent e) throws SQLException, ClassNotFoundException {
        try {
            if (cmbPatientId.getValue() == null || cmbProgramId.getValue() == null ||
                    cmbTherapistId.getValue() == null || dpSessionDate.getValue() == null ||
                    txtAmountPaid.getText().isEmpty() || cmbPaymentType.getValue() == null) {
                throw new SchedulingException("Please fill in all required fields before booking.");
            }
            if (!txtAmountPaid.getText().matches("\\d+(\\.\\d{1,2})?")) {
                throw new PaymentException("Invalid amount format. Please enter a valid number.");
            }

            TherapySessionDto sessionDto = new TherapySessionDto(
                    cmbPatientId.getValue(), cmbProgramId.getValue(),
                    cmbTherapistId.getValue(), dpSessionDate.getValue().toString());

            double fee   = Double.parseDouble(txtProgramFee.getText());
            double paid  = Double.parseDouble(txtAmountPaid.getText());
            double remaining = fee - paid;
            PaymentDto paymentDto = new PaymentDto(cmbPaymentType.getValue(), fee, remaining);

            if (sessionBO.addSession(sessionDto, paymentDto)) {
                new Alert(Alert.AlertType.INFORMATION, "Session booked successfully.").show();
                clear();
            }
        } catch (SchedulingException | PaymentException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    public void rescheduleSession(ActionEvent e) throws SQLException, ClassNotFoundException {
        try {
            if (cmbPatientId.getValue() == null || cmbProgramId.getValue() == null ||
                    cmbTherapistId.getValue() == null || dpSessionDate.getValue() == null) {
                throw new SchedulingException("Please fill all fields to reschedule.");
            }
            TherapySessionDto dto = new TherapySessionDto(
                    cmbPatientId.getValue(), cmbProgramId.getValue(),
                    cmbTherapistId.getValue(), dpSessionDate.getValue().toString());
            if (sessionBO.updateSession(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Session rescheduled.").show();
                clear();
            }
        } catch (SchedulingException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    public void cancelSession(ActionEvent e) throws SQLException, ClassNotFoundException {
        String patientId = cmbPatientId.getValue();
        if (patientId == null) { new Alert(Alert.AlertType.ERROR, "Select patient.").show(); return; }
        if (sessionBO.deleteSession(patientId)) {
            new Alert(Alert.AlertType.INFORMATION, "Session(s) cancelled.").show();
            clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Cancel failed.").show();
        }
    }

    public void generateInvoice(ActionEvent e) throws IOException {
        if (cmbPatientId.getValue() == null || cmbProgramId.getValue() == null ||
                dpSessionDate.getValue() == null || txtAmountPaid.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Fill all required fields first.").show();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Invoice.fxml"));
        Parent root = loader.load();
        InvoiceController ic = loader.getController();
        ic.setData(cmbPatientId.getValue(), txtPatientName.getText(), txtPatientContact.getText(),
                cmbProgramId.getValue(), txtProgramName.getText(), cmbTherapistId.getValue(),
                dpSessionDate.getValue().toString(), txtProgramFee.getText(),
                txtAmountPaid.getText(), cmbPaymentType.getValue());
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Invoice");
        stage.show();
    }

    private void clear() {
        cmbPatientId.setValue(null); cmbProgramId.setValue(null);
        cmbTherapistId.setValue(null); cmbPaymentType.setValue(null);
        dpSessionDate.setValue(null); txtPatientName.clear();
        txtPatientContact.clear(); txtProgramName.clear();
        txtProgramFee.clear(); txtAmountPaid.clear();
        cmbTherapistId.setDisable(false);
    }
}

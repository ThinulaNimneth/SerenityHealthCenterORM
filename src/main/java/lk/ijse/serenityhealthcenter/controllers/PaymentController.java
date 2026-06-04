package lk.ijse.serenityhealthcenter.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.PaymentBO;
import lk.ijse.serenityhealthcenter.bo.custom.ProgramBO;
import lk.ijse.serenityhealthcenter.bo.custom.SessionBO;
import lk.ijse.serenityhealthcenter.dto.PaymentDto;
import lk.ijse.serenityhealthcenter.dto.ProgramDto;
import lk.ijse.serenityhealthcenter.exception.PaymentException;

import java.util.List;

public class PaymentController {
    public ComboBox<String> cmbPatient;
    public ComboBox<String> cmbProgram;
    public TextField txtPaymentId;
    public TextField txtProgramName;
    public TextField txtProgramFee;
    public TextField txtRemainingAmount;
    public TextField txtPayingAmount;
    public DatePicker dpPayDate;
    public Button btnPay;
    public Button btnClear;

    SessionBO  sessionBO  = (SessionBO)  BOFactory.getInstance().getBO(BOFactory.BOType.SESSION);
    PaymentBO  paymentBO  = (PaymentBO)  BOFactory.getInstance().getBO(BOFactory.BOType.PAYMENT);
    ProgramBO  programBO  = (ProgramBO)  BOFactory.getInstance().getBO(BOFactory.BOType.PROGRAM);

    public void initialize() {
        loadPatients();
    }

    private void loadPatients() {
        List<String> ids = sessionBO.getPatientIdsFromTherapySessions();
        cmbPatient.setItems(FXCollections.observableArrayList(ids));
    }

    public void onPatientSelected(ActionEvent e) {
        String patientId = cmbPatient.getValue();
        if (patientId == null) return;
        List<String> programIds = sessionBO.getProgramIds(patientId);
        cmbProgram.setItems(FXCollections.observableArrayList(programIds));
    }

    public void onProgramSelected(ActionEvent e) {
        String programId = cmbProgram.getValue();
        String patientId = cmbPatient.getValue();
        if (programId == null || patientId == null) return;

        ProgramDto program = programBO.searchProgram(programId);
        Long sessionId = sessionDAO_sessionId(patientId, programId);
        PaymentDto payment = paymentBO.searchPayment(String.valueOf(sessionId));

        if (program != null) {
            txtProgramName.setText(program.getName());
            txtProgramFee.setText(program.getFee());
        }
        if (payment != null) {
            txtPaymentId.setText(String.valueOf(payment.getPaymentId()));
            txtRemainingAmount.setText(String.format("%.2f", payment.getRemainingAmount()));
        }
    }

    private Long sessionDAO_sessionId(String patientId, String programId) {
        return sessionBO.searchSessionId(patientId, programId);
    }

    public void payAction(ActionEvent e) {
        String paymentId  = txtPaymentId.getText().trim();
        String payingAmt  = txtPayingAmount.getText().trim();
        try {
            if (paymentId.isEmpty()) {
                throw new PaymentException("Please select a patient and program first.");
            }
            if (payingAmt.isEmpty()) {
                throw new PaymentException("Please enter the amount to pay.");
            }
            if (!payingAmt.matches("\\d+(\\.\\d{1,2})?")) {
                throw new PaymentException("Invalid amount format. Please enter a valid number.");
            }
            if (paymentBO.pay(paymentId, payingAmt)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment of LKR " + payingAmt + " processed successfully.").show();
                clearFields();
                loadPatients();
            }
        } catch (PaymentException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    public void clearFields() {
        cmbPatient.setValue(null); cmbProgram.setValue(null);
        txtPaymentId.clear(); txtProgramName.clear();
        txtProgramFee.clear(); txtRemainingAmount.clear();
        txtPayingAmount.clear();
        if (dpPayDate != null) dpPayDate.setValue(null);
    }

    public void clearAction(ActionEvent e) { clearFields(); }
}

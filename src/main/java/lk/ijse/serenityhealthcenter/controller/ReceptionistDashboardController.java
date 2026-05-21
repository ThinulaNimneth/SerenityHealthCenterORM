package lk.ijse.serenityhealthcenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.bo.custom.*;
import lk.ijse.serenityhealthcenter.bo.impl.*;
import lk.ijse.serenityhealthcenter.dto.*;
import lk.ijse.serenityhealthcenter.util.CustomExceptions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class ReceptionistDashboardController {

    // Header
    @FXML
    private Label lblUserName;
    @FXML
    private Button btnLogout;

    // Patient Registration Tab
    @FXML
    private TextField txtPatientName;
    @FXML
    private TextField txtPatientEmail;
    @FXML
    private TextField txtPatientPhone;
    @FXML
    private DatePicker dpDOB;
    @FXML
    private ComboBox<String> cmbGender;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtEmergencyContact;
    @FXML
    private TextArea txtMedicalHistory;
    @FXML
    private ComboBox<TherapyProgramDTO> cmbPrograms;
    @FXML
    private Button btnAddProgram;
    @FXML
    private ListView<String> lstEnrolledPrograms;
    @FXML
    private Button btnRegisterPatient;
    @FXML
    private Button btnClearPatient;

    // Patient Management Tab
    @FXML
    private TextField txtSearchPatient;
    @FXML
    private Button btnSearchPatient;
    @FXML
    private Button btnViewAllPatients;
    @FXML
    private TableView<PatientDTO> tblPatients;
    @FXML
    private TableColumn<PatientDTO, Long> colPatientId;
    @FXML
    private TableColumn<PatientDTO, String> colPatientName;
    @FXML
    private TableColumn<PatientDTO, String> colPatientEmail;
    @FXML
    private TableColumn<PatientDTO, String> colPatientPhone;
    @FXML
    private TableColumn<PatientDTO, LocalDate> colRegistrationDate;
    @FXML
    private Button btnViewDetails;
    @FXML
    private Button btnUpdatePatient;
    @FXML
    private Button btnDeletePatient;

    // Payment Processing Tab
    @FXML
    private ComboBox<PatientDTO> cmbPatientPayment;
    @FXML
    private TextField txtAmount;
    @FXML
    private ComboBox<String> cmbPaymentMethod;
    @FXML
    private TextField txtTransactionRef;
    @FXML
    private TextArea txtPaymentDescription;
    @FXML
    private Button btnProcessPayment;
    @FXML
    private Button btnGenerateInvoice;
    @FXML
    private TableView<PaymentDTO> tblPayments;

    // Session Scheduling Tab
    @FXML
    private ComboBox<PatientDTO> cmbPatientSession;
    @FXML
    private ComboBox<TherapistDTO> cmbTherapistSession;
    @FXML
    private ComboBox<TherapyProgramDTO> cmbProgramSession;
    @FXML
    private DatePicker dpSessionDate;
    @FXML
    private TextField txtSessionTime;
    @FXML
    private TextField txtDurationMins;
    @FXML
    private TextArea txtSessionNotes;
    @FXML
    private Button btnScheduleSession;
    @FXML
    private Button btnRescheduleSession;
    @FXML
    private Button btnCancelSession;
    @FXML
    private TableView<TherapySessionDTO> tblSessions;

    // BO Instances
    private final PatientBO patientBO = new PatientBOImpl();
    private final TherapyProgramBO programBO = new TherapyProgramBOImpl();
    private final PaymentBO paymentBO = new PaymentBOImpl();
    private final TherapySessionBO sessionBO = new TherapySessionBOImpl();
    private final TherapistBO therapistBO = new TherapistBOImpl();

    // Observable Lists
    private ObservableList<PatientDTO> patientList = FXCollections.observableArrayList();
    private ObservableList<PaymentDTO> paymentList = FXCollections.observableArrayList();
    private ObservableList<TherapySessionDTO> sessionList = FXCollections.observableArrayList();
    private ObservableList<String> enrolledProgramIds = FXCollections.observableArrayList();

    // Selected items for update
    private PatientDTO selectedPatient;
    private TherapySessionDTO selectedSession;

    @FXML
    public void initialize() {
        setupPatientTable();
        setupPaymentTable();
        setupSessionTable();
        loadComboBoxes();
        loadPatients();
        loadPayments();
        loadSessions();
        setupListeners();
    }

    private void setupPatientTable() {
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPatientEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPatientPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRegistrationDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        tblPatients.setItems(patientList);

        tblPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedPatient = newVal;
            if (newVal != null) {
                populatePatientFieldsForUpdate(newVal);
            }
        });
    }

    private void setupPaymentTable() {
        TableColumn<PaymentDTO, Long> colPaymentId = new TableColumn<>("Payment ID");
        TableColumn<PaymentDTO, String> colPatientName = new TableColumn<>("Patient");
        TableColumn<PaymentDTO, BigDecimal> colAmount = new TableColumn<>("Amount");
        TableColumn<PaymentDTO, LocalDate> colDate = new TableColumn<>("Date");
        TableColumn<PaymentDTO, String> colMethod = new TableColumn<>("Method");
        TableColumn<PaymentDTO, String> colStatus = new TableColumn<>("Status");

        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblPayments.getColumns().setAll(colPaymentId, colPatientName, colAmount, colDate, colMethod, colStatus);
        tblPayments.setItems(paymentList);
    }

    private void setupSessionTable() {
        TableColumn<TherapySessionDTO, Long> colSessionId = new TableColumn<>("Session ID");
        TableColumn<TherapySessionDTO, String> colPatient = new TableColumn<>("Patient");
        TableColumn<TherapySessionDTO, String> colTherapist = new TableColumn<>("Therapist");
        TableColumn<TherapySessionDTO, String> colProgram = new TableColumn<>("Program");
        TableColumn<TherapySessionDTO, LocalDate> colDate = new TableColumn<>("Date");
        TableColumn<TherapySessionDTO, String> colStatus = new TableColumn<>("Status");

        colSessionId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblSessions.getColumns().setAll(colSessionId, colPatient, colTherapist, colProgram, colDate, colStatus);
        tblSessions.setItems(sessionList);

        tblSessions.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedSession = newVal;
            if (newVal != null) {
                populateSessionFieldsForUpdate(newVal);
            }
        });
    }

    private void loadComboBoxes() {
        // Gender options
        cmbGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

        // Payment methods
        cmbPaymentMethod.setItems(FXCollections.observableArrayList("CASH", "CARD", "BANK_TRANSFER", "ONLINE"));

        // Therapy programs
        List<TherapyProgramDTO> programs = programBO.getActivePrograms();
        cmbPrograms.setItems(FXCollections.observableArrayList(programs));

        // Patients for payment dropdown
        List<PatientDTO> patients = patientBO.getAllPatients();
        cmbPatientPayment.setItems(FXCollections.observableArrayList(patients));

        // Patients for session dropdown
        cmbPatientSession.setItems(FXCollections.observableArrayList(patients));

        // Therapists for session dropdown
        loadTherapists();

        // Programs for session dropdown
        cmbProgramSession.setItems(FXCollections.observableArrayList(programs));
    }

    private void loadTherapists() {
        List<TherapistDTO> therapists = therapistBO.getAvailableTherapists();
        cmbTherapistSession.setItems(FXCollections.observableArrayList(therapists));
    }

    private void loadPatients() {
        patientList.setAll(patientBO.getAllPatients());
        // Refresh dropdowns
        List<PatientDTO> patients = patientBO.getAllPatients();
        cmbPatientPayment.setItems(FXCollections.observableArrayList(patients));
        cmbPatientSession.setItems(FXCollections.observableArrayList(patients));
    }

    private void loadPayments() {
        paymentList.setAll(paymentBO.getAllPayments());
    }

    private void loadSessions() {
        sessionList.setAll(sessionBO.getAllSessions());
    }

    private void setupListeners() {
        // Refresh patient dropdown when table selection changes
        cmbPatientPayment.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadPaymentsForPatient(newVal.getPatientId());
            }
        });
    }

    private void loadPaymentsForPatient(Long patientId) {
        paymentList.setAll(paymentBO.getPaymentsByPatient(patientId));
    }

    private void populatePatientFieldsForUpdate(PatientDTO patient) {
        txtPatientName.setText(patient.getName());
        txtPatientEmail.setText(patient.getEmail());
        txtPatientPhone.setText(patient.getPhone());
        dpDOB.setValue(patient.getDateOfBirth());
        cmbGender.setValue(patient.getGender());
        txtAddress.setText(patient.getAddress());
        txtMedicalHistory.setText(patient.getMedicalHistory());
        txtEmergencyContact.setText(patient.getEmergencyContact());
    }

    private void populateSessionFieldsForUpdate(TherapySessionDTO session) {
        cmbPatientSession.setValue(findPatientById(session.getPatientId()));
        cmbTherapistSession.setValue(findTherapistById(session.getTherapistId()));
        cmbProgramSession.setValue(findProgramById(session.getProgramId()));
        dpSessionDate.setValue(session.getSessionDate());
        txtSessionTime.setText(session.getSessionTime().toString());
        txtDurationMins.setText(session.getDurationMinutes().toString());
        txtSessionNotes.setText(session.getNotes());
    }

    private PatientDTO findPatientById(Long id) {
        return patientList.stream().filter(p -> p.getPatientId().equals(id)).findFirst().orElse(null);
    }

    private TherapistDTO findTherapistById(Long id) {
        return cmbTherapistSession.getItems().stream().filter(t -> t.getTherapistId().equals(id)).findFirst().orElse(null);
    }

    private TherapyProgramDTO findProgramById(String id) {
        return cmbProgramSession.getItems().stream().filter(p -> p.getProgramId().equals(id)).findFirst().orElse(null);
    }

    private void clearPatientForm() {
        txtPatientName.clear();
        txtPatientEmail.clear();
        txtPatientPhone.clear();
        dpDOB.setValue(null);
        cmbGender.setValue(null);
        txtAddress.clear();
        txtMedicalHistory.clear();
        txtEmergencyContact.clear();
        enrolledProgramIds.clear();
        lstEnrolledPrograms.setItems(null);
        selectedPatient = null;
        tblPatients.getSelectionModel().clearSelection();
    }

    private void clearSessionForm() {
        cmbPatientSession.setValue(null);
        cmbTherapistSession.setValue(null);
        cmbProgramSession.setValue(null);
        dpSessionDate.setValue(null);
        txtSessionTime.clear();
        txtDurationMins.clear();
        txtSessionNotes.clear();
        selectedSession = null;
        tblSessions.getSelectionModel().clearSelection();
    }

    private void clearPaymentForm() {
        cmbPatientPayment.setValue(null);
        txtAmount.clear();
        cmbPaymentMethod.setValue(null);
        txtTransactionRef.clear();
        txtPaymentDescription.clear();
    }

    @FXML
    private void handleLogout() {
        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setHeaderText("Confirm Logout");
            confirm.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Stage stage = (Stage) btnLogout.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle("Serenity MHTC - Login");
                stage.centerOnScreen();
            }
        } catch (Exception e) {
            showError("Logout failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddProgram() {
        TherapyProgramDTO selected = cmbPrograms.getValue();
        if (selected != null && !enrolledProgramIds.contains(selected.getProgramId())) {
            enrolledProgramIds.add(selected.getProgramId() + " - " + selected.getProgramName());
            lstEnrolledPrograms.setItems(FXCollections.observableArrayList(enrolledProgramIds));
        } else if (selected == null) {
            showError("Please select a program to add");
        } else {
            showWarning("Program already added");
        }
    }

    @FXML
    private void handleRegisterPatient() {
        try {
            // Validate required fields
            if (txtPatientName.getText().trim().isEmpty()) {
                throw new CustomExceptions.MissingFieldException("Patient Name");
            }
            if (txtPatientEmail.getText().trim().isEmpty()) {
                throw new CustomExceptions.MissingFieldException("Email");
            }

            PatientDTO dto = new PatientDTO();
            dto.setName(txtPatientName.getText().trim());
            dto.setEmail(txtPatientEmail.getText().trim());
            dto.setPhone(txtPatientPhone.getText().trim());
            dto.setDateOfBirth(dpDOB.getValue());
            dto.setGender(cmbGender.getValue());
            dto.setAddress(txtAddress.getText().trim());
            dto.setMedicalHistory(txtMedicalHistory.getText().trim());
            dto.setEmergencyContact(txtEmergencyContact.getText().trim());
            dto.setRegistrationDate(LocalDate.now());

            Long patientId = patientBO.savePatient(dto);

            // Enroll in selected programs (extract program IDs)
            for (String enrolled : enrolledProgramIds) {
                String programId = enrolled.split(" - ")[0];
                patientBO.enrollInProgram(patientId, programId);
            }

            showSuccess("Patient registered successfully! ID: " + patientId);
            clearPatientForm();
            loadPatients();

        } catch (CustomExceptions.ValidationException e) {
            showError("Validation error: " + e.getMessage());
        } catch (CustomExceptions.RegistrationException e) {
            showError("Registration error: " + e.getMessage());
        } catch (Exception e) {
            showError("Failed to register patient: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearPatient() {
        clearPatientForm();
    }

    @FXML
    private void handleSearchPatient() {
        String searchTerm = txtSearchPatient.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<PatientDTO> results = patientBO.searchPatients(searchTerm);
            patientList.setAll(results);
        } else {
            loadPatients();
        }
    }

    @FXML
    private void handleViewAllPatients() {
        loadPatients();
        txtSearchPatient.clear();
    }

    @FXML
    private void handleViewPatientDetails() {
        if (selectedPatient == null) {
            showError("Please select a patient to view details");
            return;
        }

        PatientDTO fullPatient = patientBO.getPatientWithPrograms(selectedPatient.getPatientId());
        showPatientDetailsDialog(fullPatient);
    }

    @FXML
    private void handleUpdatePatient() {
        if (selectedPatient == null) {
            showError("Please select a patient to update");
            return;
        }

        try {
            selectedPatient.setName(txtPatientName.getText().trim());
            selectedPatient.setEmail(txtPatientEmail.getText().trim());
            selectedPatient.setPhone(txtPatientPhone.getText().trim());
            selectedPatient.setDateOfBirth(dpDOB.getValue());
            selectedPatient.setGender(cmbGender.getValue());
            selectedPatient.setAddress(txtAddress.getText().trim());
            selectedPatient.setMedicalHistory(txtMedicalHistory.getText().trim());
            selectedPatient.setEmergencyContact(txtEmergencyContact.getText().trim());

            patientBO.updatePatient(selectedPatient);
            showSuccess("Patient updated successfully!");
            clearPatientForm();
            loadPatients();

        } catch (CustomExceptions.RegistrationException e) {
            showError("Update error: " + e.getMessage());
        } catch (Exception e) {
            showError("Failed to update patient: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeletePatient() {
        if (selectedPatient == null) {
            showError("Please select a patient to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Patient");
        confirm.setContentText("Are you sure you want to delete " + selectedPatient.getName() + "?\nThis will also delete all associated sessions and payments!");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            patientBO.deletePatient(selectedPatient.getPatientId());
            showSuccess("Patient deleted successfully!");
            clearPatientForm();
            loadPatients();
            loadPayments();
            loadSessions();
        }
    }

    @FXML
    private void handleProcessPayment() {
        try {
            PatientDTO selected = cmbPatientPayment.getValue();
            if (selected == null) {
                showError("Please select a patient");
                return;
            }

            String amountText = txtAmount.getText().trim();
            if (amountText.isEmpty()) {
                throw new CustomExceptions.MissingFieldException("Amount");
            }

            String paymentMethod = cmbPaymentMethod.getValue();
            if (paymentMethod == null) {
                throw new CustomExceptions.MissingFieldException("Payment Method");
            }

            PaymentDTO dto = new PaymentDTO();
            dto.setPaymentDate(LocalDate.now());
            dto.setAmount(new BigDecimal(amountText));
            dto.setPaymentMethod(paymentMethod);
            dto.setTransactionReference(txtTransactionRef.getText().trim());
            dto.setDescription(txtPaymentDescription.getText().trim());
            dto.setStatus("COMPLETED");
            dto.setPatientId(selected.getPatientId());

            Long paymentId = paymentBO.savePayment(dto);
            showSuccess("Payment processed successfully! Payment ID: " + paymentId);
            clearPaymentForm();
            loadPayments();
            if (selected != null) {
                loadPaymentsForPatient(selected.getPatientId());
            }

        } catch (CustomExceptions.MissingFieldException e) {
            showError(e.getMessage());
        } catch (NumberFormatException e) {
            showError("Invalid amount format");
        } catch (Exception e) {
            showError("Payment failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerateInvoice() {
        PaymentDTO selectedPayment = tblPayments.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            showError("Please select a payment to generate invoice");
            return;
        }

        StringBuilder invoice = new StringBuilder();
        invoice.append("=".repeat(50)).append("\n");
        invoice.append("         SERENITY MENTAL HEALTH CENTER\n");
        invoice.append("=".repeat(50)).append("\n");
        invoice.append("INVOICE #: ").append(selectedPayment.getPaymentId()).append("\n");
        invoice.append("Date: ").append(selectedPayment.getPaymentDate()).append("\n");
        invoice.append("-".repeat(50)).append("\n");
        invoice.append("Patient: ").append(selectedPayment.getPatientName()).append("\n");
        invoice.append("Amount: LKR ").append(selectedPayment.getAmount()).append("\n");
        invoice.append("Method: ").append(selectedPayment.getPaymentMethod()).append("\n");
        invoice.append("Reference: ").append(selectedPayment.getTransactionReference()).append("\n");
        invoice.append("Status: ").append(selectedPayment.getStatus()).append("\n");
        invoice.append("-".repeat(50)).append("\n");
        invoice.append("Thank you for choosing Serenity!\n");
        invoice.append("=".repeat(50));

        Alert invoiceDialog = new Alert(Alert.AlertType.INFORMATION);
        invoiceDialog.setTitle("Invoice");
        invoiceDialog.setHeaderText("Payment Receipt");
        invoiceDialog.setContentText(invoice.toString());
        invoiceDialog.getDialogPane().setPrefWidth(500);
        invoiceDialog.showAndWait();
    }

    @FXML
    private void handleScheduleSession() {
        try {
            PatientDTO patient = cmbPatientSession.getValue();
            TherapistDTO therapist = cmbTherapistSession.getValue();
            TherapyProgramDTO program = cmbProgramSession.getValue();

            if (patient == null) {
                throw new CustomExceptions.MissingFieldException("Patient");
            }
            if (therapist == null) {
                throw new CustomExceptions.MissingFieldException("Therapist");
            }
            if (program == null) {
                throw new CustomExceptions.MissingFieldException("Program");
            }
            if (dpSessionDate.getValue() == null) {
                throw new CustomExceptions.MissingFieldException("Session Date");
            }
            if (txtSessionTime.getText().trim().isEmpty()) {
                throw new CustomExceptions.MissingFieldException("Session Time");
            }
            if (txtDurationMins.getText().trim().isEmpty()) {
                throw new CustomExceptions.MissingFieldException("Duration");
            }

            TherapySessionDTO dto = new TherapySessionDTO();
            dto.setPatientId(patient.getPatientId());
            dto.setPatientName(patient.getName());
            dto.setTherapistId(therapist.getTherapistId());
            dto.setTherapistName(therapist.getName());
            dto.setProgramId(program.getProgramId());
            dto.setProgramName(program.getProgramName());
            dto.setSessionDate(dpSessionDate.getValue());

            // Parse time
            try {
                LocalTime time = LocalTime.parse(txtSessionTime.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
                dto.setSessionTime(time);
            } catch (DateTimeParseException e) {
                showError("Invalid time format. Please use HH:MM (e.g., 14:30)");
                return;
            }

            dto.setDurationMinutes(Integer.parseInt(txtDurationMins.getText().trim()));
            dto.setStatus("SCHEDULED");
            dto.setNotes(txtSessionNotes.getText().trim());

            Long sessionId = sessionBO.saveSession(dto);
            showSuccess("Session scheduled successfully! Session ID: " + sessionId);
            clearSessionForm();
            loadSessions();
            loadTherapists(); // Refresh therapist availability

        } catch (CustomExceptions.MissingFieldException e) {
            showError(e.getMessage());
        } catch (NumberFormatException e) {
            showError("Invalid duration. Please enter a number.");
        } catch (Exception e) {
            showError("Failed to schedule session: " + e.getMessage());
        }
    }

    @FXML
    private void handleRescheduleSession() {
        if (selectedSession == null) {
            showError("Please select a session to reschedule");
            return;
        }

        try {
            if (dpSessionDate.getValue() == null) {
                throw new CustomExceptions.MissingFieldException("New Session Date");
            }
            if (txtSessionTime.getText().trim().isEmpty()) {
                throw new CustomExceptions.MissingFieldException("New Session Time");
            }

            selectedSession.setSessionDate(dpSessionDate.getValue());
            selectedSession.setSessionTime(LocalTime.parse(txtSessionTime.getText().trim(), DateTimeFormatter.ofPattern("HH:mm")));
            selectedSession.setStatus("RESCHEDULED");
            selectedSession.setNotes(txtSessionNotes.getText().trim());

            sessionBO.updateSession(selectedSession);
            showSuccess("Session rescheduled successfully!");
            clearSessionForm();
            loadSessions();

        } catch (DateTimeParseException e) {
            showError("Invalid time format. Please use HH:MM (e.g., 14:30)");
        } catch (CustomExceptions.MissingFieldException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Failed to reschedule session: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelSession() {
        if (selectedSession == null) {
            showError("Please select a session to cancel");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancel");
        confirm.setHeaderText("Cancel Session");
        confirm.setContentText("Are you sure you want to cancel this session?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedSession.setStatus("CANCELLED");
            sessionBO.updateSession(selectedSession);
            showSuccess("Session cancelled successfully!");
            clearSessionForm();
            loadSessions();
            loadTherapists(); // Refresh therapist availability
        }
    }

    private void showPatientDetailsDialog(PatientDTO patient) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Patient Details");
        dialog.setHeaderText(patient.getName());

        StringBuilder content = new StringBuilder();
        content.append("ID: ").append(patient.getPatientId()).append("\n");
        content.append("Email: ").append(patient.getEmail()).append("\n");
        content.append("Phone: ").append(patient.getPhone()).append("\n");
        content.append("Gender: ").append(patient.getGender()).append("\n");
        content.append("DOB: ").append(patient.getDateOfBirth()).append("\n");
        content.append("Address: ").append(patient.getAddress()).append("\n");
        content.append("Registered: ").append(patient.getRegistrationDate()).append("\n");
        content.append("Emergency Contact: ").append(patient.getEmergencyContact()).append("\n");
        content.append("\n--- Medical History ---\n");
        content.append(patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "None").append("\n");
        content.append("\n--- Enrolled Programs ---\n");

        if (patient.getEnrolledProgramIds() != null && !patient.getEnrolledProgramIds().isEmpty()) {
            for (String programId : patient.getEnrolledProgramIds()) {
                content.append("  • ").append(programId).append("\n");
            }
        } else {
            content.append("  None\n");
        }

        dialog.setContentText(content.toString());
        dialog.getDialogPane().setPrefWidth(450);
        dialog.showAndWait();
    }

    public void setUserName(String userName) {
        if (lblUserName != null) {
            lblUserName.setText(userName);
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
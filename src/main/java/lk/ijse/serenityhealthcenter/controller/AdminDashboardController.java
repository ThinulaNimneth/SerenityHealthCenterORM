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
import lk.ijse.serenityhealthcenter.bo.custom.TherapistBO;
import lk.ijse.serenityhealthcenter.bo.custom.TherapyProgramBO;
import lk.ijse.serenityhealthcenter.bo.impl.TherapistBOImpl;
import lk.ijse.serenityhealthcenter.bo.impl.TherapyProgramBOImpl;
import lk.ijse.serenityhealthcenter.dto.TherapistDTO;
import lk.ijse.serenityhealthcenter.dto.TherapyProgramDTO;
import lk.ijse.serenityhealthcenter.dto.UserDTO;

import java.math.BigDecimal;
import java.util.Optional;

public class AdminDashboardController {

    // Header
    @FXML private Label lblUserName;
    @FXML private Button btnLogout;

    // Therapist Management Tab
    @FXML private TextField txtTherapistName, txtTherapistEmail, txtTherapistPhone;
    @FXML private TextField txtSpecialization, txtQualification, txtLicenseNumber, txtYearsExp;
    @FXML private CheckBox chkAvailable;
    @FXML private TableView<TherapistDTO> tblTherapists;
    @FXML private TableColumn<TherapistDTO, Long> colTherapistId;
    @FXML private TableColumn<TherapistDTO, String> colTherapistName;
    @FXML private TableColumn<TherapistDTO, String> colTherapistEmail;
    @FXML private TableColumn<TherapistDTO, String> colTherapistPhone;
    @FXML private TableColumn<TherapistDTO, String> colSpecialization;
    @FXML private TableColumn<TherapistDTO, Boolean> colAvailable;
    @FXML private Button btnSaveTherapist, btnUpdateTherapist, btnDeleteTherapist, btnClearTherapist;

    // Program Management Tab
    @FXML private TextField txtProgramId, txtProgramName, txtDuration, txtFee;
    @FXML private TextArea txtDescription;
    @FXML private TableView<TherapyProgramDTO> tblPrograms;
    @FXML private TableColumn<TherapyProgramDTO, String> colProgramId;
    @FXML private TableColumn<TherapyProgramDTO, String> colProgramName;
    @FXML private TableColumn<TherapyProgramDTO, String> colDuration;
    @FXML private TableColumn<TherapyProgramDTO, BigDecimal> colFee;
    @FXML private Button btnSaveProgram, btnUpdateProgram, btnDeleteProgram, btnClearProgram;

    // User Management fields
    @FXML private TextField txtUsername, txtFullName, txtUserEmail;
    @FXML private PasswordField txtNewPassword;
    @FXML private ComboBox<String> cmbRole;
    @FXML private TableView<UserDTO> tblUsers;

    private final TherapistBO therapistBO = new TherapistBOImpl();
    private final TherapyProgramBO programBO = new TherapyProgramBOImpl();

    private final ObservableList<TherapistDTO> therapistList = FXCollections.observableArrayList();
    private final ObservableList<TherapyProgramDTO> programList = FXCollections.observableArrayList();

    private TherapistDTO selectedTherapist;
    private TherapyProgramDTO selectedProgram;

    @FXML
    public void initialize() {
        System.out.println("AdminDashboardController initializing...");
        setupTherapistTable();
        setupProgramTable();
        loadTherapists();
        loadPrograms();

        // Setup role combo box
        if (cmbRole != null) {
            cmbRole.setItems(FXCollections.observableArrayList("ADMIN", "RECEPTIONIST"));
        }
        System.out.println("AdminDashboardController initialized successfully");
    }

    private void setupTherapistTable() {
        try {
            colTherapistId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
            colTherapistName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colTherapistEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colTherapistPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
            colAvailable.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

            tblTherapists.setItems(therapistList);

            tblTherapists.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                selectedTherapist = newVal;
                if (newVal != null) {
                    populateTherapistFields(newVal);
                }
            });
            System.out.println("Therapist table setup complete");
        } catch (Exception e) {
            System.err.println("Error setting up therapist table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupProgramTable() {
        try {
            colProgramId.setCellValueFactory(new PropertyValueFactory<>("programId"));
            colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
            colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
            colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

            tblPrograms.setItems(programList);

            tblPrograms.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                selectedProgram = newVal;
                if (newVal != null) {
                    populateProgramFields(newVal);
                }
            });
            System.out.println("Program table setup complete");
        } catch (Exception e) {
            System.err.println("Error setting up program table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTherapists() {
        try {
            therapistList.clear();
            therapistList.addAll(therapistBO.getAllTherapists());
            System.out.println("Loaded " + therapistList.size() + " therapists");
            tblTherapists.refresh();
        } catch (Exception e) {
            System.err.println("Error loading therapists: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPrograms() {
        try {
            programList.clear();
            programList.addAll(programBO.getAllPrograms());
            System.out.println("Loaded " + programList.size() + " programs");
            tblPrograms.refresh();
        } catch (Exception e) {
            System.err.println("Error loading programs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateTherapistFields(TherapistDTO dto) {
        txtTherapistName.setText(dto.getName() != null ? dto.getName() : "");
        txtTherapistEmail.setText(dto.getEmail() != null ? dto.getEmail() : "");
        txtTherapistPhone.setText(dto.getPhone() != null ? dto.getPhone() : "");
        txtSpecialization.setText(dto.getSpecialization() != null ? dto.getSpecialization() : "");
        txtQualification.setText(dto.getQualification() != null ? dto.getQualification() : "");
        txtLicenseNumber.setText(dto.getLicenseNumber() != null ? dto.getLicenseNumber() : "");
        txtYearsExp.setText(dto.getYearsOfExperience() != null ? dto.getYearsOfExperience().toString() : "");
        chkAvailable.setSelected(Boolean.TRUE.equals(dto.getIsAvailable()));
    }

    private void populateProgramFields(TherapyProgramDTO dto) {
        txtProgramId.setText(dto.getProgramId() != null ? dto.getProgramId() : "");
        txtProgramName.setText(dto.getProgramName() != null ? dto.getProgramName() : "");
        txtDuration.setText(dto.getDuration() != null ? dto.getDuration() : "");
        txtFee.setText(dto.getFee() != null ? dto.getFee().toString() : "");
        txtDescription.setText(dto.getDescription() != null ? dto.getDescription() : "");
    }

    @FXML
    void handleSaveTherapist(ActionEvent event) {
        try {
            if (txtTherapistName.getText().trim().isEmpty()) {
                showError("Therapist Name is required");
                return;
            }
            if (txtTherapistEmail.getText().trim().isEmpty()) {
                showError("Email is required");
                return;
            }

            TherapistDTO dto = new TherapistDTO();
            dto.setName(txtTherapistName.getText().trim());
            dto.setEmail(txtTherapistEmail.getText().trim());
            dto.setPhone(txtTherapistPhone.getText().trim());
            dto.setSpecialization(txtSpecialization.getText().trim());
            dto.setQualification(txtQualification.getText().trim());
            dto.setLicenseNumber(txtLicenseNumber.getText().trim());

            String yearsExp = txtYearsExp.getText().trim();
            dto.setYearsOfExperience(yearsExp.isEmpty() ? 0 : Integer.parseInt(yearsExp));
            dto.setIsAvailable(chkAvailable.isSelected());

            Long id = therapistBO.saveTherapist(dto);
            showSuccess("Therapist saved successfully! ID: " + id);
            loadTherapists();
            handleClearTherapist(null);
        } catch (NumberFormatException e) {
            showError("Invalid years of experience");
        } catch (Exception e) {
            showError("Failed to save therapist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateTherapist(ActionEvent event) {
        if (selectedTherapist == null) {
            showError("Please select a therapist to update");
            return;
        }
        try {
            selectedTherapist.setName(txtTherapistName.getText().trim());
            selectedTherapist.setEmail(txtTherapistEmail.getText().trim());
            selectedTherapist.setPhone(txtTherapistPhone.getText().trim());
            selectedTherapist.setSpecialization(txtSpecialization.getText().trim());
            selectedTherapist.setQualification(txtQualification.getText().trim());
            selectedTherapist.setLicenseNumber(txtLicenseNumber.getText().trim());

            String yearsExp = txtYearsExp.getText().trim();
            selectedTherapist.setYearsOfExperience(yearsExp.isEmpty() ? 0 : Integer.parseInt(yearsExp));
            selectedTherapist.setIsAvailable(chkAvailable.isSelected());

            therapistBO.updateTherapist(selectedTherapist);
            showSuccess("Therapist updated successfully!");
            loadTherapists();
            handleClearTherapist(null);
        } catch (Exception e) {
            showError("Failed to update therapist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteTherapist(ActionEvent event) {
        if (selectedTherapist == null) {
            showError("Please select a therapist to delete");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete " + selectedTherapist.getName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            therapistBO.deleteTherapist(selectedTherapist.getTherapistId());
            showSuccess("Therapist deleted successfully!");
            loadTherapists();
            handleClearTherapist(null);
        }
    }

    @FXML
    void handleClearTherapist(ActionEvent event) {
        txtTherapistName.clear();
        txtTherapistEmail.clear();
        txtTherapistPhone.clear();
        txtSpecialization.clear();
        txtQualification.clear();
        txtLicenseNumber.clear();
        txtYearsExp.clear();
        chkAvailable.setSelected(true);
        selectedTherapist = null;
        tblTherapists.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSaveProgram(ActionEvent event) {
        try {
            if (txtProgramId.getText().trim().isEmpty()) {
                showError("Program ID is required");
                return;
            }
            if (txtProgramName.getText().trim().isEmpty()) {
                showError("Program Name is required");
                return;
            }
            if (txtFee.getText().trim().isEmpty()) {
                showError("Fee is required");
                return;
            }

            TherapyProgramDTO dto = new TherapyProgramDTO();
            dto.setProgramId(txtProgramId.getText().trim().toUpperCase());
            dto.setProgramName(txtProgramName.getText().trim());
            dto.setDuration(txtDuration.getText().trim());
            dto.setFee(new BigDecimal(txtFee.getText().trim()));
            dto.setDescription(txtDescription.getText().trim());
            dto.setIsActive(true);

            String id = programBO.saveProgram(dto);
            showSuccess("Program saved successfully! ID: " + id);
            loadPrograms();
            handleClearProgram(null);
        } catch (NumberFormatException e) {
            showError("Invalid fee amount");
        } catch (Exception e) {
            showError("Failed to save program: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateProgram(ActionEvent event) {
        if (selectedProgram == null) {
            showError("Please select a program to update");
            return;
        }
        try {
            selectedProgram.setProgramName(txtProgramName.getText().trim());
            selectedProgram.setDuration(txtDuration.getText().trim());
            selectedProgram.setFee(new BigDecimal(txtFee.getText().trim()));
            selectedProgram.setDescription(txtDescription.getText().trim());

            programBO.updateProgram(selectedProgram);
            showSuccess("Program updated successfully!");
            loadPrograms();
            handleClearProgram(null);
        } catch (Exception e) {
            showError("Failed to update program: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteProgram(ActionEvent event) {
        if (selectedProgram == null) {
            showError("Please select a program to delete");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + selectedProgram.getProgramName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            programBO.deleteProgram(selectedProgram.getProgramId());
            showSuccess("Program deleted successfully!");
            loadPrograms();
            handleClearProgram(null);
        }
    }

    @FXML
    void handleClearProgram(ActionEvent event) {
        txtProgramId.clear();
        txtProgramName.clear();
        txtDuration.clear();
        txtFee.clear();
        txtDescription.clear();
        selectedProgram = null;
        tblPrograms.getSelectionModel().clearSelection();
    }

    @FXML
    void handleAddUser(ActionEvent event) {
        showInfo("User Management", "Full user management will be implemented soon.");
    }

    @FXML
    void handleChangePassword(ActionEvent event) {
        showInfo("Change Password", "Password change functionality coming soon.");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Stage stage = (Stage) btnLogout.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.centerOnScreen();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Logout failed: " + e.getMessage());
        }
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

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
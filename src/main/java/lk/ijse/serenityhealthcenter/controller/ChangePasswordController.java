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

import java.math.BigDecimal;
import java.util.Optional;
public class ChangePasswordController {

    // User Info
    @FXML
    private Label lblUserName;
    @FXML private Button btnLogout;

    // Therapist Management
    @FXML private TextField txtTherapistName, txtTherapistEmail, txtTherapistPhone;
    @FXML private TextField txtSpecialization, txtQualification, txtLicenseNumber, txtYearsExp;
    @FXML private CheckBox chkAvailable;
    @FXML private TableView<TherapistDTO> tblTherapists;
    @FXML private TableColumn<TherapistDTO, Long> colTherapistId;
    @FXML private TableColumn<TherapistDTO, String> colTherapistName, colTherapistEmail, colTherapistPhone;
    @FXML private TableColumn<TherapistDTO, String> colSpecialization;
    @FXML private TableColumn<TherapistDTO, Boolean> colAvailable;

    // Program Management
    @FXML private TextField txtProgramId, txtProgramName, txtDuration, txtFee;
    @FXML private TextArea txtDescription;
    @FXML private TableView<TherapyProgramDTO> tblPrograms;
    @FXML private TableColumn<TherapyProgramDTO, String> colProgramId, colProgramName, colDuration;
    @FXML private TableColumn<TherapyProgramDTO, BigDecimal> colFee;

    // User Management
    @FXML private TextField txtUsername, txtFullName, txtUserEmail;
    @FXML private PasswordField txtNewPassword;
    @FXML private ComboBox<String> cmbRole;
    @FXML private TableView tblUsers;

    private final TherapistBO therapistBO = new TherapistBOImpl();
    private final TherapyProgramBO programBO = new TherapyProgramBOImpl();

    private TherapistDTO selectedTherapist;
    private TherapyProgramDTO selectedProgram;

    @FXML
    public void initialize() {
        setupTherapistTable();
        setupProgramTable();
        loadTherapists();
        loadPrograms();

        // Setup role combo box
        if (cmbRole != null) {
            cmbRole.setItems(FXCollections.observableArrayList("ADMIN", "RECEPTIONIST"));
        }
    }

    private void setupTherapistTable() {
        colTherapistId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colTherapistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTherapistEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTherapistPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

        tblTherapists.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTherapist = newVal;
                populateTherapistFields(newVal);
            }
        });
    }

    private void setupProgramTable() {
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

        tblPrograms.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedProgram = newVal;
                populateProgramFields(newVal);
            }
        });
    }

    private void loadTherapists() {
        ObservableList<TherapistDTO> therapists = FXCollections.observableArrayList(therapistBO.getAllTherapists());
        tblTherapists.setItems(therapists);
    }

    private void loadPrograms() {
        ObservableList<TherapyProgramDTO> programs = FXCollections.observableArrayList(programBO.getAllPrograms());
        tblPrograms.setItems(programs);
    }

    private void populateTherapistFields(TherapistDTO dto) {
        txtTherapistName.setText(dto.getName());
        txtTherapistEmail.setText(dto.getEmail());
        txtTherapistPhone.setText(dto.getPhone());
        txtSpecialization.setText(dto.getSpecialization());
        txtQualification.setText(dto.getQualification());
        txtLicenseNumber.setText(dto.getLicenseNumber());
        txtYearsExp.setText(dto.getYearsOfExperience() != null ? dto.getYearsOfExperience().toString() : "");
        chkAvailable.setSelected(dto.getIsAvailable());
    }

    private void populateProgramFields(TherapyProgramDTO dto) {
        txtProgramId.setText(dto.getProgramId());
        txtProgramName.setText(dto.getProgramName());
        txtDuration.setText(dto.getDuration());
        txtFee.setText(dto.getFee().toString());
        txtDescription.setText(dto.getDescription());
    }

    @FXML
    void handleSaveTherapist(ActionEvent event) {
        try {
            TherapistDTO dto = new TherapistDTO();
            dto.setName(txtTherapistName.getText());
            dto.setEmail(txtTherapistEmail.getText());
            dto.setPhone(txtTherapistPhone.getText());
            dto.setSpecialization(txtSpecialization.getText());
            dto.setQualification(txtQualification.getText());
            dto.setLicenseNumber(txtLicenseNumber.getText());
            dto.setYearsOfExperience(txtYearsExp.getText().isEmpty() ? 0 : Integer.parseInt(txtYearsExp.getText()));
            dto.setIsAvailable(chkAvailable.isSelected());

            Long id = therapistBO.saveTherapist(dto);
            showSuccess("Therapist saved successfully! ID: " + id);
            loadTherapists();
            handleClearTherapist(null);
        } catch (Exception e) {
            showError("Failed to save therapist: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateTherapist(ActionEvent event) {
        if (selectedTherapist == null) {
            showError("Please select a therapist to update");
            return;
        }
        try {
            selectedTherapist.setName(txtTherapistName.getText());
            selectedTherapist.setEmail(txtTherapistEmail.getText());
            selectedTherapist.setPhone(txtTherapistPhone.getText());
            selectedTherapist.setSpecialization(txtSpecialization.getText());
            selectedTherapist.setQualification(txtQualification.getText());
            selectedTherapist.setLicenseNumber(txtLicenseNumber.getText());
            selectedTherapist.setYearsOfExperience(Integer.parseInt(txtYearsExp.getText()));
            selectedTherapist.setIsAvailable(chkAvailable.isSelected());

            therapistBO.updateTherapist(selectedTherapist);
            showSuccess("Therapist updated successfully!");
            loadTherapists();
            handleClearTherapist(null);
        } catch (Exception e) {
            showError("Failed to update therapist: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteTherapist(ActionEvent event) {
        if (selectedTherapist == null) {
            showError("Please select a therapist to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Therapist");
        confirm.setContentText("Are you sure you want to delete " + selectedTherapist.getName() + "?");

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
            TherapyProgramDTO dto = new TherapyProgramDTO();
            dto.setProgramId(txtProgramId.getText());
            dto.setProgramName(txtProgramName.getText());
            dto.setDuration(txtDuration.getText());
            dto.setFee(new BigDecimal(txtFee.getText()));
            dto.setDescription(txtDescription.getText());
            dto.setIsActive(true);

            String id = programBO.saveProgram(dto);
            showSuccess("Program saved successfully! ID: " + id);
            loadPrograms();
            handleClearProgram(null);
        } catch (Exception e) {
            showError("Failed to save program: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateProgram(ActionEvent event) {
        if (selectedProgram == null) {
            showError("Please select a program to update");
            return;
        }
        try {
            selectedProgram.setProgramName(txtProgramName.getText());
            selectedProgram.setDuration(txtDuration.getText());
            selectedProgram.setFee(new BigDecimal(txtFee.getText()));
            selectedProgram.setDescription(txtDescription.getText());

            programBO.updateProgram(selectedProgram);
            showSuccess("Program updated successfully!");
            loadPrograms();
            handleClearProgram(null);
        } catch (Exception e) {
            showError("Failed to update program: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteProgram(ActionEvent event) {
        if (selectedProgram == null) {
            showError("Please select a program to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Program");
        confirm.setContentText("Are you sure you want to delete " + selectedProgram.getProgramName() + "?");

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
        showInfo("User Management", "User add functionality - Connect to UserBO.saveUser()");
    }

    @FXML
    void handleChangePassword(ActionEvent event) {
        showInfo("Change Password", "Password change functionality - Connect to UserBO.changePassword()");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserName(String userName) {
        lblUserName.setText(userName);
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

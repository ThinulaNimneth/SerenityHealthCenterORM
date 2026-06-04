package lk.ijse.serenityhealthcenter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.dto.UserDto;

import java.io.IOException;

public class DashboardController {
    public Label lblUsername;
    public Label lblRole;
    public StackPane contentArea;

    public Button btnPatient;
    public Button btnTherapist;
    public Button btnProgram;
    public Button btnSession;
    public Button btnPayment;
    public Button btnUser;
    public Button btnReport;
    public Button btnLogout;

    private String currentRole;

    public void initialize() {
        UserDto user = LoginFormController.getLoggedInUser();
        if (user != null) {
            lblUsername.setText(user.getUsername());
            lblRole.setText(user.getRole().toUpperCase());
            currentRole = user.getRole();
            applyRoleVisibility(currentRole);
        }
    }

    private void applyRoleVisibility(String role) {
        boolean isAdmin = role.equalsIgnoreCase("admin");
        // Hide admin-only buttons from sidebar for receptionist
        btnUser.setVisible(isAdmin);
        btnUser.setManaged(isAdmin);
        btnTherapist.setVisible(isAdmin);
        btnTherapist.setManaged(isAdmin);
        btnProgram.setVisible(isAdmin);
        btnProgram.setManaged(isAdmin);
    }

    /** Guard: returns true if user has required role, shows error if not */
    private boolean hasRole(String required) {
        if (required.equalsIgnoreCase(currentRole)) return true;
        if (required.equalsIgnoreCase("admin") && !currentRole.equalsIgnoreCase("admin")) {
            new Alert(Alert.AlertType.ERROR,
                    "Access Denied: This section is restricted to Admin users only.").show();
            return false;
        }
        return true;
    }

    public void handlePatient(ActionEvent e) throws IOException {
        if (hasRole("receptionist") || hasRole("admin")) loadView("/view/PatientManagement.fxml");
    }

    public void handleTherapist(ActionEvent e) throws IOException {
        if (!hasRole("admin")) return;
        loadView("/view/TherapistManagement.fxml");
    }

    public void handleProgram(ActionEvent e) throws IOException {
        if (!hasRole("admin")) return;
        loadView("/view/TherapyProgram.fxml");
    }

    public void handleSession(ActionEvent e) throws IOException {
        loadView("/view/TherapySession.fxml");
    }

    public void handlePayment(ActionEvent e) throws IOException {
        loadView("/view/PaymentManagement.fxml");
    }

    public void handleUser(ActionEvent e) throws IOException {
        if (!hasRole("admin")) return;
        loadView("/view/UserManagement.fxml");
    }

    public void handleReport(ActionEvent e) throws IOException {
        loadView("/view/Report.fxml");
    }

    private void loadView(String fxmlPath) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
        contentArea.getChildren().setAll(pane);
    }

    public void handleLogout(ActionEvent e) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Serenity - Login");
        stage.centerOnScreen();
    }
}

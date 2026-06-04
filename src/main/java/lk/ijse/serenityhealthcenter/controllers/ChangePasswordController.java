package lk.ijse.serenityhealthcenter.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.UserBO;
import lk.ijse.serenityhealthcenter.dto.UserDto;
import lk.ijse.serenityhealthcenter.exception.RegistrationException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.SQLException;

public class ChangePasswordController {
    public TextField txtCurrentUsername;
    public TextField txtNewUsername;
    public PasswordField txtCurrentPassword;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;
    public Button btnChangeUsername;
    public Button btnChangePassword;
    public Button btnClear;
    public Button btnClose;
    public Label lblLoggedInAs;

    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    public void initialize() {
        UserDto loggedIn = LoginFormController.getLoggedInUser();
        if (loggedIn != null) {
            lblLoggedInAs.setText("Logged in as: " + loggedIn.getUsername() + " (" + loggedIn.getRole() + ")");
            txtCurrentUsername.setText(loggedIn.getUsername());
        }
    }

    /** Change username for the currently logged-in user */
    public void changeUsername(ActionEvent e) throws SQLException, ClassNotFoundException {
        String currentUsername = txtCurrentUsername.getText().trim();
        String newUsername     = txtNewUsername.getText().trim();
        try {
            if (newUsername.isEmpty()) {
                throw new RegistrationException("New username cannot be empty.");
            }
            if (!newUsername.matches("[a-zA-Z0-9]{4,}")) {
                throw new RegistrationException("Username must be at least 4 alphanumeric characters.");
            }
            UserDto user = userBO.getData(currentUsername);
            if (user == null) {
                throw new RegistrationException("Current user not found.");
            }
            // Check new username not already taken
            UserDto existing = userBO.getData(newUsername);
            if (existing != null && existing.getId() != user.getId()) {
                throw new RegistrationException("Username '" + newUsername + "' is already taken.");
            }
            user.setUsername(newUsername);
            if (userBO.updateUser(user)) {
                // Update the logged-in session
                LoginFormController.loggedInUser = userBO.getData(newUsername);
                lblLoggedInAs.setText("Logged in as: " + newUsername + " (" + user.getRole() + ")");
                txtCurrentUsername.setText(newUsername);
                new Alert(Alert.AlertType.INFORMATION, "Username changed to '" + newUsername + "' successfully.").show();
                txtNewUsername.clear();
            }
        } catch (RegistrationException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    /** Change password for the currently logged-in user */
    public void changePassword(ActionEvent e) throws SQLException, ClassNotFoundException {
        String currentUsername = txtCurrentUsername.getText().trim();
        String currentPwd = txtCurrentPassword.getText();
        String newPwd     = txtNewPassword.getText();
        String confirmPwd = txtConfirmPassword.getText();
        try {
            if (currentPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
                throw new RegistrationException("Please fill in all password fields.");
            }
            UserDto user = userBO.getData(currentUsername);
            if (user == null) {
                throw new RegistrationException("Current user not found.");
            }
            if (!BCrypt.checkpw(currentPwd, user.getPassword())) {
                throw new RegistrationException("Current password is incorrect.");
            }
            if (!newPwd.equals(confirmPwd)) {
                throw new RegistrationException("New passwords do not match.");
            }
            if (!newPwd.matches("[a-zA-Z0-9]{6,}")) {
                throw new RegistrationException("New password must be at least 6 alphanumeric characters.");
            }
            user.setPassword(BCrypt.hashpw(newPwd, BCrypt.gensalt()));
            if (userBO.updateUser(user)) {
                // Refresh session
                LoginFormController.loggedInUser = userBO.getData(currentUsername);
                new Alert(Alert.AlertType.INFORMATION, "Password changed successfully.").show();
                clearPasswordFields();
            }
        } catch (RegistrationException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    public void clearFields(ActionEvent e) {
        txtNewUsername.clear();
        clearPasswordFields();
    }

    private void clearPasswordFields() {
        txtCurrentPassword.clear();
        txtNewPassword.clear();
        txtConfirmPassword.clear();
    }

    public void closeWindow(ActionEvent e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}

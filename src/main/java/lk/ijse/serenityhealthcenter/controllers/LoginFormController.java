package lk.ijse.serenityhealthcenter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.UserBO;
import lk.ijse.serenityhealthcenter.dao.DAOFactory;
import lk.ijse.serenityhealthcenter.dao.custom.UserDAO;
import lk.ijse.serenityhealthcenter.dto.UserDto;
import lk.ijse.serenityhealthcenter.exception.LoginException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;

public class LoginFormController {
    public TextField txtUsername;
    public PasswordField txtPassword;
    public TextField txtPasswordVisible;
    public Button btnTogglePassword;
    public Button btnLogin;
    public Hyperlink registerLink;

    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);
    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    static UserDto loggedInUser;

    public void initialize() {
        txtPassword.textProperty().addListener((obs, old, nv) -> txtPasswordVisible.setText(nv));
        txtPasswordVisible.textProperty().addListener((obs, old, nv) -> txtPassword.setText(nv));
    }

    public void loginOnAction(ActionEvent e) throws IOException {
        String username = txtUsername.getText().trim();
        String password = txtPassword.isVisible() ? txtPassword.getText() : txtPasswordVisible.getText();
        try {
            if (username.isEmpty() || password.isEmpty()) {
                throw new LoginException("Username and password are required. Please fill in both fields.");
            }
            UserDto user = userBO.loginUser(username);
            if (user == null) {
                throw new LoginException("Invalid credentials: User '" + username + "' not found.");
            }
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new LoginException("Invalid credentials: Incorrect password.");
            }
            loggedInUser = user;
            loadDashboard(e);
        } catch (LoginException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    private void loadDashboard(ActionEvent e) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Serenity - Dashboard");
        stage.centerOnScreen();
    }

    public void registrationOnAction(ActionEvent e) throws IOException {
        if (userDAO.ifHaveAdmin()) {
            new Alert(Alert.AlertType.ERROR, "Admin already exists. Contact administrator to add more users.").show();
            return;
        }
        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/Registration.fxml"));
        Stage stage = (Stage) registerLink.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Register Admin");
        stage.centerOnScreen();
    }

    public void togglePasswordVisibility(ActionEvent e) {
        if (txtPassword.isVisible()) {
            txtPasswordVisible.setText(txtPassword.getText());
            txtPassword.setVisible(false);
            txtPasswordVisible.setVisible(true);
            btnTogglePassword.setText("👀");
        } else {
            txtPassword.setText(txtPasswordVisible.getText());
            txtPasswordVisible.setVisible(false);
            txtPassword.setVisible(true);
            btnTogglePassword.setText("🙈");
        }
    }

    public static UserDto getLoggedInUser() {
        return loggedInUser;
    }
}

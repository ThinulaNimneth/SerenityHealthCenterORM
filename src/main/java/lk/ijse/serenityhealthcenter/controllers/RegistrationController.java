package lk.ijse.serenityhealthcenter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.UserBO;
import lk.ijse.serenityhealthcenter.dto.UserDto;
import lk.ijse.serenityhealthcenter.exception.RegistrationException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;

public class RegistrationController {
    public TextField txtUsername;
    public TextField txtEmail;
    public PasswordField txtPassword;
    public Button btnRegister;
    public Button btnBack;

    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    public void registerOnAction(ActionEvent e) throws IOException {
        String username = txtUsername.getText().trim();
        String email    = txtEmail.getText().trim();
        String password = txtPassword.getText();
        try {
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                throw new RegistrationException("All fields are required. Please fill in username, email and password.");
            }
            if (!email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
                throw new RegistrationException("Invalid email format. Please enter a valid email address.");
            }
            if (!password.matches("[a-zA-Z0-9]{6,}")) {
                throw new RegistrationException("Password must be at least 6 alphanumeric characters.");
            }
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            UserDto dto = new UserDto(username, email, hashed, "admin");
            if (userBO.registerUser(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Admin registered successfully. Please login.").show();
                backOnAction(e);
            } else {
                throw new RegistrationException("Registration failed. Please try again.");
            }
        } catch (RegistrationException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    public void backOnAction(ActionEvent e) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        Stage stage = (Stage) btnRegister.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Serenity - Login");
        stage.centerOnScreen();
    }
}

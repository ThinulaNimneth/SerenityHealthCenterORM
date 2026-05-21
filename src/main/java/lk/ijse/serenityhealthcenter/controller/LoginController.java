package lk.ijse.serenityhealthcenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.bo.custom.UserBO;
import lk.ijse.serenityhealthcenter.bo.impl.UserBOImpl;
import lk.ijse.serenityhealthcenter.dto.UserDTO;
import lk.ijse.serenityhealthcenter.util.CustomExceptions;

import java.io.IOException;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private CheckBox chkShowPassword;
    @FXML private Button btnLogin;
    @FXML private Label lblMessage;

    private final UserBO userBO = new UserBOImpl();


    @FXML
    public void initialize(){
        txtPasswordVisible = new TextField();
        txtPasswordVisible.setManaged(false);
        txtPasswordVisible.setVisible(false);

        if (chkShowPassword != null){
            chkShowPassword.setOnAction(event -> {
                if (chkShowPassword.isSelected()){
                    txtPasswordVisible.setText(txtPassword.getText());
                    txtPasswordVisible.setVisible(true);
                    txtPassword.setVisible(false);
                }else {
                    txtPassword.setText(txtPasswordVisible.getText());
                    txtPassword.setVisible(true);
                    txtPasswordVisible.setVisible(false);
                }
            });
        }
        //enter
        if (txtPassword != null){
            txtPassword.setOnAction(this::handleLogin);
        }
    }


    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = chkShowPassword != null && chkShowPassword.isSelected()
                ? txtPasswordVisible.getText()
                : txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }

        try {
            UserDTO user = userBO.login(username, password);

            if (user != null) {
                showSuccess("Login successful!");
                // Navigate to dashboard based on role
                loadDashboard(user);
            }
        } catch (CustomExceptions.InvalidCredentialsException e) {
            showError("Invalid username or password");
        } catch (CustomExceptions.AccountLockedException e) {
            showError("Account is locked. Contact administrator.");
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void loadDashboard(UserDTO user) {
        try {
            Stage stage = (Stage) btnLogin.getScene().getWindow();

            if (user.getRole().equals("ADMIN")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
                Scene scene = new Scene(loader.load());
                AdminDashboardController controller = loader.getController();
                controller.setUserName(user.getFullName());
                stage.setScene(scene);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReceptionistDashboard.fxml"));
                Scene scene = new Scene(loader.load());
                ReceptionistDashboardController controller = loader.getController();
                controller.setUserName(user.getFullName());
                stage.setScene(scene);
            }

            stage.setTitle("Serenity MHTC - " + user.getRole() + " Dashboard");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load dashboard: " + e.getMessage());
        }
    }

    private void showError(String message) {
        if (lblMessage != null) {
            lblMessage.setText(message);
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }


    private void showSuccess(String message) {
        if (lblMessage != null) {
            lblMessage.setText(message);
            lblMessage.setStyle("-fx-text-fill: green;");
        }
    }







}

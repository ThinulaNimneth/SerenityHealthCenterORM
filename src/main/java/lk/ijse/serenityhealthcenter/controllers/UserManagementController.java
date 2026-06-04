package lk.ijse.serenityhealthcenter.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.UserBO;
import lk.ijse.serenityhealthcenter.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserManagementController {
    public TextField txtUserId;
    public TextField txtUsername;
    public TextField txtEmail;
    public TextField txtPassword;
    public ComboBox<String> cmbRole;

    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnSearch;

    public TableView<UserDto> tblUsers;
    public TableColumn<UserDto, Long> colId;
    public TableColumn<UserDto, String> colUsername;
    public TableColumn<UserDto, String> colEmail;
    public TableColumn<UserDto, String> colRole;

    public Hyperlink lnkChangePassword;

    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    public void initialize() throws SQLException, ClassNotFoundException {
        cmbRole.setItems(FXCollections.observableArrayList("admin", "receptionist"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        loadAll();
    }

    private void loadAll() throws SQLException, ClassNotFoundException {
        List<UserDto> users = userBO.getAllUsers();
        ObservableList<UserDto> list = FXCollections.observableArrayList(users);
        tblUsers.setItems(list);
    }

    public void addUser(ActionEvent e) throws SQLException, ClassNotFoundException {
        if (!isValid()) { new Alert(Alert.AlertType.ERROR, "Please enter valid data.").show(); return; }
        String hashed = BCrypt.hashpw(txtPassword.getText(), BCrypt.gensalt());
        UserDto dto = new UserDto(txtUsername.getText().trim(), txtEmail.getText().trim(), hashed, cmbRole.getValue());
        if (userBO.addUser(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "User added.").show();
            loadAll(); clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to add user.").show();
        }
    }

    public void updateUser(ActionEvent e) throws SQLException, ClassNotFoundException {
        String id = txtUserId.getText().trim();
        if (id.isEmpty()) { new Alert(Alert.AlertType.ERROR, "Select a user first.").show(); return; }
        UserDto dto = new UserDto(Long.parseLong(id), txtUsername.getText().trim(),
                txtEmail.getText().trim(), txtPassword.getText(), cmbRole.getValue());
        if (userBO.updateUser(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "User updated.").show();
            loadAll(); clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Update failed.").show();
        }
    }

    public void deleteUser(ActionEvent e) throws SQLException, ClassNotFoundException {
        String id = txtUserId.getText().trim();
        if (id.isEmpty()) { new Alert(Alert.AlertType.ERROR, "Enter User ID.").show(); return; }
        if (userBO.deleteUser(id)) {
            new Alert(Alert.AlertType.INFORMATION, "User deleted.").show();
            loadAll(); clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Delete failed.").show();
        }
    }

    public void searchUser(ActionEvent e) {
        String id = txtUserId.getText().trim();
        UserDto dto = userBO.searchUser(id);
        if (dto != null) {
            txtUsername.setText(dto.getUsername());
            txtEmail.setText(dto.getEmail());
            cmbRole.setValue(dto.getRole());
        } else {
            new Alert(Alert.AlertType.ERROR, "User not found.").show();
        }
    }

    public void onTableClicked(MouseEvent e) {
        UserDto dto = tblUsers.getSelectionModel().getSelectedItem();
        if (dto != null) {
            txtUserId.setText(String.valueOf(dto.getId()));
            txtUsername.setText(dto.getUsername());
            txtEmail.setText(dto.getEmail());
            cmbRole.setValue(dto.getRole());
        }
    }

    public void openChangePassword(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ChangePassword.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Change Password");
        stage.show();
    }

    private void clear() {
        txtUserId.clear(); txtUsername.clear(); txtEmail.clear();
        txtPassword.clear(); cmbRole.setValue(null);
    }

    private boolean isValid() {
        return txtUsername.getText().matches("[a-zA-Z0-9]{4,}") &&
                txtEmail.getText().matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$") &&
                txtPassword.getText().matches("[a-zA-Z0-9]{6,}") &&
                cmbRole.getValue() != null;
    }

    public void usernameValidation(KeyEvent e) {
        txtUsername.setStyle(txtUsername.getText().matches("[a-zA-Z0-9]{4,}")
                ? "-fx-border-color: green; -fx-border-width: 2;" : "-fx-border-color: red; -fx-border-width: 2;");
    }
    public void emailValidation(KeyEvent e) {
        txtEmail.setStyle(txtEmail.getText().matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
                ? "-fx-border-color: green; -fx-border-width: 2;" : "-fx-border-color: red; -fx-border-width: 2;");
    }
    public void passwordValidation(KeyEvent e) {
        txtPassword.setStyle(txtPassword.getText().matches("[a-zA-Z0-9]{6,}")
                ? "-fx-border-color: green; -fx-border-width: 2;" : "-fx-border-color: red; -fx-border-width: 2;");
    }
}

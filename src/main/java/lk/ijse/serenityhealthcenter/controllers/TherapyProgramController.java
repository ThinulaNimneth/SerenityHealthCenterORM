package lk.ijse.serenityhealthcenter.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.ProgramBO;
import lk.ijse.serenityhealthcenter.dto.ProgramDto;

import java.sql.SQLException;

public class TherapyProgramController {
    public TextField txtProgramId;
    public TextField txtName;
    public TextField txtDuration;
    public TextField txtFee;

    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;

    public TableView<ProgramDto> tblPrograms;
    public TableColumn<ProgramDto, String> colId;
    public TableColumn<ProgramDto, String> colName;
    public TableColumn<ProgramDto, String> colDuration;
    public TableColumn<ProgramDto, String> colFee;

    ProgramBO programBO = (ProgramBO) BOFactory.getInstance().getBO(BOFactory.BOType.PROGRAM);

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        loadAll();
    }

    private void loadAll() throws SQLException, ClassNotFoundException {
        tblPrograms.setItems(programBO.getAllPrograms());
    }

    public void addProgram(ActionEvent e) throws SQLException, ClassNotFoundException {
        ProgramDto dto = getFormData();
        if (programBO.addProgram(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "Program added.").show();
            loadAll(); clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to add program.").show();
        }
    }

    public void updateProgram(ActionEvent e) throws SQLException, ClassNotFoundException {
        ProgramDto dto = getFormData();
        if (programBO.updateProgram(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "Program updated.").show();
            loadAll(); clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Update failed.").show();
        }
    }

    public void deleteProgram(ActionEvent e) throws SQLException, ClassNotFoundException {
        String id = txtProgramId.getText().trim();
        if (id.isEmpty()) { new Alert(Alert.AlertType.ERROR, "Enter Program ID.").show(); return; }
        if (programBO.deleteProgram(id)) {
            new Alert(Alert.AlertType.INFORMATION, "Program deleted.").show();
            loadAll(); clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Delete failed.").show();
        }
    }

    public void onTableClicked(MouseEvent e) {
        ProgramDto dto = tblPrograms.getSelectionModel().getSelectedItem();
        if (dto != null) {
            txtProgramId.setText(dto.getProgramId());
            txtName.setText(dto.getName());
            txtDuration.setText(dto.getDuration());
            txtFee.setText(dto.getFee());
        }
    }

    private ProgramDto getFormData() {
        return new ProgramDto(txtProgramId.getText().trim(), txtName.getText().trim(),
                txtDuration.getText().trim(), txtFee.getText().trim());
    }

    private void clear() {
        txtProgramId.clear(); txtName.clear(); txtDuration.clear(); txtFee.clear();
    }
}

package lk.ijse.serenityhealthcenter.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenityhealthcenter.bo.BOFactory;
import lk.ijse.serenityhealthcenter.bo.custom.PatientBO;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dto.PatientDto;
import lk.ijse.serenityhealthcenter.entity.Patient;
import org.hibernate.Session;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportController {
    public ComboBox<String> cmbReportType;
    public Button btnGenerate;
    public TableView tblData;

    PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOType.PATIENT);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbReportType.setItems(FXCollections.observableArrayList(
                "All Patients",
                "Patients Enrolled in ALL Programs (HQL - Part A.2)",
                "Patients with Enrolled Programs (HQL - Part A.4)"));
        cmbReportType.getSelectionModel().selectFirst();
        cmbReportType.setOnAction(ev -> setupColumns());
        setupColumns();
    }

    private void setupColumns() {
        tblData.getColumns().clear();
        String type = cmbReportType.getValue();
        if (type == null) return;
        switch (type) {
            case "All Patients" -> tblData.getColumns().addAll(
                    col("Patient ID", "id"), col("Name", "name"),
                    col("Email", "email"), col("Tel", "tel"), col("Reg. Date", "registerDate"));
            case "Patients Enrolled in ALL Programs (HQL - Part A.2)" -> tblData.getColumns().addAll(
                    col("Patient ID", "id"), col("Name", "name"),
                    col("Email", "email"), col("Tel", "tel"), col("Reg. Date", "registerDate"));
            case "Patients with Enrolled Programs (HQL - Part A.4)" -> tblData.getColumns().addAll(
                    col("Patient + Program", "name"));
        }
    }

    private TableColumn col(String title, String field) {
        TableColumn c = new TableColumn(title);
        c.setCellValueFactory(new PropertyValueFactory<>(field));
        c.setPrefWidth(160);
        return c;
    }

    @SuppressWarnings("unchecked")
    public void generateReport(ActionEvent e) {
        tblData.getItems().clear();
        String type = cmbReportType.getValue();
        if (type == null) return;

        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            switch (type) {
                case "All Patients" -> {
                    List<Patient> patients = session.createQuery("FROM Patient ORDER BY name", Patient.class).list();
                    List<PatientDto> dtos = new ArrayList<>();
                    for (Patient p : patients) {
                        dtos.add(new PatientDto(p.getId(), p.getName(), p.getEmail(),
                                p.getAddress(), p.getTel(), p.getRegisterDate()));
                    }
                    tblData.setItems(FXCollections.observableArrayList(dtos));
                }
                case "Patients Enrolled in ALL Programs (HQL - Part A.2)" -> {
                    // Part A.2 — HQL double NOT EXISTS: patients enrolled in EVERY available program
                    String hql =
                            "FROM Patient p " +
                                    "WHERE NOT EXISTS (" +
                                    "    FROM Program prog " +
                                    "    WHERE NOT EXISTS (" +
                                    "        FROM TherapySession ts " +
                                    "        WHERE ts.patient = p AND ts.program = prog" +
                                    "    )" +
                                    ")";
                    List<Patient> patients = session.createQuery(hql, Patient.class).list();
                    List<PatientDto> dtos = new ArrayList<>();
                    for (Patient p : patients) {
                        dtos.add(new PatientDto(p.getId(), p.getName(), p.getEmail(),
                                p.getAddress(), p.getTel(), p.getRegisterDate()));
                    }
                    tblData.setItems(FXCollections.observableArrayList(dtos));
                    if (dtos.isEmpty()) {
                        new Alert(Alert.AlertType.INFORMATION,
                                "No patient is currently enrolled in ALL therapy programs.").show();
                    }
                }
                case "Patients with Enrolled Programs (HQL - Part A.4)" -> {
                    // Part A.4 — HQL JOIN: patients with their therapy programs
                    String hql =
                            "SELECT p.name || ' [' || p.id || ']  →  ' || prog.name || ' (' || prog.programId || ')' " +
                                    "FROM Patient p " +
                                    "JOIN TherapySession ts ON ts.patient = p " +
                                    "JOIN ts.program prog " +
                                    "ORDER BY p.name, prog.programId";

                    // Use a simple DTO wrapper for display
                    List<String> rows = session.createQuery(hql, String.class).list();
                    List<PatientDto> display = new ArrayList<>();
                    for (String row : rows) {
                        PatientDto d = new PatientDto();
                        d.setName(row);
                        display.add(d);
                    }
                    tblData.setItems(FXCollections.observableArrayList(display));
                }
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Report error: " + ex.getMessage()).show();
            ex.printStackTrace();
        }
    }
}

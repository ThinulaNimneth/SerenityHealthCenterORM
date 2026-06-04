package lk.ijse.serenityhealthcenter.controllers;


import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvoiceController {
    public Label lblInvoiceNumber;
    public Label lblDate;
    public Label lblPatientId;
    public Label lblPatientName;
    public Label lblContact;
    public Label lblProgramId;
    public Label lblProgramName;
    public Label lblTherapistId;
    public Label lblSessionDate;
    public Label lblPaymentType;
    public Label lblProgramFee;
    public Label lblAmountPaid;
    public Label lblBalance;
    public Button btnPrint;
    public VBox invoiceRoot;

    public void initialize() {
        lblInvoiceNumber.setText("INV-" + System.currentTimeMillis());
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public void setData(String patientId, String patientName, String contact,
                        String programId, String programName, String therapistId,
                        String sessionDate, String programFee, String amountPaid, String paymentType) {
        lblPatientId.setText(patientId);
        lblPatientName.setText(patientName);
        lblContact.setText(contact);
        lblProgramId.setText(programId);
        lblProgramName.setText(programName);
        lblTherapistId.setText(therapistId);
        lblSessionDate.setText(sessionDate);
        lblPaymentType.setText(paymentType);

        double fee     = Double.parseDouble(programFee);
        double paid    = Double.parseDouble(amountPaid);
        double balance = fee - paid;

        lblProgramFee.setText(String.format("LKR %.2f", fee));
        lblAmountPaid.setText(String.format("LKR %.2f", paid));
        lblBalance.setText(String.format("LKR %.2f", balance));
    }

    public void printInvoice(ActionEvent e) {
        //  generate and print invoices
        // javafx.print module path after Maven reload
        try {
            Class<?> printerJobClass = Class.forName("javafx.print.PrinterJob");
            Object job = printerJobClass.getMethod("createPrinterJob").invoke(null);
            if (job != null) {
                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                boolean show = (boolean) printerJobClass
                        .getMethod("showPrintDialog", javafx.stage.Window.class)
                        .invoke(job, window);
                if (show) {
                    boolean printed = (boolean) printerJobClass
                            .getMethod("printPage", javafx.scene.Node.class)
                            .invoke(job, invoiceRoot);
                    if (printed) printerJobClass.getMethod("endJob").invoke(job);
                }
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.INFORMATION,
                    "Invoice is displayed above. Add javafx-print to module path to enable printing.").show();
        }
    }
}

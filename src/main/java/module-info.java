module lk.ijse.serenityhealthcenter {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires jakarta.persistence;
    requires static lombok;
    requires java.naming;


    opens lk.ijse.serenityhealthcenter.entity to org.hibernate.orm.core;
    opens lk.ijse.serenityhealthcenter.controller to javafx.fxml;
    opens lk.ijse.serenityhealthcenter to javafx.fxml;

    exports lk.ijse.serenityhealthcenter;
}
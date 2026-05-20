module lk.ijse.serenityhealthcenter {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires jakarta.persistence;
    requires static lombok;
    requires java.naming;


    opens lk.ijse.serenityhealthcenter to javafx.fxml;
    exports lk.ijse.serenityhealthcenter;
}
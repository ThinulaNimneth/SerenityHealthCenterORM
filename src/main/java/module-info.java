module lk.ijse.serenityhealthcenter {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires spring.security.crypto;

    opens lk.ijse.serenityhealthcenter to javafx.fxml;
    exports lk.ijse.serenityhealthcenter;

    exports lk.ijse.serenityhealthcenter.controllers;
    opens lk.ijse.serenityhealthcenter.controllers to javafx.fxml;

    exports lk.ijse.serenityhealthcenter.config;
    opens lk.ijse.serenityhealthcenter.config to javafx.fxml;

    exports lk.ijse.serenityhealthcenter.entity;
    opens lk.ijse.serenityhealthcenter.entity to org.hibernate.orm.core;

    exports lk.ijse.serenityhealthcenter.dto;
    opens lk.ijse.serenityhealthcenter.dto to org.hibernate.orm.core;

    exports lk.ijse.serenityhealthcenter.bo.custom;
    opens lk.ijse.serenityhealthcenter.bo.custom to javafx.fxml;

    exports lk.ijse.serenityhealthcenter.dao.custom;
    opens lk.ijse.serenityhealthcenter.dao.custom to javafx.fxml;

    exports lk.ijse.serenityhealthcenter.exception;
    opens lk.ijse.serenityhealthcenter.exception to javafx.fxml;
}

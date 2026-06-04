package lk.ijse.serenityhealthcenter.config;

import lk.ijse.serenityhealthcenter.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() {
        Configuration configuration = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Patient.class)
                .addAnnotatedClass(Program.class)
                .addAnnotatedClass(Therapist.class)
                .addAnnotatedClass(TherapistProgram.class)
                .addAnnotatedClass(TherapySession.class)
                .addAnnotatedClass(Payment.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        return (factoryConfiguration == null)
                ? factoryConfiguration = new FactoryConfiguration()
                : factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
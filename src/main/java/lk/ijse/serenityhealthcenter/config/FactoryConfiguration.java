package lk.ijse.serenityhealthcenter.config;

import lk.ijse.serenityhealthcenter.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() {
        Configuration configuration = new Configuration();

        // Load properties
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("hibernate.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find hibernate.properties");
            }
            properties.load(input);
            configuration.setProperties(properties);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load hibernate.properties", e);
        }

        //  entity
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Therapist.class);
        configuration.addAnnotatedClass(TherapyProgram.class);
        configuration.addAnnotatedClass(TherapySession.class);
        configuration.addAnnotatedClass(Payment.class);

        // SessionFactory
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static FactoryConfiguration getInstance() {
        return (factoryConfiguration == null)
                ? factoryConfiguration = new FactoryConfiguration()
                : factoryConfiguration;
    }

    public SessionFactory getSession() {
        return sessionFactory;
    }
}
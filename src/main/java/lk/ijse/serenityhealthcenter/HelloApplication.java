package lk.ijse.serenityhealthcenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.UserDAO;
import lk.ijse.serenityhealthcenter.dao.custom.TherapyProgramDAO;
import lk.ijse.serenityhealthcenter.dao.impl.UserDAOImpl;
import lk.ijse.serenityhealthcenter.dao.impl.TherapyProgramDAOImpl;
import lk.ijse.serenityhealthcenter.entity.User;
import lk.ijse.serenityhealthcenter.entity.TherapyProgram;
import lk.ijse.serenityhealthcenter.util.PasswordUtil;

import java.io.IOException;
import java.math.BigDecimal;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize Hibernate SessionFactory
        FactoryConfiguration.getInstance().getSession();

        // Initialize default data (users and programs)
        initializeDefaultData();

        // Load Login screen
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Serenity Mental Health Therapy Center");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    //Initialize default admin user
    private void initializeDefaultData() {
        try {
            UserDAO userDAO = new UserDAOImpl();
            TherapyProgramDAO programDAO = new TherapyProgramDAOImpl();

            // Create default admin if not exists
            if (userDAO.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(PasswordUtil.hashPassword("admin123"));
                admin.setRole(User.UserRole.ADMIN);
                admin.setEmail("admin@serenity.lk");
                admin.setFullName("System Administrator");
                admin.setIsActive(true);
                userDAO.save(admin);
                System.out.println("✅ Default admin created: username=admin, password=admin123");
            }

            // Create default receptionist if not exists
            if (userDAO.findByUsername("receptionist").isEmpty()) {
                User receptionist = new User();
                receptionist.setUsername("receptionist");
                receptionist.setPassword(PasswordUtil.hashPassword("recep123"));
                receptionist.setRole(User.UserRole.RECEPTIONIST);
                receptionist.setEmail("receptionist@serenity.lk");
                receptionist.setFullName("Front Desk Receptionist");
                receptionist.setIsActive(true);
                userDAO.save(receptionist);
                System.out.println("✅ Default receptionist created: username=receptionist, password=recep123");
            }

            // Create therapy programs as per coursework specification
            createProgramIfNotExists(programDAO, "MT1001", "Cognitive Behavioral Therapy",
                    "12 weeks", new BigDecimal("80000.00"),
                    "Evidence-based psychotherapy for various mental health conditions");

            createProgramIfNotExists(programDAO, "MT1002", "Mindfulness-Based Stress Reduction",
                    "8 weeks", new BigDecimal("50000.00"),
                    "Mindfulness meditation techniques for stress management");

            createProgramIfNotExists(programDAO, "MT1003", "Dialectical Behavior Therapy",
                    "16 weeks", new BigDecimal("100000.00"),
                    "Comprehensive treatment for emotional regulation");

            createProgramIfNotExists(programDAO, "MT1004", "Group Therapy Sessions",
                    "6 months", new BigDecimal("120000.00"),
                    "Peer support and group counseling");

            createProgramIfNotExists(programDAO, "MT1005", "Family Counseling",
                    "3 months", new BigDecimal("40000.00"),
                    "Family-based therapeutic interventions");

        } catch (Exception e) {
            System.err.println("❌ Error initializing default data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createProgramIfNotExists(TherapyProgramDAO dao, String id, String name,
                                          String duration, BigDecimal fee, String description) {
        try {
            if (dao.findById(id).isEmpty()) {
                TherapyProgram program = new TherapyProgram();
                program.setProgramId(id);
                program.setProgramName(name);
                program.setDuration(duration);
                program.setFee(fee);
                program.setDescription(description);
                program.setIsActive(true);
                dao.save(program);
                System.out.println("✅ Therapy program created: " + id + " - " + name);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Program " + id + " may already exist: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
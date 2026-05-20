package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Long save(User user);
    void update(User user);
    void delete(Long id);
    Optional<User> findById(Long id);
    List<User> findAll();
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(User.UserRole role);
}

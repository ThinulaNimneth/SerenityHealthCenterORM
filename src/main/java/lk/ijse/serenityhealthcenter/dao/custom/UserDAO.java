package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.dao.CrudDAO;
import lk.ijse.serenityhealthcenter.entity.User;

public interface UserDAO extends CrudDAO<User> {
    boolean registerUser(User user);
    User loginUser(String username);
    boolean ifHaveAdmin();
    User search(String id);

}
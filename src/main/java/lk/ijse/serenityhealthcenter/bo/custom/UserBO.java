package lk.ijse.serenityhealthcenter.bo.custom;

import lk.ijse.serenityhealthcenter.dto.UserDTO;
import lk.ijse.serenityhealthcenter.util.CustomExceptions;

import java.util.List;

public interface UserBO {
    Long saveUser(UserDTO userDTO) throws CustomExceptions.RegistrationException;
    void updateUser(UserDTO userDTO) throws CustomExceptions.RegistrationException;
    void deleteUser(Long id);
    UserDTO getUser(Long id);
    List<UserDTO> getAllUsers();
    UserDTO login(String username, String password) throws CustomExceptions.LoginException;
    void changePassword(Long userId, String oldPassword, String newPassword) throws CustomExceptions.LoginException;
}

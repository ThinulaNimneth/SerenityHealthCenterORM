package lk.ijse.serenityhealthcenter.bo.impl;

import lk.ijse.serenityhealthcenter.bo.custom.UserBO;
import lk.ijse.serenityhealthcenter.dao.custom.UserDAO;
import lk.ijse.serenityhealthcenter.dao.impl.UserDAOImpl;
import lk.ijse.serenityhealthcenter.dto.UserDTO;
import lk.ijse.serenityhealthcenter.entity.User;
import lk.ijse.serenityhealthcenter.util.CustomExceptions;
import lk.ijse.serenityhealthcenter.util.PasswordUtil;
import lk.ijse.serenityhealthcenter.util.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserBOImpl implements UserBO {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    public Long saveUser(UserDTO userDTO) throws CustomExceptions.RegistrationException, CustomExceptions.ValidationException {
        // Validation
        if (!ValidationUtil.isNotEmpty(userDTO.getUsername())) {
            throw new CustomExceptions.MissingFieldException("Username");
        }
        if (!ValidationUtil.isValidUsername(userDTO.getUsername())) {
            throw new CustomExceptions.ValidationException("Username", "Must be 3-20 alphanumeric characters");
        }
        if (!ValidationUtil.isValidEmail(userDTO.getEmail())) {
            throw new CustomExceptions.ValidationException("Email", "Invalid email format");
        }
        if (!ValidationUtil.isValidPassword(userDTO.getPassword())) {
            throw new CustomExceptions.ValidationException("Password", "Must be at least 8 characters with letters and numbers");
        }

        // Check duplicates
        if (userDAO.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new CustomExceptions.DuplicateEntryException("Username", userDTO.getUsername());
        }
        if (userDAO.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new CustomExceptions.DuplicateEntryException("Email", userDTO.getEmail());
        }

        // Hash password and save
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
        user.setRole(User.UserRole.valueOf(userDTO.getRole()));
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setIsActive(true);

        return userDAO.save(user);
    }

    @Override
    public void updateUser(UserDTO userDTO) throws CustomExceptions.RegistrationException {
        Optional<User> existingUser = userDAO.findById(userDTO.getUserId());
        if (existingUser.isEmpty()) {
            throw new CustomExceptions.RegistrationException("User not found");
        }

        User user = existingUser.get();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
        }

        userDAO.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        userDAO.delete(id);
    }

    @Override
    public UserDTO getUser(Long id) {
        Optional<User> user = userDAO.findById(id);
        return user.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO login(String username, String password) throws CustomExceptions.LoginException {
        Optional<User> userOpt = userDAO.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new CustomExceptions.InvalidCredentialsException();
        }

        User user = userOpt.get();

        if (!user.getIsActive()) {
            throw new CustomExceptions.AccountLockedException();
        }

        if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
            throw new CustomExceptions.InvalidCredentialsException();
        }

        return convertToDTO(user);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) throws CustomExceptions.LoginException {
        Optional<User> userOpt = userDAO.findById(userId);

        if (userOpt.isEmpty()) {
            throw new CustomExceptions.LoginException("User not found");
        }

        User user = userOpt.get();

        if (!PasswordUtil.verifyPassword(oldPassword, user.getPassword())) {
            throw new CustomExceptions.InvalidCredentialsException();
        }

        user.setPassword(PasswordUtil.hashPassword(newPassword));
        userDAO.update(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}
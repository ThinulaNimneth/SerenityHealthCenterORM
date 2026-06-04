package lk.ijse.serenityhealthcenter.bo.custom.impl;

import lk.ijse.serenityhealthcenter.bo.custom.UserBO;
import lk.ijse.serenityhealthcenter.dao.DAOFactory;
import lk.ijse.serenityhealthcenter.dao.custom.UserDAO;
import lk.ijse.serenityhealthcenter.dto.UserDto;
import lk.ijse.serenityhealthcenter.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBOImpl implements UserBO {
    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public boolean registerUser(UserDto userDto) {
        return userDAO.registerUser(new User(userDto.getUsername(), userDto.getEmail(),
                userDto.getPassword(), userDto.getRole()));
    }

    @Override
    public UserDto loginUser(String username) {
        User user = userDAO.loginUser(username);
        if (user == null) return null;
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRole());
    }

    @Override
    public List<UserDto> getAllUsers() throws SQLException, ClassNotFoundException {
        List<User> users = userDAO.getAll();
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getRole()));
        }
        return dtos;
    }

    @Override
    public boolean addUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        return userDAO.save(new User(userDto.getUsername(), userDto.getEmail(),
                userDto.getPassword(), userDto.getRole()));
    }

    @Override
    public boolean updateUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        return userDAO.update(new User(userDto.getId(), userDto.getUsername(),
                userDto.getEmail(), userDto.getPassword(), userDto.getRole()));
    }

    @Override
    public boolean deleteUser(String id) throws SQLException, ClassNotFoundException {
        return userDAO.delete(id);
    }

    @Override
    public UserDto searchUser(String id) {
        User user = userDAO.search(id);
        if (user == null) return null;
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public UserDto getData(String username) {
        User user = userDAO.loginUser(username);
        if (user == null) return null;
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRole());
    }
}
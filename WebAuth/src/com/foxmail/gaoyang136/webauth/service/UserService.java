package com.foxmail.gaoyang136.webauth.service;

import java.sql.SQLException;
import java.util.List;

import com.foxmail.gaoyang136.webauth.entity.User;

public interface UserService {

	public boolean registerUser(User user) throws SQLException;

	public User getUserbyName(String userName) throws SQLException;

	public List<User> getUsersbyRoleName(String roleName, int offset, int rows) throws SQLException;
}

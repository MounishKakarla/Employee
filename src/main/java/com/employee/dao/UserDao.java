package com.employee.dao;

import java.util.Set;

import com.employee.exception.DataAccessException;
import com.employee.security.Role;

public interface UserDao {

    void createUser(String username, Set<Role> roles) throws DataAccessException;

    void assignRole(String username, Set<Role> roles) throws DataAccessException;

    void resetPassword(String username) throws DataAccessException;

    void changePassword(String username, String newPassword) throws DataAccessException;
}

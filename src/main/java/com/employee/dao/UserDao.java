package com.employee.dao;

import java.util.Set;

import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.model.User;
import com.employee.security.Role;

public interface UserDao {

    void createUser(String username,String id, Set<Role> roles) throws DataAccessException,DuplicateUserException;

    void assignRole(String username, Set<Role> roles) throws DataAccessException;

    void resetPassword(String username) throws DataAccessException;

    void changePassword(String username, String newPassword) throws DataAccessException;
    User authenticate(String username, String password)
            throws DataAccessException;


}

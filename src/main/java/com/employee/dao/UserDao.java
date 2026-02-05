package com.employee.dao;

import java.sql.Connection;
import java.util.Set;

import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.UserNotFoundException;
import com.employee.model.User;
import com.employee.security.Role;

public interface UserDao {

    void createUser(String username,String id, Set<Role> roles) throws DataAccessException,DuplicateUserException;

    void assignRole(String username, Set<Role> roles) throws DataAccessException,UserNotFoundException;

    void resetPassword(String username) throws DataAccessException, UserNotFoundException;

    void changePassword(String username, String newPassword) throws DataAccessException,UserNotFoundException;
    User authenticate(String username, String password)
            throws DataAccessException;
   
    void softDeleteByEmployeeId(String empId)
            throws DataAccessException, Exception;
    void softDeleteByEmployeeId(Connection con,String empId)
            throws DataAccessException, Exception;

}

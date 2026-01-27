package com.employee.service;

import java.util.HashSet;
import java.util.Set;

import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.UserNotFoundException;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.Role;

public class UserService extends BaseService {

    private final UserDao userDao;

    public UserService(User user, UserDao userDao) {
        super(user, null);
        this.userDao = userDao;
    }

    /* ----------------- CREATE USER --------------------*/

    public void createUser() {

        if (!hasAccess(Permission.CREATE_USER))
            return;

        try {
            System.out.print("Username: ");
            String username = sc.next();

            System.out.print("Employee ID (EMPxxx): ");
            String id = sc.next();

            System.out.print("Assign roles (comma separated): ADMIN,MANAGER,EMPLOYEE: ");
            String input = sc.next();

            Set<Role> roles = new HashSet<>();
            for (String role : input.split(",")) {
                roles.add(Role.valueOf(role.trim().toUpperCase()));
            }

            userDao.createUser(username, id, roles);
            System.out.println("User created successfully");

        } catch (DuplicateUserException e) {
            System.out.println("User already exists");
        } catch (DataAccessException e) {
            System.out.println("User creation failed");
        }
    }

    /*----------------------- ASSIGN ROLE ------------------------- */

    public void assignRole() {

        if (!hasAccess(Permission.ASSIGN_ROLE))
            return;

        try {
            System.out.print("Username: ");
            String username = sc.next();

            System.out.print("New roles (comma separated): ");
            String input = sc.next();

            Set<Role> roles = new HashSet<>();
            for (String role : input.split(",")) {
                roles.add(Role.valueOf(role.trim().toUpperCase()));
            }

            userDao.assignRole(username, roles);
            System.out.println("Roles updated successfully");

        } catch (UserNotFoundException e) {
            System.out.println("User not found");
        } catch (DataAccessException e) {
            System.out.println("Role assignment failed");
        }
    }

    /* ----------------- RESET PASSWORD -------------------*/

    public void resetPassword() {

        if (!hasAccess(Permission.RESET_PASSWORD))
            return;

        try {
            System.out.print("Username: ");
            String username = sc.next();

            userDao.resetPassword(username);
            System.out.println("Password reset successfully");

        } catch (UserNotFoundException e) {
            System.out.println("User not found");
        } catch (DataAccessException e) {
            System.out.println("Password reset failed");
        }
    }
}

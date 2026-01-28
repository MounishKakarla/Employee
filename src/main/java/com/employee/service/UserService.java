package com.employee.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.UserNotFoundException;
import com.employee.logging.AuditLogger;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.Role;

public class UserService extends BaseService {

    private static final Logger log =
            LogManager.getLogger(UserService.class);

    private final UserDao userDao;

    public UserService(User user, UserDao userDao) {
        super(user, null);
        this.userDao = userDao;
    }

   

    public void createUser() {

        if (!hasAccess(Permission.CREATE_USER))
            return;

        try {
            System.out.print("Username: ");
            String username = sc.next();

            System.out.print("Employee ID: ");
            String id = sc.next();

            System.out.print("Roles (comma separated): ");
            String input = sc.next();

            Set<Role> roles = new HashSet<>();
            for (String r : input.split(",")) {
                roles.add(Role.valueOf(r.trim().toUpperCase()));
            }

            userDao.createUser(username, id, roles);

            AuditLogger.log(
                    getUser().getUsername(),
                    "CREATE_USER",
                    username
            );

            log.info("User created: {}", username);
            System.out.println("User created successfully");

        } catch (DuplicateUserException e) {
            log.warn("Duplicate user attempt: {}", e.getMessage());
            System.out.println("User already exists");
        } catch (DataAccessException e) {
            log.error("User creation failed", e);
            System.out.println("User creation failed");
        }
    }

   

    public void assignRole() {

        if (!hasAccess(Permission.ASSIGN_ROLE))
            return;

        try {
            System.out.print("Username: ");
            String username = sc.next();

            System.out.print("New roles: ");
            String input = sc.next();

            Set<Role> roles = new HashSet<>();
            for (String r : input.split(",")) {
                roles.add(Role.valueOf(r.trim().toUpperCase()));
            }

            userDao.assignRole(username, roles);

            AuditLogger.log(
                    getUser().getUsername(),
                    "ASSIGN_ROLE",
                    username
            );

            log.info("Roles updated for user {}", username);
            System.out.println("Roles updated successfully");

        } catch (UserNotFoundException e) {
            log.warn("User not found: {}", e.getMessage());
            System.out.println("User not found");
        } catch (DataAccessException e) {
            log.error("Assign role failed", e);
            System.out.println("Role assignment failed");
        }
    }

    

    public void resetPassword() {

        if (!hasAccess(Permission.RESET_PASSWORD))
            return;

        try {
            System.out.print("Username: ");
            String username = sc.next();

            userDao.resetPassword(username);

            AuditLogger.log(
                    getUser().getUsername(),
                    "RESET_PASSWORD",
                    username
            );

            log.info("Password reset for {}", username);
            System.out.println("Password reset successful");

        } catch (UserNotFoundException e) {
            log.warn("User not found");
            System.out.println("User not found");
        } catch (DataAccessException e) {
            log.error("Reset password failed", e);
            System.out.println("Password reset failed");
        }
    }
}

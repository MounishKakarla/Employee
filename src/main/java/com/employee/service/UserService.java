package com.employee.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.employee.dao.EmployeeDao;
import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.UserNotFoundException;
import com.employee.logging.AuditLogger;
import com.employee.model.User;

import com.employee.security.Role;

public class UserService extends BaseService {

	private static final Logger log = LogManager.getLogger(UserService.class);

	private final UserDao userDao;

	public UserService(User user, EmployeeDao employeeDao, UserDao userDao) {

		super(user, employeeDao);
		this.userDao = userDao;
	}

	public void createUser() {

		try {
			System.out.print("Username: ");
			String username = sc.next();

			System.out.print("Employee ID: ");
			String empId = sc.next();
			

		
			if (!getDao().existsById(empId)) {
			    throw new EmployeeNotFoundException(
			        "Employee not found with ID: " + empId
			    );
			}


			System.out.print("Assign roles (comma separated): ADMIN,MANAGER,EMPLOYEE: ");
			String input = sc.next();

			Set<Role> roles = new HashSet<>();
			for (String r : input.split(",")) {
				roles.add(Role.valueOf(r.trim().toUpperCase()));
			}

			userDao.createUser(username, empId, roles);

			AuditLogger.log(getUser().getUsername(), "CREATE_USER", "username=" + username + ", empId=" + empId);

			log.info("User created successfully: {}", username);
			System.out.println("User created successfully");

		} catch (EmployeeNotFoundException exception) {
			log.warn(exception.getMessage());
			System.out.println(exception.getMessage());

		} catch (DuplicateUserException exception) {
			log.warn("Duplicate user attempt: {}");
			System.out.println("User already exists");

		} catch (DataAccessException exception) {
			log.error("User creation failed", exception);
			System.out.println("User creation failed");
		}
	}

	public void assignRole() {

		try {
			System.out.print("Username: ");
			String username = sc.next();

			System.out.print("New roles (comma separated): ");
			String input = sc.next();

			Set<Role> roles = new HashSet<>();
			for (String r : input.split(",")) {
				roles.add(Role.valueOf(r.trim().toUpperCase()));
			}

			userDao.assignRole(username, roles);

			AuditLogger.log(getUser().getUsername(), "ASSIGN_ROLE", username);

			log.info("Roles updated for user {}", username);
			System.out.println("Roles updated successfully");

		} catch (UserNotFoundException exception) {
			log.warn(exception.getMessage());
			System.out.println("User not found");

		} catch (DataAccessException exception) {
			log.error("Assign role failed", exception);
			System.out.println("Role assignment failed");
		}
	}

	public void resetPassword() {

		try {
			System.out.print("Username: ");
			String username = sc.next();

			userDao.resetPassword(username);

			AuditLogger.log(getUser().getUsername(), "RESET_PASSWORD", username);

			log.info("Password reset for {}", username);
			System.out.println("Password reset successful");

		} catch (UserNotFoundException exception) {
			log.warn(exception.getMessage());
			System.out.println("User not found");

		} catch (DataAccessException exception) {
			log.error("Reset password failed", exception);
			System.out.println("Password reset failed");
		}
	}
}

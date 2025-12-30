package com.employee.service;

import java.util.HashSet;
import java.util.Set;

import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.Role;

public class UserManagementService extends BaseService {

	private final UserDao userDao;

	public UserManagementService(User user, UserDao userDao) {
		super(user, null);
		this.userDao = userDao;
	}

	public void createUser() {

		if (!hasAccess(Permission.CREATE_USER))
			return;

		try {
			System.out.print("Username: ");
			String username = sc.next();

			System.out.print("Assign roles (comma separated): ADMIN,MANAGER,EMPLOYEE: ");
			String input = sc.next();

			Set<Role> roles = new HashSet<>();
			for (String r : input.split(",")) {
				roles.add(Role.valueOf(r.trim().toUpperCase()));
			}

			userDao.createUser(username, roles);

		} catch (DataAccessException e) {
			System.out.println("User creation failed");
		}
	}

	public void assignRole() {

		if (!hasAccess(Permission.ASSIGN_ROLE))
			return;

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
			System.out.println("Roles updated successfully");

		} catch (DataAccessException e) {
			System.out.println("Role assignment failed");
		}
	}
}

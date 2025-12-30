package com.employee.service;

import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.model.User;
import com.employee.security.Permission;

public class PasswordResetService extends BaseService {

	private final UserDao userDao;

	public PasswordResetService(User user, UserDao userDao) {
		super(user, null);
		this.userDao = userDao;
	}

	public void resetPassword() {

		if (!hasAccess(Permission.RESET_PASSWORD))
			return;

		try {
			System.out.print("Username: ");
			String username = sc.next();

			userDao.resetPassword(username);

		} catch (DataAccessException e) {
			System.out.println("Password reset failed");
		}
	}
}
 
package com.employee.security;

import java.util.Scanner;

import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.model.User;

public class Login {

	private static final Scanner sc = new Scanner(System.in);

	public static User login(UserDao userDao) {

		while (true) {
			try {
				System.out.print("Username: ");
				String username = sc.next().trim();

				System.out.print("Password: ");
				String password = sc.next().trim();

				User user = userDao.authenticate(username, password);

				if (user != null) {
					System.out.println("Login Successful");
					return user;
				}

				System.out.println("Invalid credentials. Try again.\n");

			} catch (DataAccessException exception) {

				System.out.println("Login system error");

			}
		}
	}
}

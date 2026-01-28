package com.employee.main;

import com.employee.dao.EmployeeDao;
import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.UserNotFoundException;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.RolePermission;
import com.employee.service.BaseService;
import com.employee.service.EmployeeService;

import com.employee.service.UserService;

public class MenuRouter {

	public static void start(User user, EmployeeDao dao, UserDao udao)
			throws EmployeeNotFoundException, DataAccessException, DuplicateUserException, UserNotFoundException {

		UserService userService = new UserService(user, udao);

		EmployeeService employeeService = new EmployeeService(user, dao, udao);

		while (true) {

			System.out.println("\n====== MAIN MENU ======");

			if (RolePermission.hasPermission(user.getRoles(), Permission.CREATE_USER))
				System.out.println("1. Create User");

			if (RolePermission.hasPermission(user.getRoles(), Permission.ASSIGN_ROLE))
				System.out.println("2. Assign Role");

			if (RolePermission.hasPermission(user.getRoles(), Permission.RESET_PASSWORD))
				System.out.println("3. Reset User Password");

			if (RolePermission.hasPermission(user.getRoles(), Permission.ADD_EMPLOYEE))
				System.out.println("4. Add Employee");

			if (RolePermission.hasPermission(user.getRoles(), Permission.UPDATE_EMPLOYEE))
				System.out.println("5. Update Employee");

			if (RolePermission.hasPermission(user.getRoles(), Permission.DELETE_EMPLOYEE))
				System.out.println("6. Delete Employee");

			if (RolePermission.hasPermission(user.getRoles(), Permission.FETCH_EMPLOYEE))
				System.out.println("7. Fetch All Employees");

			if (RolePermission.hasPermission(user.getRoles(), Permission.FETCH_EMPLOYEEBYNAME))
				System.out.println("8. Fetch Employee By Name");

			if (RolePermission.hasPermission(user.getRoles(), Permission.FETCH_EMPLOYEEBYSALARY))
				System.out.println("9. Fetch Employee By Salary");

			if (RolePermission.hasPermission(user.getRoles(), Permission.UPDATE_SELF_PROFILE))
				System.out.println("10. Update My Profile");

			if (RolePermission.hasPermission(user.getRoles(), Permission.UPDATE_SELF_PASSWORD))
				System.out.println("11. Change My Password");

			System.out.println("0. Exit");

			int ch = BaseService.sc.nextInt();

			switch (ch) {
			case 1 -> userService.createUser();
			case 2 -> userService.assignRole();
			case 3 -> userService.resetPassword();
			case 4 -> employeeService.addEmployee();
			case 5 -> employeeService.updateEmployee();
			case 6 -> employeeService.deleteEmployee();
			case 7 -> employeeService.fetchAll();
			case 8 -> employeeService.fetchByName();
			case 9 -> employeeService.fetchBySalary();
			case 10 -> employeeService.updateProfile();
			case 11 -> employeeService.changePassword();

			case 0 -> System.exit(0);
			default -> System.out.println("Invalid option");
			}
		}
	}
}

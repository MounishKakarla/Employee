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

	public static AppAction start(User user, EmployeeDao dao, UserDao udao)
			throws EmployeeNotFoundException, DataAccessException, DuplicateUserException, UserNotFoundException {

		UserService userService = new UserService(user, dao, udao);

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
			if (RolePermission.hasPermission(user.getRoles(), Permission.UPDATE_NAME_BY_ID))
				System.out.println("6. Update Employee Name By ID ");

			if (RolePermission.hasPermission(user.getRoles(), Permission.DELETE_EMPLOYEE))
				System.out.println("7. Delete Employee");

			if (RolePermission.hasPermission(user.getRoles(), Permission.FETCH_EMPLOYEE))
				System.out.println("8. Fetch All Employees");
			if (RolePermission.hasPermission(user.getRoles(), Permission.FETCH_EMPLOYEE_BY_ID))
				System.out.println("9. Fetch Employee By ID");

			if (RolePermission.hasPermission(user.getRoles(), Permission.FETCH_EMPLOYEE_BY_NAME))
				System.out.println("10. Fetch Employee By Name");

			if (RolePermission.hasPermission(user.getRoles(), Permission.FETCH_EMPLOYEE_BY_SALARY))
				System.out.println("11. Fetch Employee By Salary");

			if (RolePermission.hasPermission(user.getRoles(), Permission.UPDATE_SELF_PROFILE))
				System.out.println("12. Update My Profile");

			if (RolePermission.hasPermission(user.getRoles(), Permission.UPDATE_SELF_PASSWORD))
				System.out.println("13. Change My Password");
			if (RolePermission.hasPermission(user.getRoles(), Permission.FETCH_DELETED_EMPLOYEES))
				System.out.println("14. Fetch Deleted Employees");
			
			System.out.println("99. Logout");
			System.out.println("0. Exit Application");

			String input = BaseService.sc.next();

			int ch;
			try {
				ch = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number");
				continue;
			}

			switch (ch) {
			case 1 -> userService.createUser();
			case 2 -> userService.assignRole();
			case 3 -> userService.resetPassword();
			case 4 -> employeeService.addEmployee();
			case 5 -> employeeService.updateEmployee();
			case 6 -> employeeService.updateNameById();
			case 7 -> employeeService.deleteEmployee();
			case 8 -> employeeService.fetchAll();
			case 9 -> employeeService.fetchById();
			case 10 -> employeeService.fetchByName();
			case 11 -> employeeService.fetchBySalary();
			case 12 -> employeeService.updateProfile();
			case 13 -> employeeService.changePassword();
			case 14-> employeeService.fetchDeletedEmployees();
			
			case 99 -> {
				System.out.println("Logged out successfully");
				return AppAction.LOGOUT;
			}

			case 0 -> {
				System.out.println("Exiting application...");
				return AppAction.EXIT;
			}

			default -> System.out.println("Invalid option");
			}
		}
	}
}

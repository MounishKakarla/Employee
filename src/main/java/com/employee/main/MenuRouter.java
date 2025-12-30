package com.employee.main;

import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeDaoImpl;
import com.employee.dao.UserDao;
import com.employee.dao.UserDaoImpl;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.RolePermission;
import com.employee.service.*;

public class MenuRouter {
	private static   EmployeeDao dao = new EmployeeDaoImpl();
	private static UserDao udao=new UserDaoImpl();

    public static void start(User user) throws EmployeeNotFoundException {
    	

        UserManagementService ums = new UserManagementService(user,udao);
        PasswordResetService reset = new PasswordResetService(user,udao);
        AddEmployee add = new AddEmployee(user,dao);
        UpdateEmployee update = new UpdateEmployee(user, dao);
        DeleteEmployee delete = new DeleteEmployee(user, dao);
        FetchEmployee fetch = new FetchEmployee(user, dao);
        EmployeeService self = new EmployeeService(user, dao,udao);

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
                case 1 -> ums.createUser();
                case 2 -> ums.assignRole();
                case 3 -> reset.resetPassword();
                case 4 -> add.execute();
                case 5 -> update.execute();
                case 6 -> delete.execute();
                case 7 -> fetch.execute();
                case 8 -> fetch.fetchByName();
                case 9 -> fetch.fetchBySalary();
                case 10 -> self.updateProfile();
                case 11 -> self.changePassword();
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid option");
            }
        }
    }
}

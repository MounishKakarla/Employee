package com.employee.main;

import com.employee.dao.EmployeeDao;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.User;
import com.employee.service.AddEmployee;
import com.employee.service.BaseService;
import com.employee.service.DeleteEmployee;
import com.employee.service.EmployeeService;
import com.employee.service.FetchEmployee;
import com.employee.service.PasswordResetService;
import com.employee.service.UpdateEmployee;
import com.employee.service.UserManagementService;


public class MenuRouter {

    public static void start(User user, EmployeeDao dao) throws EmployeeNotFoundException {

        switch (user.getRole()) {
            case SUPERADMIN -> superAdminMenu(user, dao);
            case ADMIN -> adminMenu(user, dao);
            case MANAGER -> managerMenu(user, dao);
            case EMPLOYEE -> employeeMenu(user, dao);
        }
    }

    /* ================= SUPER ADMIN ================= */

    private static void superAdminMenu(User user, EmployeeDao dao) {

        UserManagementService ums = new UserManagementService(user);
        PasswordResetService reset = new PasswordResetService(user);
        AddEmployee add = new AddEmployee(user, dao);
        UpdateEmployee update = new UpdateEmployee(user, dao);
        DeleteEmployee delete = new DeleteEmployee(user, dao);
        FetchEmployee fetch = new FetchEmployee(user, dao);

        while (true) {
            System.out.println("""
            SUPER ADMIN MENU
            1.Create User
            2.Assign Role
            3.Reset User Password
            4.Add Employee
            5.Update Employee
            6.Delete Employee
            7.Fetch All Employees
            8.Fetch Employee ByName
            9.Fetch Employee BySalary
            10.Exit
            """);

            int ch = BaseService.sc.nextInt();

            switch (ch) {
                case 1 -> ums.createUser();
                case 2 -> ums.assignRole();
                case 3 -> reset.resetPassword();
                case 4 -> add.execute();
                case 5 -> update.execute();
                case 6 -> delete.execute();
                case 7 -> fetch.execute();
                case 8 ->fetch.fetchByName();
                case 9->fetch.fetchBySalary();
                case 10 -> System.exit(0);
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ================= ADMIN ================= */

    private static void adminMenu(User user, EmployeeDao dao) {

        PasswordResetService reset = new PasswordResetService(user);
        AddEmployee add = new AddEmployee(user, dao);
        UpdateEmployee update = new UpdateEmployee(user, dao);
        FetchEmployee fetch = new FetchEmployee(user, dao);

        while (true) {
            System.out.println("""
            ADMIN MENU
            1.Add Employee
            2.Update Employee
            3.Fetch All Employees
            4.Reset Employee Password
            5.Exit
            """);

            int ch = BaseService.sc.nextInt();

            switch (ch) {
                case 1 -> add.execute();
                case 2 -> update.execute();
                case 3 -> fetch.execute();
                case 4 -> reset.resetPassword();
                case 5 -> System.exit(0);
            }
        }
    }

    /* ================= MANAGER ================= */

    private static void managerMenu(User user, EmployeeDao dao) {

        AddEmployee add = new AddEmployee(user, dao);
        FetchEmployee fetch = new FetchEmployee(user, dao);

        while (true) {
            System.out.println("""
            MANAGER MENU
            1.Add Employee
            2.Fetch Employees
            3.Exit
            """);

            int ch = BaseService.sc.nextInt();

            switch (ch) {
                case 1 -> add.execute();
                case 2 -> fetch.execute();
                case 3 -> System.exit(0);
            }
        }
    }

    /* ================= EMPLOYEE ================= */

    private static void employeeMenu(User user, EmployeeDao dao) throws EmployeeNotFoundException {

        EmployeeService self = new EmployeeService(user, dao);
        FetchEmployee fetch = new FetchEmployee(user, dao);

        while (true) {
            System.out.println("""
            EMPLOYEE MENU
            1.View Employees
            2.Update My Profile
            3.Change Password
            4.Exit
            """);

            int ch = BaseService.sc.nextInt();

            switch (ch) {
                case 1 -> fetch.execute();
                case 2 -> self.updateProfile();
                case 3 -> self.changePassword();
                case 4 -> System.exit(0);
            }
        }
    }
}

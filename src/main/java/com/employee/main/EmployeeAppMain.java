package com.employee.main;

import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeDaoImpl;
import com.employee.model.User;
import com.employee.security.LoginService;
import com.employee.service.AddEmployee;
import com.employee.service.BaseService;
import com.employee.service.DeleteEmployee;
import com.employee.service.FetchEmployee;
import com.employee.service.UpdateEmployee;

public class EmployeeAppMain {

    public static void main(String[] args) {

      
        User user = LoginService.login();

       
        EmployeeDao dao = new EmployeeDaoImpl();

       
        AddEmployee addEmployee = new AddEmployee(user, dao);
        UpdateEmployee updateEmployee = new UpdateEmployee(user, dao);
        DeleteEmployee deleteEmployee = new DeleteEmployee(user, dao);
        FetchEmployee fetchEmployee = new FetchEmployee(user, dao);

        while (true) {
            System.out.println("""
            =========================
            EMPLOYEE MANAGEMENT MENU
            =========================
            1. Add Employee
            2. Update Employee
            3. Delete Employee
            4. Fetch All Employees
            5. Fetch Employee By Name
            6. Fetch Employee By Salary
            7. Exit
            =========================
            """);
            System.out.print("Enter Choice: ");

            int choice = BaseService.sc.nextInt();

            switch (choice) {
                case 1 -> addEmployee.execute();
                case 2 -> updateEmployee.execute();
                case 3 -> deleteEmployee.execute();
                case 4 -> fetchEmployee.execute();
                case 5 -> fetchEmployee.fetchByName();
                case 6 -> fetchEmployee.fetchBySalary();
                case 7 -> {
                    System.out.println("Thank you! Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

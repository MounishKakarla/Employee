package com.employee.service;

import java.util.InputMismatchException;
import java.util.Set;

import com.employee.dao.EmployeeDao;
import com.employee.exception.DataAccessException;
import com.employee.execute.EmployeeExecute;
import com.employee.model.Employee;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.RolePermission;

public class FetchEmployee implements EmployeeExecute {

    private User user;
    private EmployeeDao dao;

    public FetchEmployee(User user, EmployeeDao dao) {
        this.user = user;
        this.dao = dao;
    }

    @Override
    public void execute() {

        if (!RolePermission.hasPermission(user.getRole(), Permission.FETCH_EMPLOYEE)) {
            System.out.println("Access Denied!");
            return;
        }

        try {
            Set<Employee> employees = dao.findAll();

            if (employees.isEmpty()) {
                System.out.println("No Employees Found!");
                return;
            }

            employees.forEach(System.out::println);

        } catch (DataAccessException e) {
            System.out.println("❌ Fetch All Failed: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

        public void fetchByName() {

        if (!RolePermission.hasPermission(user.getRole(), Permission.FETCH_EMPLOYEEBYNAME)) {
            System.out.println("Access Denied! Only Admin and HR can Fetch By Name");
            return;
        }

        try {
            System.out.print("Enter Name: ");
            String name = BaseService.sc.next();

            Set<Employee> result = dao.findByName(name);

            if (result.isEmpty()) {
                System.out.println("No Employee Found with Name: " + name);
                return;
            }

            result.forEach(System.out::println);

        } catch (DataAccessException e) {
            System.out.println("❌ Fetch By Name Failed: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

    
    public void fetchBySalary() {

        if (!RolePermission.hasPermission(user.getRole(), Permission.FETCH_EMPLOYEEBYSALARY)) {
            System.out.println("Access Denied! Only Admin and HR can Fetch By Salary");
            return;
        }

        try {
            System.out.print("Enter Salary: ");
            double salary = BaseService.sc.nextDouble();

            Set<Employee> result = dao.findBySalary(salary);

            if (result.isEmpty()) {
                System.out.println("No Employee Found with Salary: " + salary);
                return;
            }

            result.forEach(System.out::println);

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input! Salary must be numeric");
            BaseService.sc.nextLine();
        } catch (DataAccessException e) {
            System.out.println(" Fetch By Salary Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

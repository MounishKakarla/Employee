package com.employee.service;

import com.employee.exception.*;
import com.employee.execute.EmployeeExecute;
import com.employee.model.Employee;
import com.employee.security.Permission;

public class AddEmployee extends BaseService implements EmployeeExecute {

    public AddEmployee(com.employee.model.User user, com.employee.dao.EmployeeDao dao) {
        super(user, dao);
    }

    @Override
    public void execute() {
        if (!hasAccess(Permission.ADD)) return;

        try {
            System.out.print("ID: ");
            String id = sc.next();
            System.out.print("Name: ");
            String name = sc.next();
            System.out.print("Salary: ");
            double salary = sc.nextDouble();

            dao.add(new Employee(id, name, salary));
            System.out.println("Employee Added");

        } catch (DuplicateEmployeeException e) {
            System.out.println(e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("System error");
        }
    }
}

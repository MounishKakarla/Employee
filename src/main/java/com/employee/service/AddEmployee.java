package com.employee.service;

import com.employee.exception.*;
import com.employee.execute.EmployeeExecute;
import com.employee.model.Employee;
import com.employee.security.Permission;
import com.employee.model.User;
import com.employee.dao.EmployeeDao;
public class AddEmployee extends BaseService implements EmployeeExecute {

    public AddEmployee(User user, EmployeeDao dao) {
        super(user, dao);
    }

    @Override
    public void execute() {
        if (!hasAccess(Permission.ADD_EMPLOYEE)) return;

        try {
           
            System.out.print("Name: ");
            String name = sc.next();
            System.out.println("Email:");
            String email=sc.next();
            System.out.println("Address:");
            String address=sc.next();
            System.out.print("Salary: ");
            double salary = sc.nextDouble();
            

            dao.add(new Employee( name,email, address, salary));
            System.out.println("Employee Added");

        } catch (DuplicateEmployeeException e) {
            System.out.println(e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("System error");
        }
    }
}

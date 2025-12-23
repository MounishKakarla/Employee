package com.employee.service;

import com.employee.exception.*;
import com.employee.execute.EmployeeExecute;
import com.employee.model.Employee;
import com.employee.security.Permission;

public class UpdateEmployee extends BaseService implements EmployeeExecute {

    public UpdateEmployee(com.employee.model.User user, com.employee.dao.EmployeeDao dao) {
        super(user, dao);
    }

    @Override
    public void execute() {

        if (!hasAccess(Permission.UPDATE)) return;

        System.out.println("1.Full Update  2.Update Name Only");
        int ch = sc.nextInt();

        try {
            if (ch == 1) {
                System.out.print("ID: ");
                String id = sc.next();
                System.out.print("Name: ");
                String name = sc.next();
                System.out.print("Salary: ");
                double salary = sc.nextDouble();

                dao.updateEmployee(new Employee(id, name, salary));
                System.out.println("Employee Updated");

            } else if (ch == 2) {
                System.out.print("ID: ");
                String id = sc.next();
                System.out.print("Name: ");
                String name = sc.next();

                dao.updateNameById(id, name);
                System.out.println("Name Updated");
            }

        } catch (EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("System error");
        }
    }
}

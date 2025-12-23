package com.employee.service;

import com.employee.exception.*;
import com.employee.execute.EmployeeExecute;
import com.employee.security.Permission;

public class DeleteEmployee extends BaseService implements EmployeeExecute {

    public DeleteEmployee(com.employee.model.User user, com.employee.dao.EmployeeDao dao) {
        super(user, dao);
    }

    @Override
    public void execute() {
        if (!hasAccess(Permission.DELETE)) return;

        try {
            System.out.print("ID: ");
            String id = sc.next();

            dao.delete(id);
            System.out.println("Employee Deleted");

        } catch (EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("System error");
        }
    }
}

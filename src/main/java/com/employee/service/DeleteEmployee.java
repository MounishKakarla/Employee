package com.employee.service;


import com.employee.execute.EmployeeExecute;
import com.employee.security.Permission;
import com.employee.util.IdValidator;
import com.employee.model.User;
import com.employee.dao.EmployeeDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeNotFoundException;


public class DeleteEmployee extends BaseService implements EmployeeExecute {

    public DeleteEmployee(User user, EmployeeDao dao) {
        super(user, dao);
    }

    @Override
    public void execute() {
        if (!hasAccess(Permission.DELETE_EMPLOYEE)) return;

        try {
        	System.out.print("Enter Employee ID (EMPxxx): ");
        	String id = sc.next();

        	if (!IdValidator.isValid(id)) {
        	    System.out.println("Invalid ID format. Use EMP001 style.");
        	    return;
        	}

        	dao.delete(id);
        	System.out.println("Employee is Deleted successfully");

           
        } catch (EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("System error");
        }
    }
}

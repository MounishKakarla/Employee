package com.employee.service;


import com.employee.execute.EmployeeExecute;
import com.employee.model.Employee;
import com.employee.security.Permission;
import com.employee.util.EmailValidator;
import com.employee.model.User;
import com.employee.dao.EmployeeDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeNotFoundException;

public class UpdateEmployee extends BaseService implements EmployeeExecute {

    public UpdateEmployee(User user, EmployeeDao dao) {
        super(user, dao);
    }

    @Override
    public void execute() {

        if (!hasAccess(Permission.UPDATE_EMPLOYEE)) return;

        System.out.println("1.Full Update  2.Update Name  Only");
        int ch = sc.nextInt();

        try {
            if (ch == 1) {
                System.out.print("EmpId:");
                String id=sc.next();
                System.out.print("Name: ");
                String name = sc.next();
                System.out.print("Email: ");
                String email = sc.next();

                if (!EmailValidator.isValid(email)) {
                    System.out.println("Invalid email format");
                    return;
                }
                System.out.print("Address: ");
                String address = sc.next();
                System.out.print("Salary: ");
                double salary = sc.nextDouble();

                dao.updateEmployee(new Employee( id,name, email, address, salary));
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
            System.out.println("Employee Not Found");
        } catch (DataAccessException e) {
            System.out.println("System error");
        }
    }
}

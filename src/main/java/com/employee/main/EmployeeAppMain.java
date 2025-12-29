package com.employee.main;

import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeDaoImpl;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.User;
import com.employee.security.LoginService;

public class EmployeeAppMain {

    public static void main(String[] args) throws EmployeeNotFoundException {

      
        User user = LoginService.login();

       
        EmployeeDao dao = new EmployeeDaoImpl();

        MenuRouter.start(user, dao);

       
    }
}

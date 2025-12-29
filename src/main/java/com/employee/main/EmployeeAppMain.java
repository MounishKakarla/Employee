package com.employee.main;

import com.employee.dao.EmployeeDao;
import com.employee.dao.EmployeeDaoImpl;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.User;
import com.employee.security.LoginService;
import com.employee.service.AddEmployee;
import com.employee.service.BaseService;
import com.employee.service.DeleteEmployee;
import com.employee.service.FetchEmployee;
import com.employee.service.UpdateEmployee;

public class EmployeeAppMain {

    public static void main(String[] args) throws EmployeeNotFoundException {

      
        User user = LoginService.login();

       
        EmployeeDao dao = new EmployeeDaoImpl();

        MenuRouter.start(user, dao);

       
    }
}

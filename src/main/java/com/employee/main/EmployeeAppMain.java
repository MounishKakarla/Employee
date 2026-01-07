package com.employee.main;

import com.employee.dao.EmployeeDao;
import com.employee.dao.UserDao;
import com.employee.dao.factory.EmployeeDaoFactory;
import com.employee.dao.factory.UserDaoFactory;
import com.employee.model.User;
import com.employee.security.Login;
import com.employee.storage.StorageSelector;
import com.employee.storage.StorageType;
import com.employee.util.DbConfigLoader;

public class EmployeeAppMain {

    public static void main(String[] args) throws Exception {

        
        StorageType storageType = StorageSelector.chooseStorage();

      
        if (storageType != StorageType.FILE) {
            DbConfigLoader.init(storageType);
        }

       
        UserDao userDao = UserDaoFactory.getUserDao(storageType);
        EmployeeDao employeeDao = EmployeeDaoFactory.getEmployeeDao(storageType);

        
        User user = Login.login(userDao);

        
        MenuRouter.start(user, employeeDao, userDao);
    }
}

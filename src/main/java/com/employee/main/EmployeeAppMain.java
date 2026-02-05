package com.employee.main;

import com.employee.dao.EmployeeDao;
import com.employee.dao.UserDao;
import com.employee.dao.factory.EmployeeDaoFactory;
import com.employee.dao.factory.UserDaoFactory;
import com.employee.model.User;
import com.employee.security.Login;
import com.employee.storage.StorageSelector;
import com.employee.storage.StorageType;
import com.employee.util.ConnectionFactory;
import com.employee.util.DbConfigLoader;

public class EmployeeAppMain {

	public static void main(String[] args) throws Exception {

	    StorageType storage = StorageSelector.chooseStorage();

	    if (storage != StorageType.FILE) {
	        DbConfigLoader.init(storage);
	        ConnectionFactory.init();  
	    }

	    EmployeeDao employeeDao =
	            EmployeeDaoFactory.getEmployeeDao(storage);
	    UserDao userDao =
	            UserDaoFactory.getUserDao(storage);

	    while (true) {
	        User user = Login.login(userDao);
	        AppAction action =
	                MenuRouter.start(user, employeeDao, userDao);

	        if (action == AppAction.EXIT) {
	            System.out.println("Application Stopped...");
	            ConnectionFactory.shutdown(); 
	            break;
	        }
	    }
	}

}

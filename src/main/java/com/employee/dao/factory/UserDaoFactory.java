package com.employee.dao.factory;

import com.employee.dao.UserDao;
import com.employee.dao.file.FileUserDaoImpl;
import com.employee.dao.jdbc.JdbcUserDaoImpl;
import com.employee.storage.StorageType;

public class UserDaoFactory {
	public static UserDao getUserDao(StorageType type) {
		return switch (type) {
		case FILE-> new FileUserDaoImpl();
		case POSTGRES,MYSQL ->new JdbcUserDaoImpl();
		

		};
	}

}

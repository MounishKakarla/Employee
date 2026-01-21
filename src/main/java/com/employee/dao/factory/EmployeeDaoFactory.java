package com.employee.dao.factory;

import com.employee.dao.EmployeeDao;
import com.employee.dao.file.FileEmployeeDaoImpl;
import com.employee.dao.jdbc.JdbcEmployeeDaoImpl;
import com.employee.storage.StorageType;

public class EmployeeDaoFactory {
	public static EmployeeDao getEmployeeDao(StorageType type) {
		return switch (type) {
		case FILE-> new FileEmployeeDaoImpl();
		case POSTGRES,MYSQL,SUPABASE ->new JdbcEmployeeDaoImpl(type);
		

		};
	}

}

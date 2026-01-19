package com.employee.dao.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;

public class FindByIdTest {
	private FileEmployeeDaoImpl dao;
	private static final String TEST_FILE = "employees-test.json";
	
	 @BeforeEach
	    void setUp() throws Exception {
	        dao = new FileEmployeeDaoImpl(TEST_FILE);
	    }
	 @Test
	 void findByIdSuccess() throws Exception{
		 Employee found=dao.findById("Emp001");
		 assertNotNull(found);
		 assertEquals("Mounish",found.getName());
	 }
	 
	

	 @Test
	 void findByIdFailure()throws Exception{
		 Employee find =dao.findById("Emp004");
		 assertNotNull(find);
		 assertThrows(EmployeeNotFoundException.class, () -> dao.findById("InvalidID"));
	 }
	 

}

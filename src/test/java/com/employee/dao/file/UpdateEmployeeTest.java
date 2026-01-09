package com.employee.dao.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.employee.dao.file.FileEmployeeDaoImpl;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;

public class UpdateEmployeeTest {
	private FileEmployeeDaoImpl dao;
	private static final String TEST_FILE = "employees-test.json";
	
	 @BeforeEach
	    void setUp() throws Exception {
	        dao = new FileEmployeeDaoImpl(TEST_FILE);
	    }
	 
	 @Test
	    void testUpdateEmployeeSuccessfully() throws Exception {
		 Employee emp = new Employee( "Emp001","Mounish","mounish2005@gmail.com","Hyderabad", 50000);

	        assertDoesNotThrow(() -> dao.updateEmployee(emp));

	      
	 }
	 @Test
	 void testUpdateEmployeeFailure()throws Exception{
		 Employee emp=new Employee("Emp004","Mounish","mounish2005@gmail.com","Hyderabad", 50000);
		 EmployeeNotFoundException Exception=assertThrows(EmployeeNotFoundException.class,
                 () -> dao.updateEmployee(emp));
		  assertEquals(
		            "Employee did not Exists.Thank You!!",
		            Exception.getMessage()
		        );
	 }
	 

}

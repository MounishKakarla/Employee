package com.employee.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.employee.dao.file.FileEmployeeDaoImpl;
import com.employee.exception.EmployeeNotFoundException;

public class DeleteEmployeeTest {
	private FileEmployeeDaoImpl dao;
	private static final String TEST_FILE = "employees-test.json";

	@BeforeEach
	void setUp() throws Exception {

		dao = new FileEmployeeDaoImpl(TEST_FILE);
	}

	@Test
	void deleteEmployeeSuccessfully() throws Exception {

		assertDoesNotThrow(() -> dao.delete("EMP003"));

	}
	
	  @Test void deleteEmployeeFailure()throws Exception{ EmployeeNotFoundException
	  Exception=assertThrows(EmployeeNotFoundException.class, () ->
	  dao.delete("Emp002")); assertEquals( "Employee did not Exists.Thank You!!",
	  Exception.getMessage() );
	  
	  }
	 

}

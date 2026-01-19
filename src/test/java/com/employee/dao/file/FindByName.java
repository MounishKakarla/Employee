package com.employee.dao.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;

public class FindByName {
	private FileEmployeeDaoImpl dao;
	private static final String TEST_FILE = "employees-test.json";

	@BeforeEach
	void setUp() throws Exception {
		dao = new FileEmployeeDaoImpl(TEST_FILE);
	}

	@Test
	void findByNameSuccess() throws Exception {
		Set<Employee> found = dao.findByName("Tharun");

		/*assertNotNull(found);

		assertFalse(found.isEmpty(), "Employee set should not be empty");

		Employee firstEmployee = found.iterator().next();
		assertEquals("Tharun", firstEmployee.getName());*/
		
		Employee firstEmployee = found.stream()
			    .findFirst()
			    .orElseThrow(() -> new AssertionError("Employee not found"));
			assertEquals("Tharun", firstEmployee.getName());

	}
	@Test
	void findByNameFailure()throws Exception{
		Set<Employee> find=dao.findByName("Monish");
		assertNotNull(find);
		 assertThrows(EmployeeNotFoundException.class, () -> dao.findByName("No Employee FInd"));
	}



}

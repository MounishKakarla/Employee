package com.employee.dao.file.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employee.dao.file.FileEmployeeDaoImpl;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class FileEmployeeDaoImplMockTest {

	private static final String TEST_FILE = "employees-test.json";

	@Spy
	private FileEmployeeDaoImpl dao = new FileEmployeeDaoImpl(TEST_FILE);

	@BeforeAll
	static void setUp() {
		File file = new File(TEST_FILE);
		if (file.exists()) {
			file.delete();
		}
	}

	@AfterAll
	static void tearDown() {
		File file = new File(TEST_FILE);
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	@Order(1)
	void testAddEmployeeSuccessfully() {
		Employee e1 = new Employee("Tharun", "tharun@gmail.com", "Hyd", 50000);
		Employee e2 = new Employee("Kaustubh", "kaustubh@gmail.com", "Hyd", 60000);

		assertDoesNotThrow(() -> dao.add(e1));
		assertDoesNotThrow(() -> dao.add(e2));
	}

	@Test
	@Order(2)
	void testDuplicateEmployeeThrowsException() {
		Employee duplicate = new Employee("Kaustubh", "kaustubh2004@gmail.com", "Hyderabad", 50000);

		DuplicateEmployeeException exception = assertThrows(DuplicateEmployeeException.class, () -> dao.add(duplicate));

		assertEquals("Employee with similar name exists.",exception.getMessage());
	}


	@Test
	@Order(3)
	void testFindByIdSuccess() throws Exception {

	    Employee found = dao.findById("Emp001")
	            .orElseThrow(() -> new AssertionError("Employee not found"));

	    assertEquals("Tharun", found.getName());
	}


	@Test
	@Order(4)
	void testFindByIdFailure() throws DataAccessException {

	    Optional<Employee> result = dao.findById("InvalidID");

	    assertTrue(result.isEmpty());
	}


	@Test
	@Order(5)
	void testFindByNameSuccess() throws Exception {
		Set<Employee> found = dao.findByName("Tharun");

		assertFalse(found.isEmpty());
		assertEquals("Tharun", found.iterator().next().getName());
	}

	@Test 
	@Order(6) 
	void testFindByNameFailure() throws DataAccessException { 
	  
	    Set<Employee> result = dao.findByName("NonExistentName");

	    
	    assertNotNull(result, "The returned set should not be null");
	    assertTrue(result.isEmpty(), "The set should be empty for a non-existent name");
	}
	@Test
	@Order(7)
	void testUpdateEmployeeSuccessfully() {
		Employee updated = new Employee("Emp001", "Mounish", "mounish@gmail.com", "Hyderabad", 55000);

		assertDoesNotThrow(() -> dao.updateEmployee(updated));
	}

	@Test
	@Order(8)
	void testUpdateEmployeeFailure() {
		Employee emp = new Employee("Emp004", "Mounish", "mounish@gmail.com", "Hyd", 50000);

		EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> dao.updateEmployee(emp));

		assertEquals("Employee not found", exception.getMessage());
	}

	@Test
	@Order(9)
	void testDeleteEmployeeSuccessfully() {
		assertDoesNotThrow(() -> dao.softDelete("Emp002"));
	}

	@Test
	@Order(10)
	void testDeleteEmployeeFailure() {
		EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> dao.softDelete("Emp004"));

		assertEquals("Employee not found", exception.getMessage());
	}

}
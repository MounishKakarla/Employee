package com.employee.dao.file.JUnits;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.employee.dao.file.FileEmployeeDaoImpl;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileEmployeeDaoImplTest {

	private static FileEmployeeDaoImpl dao;
	private static final String TEST_FILE = "employees-test.json";

	@BeforeAll
	static void setUp() {
		File file = new File(TEST_FILE);
		if (file.exists())
			file.delete();
		dao = new FileEmployeeDaoImpl(TEST_FILE);
	}

	@AfterAll
	static void tearDown() {
		File file = new File(TEST_FILE);
		if (file.exists())
			file.delete();
	}

	@Test
	@Order(1)
	void testAddEmployeeSuccessfully() {

		Employee e1 = new Employee("Tharun", "tharun@gmail.com", "Hyd", 50000);
		Employee e2 = new Employee("Kaustubh", "kaustubh@gmail.com", "Hyd", 60000);

		// e1.setId("Emp001");
		// e2.setId("Emp002");

		assertDoesNotThrow(() -> dao.add(e1));
		assertDoesNotThrow(() -> dao.add(e2));
	}

	@Test
	@Order(2)
	void testDuplicateEmployeeThrowsException() {
		Employee dup = new Employee("Kaustubh", "kaustubh2004@gmail.com", "Hyderabad", 50000);

		DuplicateEmployeeException exception = assertThrows(DuplicateEmployeeException.class, () -> dao.add(dup));
		assertEquals("Employee with similar name exists.", exception.getMessage());
	}

	@Test
	@Order(3)
	void testFindByIdSuccess() throws Exception {
		Employee found = dao.findById("Emp001");
		assertNotNull(found);
		assertEquals("Tharun", found.getName());
	}

	@Test
	@Order(4)
	void testFindByIdFailure() {
		EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class,
				() -> dao.findById("InvalidID"));
		assertEquals("Fetch by id failed", exception.getMessage());
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
	void testFindByNameFailure() {
		assertThrows(EmployeeNotFoundException.class, () -> dao.findByName("NonExistentName"));
	}

	@Test
	@Order(7)
	void testUpdateEmployeeSuccessfully() {
		Employee updatedEmp = new Employee("Emp001", "Mounish", "mounish2005@gmail.com", "Hyderabad", 55000);
		assertDoesNotThrow(() -> dao.updateEmployee(updatedEmp));
	}

	@Test
	@Order(8)
	void testUpdateEmployeeFailure() {
		Employee emp = new Employee("Emp004", "Mounish", "mounish@gmail.com", "Hyd", 50000);
		EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class,
				() -> dao.updateEmployee(emp));
		assertEquals("Employee not found", exception.getMessage());
	}

	@Test
	@Order(9)
	void testDeleteEmployeeSuccessfully() {
		assertDoesNotThrow(() -> dao.delete("Emp002"));
	}

	@Test
	@Order(10)
	void testDeleteEmployeeFailure() {
		EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> dao.delete("Emp004"));
		assertEquals("Employee not found", exception.getMessage());
	}
}

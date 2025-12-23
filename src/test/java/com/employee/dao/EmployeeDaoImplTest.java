package com.employee.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.employee.dao.EmployeeDaoImpl;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.model.Employee;

public class EmployeeDaoImplTest {

	private EmployeeDaoImpl dao;

    @BeforeEach
    void setUp() {
        dao = new EmployeeDaoImpl();

        
        File file = new File("employees.json");
        if (file.exists()) {
            file.delete();
        }
    }

    /* ================= TEST 1 ================= */
    @Test
    void testAddEmployeeSuccessfully() throws Exception {

        Employee emp = new Employee("EMP002", "Mounish Kakarla", 50000);

        assertDoesNotThrow(() -> dao.add(emp));
    }

    /* ================= TEST 2 ================= */
    @Test
    void testDuplicateEmployeeIdThrowsException() throws Exception {

        Employee emp1 = new Employee("EMP003", "Rohit", 50000);
        Employee emp2 = new Employee("EMP004", "Rineesha", 60000);

        dao.add(emp1);

        DuplicateEmployeeException exception =
                assertThrows(DuplicateEmployeeException.class,
                        () -> dao.add(emp2));

        assertEquals(
            "Employee with similar id exists,use another id.Thank You!!",
            exception.getMessage()
        );
    }

}

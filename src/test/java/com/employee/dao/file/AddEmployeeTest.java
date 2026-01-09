package com.employee.dao.file;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import com.employee.exception.DuplicateEmployeeException;
import com.employee.model.Employee;

class AddEmployeeTest {

    private static FileEmployeeDaoImpl dao;
    private static final String TEST_FILE = "employees-test.json";

    @BeforeAll
   static void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
        dao = new FileEmployeeDaoImpl(TEST_FILE);
    }

    /*@AfterEach
    void cleanup() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
    }*/

    @Test
    void testAddEmployeeSuccessfully() {
        Employee e1 = new Employee("Tharun", "tharun@gmail.com", "Hyd", 50000);
        Employee e2 = new Employee("Kaustubh", "kaustubh@gmail.com", "Hyd", 60000);

        assertDoesNotThrow(() -> dao.add(e1));
        assertDoesNotThrow(() -> dao.add(e2));
    }

    @Test
    void testDuplicateEmployeeThrowsException() throws Exception {
    	Employee emp2 = new Employee( "Kaustubh","kaustubh2004@gmail.com","Hyderabad", 50000);
        

        DuplicateEmployeeException exception =
                assertThrows(DuplicateEmployeeException.class,
                        () -> dao.add(emp2));

        assertEquals(
            "Employee with similar id exists,use another id.Thank You!!",
            exception.getMessage()
        );

    }
}

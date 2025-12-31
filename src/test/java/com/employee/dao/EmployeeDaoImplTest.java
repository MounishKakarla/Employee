package com.employee.dao;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.employee.dao.EmployeeDaoImpl;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.model.Employee;

public class EmployeeDaoImplTest {

	private EmployeeDaoImpl dao;
	private static final String TEST_FILE = "employees-test.json";


	 @BeforeEach
	    void setUp() throws Exception {

	        
	        File file = new File(TEST_FILE);
	        if (file.exists()) {
	            file.delete();
	        }

	        dao = new EmployeeDaoImpl(TEST_FILE);
	    }

    
    @Test
    void testAddEmployeeSuccessfully() throws Exception {

        Employee emp = new Employee( "Mounish","mounish2003@gmail.com","Hyderabad", 50000);

        assertDoesNotThrow(() -> dao.add(emp));
    }

    
    @Test
    void testDuplicateEmployeeIdThrowsException() throws Exception {

        Employee emp1 = new Employee( "Mounish","mounish2003@gmail.com","Hyderabad", 50000);
        Employee emp2 = new Employee( "Mounish","mounish2003@gmail.com","Hyderabad", 50000);

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

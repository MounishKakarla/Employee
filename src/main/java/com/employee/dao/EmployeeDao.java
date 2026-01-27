package com.employee.dao;

import java.util.Optional;
import java.util.Set;


import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;

public interface EmployeeDao {

    void add(Employee e)
            throws DuplicateEmployeeException, DataAccessException;

    void updateEmployee(Employee e)
            throws EmployeeNotFoundException, DataAccessException;

    void updateNameById(String id, String name)
            throws EmployeeNotFoundException, DataAccessException;

    void delete(String id)
            throws EmployeeNotFoundException, DataAccessException;

    Set<Employee> findAll() throws DataAccessException;
   Optional< Employee> findById(String id) throws DataAccessException;
    Set<Employee> findByName(String name) throws EmployeeNotFoundException, DataAccessException;
    Set<Employee> findBySalary(double salary) throws DataAccessException;
}


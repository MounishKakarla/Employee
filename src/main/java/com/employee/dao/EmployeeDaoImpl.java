package com.employee.dao;

import java.io.File;
import java.util.*;

import com.employee.exception.*;
import com.employee.model.Employee;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class EmployeeDaoImpl implements EmployeeDao {

    private static final File FILE = new File("employees.json");
    //it is main class to read/write JSON
    private final ObjectMapper mapper = new ObjectMapper();
    //Reads employees.json,Ensures JSON is an array,Returns ArrayNode for modification
    private ArrayNode readArray() throws DataAccessException {
        try {
            //System.out.println("Reading employees from: " + FILE.getAbsolutePath());

            if (!FILE.exists()) {
                System.out.println("employees.json not found, returning empty list");
                return mapper.createArrayNode();
            }

            JsonNode node = mapper.readTree(FILE);

            if (!node.isArray()) {
                throw new RuntimeException("employees.json root is not an array");
            }

            return (ArrayNode) node;

        } catch (Exception e) {
            throw new DataAccessException("Failed to read employees.json", e);
        }
    }



    private void writeArray(ArrayNode array) throws DataAccessException {
        try {
        	//Saves updated data to employees.json,Uses pretty printing for readability
        	//serialization :Converting Java objects into Json files 
            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, array);
        } catch (Exception e) {
            throw new DataAccessException("Write failed", e);
        }
    }

    @Override
    public void add(Employee e)
            throws DuplicateEmployeeException, DataAccessException {

        ArrayNode array = readArray();
        for (JsonNode n : array)
            if (n.get("id").asText().equalsIgnoreCase(e.getId()))
                throw new DuplicateEmployeeException("Employee with similar id exists,use another id.Thank You!!");

        ObjectNode obj = mapper.createObjectNode();
        obj.put("id", e.getId());
        obj.put("name", e.getName());
        obj.put("salary", e.getSalary());

        array.add(obj);
        writeArray(array);
    }

    @Override
    public void updateEmployee(Employee e)
            throws EmployeeNotFoundException, DataAccessException {

        ArrayNode array = readArray();
        for (JsonNode n : array) {
        	if (n.get("id").asText().equalsIgnoreCase(e.getId())) {
                ObjectNode o = (ObjectNode) n;
                o.put("name", e.getName());
                o.put("salary", e.getSalary());
                writeArray(array);
                return;
            }
        }
        throw new EmployeeNotFoundException("Employee not found");
    }

    @Override
    public void updateNameById(String id, String name)
            throws EmployeeNotFoundException, DataAccessException {

        ArrayNode array = readArray();
        for (JsonNode n : array) {
            if (n.get("id").asText() == id) {
            	//partial update
                ((ObjectNode) n).put("name", name);
                writeArray(array);
                return;
            }
        }
        throw new EmployeeNotFoundException("Employee not found");
    }

    @Override
    public void delete(String id)
            throws EmployeeNotFoundException, DataAccessException {

        ArrayNode array = readArray();
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).get("id").asText() == id) {
                array.remove(i);
                writeArray(array);
                return;
            }
        }
        throw new EmployeeNotFoundException("Employee not found");
    }

    @Override
    public Set<Employee> findAll() throws DataAccessException {
        Set<Employee> set = new TreeSet<>();
        try {
            for (JsonNode n : readArray())
            	//De-serialization :convert Json back to Java Objects
                set.add(mapper.treeToValue(n, Employee.class));
            return set;
        } catch (Exception e) {
            throw new DataAccessException("Fetch all failed", e);
        }
    }

    @Override
    public Employee findById(String id) throws DataAccessException {
        try {
            for (JsonNode n : readArray()) {
                if (n.get("id").asText().equalsIgnoreCase(id)) {
                    return mapper.treeToValue(n, Employee.class);
                }
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException("Fetch by id failed", e);
        }
    }


    @Override
    public Set<Employee> findByName(String name) throws DataAccessException {
        Set<Employee> set = new TreeSet<>();
        try {
            for (JsonNode n : readArray())
                if (n.get("name").asText().equalsIgnoreCase(name))
                    set.add(mapper.treeToValue(n, Employee.class));
            return set;
        } catch (Exception e) {
            throw new DataAccessException("Fetch by name failed", e);
        }
    }

    @Override
    public Set<Employee> findBySalary(double salary) throws DataAccessException {
        Set<Employee> set = new TreeSet<>();
        try {
            for (JsonNode n : readArray())
                if (n.get("salary").asDouble() == salary)
                    set.add(mapper.treeToValue(n, Employee.class));
            return set;
        } catch (Exception e) {
            throw new DataAccessException("Fetch by salary failed", e);
        }
    }
}

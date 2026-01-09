package com.employee.dao.file;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import com.employee.dao.EmployeeDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FileEmployeeDaoImpl implements EmployeeDao{
	  private final File FILE;
	    private final ObjectMapper mapper = new ObjectMapper();

	    
	    public FileEmployeeDaoImpl() {
	        this.FILE = new File("employees.json");
	    }

	    
	    public FileEmployeeDaoImpl(String filePath) {
	        this.FILE = new File(filePath);
	    }



	    //Reads employees.json,Ensures JSON is an array,Returns ArrayNode for modification
	    private ArrayNode fetchEmployeeData()throws DataAccessException {
	        try {
	            if (!FILE.exists() || FILE.length() == 0) {
	                return mapper.createArrayNode(); 
	            }

	            JsonNode node = mapper.readTree(FILE);

	            if (node == null || !node.isArray()) {
	                
	                return mapper.createArrayNode();
	            }

	            return (ArrayNode) node;

	        } catch (Exception e) {
	            throw new DataAccessException("Failed to read employees.json", e);
	        }
	    }

	    private String generateNextEmployeeId(ArrayNode array) {

	        int max = 0;

	        for (JsonNode node : array) {
	            String id = node.get("id").asText(); 

	            if (id.startsWith("EMP")) {
	                int num = Integer.parseInt(id.substring(3)); 
	                max = Math.max(max, num);
	            }
	        }

	        return String.format("EMP%03d", max + 1);
	    }




	    private void persistEmployees(ArrayNode employees) throws DataAccessException {
	        try {
	        	//Saves updated data to employees.json,Uses pretty printing for readability
	        	//serialization :Converting Java objects into Json files 
	            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, employees);
	        } catch (Exception e) {
	            throw new DataAccessException("Write failed", e);
	        }
	    }

	    @Override
	    public void add(Employee e)
	            throws DuplicateEmployeeException, DataAccessException {

	        ArrayNode array = fetchEmployeeData();
	        
	        for (JsonNode n : array) {
	        	  
	            if (n.get("name").asText().equalsIgnoreCase(e.getName())) {
	                throw new DuplicateEmployeeException("Employee with similar name exists.");
	            }
	            
	           
	            if (e.getId() != null && n.get("id").asText().equalsIgnoreCase(e.getId())) {
	                throw new DuplicateEmployeeException("Employee with this ID already exists.");
	            }
	        }
	        String id = generateNextEmployeeId(array);
	        e.setId(id);
	        
	        String newId = generateNextEmployeeId(array);

	        ObjectNode obj = mapper.createObjectNode();
	        obj.put("id", newId);
	        obj.put("name", e.getName());
	        obj.put("email", e.getEmail());
	        obj.put("address", e.getAddress());
	        obj.put("salary", e.getSalary());

	        array.add(obj);
	        persistEmployees(array);

	        
	        
	        
	    }


	    @Override
	    public void updateEmployee(Employee e)
	            throws EmployeeNotFoundException, DataAccessException {

	        ArrayNode array = fetchEmployeeData();
	        for (JsonNode n : array) {
	        	if (n.get("id").asText().equalsIgnoreCase(e.getId())) {

	                ObjectNode o = (ObjectNode) n;
	                
	                o.put("name", e.getName());
	                o.put("email", e.getEmail());
	                o.put("address", e.getAddress());
	                o.put("salary", e.getSalary());
	                persistEmployees(array);
	                return;
	            }
	        }
	        throw new EmployeeNotFoundException("Employee not found");
	    }

	    @Override
	    public void updateNameById(String id, String name)
	            throws EmployeeNotFoundException, DataAccessException {

	        ArrayNode array = fetchEmployeeData();
	        for (JsonNode n : array) {
	        	if (n.get("id").asText().equalsIgnoreCase(id)) {
	 
	            	//partial update
	                ((ObjectNode) n).put("name", name);
	                persistEmployees(array);
	                return;
	            }
	        }
	        throw new EmployeeNotFoundException("Employee not found");
	    }

	    @Override
	    public void delete(String id)
	            throws EmployeeNotFoundException, DataAccessException {

	        ArrayNode array = fetchEmployeeData();

	        for (int i = 0; i < array.size(); i++) {
	            if (array.get(i).get("id").asText().equalsIgnoreCase(id)) {
	                array.remove(i);
	                persistEmployees(array);
	                return;
	            }
	        }
	        throw new EmployeeNotFoundException("Employee not found");
	    }

	    @Override
	    public Set<Employee> findAll() throws DataAccessException {
	        Set<Employee> set = new TreeSet<>();
	        try {
	            for (JsonNode n : fetchEmployeeData())
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
	            for (JsonNode n : fetchEmployeeData()) {
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
	            for (JsonNode n :fetchEmployeeData())
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
	            for (JsonNode n : fetchEmployeeData())
	                if (n.get("salary").asDouble() == salary)
	                    set.add(mapper.treeToValue(n, Employee.class));
	            return set;
	        } catch (Exception e) {
	            throw new DataAccessException("Fetch by salary failed", e);
	        }
	    }
}

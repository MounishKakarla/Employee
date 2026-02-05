package com.employee.dao.file;

import java.io.File;
import java.sql.Connection;
import java.util.Optional;
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

public class FileEmployeeDaoImpl implements EmployeeDao {
	private final File FILE;
	private final ObjectMapper mapper = new ObjectMapper();

	public FileEmployeeDaoImpl() {
		this.FILE = new File("employees.json");
	}

	public FileEmployeeDaoImpl(String filePath) {
		this.FILE = new File(filePath);
	}

	// Reads employees.json,Ensures JSON is an array,Returns ArrayNode for
	// modification
	private ArrayNode fetchEmployeeData() throws DataAccessException {
		try {
			if (!FILE.exists() || FILE.length() == 0) {
				return mapper.createArrayNode();
			}

			JsonNode node = mapper.readTree(FILE);

			if (node == null || !node.isArray()) {

				return mapper.createArrayNode();
			}

			return (ArrayNode) node;

		} catch (Exception exception) {
			throw new DataAccessException("Failed to read employees.json", exception);
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
			// Saves updated data to employees.json,Uses pretty printing for readability
			// serialization :Converting Java objects into Json files
			mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, employees);
		} catch (Exception exception) {
			throw new DataAccessException("Write failed", exception);
		}
	}

	@Override
	public void add(Employee employee) throws DuplicateEmployeeException, DataAccessException {

		ArrayNode array = fetchEmployeeData();

		for (JsonNode node : array) {

			if (node.get("name").asText().equalsIgnoreCase(employee.getName())) {
				throw new DuplicateEmployeeException("Employee with similar name exists.");
			}

			if (employee.getId() != null && node.get("id").asText().equalsIgnoreCase(employee.getId())) {
				throw new DuplicateEmployeeException("Employee with this ID already exists.");
			}
		}
		String id = generateNextEmployeeId(array);
		employee.setId(id);

		String newId = generateNextEmployeeId(array);

		ObjectNode obj = mapper.createObjectNode();
		obj.put("id", newId);
		obj.put("name", employee.getName());
		obj.put("email", employee.getEmail());
		obj.put("address", employee.getAddress());
		obj.put("salary", employee.getSalary());
		obj.put("active", true);

		array.add(obj);
		persistEmployees(array);

	}

	@Override
	public void updateEmployee(Employee employee) throws EmployeeNotFoundException, DataAccessException {

		ArrayNode array = fetchEmployeeData();
		for (JsonNode node : array) {
			if (node.get("id").asText().equalsIgnoreCase(employee.getId())
					&& node.path("active").asBoolean(true)) {

				ObjectNode obj = (ObjectNode) node;

				obj.put("name", employee.getName());
				obj.put("email", employee.getEmail());
				obj.put("address", employee.getAddress());
				obj.put("salary", employee.getSalary());
				persistEmployees(array);
				return;
			}
		}
		throw new EmployeeNotFoundException("Employee not found");
	}

	@Override
	public void updateNameById(String id, String name) throws EmployeeNotFoundException, DataAccessException {

		ArrayNode array = fetchEmployeeData();
		for (JsonNode node : array) {
			if (node.get("id").asText().equalsIgnoreCase(id)
					&& node.path("active").asBoolean(true)) {

				// partial update
				((ObjectNode) node).put("name", name);
				persistEmployees(array);
				return;
			}
		}
		throw new EmployeeNotFoundException("Employee not found");
	}

	@Override
	public void softDelete(String id)
	        throws EmployeeNotFoundException, DataAccessException {

	    ArrayNode array = fetchEmployeeData();

	    for (JsonNode node : array) {
	        if (node.get("id").asText().equalsIgnoreCase(id)) {
	            ((ObjectNode) node).put("active", false);
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
	        for (JsonNode node : fetchEmployeeData()) {

	           
	            if (!node.path("active").asBoolean(true)) {
	                continue;
	            }

	            set.add(mapper.treeToValue(node, Employee.class));
	        }
	        return set;
	    } catch (Exception exception) {
	        throw new DataAccessException("Fetch all failed", exception);
	    }
	}
	@Override
	public Set<Employee> findDeletedEmployees() throws DataAccessException {

	    Set<Employee> deletedEmployees = new TreeSet<>();

	    try {
	        for (JsonNode node : fetchEmployeeData()) {

	            
	        	
	            if (node.has("active") && !node.get("active").asBoolean()) {

	                deletedEmployees.add(
	                        mapper.treeToValue(node, Employee.class)
	                );
	            }
	        }
	        return deletedEmployees;

	    } catch (Exception exception) {
	        throw new DataAccessException(
	                "Fetch deleted employees failed", exception
	        );
	    }
	}


	@Override
	public Optional<Employee> findById(String id) throws DataAccessException {
	    try {
	        for (JsonNode node : fetchEmployeeData()) {

	            if (node.get("id").asText().equalsIgnoreCase(id)
	                && node.path("active").asBoolean(true)) {

	                return Optional.of(
	                        mapper.treeToValue(node, Employee.class)
	                );
	            }
	        }
	        return Optional.empty();
	    } catch (Exception exception) {
	        throw new DataAccessException("Fetch by id failed", exception);
	    }
	}



	@Override
	public Set<Employee> findByName(String name) throws DataAccessException {
	    Set<Employee> set = new TreeSet<>();

	    try {
	        for (JsonNode node : fetchEmployeeData()) {

	            if (!node.path("active").asBoolean(true)) {
	                continue;
	            }

	            if (node.get("name").asText().equalsIgnoreCase(name)) {
	                set.add(mapper.treeToValue(node, Employee.class));
	            }
	        }
	        return set;
	    } catch (Exception exception) {
	        throw new DataAccessException("Fetch by name failed", exception);
	    }
	}
	@Override
	public Set<Employee> findBySalary(double salary) throws DataAccessException {
	    Set<Employee> set = new TreeSet<>();

	    try {
	        for (JsonNode node : fetchEmployeeData()) {

	            if (!node.path("active").asBoolean(true)) {
	                continue;
	            }

	            if (node.get("salary").asDouble() == salary) {
	                set.add(mapper.treeToValue(node, Employee.class));
	            }
	        }
	        return set;
	    } catch (Exception exception) {
	        throw new DataAccessException("Fetch by salary failed", exception);
	    }
	}


	
	
	@Override
	public boolean existsById(String id) throws DataAccessException {
	    try {
	        for (JsonNode node : fetchEmployeeData()) {

	            if (node.get("id").asText().equalsIgnoreCase(id)
	                && node.path("active").asBoolean(true)) {

	                return true;
	            }
	        }
	        return false;
	    } catch (Exception exception) {
	        throw new DataAccessException("Employee existence check failed", exception);
	    }
	}

	@Override
	public void softDelete(Connection con, String id) throws EmployeeNotFoundException, DataAccessException {
		// TODO Auto-generated method stub
		
	}

}

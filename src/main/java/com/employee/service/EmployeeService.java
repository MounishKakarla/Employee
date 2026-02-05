package com.employee.service;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.employee.dao.EmployeeDao;
import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.logging.AuditLogger;
import com.employee.model.Employee;
import com.employee.model.User;
import com.employee.util.ConnectionFactory;
import com.employee.util.EmailValidator;
import com.employee.util.IdValidator;
import com.employee.util.PasswordUtil;

public class EmployeeService extends BaseService {

	private static final Logger log = LogManager.getLogger(EmployeeService.class);

	private final UserDao userDao;

	public EmployeeService(User user, EmployeeDao employeeDao, UserDao userDao) {
		super(user, employeeDao);
		this.userDao = userDao;
	}

	public void addEmployee() {

		try {
			System.out.print("Name: ");
			String name = sc.next();

			System.out.print("Email: ");
			String email = sc.next();

			if (!EmailValidator.isValid(email)) {
				System.out.println("Invalid email format");
				return;
			}

			sc.nextLine();
			System.out.print("Address: ");
			String address = sc.nextLine();

			System.out.print("Salary: ");
			double salary = sc.nextDouble();

			Employee emp = new Employee(name, email, address, salary);
			getDao().add(emp);

			AuditLogger.log(getUser().getUsername(), "ADD_EMPLOYEE", emp.getId());

			log.info("Employee added successfully, id={}", emp.getId());
			System.out.println("Employee Created with ID: " + emp.getId());

		} catch (DuplicateEmployeeException exception) {
			log.warn("Duplicate employee attempt");
			System.out.println("Employee already exists");
		} catch (DataAccessException exception) {
			log.error("Add employee failed", exception);
			exception.printStackTrace();
			System.out.println("System error");
			
		}
	}

	public void updateEmployee() {

		try {
			System.out.print("Employee ID (EMPxxx): ");
			String id = sc.next();

			if (!IdValidator.isValid(id)) {
				System.out.println("Invalid ID format");
				return;
			}

			Optional<Employee> optionalEmp = getDao().findById(id);

			if (optionalEmp.isEmpty()) {
				System.out.println("Employee not found");
				return;
			}

			Employee emp = optionalEmp.get();

			System.out.print("Email: ");
			String email = sc.next();
			if (!EmailValidator.isValid(email)) {
				System.out.println("Invalid email format");
				return;
			}

			sc.nextLine();
			System.out.print("Address: ");
			String address = sc.nextLine();

			System.out.print("Salary: ");
			double salary = sc.nextDouble();

			emp.setEmail(email);
			emp.setAddress(address);
			emp.setSalary(salary);

			getDao().updateEmployee(emp);

			AuditLogger.log(getUser().getUsername(), "UPDATE_EMPLOYEE", id);

			log.info("Employee updated, id={}", id);
			System.out.println("Employee updated successfully");

		} catch (Exception exception) {
			log.error("Update employee failed", exception);
			System.out.println("Update failed");
		}
	}
	
	public void updateNameById() throws DataAccessException {
		try {
			System.out.println("Enter Employee ID : ");
			String id=sc.next();
			
			System.out.println("Enter New Name : ");
			String name=sc.next();
			getDao().updateNameById(id, name);
			AuditLogger.log(getUser().getUsername(),"UPDATED_EMPLOYEE_NAME",id +" ->" + name);
			log.info("Employee name Updated id={} ,name ={} ",id,name);
			System.out.println("Employee name Updated Successfully");
			
		}catch(EmployeeNotFoundException exception) {
			System.out.println("Employee not found");
			
		}catch(DataAccessException exception) {
			log.error("Update name Failed .",exception);
			System.out.println("Update failed");
		}
	}

	public void deleteEmployee() {

	    System.out.print("Employee ID: ");
	    String id = sc.next();

	    try (Connection con = ConnectionFactory.getConnection()) {

	        con.setAutoCommit(false); 

	        userDao.softDeleteByEmployeeId(con, id);
	        getDao().softDelete(con, id);

	        con.commit(); 

	        AuditLogger.log(
	                getUser().getUsername(),
	                "SOFT_DELETE_EMPLOYEE",
	                id
	        );

	        System.out.println("Employee and user deactivated successfully");

	    } catch (Exception exception) {

	        try {
	            ConnectionFactory.getConnection().rollback();
	        } catch (Exception ignored) {}

	        System.out.println("Deletion failed. Transaction rolled back.");
	        exception.printStackTrace();
	    }
	}



	public void fetchAll() {

		try {
			Set<Employee> employees = getDao().findAll();
			employees.forEach(System.out::println);

			AuditLogger.log(getUser().getUsername(), "FETCH_EMPLOYEES", "ALL");

		} catch (DataAccessException exception) {
			log.error("Fetch employees failed", exception);
			System.out.println("Fetch failed");
		}
	}
	public void fetchDeletedEmployees() {

		try {
			Set<Employee> employees = getDao().findDeletedEmployees();
			employees.forEach(System.out::println);

			AuditLogger.log(getUser().getUsername(), "FETCH_DELETED_EMPLOYEES", "ALL");

		} catch (DataAccessException exception) {
			log.error("Fetch employees failed", exception);
			System.out.println("Fetch failed");
		}
	}
	public void fetchById() {
		try {
			System.out.println("Enter Employee ID : ");
			String id =sc.next();
			Optional <Employee> empOpt=getDao().findById(id);
			if(empOpt.isEmpty()) {
				System.out.println("Employee not found");
				return;
			}
			Employee emp=empOpt.get();
			System.out.println(emp);
			AuditLogger.log(getUser().getUsername(),"FETCH_EMPLOYEE_BY_ID",id);
			log.info("Employee fetched by id={} ",id);
		}catch(DataAccessException exception) {
			log.error("Fetch By id failed",exception);
			System.out.println("Fetch Failed");
		}
	}

	public void updateProfile() {

		try {
			Optional<Employee> optionalEmp = getDao().findById(getUser().getId());

			if (optionalEmp.isEmpty()) {
				System.out.println("Employee record not found");
				return;
			}

			Employee emp = optionalEmp.get();

			System.out.print("Email: ");
			emp.setEmail(sc.next());

			sc.nextLine();
			System.out.print("Address: ");
			emp.setAddress(sc.nextLine());

			getDao().updateEmployee(emp);

			AuditLogger.log(getUser().getUsername(), "UPDATE_SELF_PROFILE", emp.getId());

			log.info("Profile updated");
			System.out.println("Profile updated successfully");

		} catch (Exception exception) {
			log.error("Profile update failed", exception);
			System.out.println("Profile update failed");
		}
	}

	public void fetchByName() throws EmployeeNotFoundException {

		try {
			System.out.print("Enter Name: ");
			String name = sc.next();

			Set<Employee> result = getDao().findByName(name);

			if (result.isEmpty()) {
				System.out.println("No employee found with name: " + name);
				return;
			}

			result.forEach(System.out::println);

			AuditLogger.log(getUser().getUsername(), "FETCH_EMPLOYEE_BY_NAME", name);

			log.info("Employees fetched by name={}", name);

		} catch (DataAccessException exception) {
			log.error("Fetch by name failed", exception);
			System.out.println("Fetch by name failed");
		}
	}

	public void fetchBySalary() {

		try {
			System.out.print("Enter Salary: ");
			double salary = sc.nextDouble();

			Set<Employee> result = getDao().findBySalary(salary);

			if (result.isEmpty()) {
				System.out.println("No employee found with salary: " + salary);
				return;
			}

			result.forEach(System.out::println);

			AuditLogger.log(getUser().getUsername(), "FETCH_EMPLOYEE_BY_SALARY", String.valueOf(salary));

			log.info("Employees fetched by salary={}", salary);

		} catch (InputMismatchException exception) {
			log.warn("Invalid salary input");
			System.out.println("Salary must be numeric");
			sc.nextLine();
		} catch (DataAccessException exception) {
			log.error("Fetch by salary failed", exception);
			System.out.println("Fetch by salary failed");
		}
	}

	public void changePassword() {

		try {
			System.out.print("Current Password: ");
			String oldPwd = sc.next();

			if (!getUser().getPassword().equals(PasswordUtil.encrypt(oldPwd))) {
				System.out.println("Incorrect password");
				return;
			}

			System.out.print("New Password: ");
			String newPwd = sc.next();

			System.out.print("Confirm Password: ");
			String confirmPwd = sc.next();

			if (!newPwd.equals(confirmPwd)) {
				System.out.println("Passwords do not match");
				return;
			}

			userDao.changePassword(getUser().getUsername(), newPwd);

			AuditLogger.log(getUser().getUsername(), "CHANGE_PASSWORD", getUser().getUsername());

			log.info("Password changed");
			System.out.println("Password updated. Login again.");
			System.exit(0);

		} catch (Exception exception) {
			log.error("Password change failed", exception);
			System.out.println("Password change failed");
		}
	}
}

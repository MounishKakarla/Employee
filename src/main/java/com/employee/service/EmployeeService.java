package com.employee.service;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Set;

import com.employee.dao.EmployeeDao;
import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.util.EmailValidator;
import com.employee.util.IdValidator;
import com.employee.util.PasswordUtil;

public class EmployeeService extends BaseService {

    private final UserDao userDao;

    public EmployeeService(User user, EmployeeDao dao, UserDao userDao) {
        super(user, dao);
        this.userDao = userDao;
    }

    /* ----------------ADD EMPLOYEE ---------------- */

    public void addEmployee() {

        if (!hasAccess(Permission.ADD_EMPLOYEE))
            return;

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

            System.out.println("Employee created with ID: " + emp.getId());

        } catch (DuplicateEmployeeException e) {
            System.out.println("Employee already exists");
        } catch (DataAccessException e) {
            System.out.println("System error while adding employee");
        }
    }

    /* ---------------------- UPDATE EMPLOYEE --------------------------- */

    public void updateEmployee() {

        if (!hasAccess(Permission.UPDATE_EMPLOYEE))
            return;

        System.out.println("1. Full Update  2. Update Name Only");
        int choice = sc.nextInt();

        try {
            if (choice == 1) {

                System.out.print("Employee ID: ");
                String id = sc.next();

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

                getDao().updateEmployee(new Employee(id, name, email, address, salary));
                System.out.println("Employee updated successfully");

            } else if (choice == 2) {

                System.out.print("Employee ID: ");
                String id = sc.next();

                System.out.print("New Name: ");
                String name = sc.next();

                getDao().updateNameById(id, name);
                System.out.println("Name updated successfully");
            }

        } catch (EmployeeNotFoundException e) {
            System.out.println("Employee not found");
        } catch (DataAccessException e) {
            System.out.println("System error during update");
        }
    }

    /* ----------------- DELETE EMPLOYEE ------------------------*/

    public void deleteEmployee() {

        if (!hasAccess(Permission.DELETE_EMPLOYEE))
            return;

        try {
            System.out.print("Enter Employee ID (EMPxxx): ");
            String id = sc.next();

            if (!IdValidator.isValid(id)) {
                System.out.println("Invalid ID format");
                return;
            }

            getDao().delete(id);
            System.out.println("Employee deleted successfully");

        } catch (EmployeeNotFoundException e) {
            System.out.println("Employee not found");
        } catch (DataAccessException e) {
            System.out.println("System error during delete");
        }
    }

    /* ------------------- FETCH EMPLOYEES --------------------- */

    public void fetchAllEmployees() {

        if (!hasAccess(Permission.FETCH_EMPLOYEE))
            return;

        try {
            Set<Employee> employees = getDao().findAll();
            if (employees.isEmpty()) {
                System.out.println("No employees found");
                return;
            }
            employees.forEach(System.out::println);

        } catch (DataAccessException e) {
            System.out.println("Fetch failed");
        }
    }

    public void fetchByName() throws EmployeeNotFoundException {

        if (!hasAccess(Permission.FETCH_EMPLOYEEBYNAME))
            return;

        try {
            System.out.print("Enter Name: ");
            String name = sc.next();

            Set<Employee> result = getDao().findByName(name);
            if (result.isEmpty()) {
                System.out.println("No employee found with name: " + name);
                return;
            }
            result.forEach(System.out::println);

        } catch (DataAccessException e) {
            System.out.println("Fetch by name failed");
        }
    }

    public void fetchBySalary() {

        if (!hasAccess(Permission.FETCH_EMPLOYEEBYSALARY))
            return;

        try {
            System.out.print("Enter Salary: ");
            double salary = sc.nextDouble();

            Set<Employee> result = getDao().findBySalary(salary);
            if (result.isEmpty()) {
                System.out.println("No employee found with salary: " + salary);
                return;
            }
            result.forEach(System.out::println);

        } catch (InputMismatchException e) {
            System.out.println("Salary must be numeric");
            sc.nextLine();
        } catch (DataAccessException e) {
            System.out.println("Fetch by salary failed");
        }
    }

    /*------------------- SELF PROFILE UPDATE --------------------*/

    public void updateProfile() throws EmployeeNotFoundException {

        if (!hasAccess(Permission.UPDATE_SELF_PROFILE))
            return;

        try {
            Optional<Employee> optional = getDao().findById(getUser().getId());

            if (optional.isEmpty()) {
                System.out.println("Employee record not found");
                return;
            }

            Employee emp = optional.get();

            System.out.print("Email: ");
            emp.setEmail(sc.next());

            sc.nextLine();
            System.out.print("Address: ");
            emp.setAddress(sc.nextLine());

            getDao().updateEmployee(emp);
            System.out.println("Profile updated successfully");

        } catch (DataAccessException e) {
            System.out.println("Profile update failed");
        }
    }

    /* ================= PASSWORD ================= */

    public void changePassword() {

        if (!hasAccess(Permission.UPDATE_SELF_PASSWORD))
            return;

        try {
            System.out.print("Current Password: ");
            String oldPwd = sc.next();

            if (!getUser().getPassword().equals(PasswordUtil.encrypt(oldPwd))) {
                System.out.println("Incorrect current password");
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
            System.out.println("Password updated. Please login again.");
            System.exit(0);

        } catch (Exception e) {
            System.out.println("Password change failed");
        }
    }
}

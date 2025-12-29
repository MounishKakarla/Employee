package com.employee.service;

import com.employee.dao.EmployeeDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.util.PasswordUtil;
import com.employee.util.UserFileUtil;

public class EmployeeService extends BaseService {

    public EmployeeService(User user, EmployeeDao dao) {
        super(user, dao);
    }

    public void updateProfile() throws EmployeeNotFoundException {

        if (!hasAccess(Permission.UPDATE_SELF_PROFILE)) {
            System.out.println("Access Denied");
            return;
        }

        try {
            System.out.print("Employee ID: ");
            String id = sc.next();

            Employee emp = dao.findAll().stream()
                    .filter(e -> e.getId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);

            if (emp == null) {
                System.out.println(" Employee not found");
                return;
            }

            
            System.out.print("Email: ");
            try {
                emp.setEmail(sc.next()); 
            } catch (IllegalArgumentException ex) {
                System.out.println("Error " + ex.getMessage());
                return;
            }
            sc.nextLine(); 
            System.out.print("Address: ");
            emp.setAddress(sc.nextLine());

            dao.updateEmployee(emp);

            System.out.println(" Profile updated successfully");

        } catch (DataAccessException e) {
            System.out.println(" Profile update failed");
        }
    }


    public void changePassword() {

        if (!hasAccess(Permission.UPDATE_SELF_PASSWORD)) return;

        try {
            System.out.print("Enter Current Password: ");
            String oldPwd = sc.next();

            if (!user.getPassword().equals(PasswordUtil.encrypt(oldPwd))) {
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

            user.setPassword(PasswordUtil.encrypt(newPwd));
            UserFileUtil.updateUserPassword(user);

            System.out.println("Password updated. Please login again.");
            System.exit(0);

        } catch (Exception e) {
            System.out.println("Password change failed");
        }
    }
}

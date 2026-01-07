package com.employee.service;

import com.employee.dao.EmployeeDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.util.PasswordUtil;

import com.employee.dao.UserDao;

public class EmployeeService extends BaseService {

    private final UserDao userDao;

    public EmployeeService(User user, EmployeeDao empDao, UserDao userDao) {
        super(user, empDao);
        this.userDao = userDao;
    }

    public void updateProfile() {

        if (!hasAccess(Permission.UPDATE_SELF_PROFILE)) {
            System.out.println("Profile update not applicable");
            return;
        }

        try {
            Employee emp = dao.findById(user.getId());

            if (emp == null) {
                System.out.println("Employee record not found");
                return;
            }

            System.out.print("Email: ");
            emp.setEmail(sc.next());

            sc.nextLine();
            System.out.print("Address: ");
            emp.setAddress(sc.nextLine());

            dao.updateEmployee(emp);
            System.out.println("Profile updated successfully");

        } catch (Exception e) {
            System.out.println("Profile update failed");
            e.printStackTrace();
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
            userDao.changePassword(user.getUsername(), newPwd);

           

            System.out.println("Password updated. Please login again.");
            System.exit(0);

        } catch (Exception e) {
            System.out.println("Password change failed");
        }
    }
}

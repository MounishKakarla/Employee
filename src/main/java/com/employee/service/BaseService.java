package com.employee.service;

import java.util.Scanner;

import com.employee.dao.EmployeeDao;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.RolePermission;

public abstract class BaseService {

    protected User user;
    protected EmployeeDao dao;
    public static final Scanner sc = new Scanner(System.in);

    protected BaseService(User user, EmployeeDao dao) {
        this.user = user;
        this.dao = dao;
    }
  

    protected boolean hasAccess(Permission permission) {
        if (!RolePermission.hasPermission(user.getRoles(), permission)) {
            System.out.println("Access Denied");
            return false;
        }
        return true;
    }

}


package com.employee.security;

import java.util.*;

public class RolePermission {

    private static final Map<Role, Set<Permission>> MAP = new HashMap<>();

    static {
        MAP.put(Role.SUPERADMIN, EnumSet.allOf(Permission.class));

        MAP.put(Role.ADMIN, EnumSet.of(
                
                Permission.ADD_EMPLOYEE,
                Permission.UPDATE_EMPLOYEE,
                Permission.FETCH_EMPLOYEEBYNAME,
                Permission.FETCH_EMPLOYEEBYSALARY,
                Permission.FETCH_EMPLOYEE
        ));

        MAP.put(Role.MANAGER, EnumSet.of(
                
                Permission.UPDATE_EMPLOYEE,
                Permission.FETCH_EMPLOYEE
        ));

        MAP.put(Role.EMPLOYEE, EnumSet.of(
                Permission.UPDATE_SELF_PROFILE,
                Permission.UPDATE_SELF_PASSWORD,
                Permission.FETCH_EMPLOYEE
        ));
    }

    public static boolean hasPermission(Role role,Permission permission) {
		return MAP.get(role).contains(permission);	}
}

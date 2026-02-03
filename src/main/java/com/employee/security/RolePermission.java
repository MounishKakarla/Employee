package com.employee.security;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RolePermission {

	private static final Map<Role, Set<Permission>> MAP = new HashMap<>();

	static {
		MAP.put(Role.SUPERADMIN, EnumSet.allOf(Permission.class));

		MAP.put(Role.ADMIN, EnumSet.of(

				Permission.ADD_EMPLOYEE, Permission.UPDATE_EMPLOYEE, Permission.UPDATE_NAME_BY_ID,
				Permission.FETCH_EMPLOYEE_BY_ID, Permission.FETCH_EMPLOYEE_BY_NAME, Permission.FETCH_EMPLOYEE_BY_SALARY,
				Permission.FETCH_EMPLOYEE));

		MAP.put(Role.MANAGER, EnumSet.of(

				Permission.UPDATE_EMPLOYEE, Permission.UPDATE_NAME_BY_ID,

				Permission.FETCH_EMPLOYEE));

		MAP.put(Role.EMPLOYEE,
				EnumSet.of(Permission.UPDATE_SELF_PROFILE, Permission.UPDATE_SELF_PASSWORD, Permission.FETCH_EMPLOYEE));
	}

	public static boolean hasPermission(Set<Role> roles, Permission permission) {
		for (Role role : roles) {
			if (MAP.getOrDefault(role, Set.of()).contains(permission)) {
				return true;
			}
		}
		return false;
	}
}

package com.employee.security;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class RolePermission {
	public static final Map<Role,EnumSet<Permission>>map=new HashMap<>();
	static {
		map.put(Role.ADMIN,EnumSet.allOf(Permission.class));
		map.put(Role.HR,EnumSet.of(Permission.ADD,Permission.UPDATE,Permission.UPDATEBYNAME,Permission.DELETE,Permission.FETCHALL,Permission.FETCHBYNAME,Permission.FETCHBYSALARY));
		map.put(Role.EMPLOYEE,EnumSet.of(Permission.FETCHALL) );
	}
	public static boolean hasPermission(Role role,Permission permission) {
		return map.get(role).contains(permission);	}

}

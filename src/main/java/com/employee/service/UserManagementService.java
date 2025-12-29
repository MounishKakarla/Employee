package com.employee.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.employee.dao.EmployeeDao;
import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.Role;
import com.employee.util.PasswordGenerator;
import com.employee.util.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserManagementService extends BaseService{
	

	private static final File FILE=new File("users.json");
	private final ObjectMapper mapper=new ObjectMapper();
	
	public UserManagementService(User user) {
		super(user,null);
	}
	
	public void  createUser() {
		if(!hasAccess(Permission.CREATE_USER)) return;
		
	
	try {
		System.out.println(" UserName:");
		String uname=sc.next();
		
		System.out.println("Assign Role:");
		System.out.println("1.Admin 2.Manager 3.Employee  ");
		int ch=sc.nextInt();
		Role role=switch(ch) {
		case 1->Role.ADMIN;
		case 2->Role.MANAGER;
		default-> Role.EMPLOYEE;
		};
		List <User> users=FILE.exists() 
				?mapper.readValue(FILE, 
						mapper.getTypeFactory().
						constructCollectionType(List.class, User.class)):
							new ArrayList<>();
		String tempPwd=PasswordGenerator.generate();
		users.add(new User(uname,PasswordUtil.encrypt(tempPwd),role));
		mapper.writerWithDefaultPrettyPrinter().writeValue(FILE,users);
		System.out.println("User created Successfully");
		System.out.println("Temporary Password : "+tempPwd);
		
	
		
		
	}catch(Exception e) {
		System.out.println("User creation failed");
		
	}}
	public void assignRole() {
		if(!hasAccess(Permission.ASSIGN_ROLE)) return;
		try {
			List <User> users=mapper.readValue(FILE, 
							mapper.getTypeFactory().
							constructCollectionType(List.class, User.class));
			System.out.println("UserName:");
			String uname=sc.next();
			
			for(User u:users) {
				if(u.getUsername().equals(uname)) {
					System.out.println("New Role : 1. Admin 2.Manager 3.Employee");
					int ch=sc.nextInt();
					u.setRole(ch==1 ? Role.ADMIN :ch==2? Role.MANAGER : Role.EMPLOYEE);
					mapper.writeValue(FILE,users);
					System.out.println("Role is Updated ");
					return;
				
				}
			}
			System.out.println("User not found");
			
			
		}catch(Exception e) {
			System.out.println("Role Assignment Failed");
		}
	}

}

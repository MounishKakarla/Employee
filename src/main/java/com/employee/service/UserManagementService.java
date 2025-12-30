package com.employee.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.security.Role;
import com.employee.util.PasswordGenerator;
import com.employee.util.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserManagementService extends BaseService {

    private static final File FILE = new File("users.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public UserManagementService(User user) {
        super(user, null);
    }

    

    public void createUser() {

        if (!hasAccess(Permission.CREATE_USER)) return;

        try {
            System.out.print("Username: ");
            String uname = sc.next();

            System.out.println("Assign roles (comma separated): ADMIN,MANAGER,EMPLOYEE");
            String input = sc.next();

            Set<Role> roles = new HashSet<>();
            for (String r : input.split(",")) {
                roles.add(Role.valueOf(r.trim().toUpperCase()));
            }

            List<User> users = FILE.exists()
                    ? mapper.readValue(
                            FILE,
                            mapper.getTypeFactory()
                                  .constructCollectionType(List.class, User.class))
                    : new ArrayList<>();

            String tempPwd = PasswordGenerator.generate();
            users.add(new User(uname, PasswordUtil.encrypt(tempPwd), roles));

            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, users);

            System.out.println("User created successfully");
            System.out.println("Temporary Password: " + tempPwd);

        } catch (Exception e) {
            System.out.println("User creation failed: " + e.getMessage());
        }
    }

  

    public void assignRole() {

        if (!hasAccess(Permission.ASSIGN_ROLE)) return;

        try {
            List<User> users = mapper.readValue(
                    FILE,
                    mapper.getTypeFactory()
                          .constructCollectionType(List.class, User.class));

            System.out.print("Username: ");
            String uname = sc.next();

            for (User u : users) {
                if (u.getUsername().equalsIgnoreCase(uname)) {

                    System.out.println("Enter roles (comma separated): ADMIN,MANAGER,EMPLOYEE");
                    String input = sc.next();

                    Set<Role> roles = new HashSet<>();
                    for (String r : input.split(",")) {
                        roles.add(Role.valueOf(r.trim().toUpperCase()));
                    }

                    u.setRoles(roles);

                    mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, users);
                    System.out.println("Roles updated successfully");
                    return;
                }
            }

            System.out.println("User not found");

        } catch (Exception e) {
            System.out.println("Role assignment failed: " + e.getMessage());
        }
    }
}

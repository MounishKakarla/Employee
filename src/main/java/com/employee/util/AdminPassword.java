package com.employee.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.employee.model.User;
import com.employee.security.Role;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdminPassword {

    public static void main(String[] args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("users.json");

        List<User> users;

        
        if (!file.exists() || file.length() == 0) {
            users = new ArrayList<>();
        } else {
            users = mapper.readValue(
                    file,
                    new TypeReference<List<User>>() {}
            );
        }

      
        User superAdmin = new User(
                "superadmin",
                PasswordUtil.encrypt("super123"),
                Role.SUPERADMIN
        );

        users.add(superAdmin);

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);

        System.out.println("Super Admin created successfully");
    }
}

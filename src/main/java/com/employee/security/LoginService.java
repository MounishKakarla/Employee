package com.employee.security;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import com.employee.model.User;
import com.employee.util.PasswordUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginService {

    private static final String FILE_PATH = "users.json";

    public static User login() {

        ObjectMapper mapper = new ObjectMapper();
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Username: ");
                String inputUserName = sc.next().trim();

                System.out.print("Password: ");
                String inputPassword = sc.next().trim();

                File file = new File(FILE_PATH);

                if (!file.exists()) {
                    System.out.println("users.json file not found");
                    continue;
                }

                List<User> users = mapper.readValue(
                        file,
                        new TypeReference<List<User>>() {}
                );

                for (User user : users) {
                	if (user.getUsername().equals(inputUserName)
                	        && user.getPassword().equals(PasswordUtil.encrypt(inputPassword))) {

                	    System.out.println("Login Successful");
                	    return user;
                	}
                }

                System.out.println(" Invalid credentials. Try again.\n");

            } catch (Exception e) {
                System.out.println(" User file error: " + e.getMessage());
            }
        }
    }
}

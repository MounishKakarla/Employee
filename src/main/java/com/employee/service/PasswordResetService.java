package com.employee.service;

import java.io.File;
import java.util.List;

import com.employee.model.User;
import com.employee.security.Permission;
import com.employee.util.PasswordGenerator;
import com.employee.util.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PasswordResetService extends BaseService {

    private static final File FILE = new File("users.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public PasswordResetService(User user) {
        super(user, null);
    }

    public void resetPassword() {

        if (!hasAccess(Permission.RESET_PASSWORD)) return;

        try {
            System.out.print("Enter Username to reset password: ");
            String uname = sc.next();

            List<User> users = mapper.readValue(
                    FILE,
                    mapper.getTypeFactory().constructCollectionType(List.class, User.class)
            );

            for (User u : users) {
                if (u.getUsername().equals(uname)) {

                    String tempPwd = PasswordGenerator.generate();
                    u.setPassword(PasswordUtil.encrypt(tempPwd));

                    mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, users);

                    System.out.println("Password reset successful");
                    System.out.println("Temporary Password: " + tempPwd);
                    return;
                }
            }

            System.out.println("User not found");

        } catch (Exception e) {
            System.out.println("Password reset failed");
        }
    }
}

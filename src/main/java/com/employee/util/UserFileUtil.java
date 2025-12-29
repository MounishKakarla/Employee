package com.employee.util;

import java.io.File;
import java.util.List;

import com.employee.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserFileUtil {
	  private static final File FILE = new File("users.json");
	    private static final ObjectMapper mapper = new ObjectMapper();

	    public static void updateUserPassword(User updatedUser) throws Exception {

	        List<User> users = mapper.readValue(
	                FILE,
	                mapper.getTypeFactory().constructCollectionType(List.class, User.class)
	        );

	        for (User u : users) {
	            if (u.getUsername().equals(updatedUser.getUsername())) {
	                u.setPassword(updatedUser.getPassword());
	                break;
	            }
	        }

	        mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, users);
	    }
}

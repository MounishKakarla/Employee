package com.employee.dao.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.UserNotFoundException;
import com.employee.model.User;
import com.employee.security.Role;
import com.employee.util.PasswordUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileUserDaoImpl implements UserDao {
	private final File FILE;
	private final ObjectMapper mapper = new ObjectMapper();
	public FileUserDaoImpl() {
		this.FILE=new File("users.json");
	}
	public FileUserDaoImpl(String filePath) {
		this.FILE=new File(filePath);
	}

	private List<User> fetchUsers() throws Exception {
		if (!FILE.exists() || FILE.length() == 0) {
			return new ArrayList<>();
		}
		return mapper.readValue(FILE, new TypeReference<List<User>>() {
		});
	}

	private void persistUsers(List<User> users) throws Exception {
		mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, users);
	}

	
	@Override
	public void createUser(String username, String id, Set<Role> roles) throws DataAccessException, DuplicateUserException {
	    try {
	        List<User> users = fetchUsers();
	        for (User user : users) {
	            if (user.getUsername().equalsIgnoreCase(username)) {
	                throw new DuplicateUserException("User '" + username + "' already exists");
	            }
	        }
	        String tempPassword = "Temp@" + System.currentTimeMillis();
	        users.add(new User(username, id, PasswordUtil.encrypt(tempPassword), roles));
	        persistUsers(users);
	    } catch (DuplicateUserException exception) {
	        throw exception; 
	    } catch (Exception exception) {
	        throw new DataAccessException("Create user failed", exception);
	    }
	}

	@Override
	public void assignRole(String username, Set<Role> roles) throws DataAccessException, UserNotFoundException {
	    try {
	        List<User> users = fetchUsers();
	        for (User user: users) {
	            if (user.getUsername().equalsIgnoreCase(username)) {
	                user.setRoles(roles);
	                persistUsers(users);
	                return;
	            }
	        }
	        throw new UserNotFoundException("User not found");
	    } catch (UserNotFoundException exception) {
	        throw exception; // Pass through to test
	    } catch (Exception exception) {
	        throw new DataAccessException("Assign role failed", exception);
	    }
	}

	
	@Override
	public void changePassword(String username, String newPassword) throws DataAccessException, UserNotFoundException {
	    try {
	        List<User> users = fetchUsers();
	        for (User user : users) {
	            if (user.getUsername().equalsIgnoreCase(username)) {
	                user.setPassword(PasswordUtil.encrypt(newPassword));
	                persistUsers(users);
	                return;
	            }
	        }
	        throw new UserNotFoundException("User not found");
	    } catch (UserNotFoundException exception) {
	        throw exception; 
	    } catch (Exception exception) {
	        throw new DataAccessException("Change password failed",exception);
	    }
	}


	@Override
	public void resetPassword(String username) throws DataAccessException, UserNotFoundException {
	    try {
	        List<User> users = fetchUsers();
	        for (User user : users) {
	            if (user.getUsername().equalsIgnoreCase(username)) {
	                String temp = "Reset@" + UUID.randomUUID().toString().substring(0, 5);
	                user.setPassword(PasswordUtil.encrypt(temp));
	                persistUsers(users);
	                return;
	            }
	        }
	        throw new UserNotFoundException("User not found");
	    } catch (UserNotFoundException exception) {
	        throw exception;
	    } catch (Exception exception) {
	        throw new DataAccessException("Reset password failed", exception);
	    }
	}

	@Override
	public User authenticate(String username, String password)
	        throws DataAccessException {

	    try {
	        List<User> users = fetchUsers();
	        String encryptedInput = PasswordUtil.encrypt(password);

	        for (User user : users) {
	            if (user.getUsername().equals(username)
	                && user.getPassword().equals(encryptedInput)) {
	                return user;
	            }
	        }

	        return null;

	    } catch (Exception exception) {
	        throw new DataAccessException("Login failed", exception);
	    }
	}



}

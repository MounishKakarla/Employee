package com.user.dao.file.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.employee.dao.file.FileUserDaoImpl;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.UserNotFoundException;
import com.employee.model.User;
import com.employee.security.Role;

import org.junit.jupiter.api.AfterEach;

public class FileUserDaoImplTest {
	private FileUserDaoImpl dao;
	private File testFile;

	@BeforeEach
	void setup() {
		testFile = new File("test-users.json");
		/*
		 * if (testFile.exists()) { testFile.delete(); }
		 */
		dao = new FileUserDaoImpl(testFile.getAbsolutePath());
	}

	@AfterEach
	void cleanup() {
		if (testFile.exists()) {
			testFile.delete();
		}
	}

	@Test
	void createUser_success() throws Exception {
		assertDoesNotThrow(() -> dao.createUser("mounish", "EMP001", Set.of(Role.EMPLOYEE)));

		// User user = dao.authenticate("mounish", "Wrong"); // password printed in
		// console
		// assertNotNull(user);
	}

	@Test
	void createUser_Failure() throws Exception {
		dao.createUser("admin", "Emp002", Set.of(Role.MANAGER));

		DuplicateUserException exception = assertThrows(DuplicateUserException.class,
				() -> dao.createUser("admin", "Emp003", Set.of(Role.ADMIN)));

		assertEquals("User 'admin' already exists", exception.getMessage());
	}

	@Test
	void authenticate_Success() throws Exception {

		dao.createUser("mounish", "EMP001", Set.of(Role.EMPLOYEE));
		dao.changePassword("mounish", "Mouni@2003");

		User user = dao.authenticate("mounish", "Mouni@2003");
		assertNotNull(user);
		assertEquals("mounish", user.getUsername());
	}

	@Test
	void authenticate_Failure() throws Exception {
		User user = dao.authenticate("mounish", "wrong_password");

		assertNull(user);
	}

	@Test
	void assignRole_Success() throws Exception {

		dao.createUser("mounish", "EMP001", Set.of(Role.EMPLOYEE));

		assertDoesNotThrow(() -> dao.assignRole("mounish", Set.of(Role.ADMIN, Role.MANAGER)));
	}

	@Test
	void assignRole_Failure() throws Exception {

		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> dao.assignRole("ghost", Set.of(Role.ADMIN)));
		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void changePassword_Success() throws Exception {

		dao.createUser("mounish", "EMP001", Set.of(Role.EMPLOYEE));

		assertDoesNotThrow(() -> dao.changePassword("mounish", "NewPass@123"));
	}

	@Test
	void changePassword_Failure() throws Exception {
		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> dao.changePassword("ghost", "Mouni@2003"));

		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void resetPassword_Success() throws Exception {

		dao.createUser("mounish", "EMP001", Set.of(Role.EMPLOYEE));

		assertDoesNotThrow(() -> dao.resetPassword("mounish"));
	}

	@Test
	void resetPassword_Failure() throws Exception {
		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> dao.resetPassword("ghost"));

		assertEquals("User not found", exception.getMessage());
	}

}

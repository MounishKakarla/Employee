package com.user.dao.file.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employee.dao.file.FileUserDaoImpl;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.UserNotFoundException;
import com.employee.model.User;
import com.employee.security.Role;

@ExtendWith(MockitoExtension.class)
public class FileUserDaoImplMockTest {

	@Mock
	private FileUserDaoImpl dao;

	@Test
	void createUser_success() throws Exception {
		dao.createUser("mounish", "EMP001", Set.of(Role.EMPLOYEE));

		verify(dao, times(1)).createUser("mounish", "EMP001", Set.of(Role.EMPLOYEE));
	}

	@Test
	void createUser_Failure() throws Exception {

		doThrow(new DuplicateUserException("User already exists")).when(dao).createUser(eq("admin"), anyString(),
				anySet());

		assertThrows(DuplicateUserException.class, () -> dao.createUser("admin", "Emp001", Set.of(Role.ADMIN)));
	}

	@Test
	void authenticate_Success() throws Exception {

		User mockUser = new User();
		mockUser.setUsername("mounish");

		when(dao.authenticate("mounish", "Mouni@2003")).thenReturn(mockUser);

		User user = dao.authenticate("mounish", "Mouni@2003");

		assertNotNull(user);
		assertEquals("mounish", user.getUsername());
	}

	@Test
	void authenticate_Failure() throws Exception {

		when(dao.authenticate("mounish", "wrong")).thenReturn(null);

		User user = dao.authenticate("mounish", "wrong");

		assertNull(user);
	}

	@Test
	void assignRole_Success() throws Exception {

		dao.assignRole("mounish", Set.of(Role.ADMIN, Role.MANAGER));

		verify(dao).assignRole(eq("mounish"), anySet());
	}

	@Test
	void assignRole_Failure() throws Exception {

		doThrow(new UserNotFoundException("User not found")).when(dao).assignRole(eq("ghost"), anySet());

		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> dao.assignRole("ghost", Set.of(Role.ADMIN)));
		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void changePassword_Success() throws Exception {
		dao.changePassword("mounish", "Mouni@2003");
		verify(dao).changePassword("mounish", "Mouni@2003");
	}

	@Test
	void changePassword_Failure() throws Exception {
		doThrow(new UserNotFoundException("User Not Found")).when(dao).changePassword("ghost", "Mouni@2003");

		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> dao.changePassword("ghost", "Mouni@2003"));
		assertEquals("User Not Found", exception.getMessage());
	}

	@Test
	void resetPassword_Success() throws Exception {
		dao.resetPassword("mounish");
		verify(dao).resetPassword("mounish");
	}

	@Test
	void resetPassword_Failure() throws Exception {
		doThrow(new UserNotFoundException("User Not Found")).when(dao).resetPassword("ghost");

		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> dao.resetPassword("ghost"));
		assertEquals("User Not Found", exception.getMessage());
	}
}

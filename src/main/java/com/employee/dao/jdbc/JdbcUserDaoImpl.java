package com.employee.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.employee.dao.UserDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.model.User;
import com.employee.security.Role;

import com.employee.util.ConnectionFactory;
import com.employee.util.PasswordUtil;

public class JdbcUserDaoImpl implements UserDao {

	@Override
	public void createUser(String username, String employeeId, Set<Role> roles)
			throws DataAccessException, DuplicateUserException {

		try (Connection con = ConnectionFactory.getConnection()) {

			PreparedStatement check = con.prepareStatement("SELECT * FROM users WHERE username = ?");
			check.setString(1, username);

			if (check.executeQuery().next()) {
				throw new DuplicateUserException("User '" + username + "' already exists");
			}

			String tempPassword = "Temp@" + System.currentTimeMillis();
			String hashed = PasswordUtil.encrypt(tempPassword);

			PreparedStatement ps = con.prepareStatement("INSERT INTO users(username, password, id) VALUES (?, ?, ?)");
			ps.setString(1, username);
			ps.setString(2, hashed);
			ps.setString(3, employeeId);
			ps.executeUpdate();

			for (Role role : roles) {
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO user_roles(username, role) VALUES (?, ?)");
				pstmt.setString(1, username);
				pstmt.setString(2, role.name());
				pstmt.executeUpdate();
			}

			System.out.println("User created successfully");
			System.out.println("Temporary Password: " + tempPassword);

		} catch (DuplicateUserException exception) {
			throw exception;
		} catch (SQLException exception) {

			throw new DataAccessException("Create user failed", exception);
		}
	}

	@Override
	public void assignRole(String username, Set<Role> roles) throws DataAccessException {
		try (Connection con = ConnectionFactory.getConnection()) {
			PreparedStatement ps = con.prepareStatement("Delete from user_roles where username=?");
			ps.setString(1, username);
			ps.executeUpdate();

			for (Role r : roles) {
				PreparedStatement pstmt = con.prepareStatement("Insert into  user_roles values(?,?)");
				pstmt.setString(1, username);
				pstmt.setString(2, r.name());
				pstmt.executeUpdate();
			}

		} catch (SQLException se) {
			throw new DataAccessException("Assign role is  failed", se);
		}
	}

	@Override
	public void resetPassword(String username) throws DataAccessException {
		String temp = "Reset@" + System.currentTimeMillis();
		String sql = "Update users set password=? where username=?";
		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, PasswordUtil.encrypt(temp));
			ps.setString(2, username);
			ps.executeUpdate();
			System.out.println("Temporary Password: " + temp);
		} catch (SQLException se) {
			throw new DataAccessException("Reset Password failed ", se);
		}

	}

	@Override
	public void changePassword(String username, String newPassword) throws DataAccessException {
		String sql = "Update users set password=? where username=?";
		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, PasswordUtil.encrypt(newPassword));
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (SQLException se) {
			throw new DataAccessException("Change  Password failed ", se);

		}

	}

	@Override
	public User authenticate(String username, String password) throws DataAccessException {

		String sql = """
				SELECT u.username, u.password, u.id, ur.role
				FROM users u
				LEFT JOIN user_roles ur
				ON u.username = ur.username
				WHERE u.username = ?
				""";

		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			}

			String dbHash = rs.getString("password");
			String inputHash = PasswordUtil.encrypt(password);

			if (!dbHash.equals(inputHash)) {
				return null;
			}

			User user = new User();
			user.setUsername(rs.getString("username"));
			user.setPassword(dbHash);
			user.setId(rs.getString("id"));

			Set<Role> roles = new HashSet<>();

			String role = rs.getString("role");
			if (role != null) {
				roles.add(Role.valueOf(role));
			}

			while (rs.next()) {
				role = rs.getString("role");
				if (role != null) {
					roles.add(Role.valueOf(role));
				}
			}

			user.setRoles(roles);
			return user;

		} catch (Exception exception) {
			throw new DataAccessException("Login failed", exception);
		}
	}
	@Override
	public void deleteByEmployeeId(String id) throws DataAccessException {
	    String sql = "DELETE FROM users WHERE id = ?";
	    try (Connection con = ConnectionFactory.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, id);
	        ps.executeUpdate();

	    } catch (SQLException e) {
	        throw new DataAccessException(
	                "Failed to delete user for employee " + id, e
	        );
	    }
	}


}

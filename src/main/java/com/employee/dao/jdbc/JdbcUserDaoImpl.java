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
import com.employee.exception.UserNotFoundException;
import com.employee.model.User;
import com.employee.security.Role;
import com.employee.util.ConnectionFactory;
import com.employee.util.PasswordUtil;

public class JdbcUserDaoImpl implements UserDao {

    
    @Override
    public void createUser(String username,
                           String employeeId,
                           Set<Role> roles)
            throws DataAccessException, DuplicateUserException {

        try (Connection con = ConnectionFactory.getConnection()) {

           
            PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND active=true"
            );
            check.setString(1, username);

            if (check.executeQuery().next()) {
                throw new DuplicateUserException(
                        "User '" + username + "' already exists"
                );
            }

            String tempPassword =
                    "Temp@" + System.currentTimeMillis();

            String hashed =
                    PasswordUtil.encrypt(tempPassword);

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(username, password, id, active) VALUES (?, ?, ?, true)"
            );
            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.setString(3, employeeId);
            ps.executeUpdate();

            for (Role role : roles) {
                PreparedStatement pstmt = con.prepareStatement(
                        "INSERT INTO user_roles(username, role) VALUES (?, ?)"
                );
                pstmt.setString(1, username);
                pstmt.setString(2, role.name());
                pstmt.executeUpdate();
            }

            System.out.println("User created successfully");
            System.out.println("Temporary Password: " + tempPassword);

        } catch (DuplicateUserException exception) {
            throw exception;
        } catch (SQLException exception) {
            throw new DataAccessException(
                    "Create user failed", exception
            );
        }
    }

    @Override
    public void assignRole(String username,
                           Set<Role> roles)
            throws DataAccessException, UserNotFoundException {

        try (Connection con = ConnectionFactory.getConnection()) {

           
            PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND active=true"
            );
            check.setString(1, username);

            if (!check.executeQuery().next()) {
                throw new UserNotFoundException(
                        "Active user not found"
                );
            }

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM user_roles WHERE username=?"
            );
            ps.setString(1, username);
            ps.executeUpdate();

            for (Role r : roles) {
                PreparedStatement pstmt = con.prepareStatement(
                        "INSERT INTO user_roles(username, role) VALUES (?, ?)"
                );
                pstmt.setString(1, username);
                pstmt.setString(2, r.name());
                pstmt.executeUpdate();
            }

        } catch (UserNotFoundException exception) {
            throw exception;
        } catch (SQLException exception) {
            throw new DataAccessException(
                    "Assign role failed", exception
            );
        }
    }

 
    @Override
    public void resetPassword(String username)
            throws DataAccessException, UserNotFoundException {

        String temp = "Reset@" + System.currentTimeMillis();

        String sql = """
                UPDATE users
                SET password=?
                WHERE username=? AND active=true
                """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1,
                    PasswordUtil.encrypt(temp));
            ps.setString(2, username);

            if (ps.executeUpdate() == 0) {
                throw new UserNotFoundException(
                        "Active user not found"
                );
            }

            System.out.println(
                    "Temporary Password: " + temp
            );

        } catch (UserNotFoundException exception) {
            throw exception;
        } catch (SQLException exception) {
            throw new DataAccessException(
                    "Reset password failed", exception
            );
        }
    }

   
    @Override
    public void changePassword(String username,
                               String newPassword)
            throws DataAccessException, UserNotFoundException {

        String sql = """
                UPDATE users
                SET password=?
                WHERE username=? AND active=true
                """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1,
                    PasswordUtil.encrypt(newPassword));
            ps.setString(2, username);

            if (ps.executeUpdate() == 0) {
                throw new UserNotFoundException(
                        "Active user not found"
                );
            }

        } catch (UserNotFoundException exception) {
            throw exception;
        } catch (SQLException exception) {
            throw new DataAccessException(
                    "Change password failed", exception
            );
        }
    }

    
    @Override
    public User authenticate(String username,
                             String password)
            throws DataAccessException {

        String sql = """
                SELECT u.username, u.password, u.id, ur.role
                FROM users u
                LEFT JOIN user_roles ur
                ON u.username = ur.username
                WHERE u.username = ? AND u.active=true
                """;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            String dbHash = rs.getString("password");
            String inputHash =
                    PasswordUtil.encrypt(password);

            if (!dbHash.equals(inputHash)) {
                return null;
            }

            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(dbHash);
            user.setId(rs.getString("id"));

            Set<Role> roles = new HashSet<>();

            do {
                String role = rs.getString("role");
                if (role != null) {
                    roles.add(Role.valueOf(role));
                }
            } while (rs.next());

            user.setRoles(roles);
            return user;

        } catch (Exception exception) {
            throw new DataAccessException(
                    "Login failed", exception
            );
        }
    }

    
    @Override
    public void softDeleteByEmployeeId(String empId)
            throws DataAccessException {

        String sql =
                "UPDATE users SET active=false WHERE id=?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, empId);
            ps.executeUpdate();

        } catch (SQLException exception) {
            throw new DataAccessException(
                    "User soft delete failed", exception
            );
        }
    }
}

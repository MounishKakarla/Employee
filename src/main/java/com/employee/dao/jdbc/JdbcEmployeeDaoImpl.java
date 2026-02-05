package com.employee.dao.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.employee.dao.EmployeeDao;
import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateEmployeeException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;
import com.employee.storage.StorageType;
import com.employee.util.ConnectionFactory;

public class JdbcEmployeeDaoImpl implements EmployeeDao {

	private final StorageType dbType;

	public JdbcEmployeeDaoImpl(StorageType dbType) {
		this.dbType = dbType;
	}

	@Override
	public void add(Employee emp) throws DuplicateEmployeeException, DataAccessException {

		try (Connection con = ConnectionFactory.getConnection()) {

			if (dbType == StorageType.POSTGRES || dbType == StorageType.SUPABASE) {

				String sql = """
						INSERT INTO employees
						(name, email, address, salary, active)
						VALUES (?, ?, ?, ?, true)
						RETURNING id
						""";

				try (PreparedStatement ps = con.prepareStatement(sql)) {

					ps.setString(1, emp.getName());
					ps.setString(2, emp.getEmail());
					ps.setString(3, emp.getAddress());
					ps.setDouble(4, emp.getSalary());

					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						emp.setId(rs.getString("id"));
					}
				}

			} else if (dbType == StorageType.MYSQL) {

				String sql = """
						INSERT INTO employees
						(name, email, address, salary, active)
						VALUES (?, ?, ?, ?, true)
						""";

				try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

					ps.setString(1, emp.getName());
					ps.setString(2, emp.getEmail());
					ps.setString(3, emp.getAddress());
					ps.setDouble(4, emp.getSalary());

					ps.executeUpdate();

					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
						emp.setId(rs.getString(1));
					}
				}
			}

		} catch (SQLException exception) {
			throw new DataAccessException("Employee insert failed", exception);
		}
	}

	@Override
	public void updateEmployee(Employee emp) throws EmployeeNotFoundException, DataAccessException {

		String sql = """
				UPDATE employees
				SET name=?, email=?, address=?, salary=?
				WHERE id=? AND active=true
				""";

		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, emp.getName());
			ps.setString(2, emp.getEmail());
			ps.setString(3, emp.getAddress());
			ps.setDouble(4, emp.getSalary());
			ps.setString(5, emp.getId());

			if (ps.executeUpdate() == 0) {
				throw new EmployeeNotFoundException("Employee not found or inactive");
			}

		} catch (SQLException exception) {
			throw new DataAccessException("Update failed", exception);
		}
	}

	@Override
	public void updateNameById(String id, String name) throws EmployeeNotFoundException, DataAccessException {

		String sql = """
				UPDATE employees
				SET name=?
				WHERE id=? AND active=true
				""";

		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, name);
			ps.setString(2, id);

			if (ps.executeUpdate() == 0) {
				throw new EmployeeNotFoundException("Employee not found or inactive");
			}

		} catch (SQLException exception) {
			throw new DataAccessException("Update name failed", exception);
		}
	}

	@Override
	public void softDelete(String id) throws EmployeeNotFoundException, DataAccessException {

		String sql = """
				UPDATE employees
				SET active=false
				WHERE id=? AND active=true
				""";

		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, id);

			if (ps.executeUpdate() == 0) {
				throw new EmployeeNotFoundException("Employee not found or already deleted");
			}

		} catch (SQLException exception) {
			throw new DataAccessException("Soft delete failed", exception);
		}
	}

	@Override
	public Set<Employee> findAll() throws DataAccessException {

		Set<Employee> set = new HashSet<>();

		String sql = "SELECT * FROM employees WHERE active=true";

		try (Connection con = ConnectionFactory.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				set.add(map(rs));
			}
			return set;

		} catch (SQLException exception) {
			throw new DataAccessException("Fetch all failed", exception);
		}
	}
	@Override
	public Set<Employee> findDeletedEmployees() throws DataAccessException {

		Set<Employee> set = new HashSet<>();

		String sql = "SELECT * FROM employees WHERE active=false";

		try (Connection con = ConnectionFactory.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				set.add(map(rs));
			}
			return set;

		} catch (SQLException exception) {
			throw new DataAccessException("Fetch all Inactive  Employees failed", exception);
		}
	}

	@Override
	public Optional<Employee> findById(String id) throws DataAccessException {

		String sql = """
				SELECT * FROM employees
				WHERE id=? AND active=true
				""";

		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return Optional.of(map(rs));
			}
			return Optional.empty();

		} catch (SQLException exception) {
			throw new DataAccessException("Fetch by id failed", exception);
		}
	}

	@Override
	public Set<Employee> findByName(String name) throws DataAccessException {

		Set<Employee> set = new HashSet<>();

		String sql = """
				SELECT * FROM employees
				WHERE name=? AND active=true
				""";

		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				set.add(map(rs));
			}
			return set;

		} catch (SQLException exception) {
			throw new DataAccessException("Fetch by name failed", exception);
		}
	}

	@Override
	public Set<Employee> findBySalary(double salary) throws DataAccessException {

		Set<Employee> set = new HashSet<>();

		String sql = """
				SELECT * FROM employees
				WHERE salary=? AND active=true
				""";

		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDouble(1, salary);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				set.add(map(rs));
			}
			return set;

		} catch (SQLException exception) {
			throw new DataAccessException("Fetch by salary failed", exception);
		}
	}

	@Override
	public boolean existsById(String id) throws DataAccessException {

		String sql = """
				SELECT * FROM employees
				WHERE id=? AND active=true
				""";

		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, id);
			return ps.executeQuery().next();

		} catch (SQLException exception) {
			throw new DataAccessException("Employee existence check failed", exception);
		}
	}

	private Employee map(ResultSet rs) throws SQLException {

		return new Employee(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("address"),
				rs.getDouble("salary"));
	}

	@Override
	public void softDelete(Connection con, String id)
	        throws EmployeeNotFoundException, DataAccessException {

	    String sql = """
	        UPDATE employees
	        SET active=false
	        WHERE id=? AND active=true
	        """;

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, id);

	        if (ps.executeUpdate() == 0) {
	            throw new EmployeeNotFoundException("Employee not found");
	        }
	    } catch (SQLException exception) {
	        throw new DataAccessException("Employee soft delete failed", exception);
	    }
	}


}

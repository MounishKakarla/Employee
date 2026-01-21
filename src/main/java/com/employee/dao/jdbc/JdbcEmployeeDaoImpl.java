package com.employee.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
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
						INSERT INTO employees (name, email, address, salary)
						VALUES (?, ?, ?, ?)
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

				String sql = "INSERT INTO employees (name, email, address, salary) VALUES (?, ?, ?, ?)";

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

		} catch (SQLException ex) {

			throw new DataAccessException("Employee insert failed", ex);
		}
	}

	@Override
	public void updateEmployee(Employee emp) throws EmployeeNotFoundException, DataAccessException {
		String sql = "Update employees set name=?,email=?,address=?,salary=? where id=?";
		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, emp.getName());
			ps.setString(2, emp.getEmail());
			ps.setString(3, emp.getAddress());
			ps.setDouble(4, emp.getSalary());
			ps.setString(5, emp.getId());

			ps.executeUpdate();

		} catch (SQLException se) {
			throw new EmployeeNotFoundException("No Employee With that id");
		}

	}

	@Override
	public void updateNameById(String id, String name) throws EmployeeNotFoundException, DataAccessException {
		String sql = "Update employees set name=? where id=?";
		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, name);

			ps.setString(2, id);

			ps.executeUpdate();

		} catch (SQLException se) {
			throw new EmployeeNotFoundException("No Employee With that id");

		}
	}

	@Override
	public void delete(String id) throws EmployeeNotFoundException, DataAccessException {
		String sql = "Delete from employees where id=?";
		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException se) {
			throw new EmployeeNotFoundException("No Employee With that id");
		}

	}

	@Override
	public Set<Employee> findAll() throws DataAccessException {
		Set<Employee> set = new HashSet<>();
		String sql = "Select * from employees";
		try (Connection con = ConnectionFactory.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				set.add(map(rs));
			}

		} catch (SQLException se) {
			throw new DataAccessException("No Employee Found", se);
		}
		return set;

	}

	@Override
	public Employee findById(String id) throws EmployeeNotFoundException,DataAccessException {
		String sql = "Select * from employees Where id=?";
		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return map(rs);
			}
		} catch (SQLException se) {
			throw new EmployeeNotFoundException("No Employee Found with that id");
		}
		return null;
	}

	@Override
	public Set<Employee> findByName(String name) throws EmployeeNotFoundException, DataAccessException {
		Set<Employee> set = new HashSet<>();
		String sql = "Select * from employees Where name=?";
		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				set.add(map(rs));
			}
		} catch (SQLException se) {
			throw new EmployeeNotFoundException("No Employee Found with that Name ");
		}
		return set;

	}

	@Override
	public Set<Employee> findBySalary(double salary) throws DataAccessException {
		Set<Employee> set = new HashSet<>();
		String sql = "Select * from employees Where salary=?";
		try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setDouble(1, salary);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				set.add(map(rs));
			}
		} catch (SQLException se) {
			throw new DataAccessException("No Employee Found with that Salary", se);
		}
		return set;

	}

	private Employee map(ResultSet rs) throws SQLException {
		return new Employee(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("address"),
				rs.getDouble("salary"));

	}

}

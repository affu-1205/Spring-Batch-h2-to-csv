package com.example.com.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.com.model.Employee;


@Component
public class Reader extends JdbcCursorItemReader<Employee> implements ItemReader<Employee>{
	
	public Reader(@Autowired DataSource dataSource) {
		setDataSource(dataSource);
		setSql("SELECT EMPLOYEE_ID,EMPLOYEE_NAME FROM employee");
		setRowMapper(new EmployeeRowMapper());
	}
	
	public class EmployeeRowMapper implements RowMapper<Employee>{

		@Override
		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			Employee employee = new Employee();
			employee.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
			employee.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
			return employee;
		}
		
	}

}

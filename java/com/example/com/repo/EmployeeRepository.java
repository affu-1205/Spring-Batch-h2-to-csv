package com.example.com.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.com.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

}

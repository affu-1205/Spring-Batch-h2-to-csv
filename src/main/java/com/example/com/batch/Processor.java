package com.example.com.batch;

import org.springframework.batch.item.ItemProcessor;

import com.example.com.model.Employee;

public class Processor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee employee) throws Exception {
		return employee;
	}

}

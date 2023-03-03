package com.example.com.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.example.com.batch.Processor;
import com.example.com.model.Employee;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	@Bean
	public Processor processor() {
		return new Processor();
	}
	
	@Bean
	public FlatFileItemWriter<Employee> writer(){
		FlatFileItemWriter<Employee> writer = new FlatFileItemWriter<Employee>();
		writer.setResource(new FileSystemResource("${input}"));
		//C:/Users/afrsulth/Downloads/SpringBatch_1/SpringBatch_1/src/main/resources/employee.csv
		DelimitedLineAggregator<Employee> lineAggregator = new DelimitedLineAggregator<Employee>();
		lineAggregator.setDelimiter(",");
		BeanWrapperFieldExtractor<Employee> fieldExtractor = new BeanWrapperFieldExtractor<Employee>();
		fieldExtractor.setNames(new String[]{"employeeId","employeeName"});
		lineAggregator.setFieldExtractor(fieldExtractor);
		writer.setLineAggregator(lineAggregator);
		   return writer;
		}

	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory,
			StepBuilderFactory stepBuilderFactory,
			ItemReader<Employee> itemReader,
			ItemProcessor<Employee, Employee> itemProcessor,
			ItemWriter<Employee> itemWriter)
    {
	Step step1 = stepBuilderFactory.get("step1").<Employee,Employee>chunk(100)

			.reader(itemReader)
 
			.processor(processor())

			.writer(itemWriter)

			.build();
 
	return jobBuilderFactory.get("exportEmployeeJob")
			.incrementer(new RunIdIncrementer())

			.flow(step1)
			.end()
			.build();
	}
}
